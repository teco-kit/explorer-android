package edu.teco.explorer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class IncrementalRecorder {

    private final String INITDATASETINCREMENT = "/api/deviceapi/initDatasetIncrement";
    private final String ADDDATASETINCREMENT = "/api/deviceapi/addDatasetIncrement";
    private final String ADDDATASETINCREMENTBATCH = "/api/deviceapi/addDatasetIncrementBatch";

    private final Object sync = new Object();

    private boolean useDeviceTime;
    private String baseUrl;
    private String projectKey;
    private String datasetKey;
    private int counter;

    private NetworkCommunicator communicator;

    private ExecutorService executorService;
    private HashMap<String, List<Pair<Long, String>>> dataStore;


    /**
     * An object to incrementally record datasets
     * @param baseUrl The url of the backend server as well as the port
     * @param projectKey The key for the project, to be found on the settings page
     * @param datasetName The name of the dataset
     * @param useDeviceTime True if you want to use the time from the device instead of providing your own timestamps
     */
    protected IncrementalRecorder(String baseUrl, String projectKey, String datasetName, boolean useDeviceTime) throws Exception {
        this.useDeviceTime = useDeviceTime;
        this.baseUrl = baseUrl;
        this.counter = 0;
        this.projectKey = projectKey;
        this.dataStore = new HashMap<>();
        this.communicator = new NetworkCommunicator(this.baseUrl + ADDDATASETINCREMENTBATCH);
        this.datasetKey = getDatasetKey(baseUrl, projectKey, datasetName);
        if (this.datasetKey == null) {
            throw new Exception("Could not generate incremental dataset");
        }
        this.executorService = Executors.newCachedThreadPool();
    }

    /**
     * Obtains the dataset key which can be used to append datapoints to this new dataset
     * @param baseUrl The url of the backend with port
     * @param projectKey The key to the project under which the new dataset is created
     * @param name The name of the dataset
     * @return Returns the datasetKey or null if an error occurred
     */
    private String getDatasetKey(String baseUrl, String projectKey, String name) {
        JSONObject req = new JSONObject();
        try {
            req.put("deviceApiKey", projectKey);
            req.put("name", name);
            JSONObject ret = new NetworkCommunicator(baseUrl + INITDATASETINCREMENT).sendPost(req);
            if (ret.getInt("STATUS") != 200) {
                return null;
            }
            return ret.getJSONObject("MESSAGE").getString("datasetKey");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Uploads dataPoint
     * @param datapoint The dataPoint to upload as JSON
     * @param time Record time of the dataPoint
     */
    protected void uploadDataPoint(String sensorName, double datapoint, long time) {
        synchronized (sync) {
            if (!this.dataStore.containsKey(sensorName)) {
                this.dataStore.put(sensorName, new LinkedList<>());
            }
            if (this.useDeviceTime) {
                time = new Date().getTime();
            }
            this.dataStore.get(sensorName).add(new Pair(time, datapoint));
            this.counter++;
            if (this.counter > 1000) {
                Map<String, List<Pair<Long, String>>> tmpMap;
                tmpMap = this.dataStore;
                this.dataStore = new HashMap<>();
                this.upload(tmpMap);
                this.counter = 0;
            }
        }
    }

    /**
     *  Uploads the collected datapoints in bulk
     * @param tmpMap The datapoints to upload
     */
    protected void upload(Map<String, List<Pair<Long, String>>> tmpMap) {
        Runnable runnable = () -> {
            Set<String> keySet = tmpMap.keySet();
            JSONObject req = new JSONObject();
            req.put("datasetKey", this.datasetKey);
            JSONArray data = new JSONArray();
            for (String sensorname : keySet) {
                JSONObject sensor = new JSONObject();
                sensor.put("sensorname", sensorname);
                JSONArray timeSeriesData = new JSONArray();
                long start = Long.MAX_VALUE;
                long end = Long.MIN_VALUE;
                for (Pair<Long, String> sensorData : tmpMap.get(sensorname)) {
                    if (sensorData.getFirst() < start) {
                        start = sensorData.getFirst();
                    }
                    if (sensorData.getFirst() > end) {
                        end = sensorData.getFirst();
                    }
                    JSONObject datapoint = new JSONObject();
                    datapoint.put("timestamp", sensorData.getFirst());
                    datapoint.put("datapoint", sensorData.getSecond());
                    timeSeriesData.put(datapoint);
                }
                sensor.put("timeSeriesData", timeSeriesData);
                sensor.put("start", start);
                sensor.put("end", end);
                data.put(sensor);
            }
            req.put("data", data);
            JSONObject ret = communicator.sendPost(req);
            if (ret.getInt("STATUS") != 200) {
                throw new RuntimeException("Upload failed");
            }
        };
        this.executorService.execute(runnable);
    }

    public void onComplete() {
        synchronized (this) {
            upload(this.dataStore);
            this.executorService.shutdown();
            while (!this.executorService.isTerminated()) {
            }
        }
    }

    /**
     * Appends a single datapoint to the dataset
     * @param sensorName The datapoint to append
     * @param value The value to transmit
     * @return true if the append was successful
     */
    public abstract void addDataPoint(String sensorName, double value);

    /**
     * Appends a single datapoint to the dataset
     * @param time Record time of the dataPoint
     * @param sensorName The datapoint to append
     * @param value The value to transmit
     * @return true if the append was successful
     */
    public abstract void addDataPoint(long time, String sensorName, double value);
}

package edu.teco.explorer;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class IncrementalRecorder {

    private final String INITDATASETINCREMENT = "/api/deviceapi/initDatasetIncrement";
    private final String ADDDATASETINCREMENT = "/api/deviceapi/addDatasetIncrement";

    private boolean useServerTime;
    private String baseUrl;
    private String projectKey;
    private String datasetKey;
    private ExecutorService executorService;


    /**
     * An object to incrementally record datasets
     * @param baseUrl The url of the backend server as well as the port
     * @param projectKey The key for the project, to be found on the settings page
     * @param datasetName The name of the dataset
     * @param useServerTime True if you want to use servertime instead of providing your own timestamps
     */
    protected IncrementalRecorder(String baseUrl, String projectKey, String datasetName, boolean useServerTime) throws Exception {
        this.useServerTime = useServerTime;
        this.baseUrl = baseUrl;
        this.projectKey = projectKey;
        this.datasetKey = getDatasetKey(baseUrl, projectKey, datasetName);
        if (this.datasetKey == null) {
            throw new Exception("Could not generate incremental dataset");
        }
        this.executorService = Executors.newFixedThreadPool(2);
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
            JSONObject ret = NetworkCommunicator.sendPost(baseUrl + INITDATASETINCREMENT, req);
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
     * @return true if the append was successful
     */
    protected CompletableFuture<Boolean> uploadDataPoint(String sensorName, double datapoint, long time) {
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {

            JSONObject req = new JSONObject();
            String stringTime = Long.toString(time);
            if (this.useServerTime) {
                stringTime = null;
            }
            try {
                req.put("datasetKey", this.datasetKey);
                req.put("time", stringTime);
                req.put("datapoint", datapoint);
                req.put("sensorname", sensorName);
                JSONObject ret = NetworkCommunicator.sendPost(this.baseUrl + ADDDATASETINCREMENT, req);
                System.out.println("RES: " + ret);
                return ret.getInt("STATUS") == 200;
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("EXCEPTION");
                return false;
            }
        }, this.executorService);
        return future;
    }

    public void onComplete() {
        this.executorService.shutdown();
    }

    /**
     * Appends a single datapoint to the dataset
     * @param sensorName The datapoint to append
     * @param value The value to transmit
     * @return true if the append was successful
     */
    public abstract CompletableFuture<Boolean> addDataPoint(String sensorName, double value);

    /**
     * Appends a single datapoint to the dataset
     * @param time Record time of the dataPoint
     * @param sensorName The datapoint to append
     * @param value The value to transmit
     * @return true if the append was successful
     */
    public abstract CompletableFuture<Boolean> addDataPoint(long time, String sensorName, double value);
}

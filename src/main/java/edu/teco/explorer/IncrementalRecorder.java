package edu.teco.explorer;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class IncrementalRecorder {

    private final String INITDATASETINCREMENT = "/api/deviceapi/initDatasetIncrement";
    private final String ADDDATASETINCREMENT = "/api/deviceapi/addDatasetIncrement";

    private boolean useServerTime;
    private String baseUrl;
    private String projectKey;
    private String datasetKey;


    /**
     * An object to incrementally record datasets
     * @param baseUrl The url of the backend server as well as the port
     * @param projectKey The key for the project, to be found on the settings page
     * @param useServerTime True if you want to use servertime instead of providing your own timestamps
     */
    protected IncrementalRecorder(String baseUrl, String projectKey, boolean useServerTime) throws Exception {
        this.useServerTime = useServerTime;
        this.baseUrl = baseUrl;
        this.projectKey = projectKey;
        this.datasetKey = getDatasetKey(baseUrl, projectKey);
        if (this.datasetKey == null) {
            throw new Exception("Could not generate incremental dataset");
        }
    }

    /**
     * Obtains the dataset key which can be used to append datapoints to this new dataset
     * @param baseUrl The url of the backend with port
     * @param projectKey The key to the project under which the new dataset is created
     * @return Returns the datasetKey or null if an error occurred
     */
    private String getDatasetKey(String baseUrl, String projectKey) {
        JSONObject req = new JSONObject();
        try {
            req.put("deviceApiKey", projectKey);
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
    protected boolean uploadDataPoint(String sensorName, double datapoint, int time) {
        JSONObject req = new JSONObject();
        String stringTime = Integer.toString(time);
        if (this.useServerTime) {
            stringTime = null;
        }
        try {
            req.put("datasetKey", this.datasetKey);
            req.put("time", stringTime);
            req.put("datapoint", datapoint);
            req.put("sensorname", sensorName);
            JSONObject ret = NetworkCommunicator.sendPost(this.baseUrl + ADDDATASETINCREMENT, req);
            return ret.getInt("STATUS") == 200;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Appends a single datapoint to the dataset
     * @param datapoint The datapoint to append
     * @return true if the append was successful
     */
    public abstract boolean addDataPoint(String timeSeriesName, double datapoint);

    /**
     * Appends a single datapoint to the dataset
     * @param datapoint The datapoint to append
     * @return true if the append was successful
     */
    public abstract boolean addDataPoint(String timeSeriesName, double datapoint, int time);
}

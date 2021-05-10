package edu.teco.explorer;

import org.json.JSONException;
import org.json.JSONObject;

public class Recorder {

    private final String UPLOADDATASETURL = "/api/deviceapi/uploadDataset";

    private String backendUrl;
    private String projectKey;

    /**
     * Class for recording datasets and sending them to the Explorer
     * @param backendUrl The backend-URL of the explorer
     */
    public Recorder(String backendUrl, String key) {
        this.backendUrl = backendUrl;
        this.projectKey = key;
    }

    /**
     * Upload datasets in increments
     * @param useServerTime True if you don't want to provide own timestamps
     * @return An object to record the dataset incrementally
     */
    public IncrementalRecorder getIncrementalDataset(boolean useServerTime) throws Exception {
        if (!useServerTime) {
            return new IncrementalRecorderUserTime(this.backendUrl, projectKey);
        }
        else {
            return new IncrementalRecorderServerTime(this.backendUrl, projectKey);
        }
    }


    public boolean sendDataset(JSONObject dataset) {
        JSONObject sendObj = new JSONObject();
        try {
            sendObj.put("key", this.projectKey);
            sendObj.put("payload", dataset);
            JSONObject ret = NetworkCommunicator.sendPost(this.backendUrl + this.UPLOADDATASETURL, sendObj);
            if (ret == null || ret.getInt("STATUS") != 200) {
                return false;
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}

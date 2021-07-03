package edu.teco.explorer;

public class IncrementalRecorderDeviceTime extends IncrementalRecorder{

    /**
     * An object to incrementally record datasets
     * @param baseUrl       The url of the backend server as well as the port
     * @param projectKey    The key for the project, to be found on the settings page
     * @param datasetName   The name of the dataset
     * @throws Exception Failed to create dataset in backend
     */
    protected IncrementalRecorderDeviceTime(String baseUrl, String projectKey, String datasetName) throws Exception {
        super(baseUrl, projectKey, datasetName, true);
    }


    /**
     * Appends a single datapoint to the dataset
     * @param sensorName The datapoint to append
     * @param value The value to transmit
     */
    public void addDataPoint(String sensorName, double value) {
        super.uploadDataPoint(sensorName, value, -1);
    }

    /**
     * Dummy method. Do not use this method
     * @throws UnsupportedOperationException Always
     */
    @Override
    public void addDataPoint(long time, String sensorName, double value) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("You probably wanted to use the other addDataPoint - method");
    }
}

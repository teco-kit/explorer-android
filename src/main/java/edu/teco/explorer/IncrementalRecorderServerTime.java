package edu.teco.explorer;

import java.util.concurrent.CompletableFuture;

public class IncrementalRecorderServerTime extends IncrementalRecorder{

    /**
     * An object to incrementally record datasets
     * @param baseUrl       The url of the backend server as well as the port
     * @param projectKey    The key for the project, to be found on the settings page
     * @param datasetName          The name of the dataset
     */
    protected IncrementalRecorderServerTime(String baseUrl, String projectKey, String datasetName) throws Exception {
        super(baseUrl, projectKey, datasetName, true);
    }


    /**
     * Appends a single datapoint to the dataset
     * @param sensorName The datapoint to append
     * @param value The value to transmit
     * @return true if the append was successful
     */
    public CompletableFuture<Boolean> addDataPoint(String sensorName, double value) {
        return super.uploadDataPoint(sensorName, value, -1);
    }

    /**
     * Dummy method. Do not use this method
     * @throws UnsupportedOperationException Always
     */
    @Override
    public CompletableFuture<Boolean> addDataPoint(long time, String sensorName, double value) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("You probably wanted to use the other addDataPoint - method");
    }
}

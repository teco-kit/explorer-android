package edu.teco.explorer;

import java.util.concurrent.CompletableFuture;

public class IncrementalRecorderUserTime extends IncrementalRecorder{
    /**
     * An object to incrementally record datasets
     *
     * @param baseUrl       The url of the backend server as well as the port
     * @param projectKey    The key for the project, to be found on the settings page
     * @param datasetName          The name of the dataset
     */
    IncrementalRecorderUserTime(String baseUrl, String projectKey, String datasetName) throws Exception {
        super(baseUrl, projectKey, datasetName, false);
    }

    /**
     * Dummy method. Do not use this method
     * @throws UnsupportedOperationException Always
     */
    @Override
    public void addDataPoint(String timeSeriesName, double datapoint) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("You probably wanted to use the other addDataPoint - method");
    }


    /**
     * Appends a single datapoint to the dataset
     * @param time Record time of the dataPoint
     * @param sensorName The datapoint to append
     * @param value The value to transmit
     */
    public void addDataPoint(long time, String sensorName, double value) {
        super.uploadDataPoint(sensorName, value, time);
    }

}

package edu.teco.explorer;


public class IncrementalRecorderUserTime extends IncrementalRecorder{
    /**
     * An object to incrementally record datasets
     *
     * @param baseUrl       The url of the backend server as well as the port
     * @param projectKey    The key for the project, to be found on the settings page
     * @param name          The name of the dataset
     */
    IncrementalRecorderUserTime(String baseUrl, String projectKey, String name) throws Exception {
        super(baseUrl, projectKey, name, false);
    }

    /**
     * Dummy method. Do not use this method
     * @param datapoint The datapoint to append
     * @throws UnsupportedOperationException Always
     */
    @Override
    public boolean addDataPoint(String timeSeriesName, double datapoint) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("You probably wanted to use the other addDataPoint - method");
    }


    /**
     * Appends a single datapoint to the dataset
     * @param datapoint The datapoint to append
     * @param time Record time of the dataPoint
     * @return true if the append was successful
     */
    public boolean addDataPoint(String timeSeriesName, double datapoint, int time) {
        return super.uploadDataPoint(timeSeriesName, datapoint, time);
    }

}

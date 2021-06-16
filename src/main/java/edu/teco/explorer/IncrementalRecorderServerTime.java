package edu.teco.explorer;

public class IncrementalRecorderServerTime extends IncrementalRecorder{

    /**
     * An object to incrementally record datasets
     * @param baseUrl       The url of the backend server as well as the port
     * @param projectKey    The key for the project, to be found on the settings page
     * @param name          The name of the dataset
     */
    protected IncrementalRecorderServerTime(String baseUrl, String projectKey, String name) throws Exception {
        super(baseUrl, projectKey, name,true);
    }


    /**
     * Appends a single datapoint to the dataset
     * @param datapoint The datapoint to append
     * @return true if the append was successful
     */
    public boolean addDataPoint(String timeSeriesName, double datapoint) {
        return super.uploadDataPoint(timeSeriesName, datapoint, -1);
    }

    /**
     * Dummy method. Do not use this method
     * @param datapoint The datapoint to append
     * @throws UnsupportedOperationException Always
     */
    @Override
    public boolean addDataPoint(String timeSeriesName, double datapoint, int time) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("You probably wanted to use the other addDataPoint - method");
    }
}

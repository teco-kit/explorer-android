package edu.teco.explorer;

import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

@Disabled
public class IncrementalRecorderUserTimeTest {

    private Recorder recorder;

    @Test
    public void generateRecorder() throws Exception {
        try (MockedStatic<NetworkCommunicator> communicator = Mockito.mockStatic(NetworkCommunicator.class)) {
            JSONObject retObj = new JSONObject();
            JSONObject messageObj = new JSONObject();
            messageObj.put("datasetKey", "fakeDatasetKey");
            retObj.put("MESSAGE", messageObj);
            retObj.put("STATUS", 200);

            //when(NetworkCommunicator.sendPost(any(String.class), any(JSONObject.class))).thenReturn(retObj);

            recorder = new Recorder("http://localhost:3000", "fakeDatasetKey");
            IncrementalRecorder incRecorder = recorder.getIncrementalDataset("testDatasetName", false);
        }
    }

    @Test
    public void uploadDatasetIncrement() throws Exception{
        try (MockedStatic<NetworkCommunicator> communicator = Mockito.mockStatic(NetworkCommunicator.class)) {
            JSONObject retObj = new JSONObject();
            JSONObject messageObj = new JSONObject();
            messageObj.put("datasetKey", "fakeDatasetKey");
            retObj.put("MESSAGE", messageObj);
            retObj.put("STATUS", 200);

            //when(NetworkCommunicator.sendPost(any(String.class), any(JSONObject.class))).thenReturn(retObj);

            recorder = new Recorder("http://localhost:3000", "fakeDatasetKey");
            IncrementalRecorder incRecorder = recorder.getIncrementalDataset("testDatasetName", false);
            //CompletableFuture<Boolean> res = incRecorder.addDataPoint(1595506316000L, "accX", 123);
            //Assertions.assertTrue(res.get());
        }

    }

    @Test
    public void addDataPointWithoutTime() throws Exception{
        try (MockedStatic<NetworkCommunicator> communicator = Mockito.mockStatic(NetworkCommunicator.class)) {
            Assertions.assertThrows(UnsupportedOperationException.class, () -> {
                JSONObject retObj = new JSONObject();
                JSONObject messageObj = new JSONObject();
                messageObj.put("datasetKey", "fakeDatasetKey");
                retObj.put("MESSAGE", messageObj);
                retObj.put("STATUS", 200);

                //when(NetworkCommunicator.sendPost(any(String.class), any(JSONObject.class))).thenReturn(retObj);

                recorder = new Recorder("http://localhost:3000", "fakeDatasetKey");
                IncrementalRecorder incRecorder = recorder.getIncrementalDataset("testDatasetName", false);
                //CompletableFuture<Boolean> res = incRecorder.addDataPoint("accX", 123);
                //Assertions.assertTrue(res.get());
            });
        }
    }

    @Test
    public void generateRecorderFails() throws Exception {
        try (MockedStatic<NetworkCommunicator> communicator = Mockito.mockStatic(NetworkCommunicator.class)) {
            Assertions.assertThrows(Exception.class, () -> {
                JSONObject retObj = new JSONObject();
                JSONObject messageObj = new JSONObject();
                messageObj.put("datasetKey", "fakeDatasetKey");
                retObj.put("MESSAGE", messageObj);
                retObj.put("STATUS", 400);
                //when(NetworkCommunicator.sendPost(any(String.class), any(JSONObject.class))).thenReturn(retObj);
                recorder = new Recorder("http://localhost:3000", "fakeKey");
                IncrementalRecorder incRecorder = recorder.getIncrementalDataset("testDatasetName", false);
            });
        }
    }

    @Test
    public void generateRecorderNoNetwork() throws Exception {
        try (MockedStatic<NetworkCommunicator> communicator = Mockito.mockStatic(NetworkCommunicator.class)) {
            Assertions.assertThrows(Exception.class, () -> {
                //when(NetworkCommunicator.sendPost(any(String.class), any(JSONObject.class))).thenReturn(null);
                recorder = new Recorder("http://localhost:3000", "fakeKey");
                IncrementalRecorder incRecorder = recorder.getIncrementalDataset("testDatasetName", false);
            });
        }
    }
}
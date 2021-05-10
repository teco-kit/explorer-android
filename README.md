# explorer-java
![Tests](https://github.com/teco-kit/explorer-android/actions/workflows/test.yml/badge.svg)

Java library for https://github.com/teco-kit/explorer. 
Can be used to upload datasets as whole or incrementally. 
Written in Java. Can be used in Android projects.


## How to use

#### Upload datasets as a whole

```java
Recorder recorder = new Recorder("explorerBackendUrl", "deviceApiKey");
boolean res = recorder.sendDataset(JSONObject); // Dataset as JSONObject
```

#### Upload datasets in increments with custom timestamps

```java
Recorder recorder = new Recorder("explorerBackendUrl", "deviceApiKey");
IncrementalRecorder incRecorder = recorder.getIncrementalDataset(false); // false to use custom timestamps
boolean res = incRecorder.addDataPoint("accX", 123, 1595506316); // true if successful, false if not.

// This will throw an UnsupportedOperationException because no timestamp was provided
boolean res = incRecorder.addDataPoint("accX", 124);
```

#### Upload datasets in increments with timestamps from server

```java
Recorder recorder = new Recorder("explorerBackendUrl", "deviceApiKey");
IncrementalRecorder incRecorder = recorder.getIncrementalDataset(true); // true to use servertime
boolean res = incRecorder.addDataPoint("accX", 123); // true if successful, false if not

// This will throw an UnsupportedOperationException because a timestamp was provided
boolean res = incRecorder.addDataPoint("accX", 123, 1595506316); 
```

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

#### Upload datasets in increments

```java
Recorder recorder = new Recorder("explorerBackendUrl", "deviceApiKey");
IncrementalRecorder incRecorder = recorder.getIncrementalDataset(false); // true if you want to use servertime
boolean res = incRecorder.addDataPoint("accX", 123, 1595506316); // true if successful, false if not
```

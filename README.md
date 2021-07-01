# explorer-java
![Tests](https://github.com/teco-kit/explorer-android/actions/workflows/deploy.yml/badge.svg)
![Maven Central](https://img.shields.io/maven-central/v/edu.teco.explorer/ExplorerJava?color=%2348c653)

Java library for https://github.com/teco-kit/explorer. 
Can be used to upload datasets as whole or incrementally. 
Written in Java. Can be used in Android projects.

## How to install
The library can be found in Maven Central.

### Gradle
1. Add mavenCentral to you repositories if it is not alreaedy there
```gradle
repositories {
  mavenCentral()
}
```

2. Import the library
```gradle
dependencies {
  implementation 'edu.teco.explorer:ExplorerJava:${VERSION}
}
```

### Maven
Include the library as a dependency in pom.xml
```xml
<dependencies>
  <dependency>
    <groupId>edu.teco.explorer</groupId>
    <artifactId>ExplorerJava</artifactId>
    <version>${VERSION}</version>
  </dependency>
</dependencies>
```


## How to use

#### Upload datasets as a whole

```java
Recorder recorder = new Recorder("explorerBackendUrl", "deviceApiKey");
boolean res = recorder.sendDataset(JSONObject); // Dataset as JSONObject
```

#### Upload datasets in increments with custom timestamps

```java
Recorder recorder = new Recorder("explorerBackendUrl", "deviceApiKey");
IncrementalRecorder incRecorder = recorder.getIncrementalDataset("datasetName", false); // false to use custom timestamps

// The CompletableFuture indicates whether the transmission was successful
CompletableFuture<Boolean> res = incRecorder.addDataPoint("accX", 123, 1595506316);

// This will throw an UnsupportedOperationException because no timestamp was provided
CompletableFuture<Boolean> res = incRecorder.addDataPoint("accX", 124);

incRecorder.onComplete();
```

#### Upload datasets in increments with timestamps from server

```java
Recorder recorder = new Recorder("explorerBackendUrl", "deviceApiKey");
IncrementalRecorder incRecorder = recorder.getIncrementalDataset("datasetName", true); // true to use servertime

// The CompletableFuture indicates whether the transmission was successful
CompletableFuture<Boolean> res = incRecorder.addDataPoint("accX", 123);

// This will throw an UnsupportedOperationException because a timestamp was provided
CompletableFuture<Boolean> res = incRecorder.addDataPoint("accX", 123, 1595506316); 
```

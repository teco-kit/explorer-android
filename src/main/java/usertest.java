import edu.teco.explorer.IncrementalRecorder;
import edu.teco.explorer.Recorder;

public class usertest {

    public static void main(String[] args) {
        Recorder recorder = new Recorder("http://localhost:3000", "0qHnsoMBo0a/FqLI95NLf0PEj50iIZP1w8QD0GsB1d5sNgerP2Gwh39iZvy6GqrtXJxMJbuiCCvMYjq0EqNfBw==");
        try {
            IncrementalRecorder rec = recorder.getIncrementalDataset("testNameJava4", true);
            for (long i = 0; i < 10; i++) {
                //rec.addDataPoint(1625298120220L + (i * 1000), "accX", i);
                long start = System.currentTimeMillis();
                rec.addDataPoint( "accX", i);
                long finish = System.currentTimeMillis();
                long timeElapsed = finish - start;
                Thread.sleep(100 - timeElapsed);

            }
            rec.onComplete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

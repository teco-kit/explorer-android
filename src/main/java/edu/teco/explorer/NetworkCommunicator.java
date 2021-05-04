package edu.teco.explorer;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkCommunicator {

    /**
     * Sends data with post request to server and gets its response
     * @param requestURL The URL you want to send the data to
     * @param sendObject The data to be send as JsonObject
     * @return A JsonObject with your data
     */
    public static JSONObject sendPost(final String requestURL, final JSONObject sendObject) {
        final JSONObject returnObj = new JSONObject();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(requestURL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    // Sending the data
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(sendObject.toString());
                    os.flush();
                    os.close();

                    boolean statusNoError = conn.getResponseCode() < 200 || conn.getResponseCode() > 299;
                    BufferedReader br;
                    if (statusNoError) {
                        br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    } else {
                        br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    }
                    StringBuilder builder = new StringBuilder();
                    String strCurrentLine;
                    while ((strCurrentLine = br.readLine()) != null) {
                        builder.append(strCurrentLine);
                    }
                    String msg = builder.toString();
                    returnObj.put("STATUS", conn.getResponseCode());
                    if (statusNoError) {
                        returnObj.put("ERROR", new JSONObject(msg));
                    } else {
                        returnObj.put("MESSAGE", new JSONObject(msg));
                    }

                    conn.disconnect();
;
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
            return returnObj;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}

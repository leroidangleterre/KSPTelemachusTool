package ksptelemachustool;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author arthu
 */
public class KSPTelemachusTool {

    public static void main(String[] args) {

        Timer timer = new Timer();
        int delay = 0;
        int period = 10000;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                URL url;
                try {
                    url = new URL("http://127.0.0.1:8085/telemachus/datalink?long=v.long&lat=v.lat&alt=v.altitude");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");

                    int status = con.getResponseCode();

                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuilder content = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();
                    System.out.println(content.toString());
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        },
                delay,
                period);

    }

}

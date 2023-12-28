package ksptelemachustool;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
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
        int period = 1000;

        KSPHistory history = new KSPHistory();
        KSPFrame frame = new KSPFrame(history);

        String filepath = "./coordinates.txt";
        try {
            FileWriter writer = new FileWriter(filepath);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    URL url;
                    try {

                        url = new URL("http://127.0.0.1:8085/telemachus/datalink?"
                                + "time=v.missionTime"
                                + "&long=v.long"
                                + "&lat=v.lat"
                                + "&alt=v.altitude"
                                + "&heightFromTerrain=v.heightFromTerrain"
                                + "&terrainHeight=v.terrainHeight"
                                + "&throttle=f.throttle"
                                + "&speed=v.surfaceSpeed"
                                + "&vSpeed=v.verticalSpeed"
                        );
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod("GET");

                        int status = con.getResponseCode();

                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String inputLine;
                        StringBuilder content = new StringBuilder();
                        while ((inputLine = in.readLine()) != null) {
                            content.append(inputLine);
                        }

                        String infoToBeWritten = content.toString();
                        if (!infoToBeWritten.contains("error")) {
                            writer.write(infoToBeWritten + '\n');
                            String text = content.toString();
                            String dataLines[] = text.split(",");

                            String currentAltString = dataLines[3].split(":")[1];
                            String currentAirspeedString = dataLines[7].split(":")[1];
                            currentAltString = truncate2(currentAltString);
                            currentAirspeedString = truncate2(currentAirspeedString);
                            double currentAlt = new Double(currentAltString);
                            double currentAirspeed = new Double(currentAirspeedString);

                            System.out.println("altitude: " + currentAlt + ", airspeed: " + currentAirspeed);
                            history.update(currentAlt, currentAirspeed);
                            frame.repaint();
                        }
                        writer.flush();

                        in.close();

                    } catch (IOException ex) {
                        System.out.println("exception :" + ex);
                    }
                }

                /**
                 * Keep only two digits after decimal point
                 *
                 */
                private String truncate2(String value) {
                    int decimalPointPosition = value.indexOf('.');
                    if (decimalPointPosition == -1) {
                        // No decimal point, no change needed.
                        return value;
                    } else {
                        // keep characters from beginning to 2 after decimal point.
                        return value.substring(0, decimalPointPosition + 3);
                    }
                }
            },
                    delay,
                    period);

        } catch (IOException e) {
            System.out.println("Cannot write to file.");
        }
    }

}

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
                            currentAltString = KSPUtils.truncate(currentAltString, 2);
                            currentAirspeedString = KSPUtils.truncate(currentAirspeedString, 2);
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

            },
                    delay,
                    period);

        } catch (IOException e) {
            System.out.println("Cannot write to file.");
        }
    }

}

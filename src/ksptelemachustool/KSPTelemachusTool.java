package ksptelemachustool;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author arthu
 */
public class KSPTelemachusTool {

    public static void main(String[] args) {

        Timer timer = new Timer();
        int delay = 0;
        int period = 1000;

        int width = 1000;
        int height = 700;

        KSPHistory history = new KSPHistory();

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension windowSize = new Dimension(width, height);

        KSPPanel panel = new KSPPanel(history);
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);

        JPanel toolbar = new JPanel();

        // Two mutually-excluded checkboxes
        JCheckBox groundSpeedBox = new JCheckBox("Ground Speed", true);
        JCheckBox orbitalSpeedBox = new JCheckBox("Orbital Speed", false);

        groundSpeedBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                groundSpeedBox.setSelected(true);
                orbitalSpeedBox.setSelected(false);
                panel.setOrbitalSpeed(false);
            }
        });
        toolbar.add(groundSpeedBox);

        orbitalSpeedBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                groundSpeedBox.setSelected(false);
                orbitalSpeedBox.setSelected(true);
                panel.setOrbitalSpeed(true);
            }
        });
        toolbar.add(orbitalSpeedBox);

        frame.add(toolbar, BorderLayout.SOUTH);

        frame.setPreferredSize(windowSize);
        frame.pack();
        frame.setVisible(true);

        String filepath = "./coordinates.txt";
        try {
            FileWriter writer = new FileWriter(filepath);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    URL url;
                    try {

                        url = new URL("http://127.0.0.1:8085/telemachus/datalink?"
                                /* 0 */ + "time=v.missionTime"
                                /* 1 */ + "&long=v.long"
                                /* 2*/ + "&lat=v.lat"
                                /* 3 */ + "&alt=v.altitude"
                                /* 4 */ + "&heightFromTerrain=v.heightFromTerrain"
                                /* 5 */ + "&terrainHeight=v.terrainHeight"
                                /* 6 */ + "&throttle=f.throttle"
                                /* 7 */ + "&speed=v.surfaceSpeed"
                                /* 8 */ + "&vSpeed=v.verticalSpeed"
                                /* 9 */ + "&vOrbitalVelocity=v.orbitalVelocity"
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
                            String currentOrbitalspeedString = dataLines[9].split(":")[1];
                            currentAltString = KSPUtils.truncate(currentAltString, 2);
                            currentAirspeedString = KSPUtils.truncate(currentAirspeedString, 2);
                            currentOrbitalspeedString = KSPUtils.truncate(currentOrbitalspeedString, 2);

                            double currentAlt = new Double(currentAltString);
                            double currentAirspeed = new Double(currentAirspeedString);
                            double currentOrbitalSpeed = new Double(currentOrbitalspeedString);

                            history.update(currentAlt, currentAirspeed, currentOrbitalSpeed);
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

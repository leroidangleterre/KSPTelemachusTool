package ksptelemachustool;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 *
 * @author arthu
 */
public class KSPPanel extends JPanel {

    private KSPHistory history;

    private double minSpeed = -10, maxSpeed = 10, minAlt = 0, maxAlt = 0;

    public KSPPanel(KSPHistory newHistory) {
        super();
        this.history = newHistory;
    }

    @Override
    public void paintComponent(Graphics g) {
//        System.out.println("KSP panel repainting now.");

        int radius = 5;

        clearScreen(g);

        int graphicsWidth = g.getClipBounds().width;
        int graphicsHeight = g.getClipBounds().height;

        drawSpeedAxis(g, graphicsHeight, graphicsWidth);
        drawAltitudeAxis(g, graphicsHeight, graphicsWidth);

        // Paint dots
        g.setColor(Color.red);
        for (int i = 0; i < history.getNbElements(); i++) {
            double speed = history.getSpeed(i);
            double alt = history.getAlt(i);
            int xApp = (int) ((speed - minSpeed) * graphicsWidth / (maxSpeed - minSpeed));
            int yApp = (int) ((1 - (alt - minAlt) / (maxAlt - minAlt)) * graphicsHeight);

            g.fillOval(xApp - radius, yApp - radius, 2 * radius, 2 * radius);
        }
    }

    private void drawSpeedAxis(Graphics g, int graphicsHeight, int graphicsWidth) {
        // Set max speed
        maxSpeed = 0;
        for (int i = 0; i < history.getNbElements(); i++) {
            double currentSpeed = history.getSpeed(i);
            maxSpeed = Math.max(maxSpeed, currentSpeed + 1);
        }

        // Paint graph axes
        g.setColor(Color.black);
        g.drawLine(0, graphicsHeight - 1, graphicsWidth, graphicsHeight - 1);

        double speedUnit = Math.pow(10, Math.floor(Math.log10(maxSpeed)));
        Graphics2D g2 = (Graphics2D) g;
        for (double speed = 0; speed < maxSpeed; speed += speedUnit) {
            int xApp = (int) (speed * graphicsWidth / maxSpeed);
            g2.setStroke(new BasicStroke(5));
            g2.drawLine(xApp, graphicsHeight - 0, xApp, graphicsHeight - 10);
            g2.drawString(KSPUtils.truncate(speed + "", 0), xApp, graphicsHeight - 15);

            // Add subdivisions
            for (int i = 1; i <= 9; i++) {
                double intermSpeed = speed + i * speedUnit / 10;
                xApp = (int) (intermSpeed * graphicsWidth / maxSpeed);
                if (i == 5) {
                    g2.setStroke(new BasicStroke(3));
                } else {
                    g2.setStroke(new BasicStroke(1));
                }
                g.drawLine(xApp, graphicsHeight - 0, xApp, graphicsHeight - 10);
            }
        }
    }

    private void drawAltitudeAxis(Graphics g, int graphicsHeight, int graphicsWidth) {
        // Set max alt
        maxAlt = 0;
        minAlt = 10000;
        for (int i = 0; i < history.getNbElements(); i++) {
            double currentAlt = history.getAlt(i);
            maxAlt = Math.max(maxAlt, currentAlt + 1);
            if (maxAlt > 1000) {
                minAlt = 0;
            } else {
                minAlt = Math.min(minAlt, currentAlt - 1);
            }
        }
        System.out.println("alt: from " + minAlt + " to " + maxAlt);

        // Paint graph axes
        g.setColor(Color.black);
        g.drawLine(1, graphicsHeight, 1, 0);

        double altUnit = Math.pow(10, Math.floor(Math.log10(maxAlt - minAlt)));
        Graphics2D g2 = (Graphics2D) g;
        for (double alt = minAlt; alt <= maxAlt; alt += altUnit) {
            int yApp = (int) ((1 - (alt - minAlt) / (maxAlt - minAlt)) * graphicsHeight);
            System.out.println("yApp = " + yApp + ", graphics height: " + graphicsHeight);
            g2.setStroke(new BasicStroke(5));
            g2.drawLine(10, yApp, 15, yApp);
            g2.drawString(KSPUtils.truncate(alt + "", 0), 15, yApp);

            // Add subdivisions
//            for (int i = 1; i <= 9; i++) {                double intermAlt = alt + i * altUnit / 10;                yApp = (int) ((1 - (intermAlt - minAlt) / (maxAlt - minAlt)) * graphicsHeight);                if (i == 5) {                    g2.setStroke(new BasicStroke(3));                } else {                    g2.setStroke(new BasicStroke(1));                }                g.drawLine(10, graphicsHeight - yApp, 12, graphicsHeight - yApp);            }
        }
    }

    private void clearScreen(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
    }

    protected void clearData() {
        history.reset();
    }
}

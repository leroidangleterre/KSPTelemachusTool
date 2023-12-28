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

    private double maxSpeed, maxAlt;

    public KSPPanel(KSPHistory newHistory) {
        super();
        this.history = newHistory;
    }

    @Override
    public void paintComponent(Graphics g) {
        System.out.println("KSP panel repainting now.");

        int radius = 5;

        clearScreen(g);

        // Set max speed and alt
        for (int i = 0; i < history.getNbElements(); i++) {
            double currentSpeed = history.getSpeed(i);
            double currentAlt = history.getAlt(i);
            maxSpeed = Math.max(maxSpeed, currentSpeed + 1);
            maxAlt = Math.max(maxAlt, currentAlt + 1);
        }

        int graphicsWidth = g.getClipBounds().width;
        int graphicsHeight = g.getClipBounds().height;

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

        // Paint dots
        g.setColor(Color.red);
        for (int i = 0; i < history.getNbElements(); i++) {
            double speed = history.getSpeed(i);
            double alt = history.getAlt(i);
            int xApp = (int) (speed * graphicsWidth / maxSpeed);
            int yApp = graphicsHeight - (int) (alt * graphicsHeight / maxAlt);

            g.fillOval(xApp - radius, yApp - radius, 2 * radius, 2 * radius);
        }
    }

    private void clearScreen(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
    }
}

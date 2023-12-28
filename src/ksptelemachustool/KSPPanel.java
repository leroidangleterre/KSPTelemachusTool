package ksptelemachustool;

import java.awt.Color;
import java.awt.Graphics;
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

        // Paint dots
        int graphicsWidth = g.getClipBounds().width;
        int graphicsHeight = g.getClipBounds().height;
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

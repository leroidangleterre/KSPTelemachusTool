package ksptelemachustool;

import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 * GUI for altitude and airspeed graphic display
 *
 * @author arthu
 */
public class KSPFrame extends JFrame {

    private int width = 600;
    private int height = 480;

    private KSPPanel panel;

    public KSPFrame(KSPHistory history) {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(width, height));
        setSize(new Dimension(width, height));
        panel = new KSPPanel(history);
        this.setContentPane(panel);
        setVisible(true);
    }

}

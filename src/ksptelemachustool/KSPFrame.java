package ksptelemachustool;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * GUI for altitude and airspeed graphic display
 *
 * @author arthu
 */
public class KSPFrame extends JFrame {

    private int width = 1000;
    private int height = 1000;

    private KSPPanel panel;

    public KSPFrame(KSPHistory history) {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(width, height));
        setSize(new Dimension(width, height));
        panel = new KSPPanel(history);
        this.setContentPane(panel);

        JButton clearButton = new JButton();
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.clearData();
            }
        });

        this.setLayout(new BorderLayout());
        this.add(clearButton, BorderLayout.SOUTH);
        setVisible(true);
    }

}

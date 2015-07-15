package pl.morecraft.dev.studia.womw.base.gui;

import pl.morecraft.dev.studia.womw.misc.Configuration;
import pl.morecraft.dev.studia.womw.misc.CoreBasicMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import static pl.morecraft.dev.studia.womw.misc.LoadFromRes.loadImageAsImage;

public class MainFrame extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final MainPanel mainPresentationPanel;

    public MainFrame() {

        if (System.getProperty("os.name").startsWith("Windows"))
            CoreBasicMain.setUILookAndFeel();

        this.setTitle("World of MoreWires");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new GridLayout(1, 1));

        this.setIconImage(loadImageAsImage("icons/womw_icon.png"));

		/*
         * Initializing MainPanel
		 */
        this.mainPresentationPanel = new MainPanel();

		/*
         * Size of MainPanel (MainFrame too)
		 */
        this.mainPresentationPanel.setPreferredSize(new Dimension(800, 600));

		/*
		 * Adding components
		 */
        this.add(this.mainPresentationPanel);
        this.setMinimumSize(this.mainPresentationPanel.getMinimumSize());

		/*
		 * Adding closing operation
		 */
        this.addWindowListener(exitListener);

		/*
		 * Packing and showing
		 */
        this.pack();
        this.setLocationRelativeTo(null);

    }

    private static WindowListener exitListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            if (Configuration.AUTOSAVE)
                Configuration.saveConfiguration(null);
        }
    };
}

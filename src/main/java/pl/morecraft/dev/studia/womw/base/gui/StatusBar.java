package pl.morecraft.dev.studia.womw.base.gui;

import pl.morecraft.dev.studia.womw.misc.Configuration;

import javax.swing.*;
import java.awt.*;

public class StatusBar extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private final JLabel point;
    private final JLabel timer;
    private final JLabel cpr;
    private final JLabel dbc;

    public StatusBar() {
        this.setMinimumSize(new Dimension(100, 20));
        this.setPreferredSize(new Dimension(100, 20));
        this.setLayout(new GridLayout(1, 7));

        this.cpr = new JLabel("  CPR: " + Configuration.CYCLES_PER_ROUND) {
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(213, 223, 229));
                g2d.drawLine(this.getWidth() - 40, 0, this.getWidth() - 40, this.getHeight());
            }
        };
        this.add(this.cpr);

        this.dbc = new JLabel("DBC: " + Configuration.DELAY_BETWEEN_CYCLES + "ms");
        this.add(this.dbc);

        this.add(new JLabel());
        this.add(new JLabel());
        this.add(new JLabel());

        this.timer = new JLabel("T: 0ms", JLabel.RIGHT);
        this.add(this.timer);

        this.point = new JLabel("P: X=0 Y=0  ", JLabel.RIGHT) {
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(213, 223, 229));
                g2d.drawLine(14, 0, 14, this.getHeight());
            }
        };
        this.add(this.point);

    }

    public void showPoint(int x, int y) {
        this.point.setText("P: X=" + String.format("%-4s", x) + " Y=" + String.format("%-4s", y));
    }

    public void showMilisec(long l) {
        this.timer.setText("T: " + String.format("%6s", l) + "ms");
    }

    public void showFormConf() {
        this.dbc.setText("DBC: " + Configuration.DELAY_BETWEEN_CYCLES + "ms");
        this.cpr.setText("  CPR: " + Configuration.CYCLES_PER_ROUND);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(213, 223, 229));
        g2d.drawLine(0, 0, this.getWidth(), 0);
        g2d.drawLine(0, 19, this.getWidth(), 19);
        g2d.setColor(Color.WHITE);
        g2d.drawLine(0, 1, this.getWidth(), 1);
        g2d.drawLine(0, 18, this.getWidth(), 18);

    }

}

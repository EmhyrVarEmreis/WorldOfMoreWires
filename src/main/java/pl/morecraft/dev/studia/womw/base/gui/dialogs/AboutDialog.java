package pl.morecraft.dev.studia.womw.base.gui.dialogs;

import pl.morecraft.dev.studia.womw.misc.LoadFromRes;
import pl.morecraft.dev.studia.womw.misc.Translator;

import javax.swing.*;
import java.awt.*;

public class AboutDialog extends JDialog {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private final Image imgLogo;
    private final JLabel programName;
    private final JLabel author;
    private final JLabel date;
    private final JLabel req;
    private final JPanel imgP;
    private final JButton closeButton;

    public AboutDialog(Frame parent) {
        super(parent);

        this.setLayout(null);
        this.setSize(400, 180);
        this.setResizable(false);
        this.setTitle(Translator.getString("ABOUT"));

        this.closeButton = new JButton(LoadFromRes.loadImageAsIconImage("icons/delete.png"));
        this.closeButton.setBounds(380, 126, 20, 20);
        this.closeButton.setBorderPainted(false);
        this.closeButton.setBorder(BorderFactory.createEmptyBorder());
        this.closeButton.addActionListener(e -> AboutDialog.this.dispose());

        this.imgLogo = LoadFromRes.loadImageAsImage("logo_m.png");
        this.programName = new JLabel("World of MoreWires", JLabel.RIGHT);
        this.programName.setFont(new Font("Tahoma", Font.BOLD, 20));
        this.programName.setBounds(140, 5, 240, 20);

        this.author = new JLabel("Mateusz Stefaniak", JLabel.RIGHT);
        this.author.setFont(new Font("Tahoma", Font.PLAIN, 12));
        this.author.setBounds(140, 20, 240, 20);

        this.date = new JLabel("2014", JLabel.RIGHT);
        this.date.setFont(new Font("Tahoma", Font.PLAIN, 10));
        this.date.setBounds(140, 30, 240, 20);

        this.req = new JLabel("<html><b>" + Translator.getString("REQUIREMENTS") + "</b><br/>CPU: Pentium 4 3.0 GHz<br/>RAM: 1024 MB<br/>" +
                "GPU: " + Translator.getString("REQUIREMENTS_GPU") + "<br/>HDD: 200 MB<br/>Java: 1.7</html>");
        this.req.setFont(new Font("Tahoma", Font.PLAIN, 10));
        this.req.setBounds(140, 45, 240, 100);

        this.imgP = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.drawImage(AboutDialog.this.imgLogo, 0, 0, this);
            }
        };
        this.imgP.setBounds(0, 0, 128, 128);

        System.out.println();

        this.add(this.closeButton);
        this.add(this.imgP);
        this.add(this.programName);
        this.add(this.author);
        this.add(this.date);
        this.add(this.req);

    }
}

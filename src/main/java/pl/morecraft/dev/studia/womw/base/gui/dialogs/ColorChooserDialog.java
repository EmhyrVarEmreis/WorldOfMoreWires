package pl.morecraft.dev.studia.womw.base.gui.dialogs;

import pl.morecraft.dev.studia.womw.misc.LoadFromRes;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ColorChooserDialog extends JDialog {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Color color = null;
    private final JPanel c;
    private final JPanel cR;
    private final JPanel cG;
    private final JPanel cB;
    private final JButton bA;
    private final JSlider jsR, jsG, jsB;

    public ColorChooserDialog(Frame parent, Color initColor) {
        super(parent);

        if (initColor == null)
            this.color = new Color(0, 0, 0);
        else
            this.color = initColor;

        this.setLayout(new GridLayout(5, 1));
        this.setSize(300, 340);
        this.setTitle("Wybierz kolor");

        this.c = new JPanel();
        this.c.setBackground(initColor);

        this.jsR = new JSlider(0, 255, this.color.getRed());
        this.jsR.addChangeListener(arg0 -> ColorChooserDialog.this.refreshColor());
        this.jsG = new JSlider(0, 255, this.color.getGreen());
        this.jsG.addChangeListener(arg0 -> ColorChooserDialog.this.refreshColor());
        this.jsB = new JSlider(0, 255, this.color.getBlue());
        this.jsB.addChangeListener(arg0 -> ColorChooserDialog.this.refreshColor());

        this.cR = new JPanel();
        this.cR.setBorder(BorderFactory.createTitledBorder(null, "R", TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 11)));
        this.cR.add(this.jsR);
        this.cG = new JPanel();
        this.cG.setBorder(BorderFactory.createTitledBorder(null, "G", TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 11)));
        this.cG.add(this.jsG);
        this.cB = new JPanel();
        this.cB.setBorder(BorderFactory.createTitledBorder(null, "B", TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 11)));
        this.cB.add(this.jsB);

        this.bA = new JButton(LoadFromRes.loadImageAsIconImage("icons/apply.png"));
        this.bA.addActionListener(e -> ColorChooserDialog.this.dispose());

        this.add(this.c);
        this.add(this.cR);
        this.add(this.cG);
        this.add(this.cB);
        this.add(this.bA);

    }

    private void refreshColor() {
        this.color = new Color(this.jsR.getValue(), this.jsG.getValue(), this.jsB.getValue());
        this.c.setBackground(this.color);
    }

    public Color getColor() {
        return this.color;
    }
}

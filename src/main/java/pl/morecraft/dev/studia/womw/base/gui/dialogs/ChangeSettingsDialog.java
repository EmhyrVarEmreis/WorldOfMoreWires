package pl.morecraft.dev.studia.womw.base.gui.dialogs;

import pl.morecraft.dev.studia.womw.misc.Configuration;
import pl.morecraft.dev.studia.womw.misc.LoadFromRes;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ChangeSettingsDialog extends JDialog {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private final JPanel colors, p2, p3;
    private final JPanel[] plc;
    private final JLabel[] llc;
    private final JButton[] blc;
    private final String[] lsc;
    private final JButton bA, bClose;
    private final JSlider jss;
    private final JLabel l1a;
    private final JCheckBox jch1;

    public ChangeSettingsDialog(final Frame parent) {
        super(parent);

        this.setLayout(null);
        this.setSize(302, 315);
        this.setResizable(false);
        this.setTitle("Ustawienia");

        this.colors = new JPanel();
        this.colors.setBounds(0, 0, 300, 116);
        this.colors.setBorder(BorderFactory.createTitledBorder(null, "KOLORY", TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
                Font.BOLD, 11)));
        this.colors.setLayout(null);

        this.plc = new JPanel[4];
        this.llc = new JLabel[4];
        this.blc = new JButton[4];
        this.lsc = new String[]{"Przewodnik", "Głowa elektronu", "Ogon elektronu", "Tło mapy"};

        for (int i = 0; i < 4; i++) {
            final int ii = i;
            this.plc[i] = new JPanel();
            this.plc[i].setBounds(190, 20 + i * 20, 30, 20);
            this.llc[i] = new JLabel(this.lsc[i]);
            this.llc[i].setBounds(30, 20 + i * 20, 120, 20);
            this.blc[i] = new JButton(LoadFromRes.loadImageAsIconImage("icons/edit.png"));
            this.blc[i].setBounds(240, 20 + i * 20, 20, 20);
            this.blc[i].addActionListener(e -> {
                ColorChooserDialog d = new ColorChooserDialog(parent, ChangeSettingsDialog.this.plc[ii].getBackground());
                d.setLocationRelativeTo(ChangeSettingsDialog.this);
                d.setModal(true);
                d.setVisible(true);
                ChangeSettingsDialog.this.plc[ii].setBackground(d.getColor());
            });
            // this.blc[i].setContentAreaFilled(false);
            this.colors.add(this.plc[i]);
            this.colors.add(this.llc[i]);
            this.colors.add(this.blc[i]);
        }

        this.plc[0].setBackground(Configuration.CONDUCTOR_COLOR);
        this.plc[1].setBackground(Configuration.HEAD_COLOR);
        this.plc[2].setBackground(Configuration.TAIL_COLOR);
        this.plc[3].setBackground(Configuration.NONE_COLOR);

        this.p2 = new JPanel();
        this.p2.setBounds(0, 116, 300, 56);
        this.p2.setBorder(BorderFactory
                .createTitledBorder(null, "SKALA", TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 11)));
        this.p2.setLayout(null);

        this.l1a = new JLabel(Configuration.SCALE + "", JLabel.RIGHT);
        this.l1a.setBounds(250, 20, 20, 20);

        this.jss = new JSlider(1, Configuration.MAX_SCALE, Configuration.SCALE);
        this.jss.setBounds(30, 20, 200, 20);
        this.jss.addChangeListener(arg0 -> ChangeSettingsDialog.this.l1a.setText(ChangeSettingsDialog.this.jss.getValue() + ""));

        this.p3 = new JPanel();
        this.p3.setBounds(0, 172, 300, 67);
        this.p3.setBorder(BorderFactory.createTitledBorder(null, "AUTOZAPIS USTAWIEŃ", TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
                Font.BOLD, 11)));
        this.p3.setLayout(null);

        this.jch1 = new JCheckBox("<html>Autozapis ostatnich ustawień przy zamykaniu programu. (Zapis do pliku w folderze z programem)</html>");
        this.jch1.setFont(new Font("Arial", Font.PLAIN, 9));
        this.jch1.setBounds(15, 20, 280, 30);
        this.jch1.setSelected(Configuration.AUTOSAVE);
        this.jch1.addChangeListener(e -> Configuration.AUTOSAVE = ChangeSettingsDialog.this.jch1.isSelected());

        this.p3.add(this.jch1);

        this.bA = new JButton(LoadFromRes.loadImageAsIconImage("icons/apply.png"));
        this.bA.setBounds(0, 244, 270, 30);
        this.bA.addActionListener(arg0 -> {
                    int n = JOptionPane.showConfirmDialog(ChangeSettingsDialog.this.getParent(),
                            "Czy chcesz zapisać konfigurację teraz? Zostanie zapisana w folderze z programem.", "Pytanie", JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if (n == 0 || Configuration.AUTOSAVE) {
                        ChangeSettingsDialog.this.storeConf();
                        Configuration.saveConfiguration(null);
                        if (!Configuration.AUTOSAVE)
                            JOptionPane.showMessageDialog(ChangeSettingsDialog.this.getParent(), "Zapisano!", "Informacja",
                                    JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(ChangeSettingsDialog.this.getParent(),
                                "Konfiguracja zostanie zapomniana po ponownym uruchomieniu programu!", "Informacja", JOptionPane.INFORMATION_MESSAGE);
                    }
                    ChangeSettingsDialog.this.dispose();
                }
        );

        this.bClose = new JButton(LoadFromRes.loadImageAsIconImage("icons/delete.png"));
        this.bClose.setBounds(270, 244, 30, 30);
        this.bClose.addActionListener(e -> ChangeSettingsDialog.this.dispose());

        this.p2.add(this.l1a);
        this.p2.add(this.jss);

        this.add(this.colors);
        this.add(this.p2);
        this.add(this.p3);
        this.add(this.bA);
        this.add(this.bClose);

    }

    private void storeConf() {
        Configuration.SCALE = this.jss.getValue();
        Configuration.CONDUCTOR_COLOR = this.plc[0].getBackground();
        Configuration.HEAD_COLOR = this.plc[1].getBackground();
        Configuration.TAIL_COLOR = this.plc[2].getBackground();
        Configuration.NONE_COLOR = this.plc[3].getBackground();
    }

}

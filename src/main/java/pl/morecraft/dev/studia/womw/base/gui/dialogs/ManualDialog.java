package pl.morecraft.dev.studia.womw.base.gui.dialogs;

import pl.morecraft.dev.studia.womw.misc.LoadFromRes;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ManualDialog extends JDialog {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final JEditorPane jep1;
    private final JScrollPane sc1;

    public ManualDialog(Frame parent) {
        super(parent);

        this.setLayout(new GridLayout(1, 1));
        this.setSize(600, 400);
        this.setTitle("Istrukcja obs�ugi interfejsu");

        this.jep1 = new JEditorPane();
        this.jep1.setEditable(false);
        try {
            this.jep1.setPage(ManualDialog.class.getResource("/" + LoadFromRes.RESOURCES_PREFIX + "resources/pomoc.html"));
        } catch (IOException e) {
            this.jep1.setText("Wyst�pi� b��d podczas �adowania instrukcji!");
        }

        this.sc1 = new JScrollPane(this.jep1, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        this.add(this.sc1);
    }
}

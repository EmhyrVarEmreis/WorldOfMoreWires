package pl.morecraft.dev.studia.womw.base.gui.dialogs;

import pl.morecraft.dev.studia.womw.misc.Configuration;
import pl.morecraft.dev.studia.womw.misc.Translator;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

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
        this.setTitle(Translator.getString("MANUAL_TITLE"));

        this.jep1 = new JEditorPane();
        this.jep1.setEditable(false);
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource(
                    "lng/" + Configuration.LANGUAGE.getCode() + "/help.html");
            if (url == null) {
                throw new IOException();
            }
            this.jep1.setPage(url);
        } catch (IOException e) {
            this.jep1.setText(Translator.getString("MANUAL_LOADING_ERROR"));
        }

        this.sc1 = new JScrollPane(this.jep1, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        this.add(this.sc1);
    }
}

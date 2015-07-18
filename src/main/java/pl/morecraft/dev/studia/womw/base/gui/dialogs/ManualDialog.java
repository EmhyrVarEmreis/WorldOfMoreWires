package pl.morecraft.dev.studia.womw.base.gui.dialogs;

import pl.morecraft.dev.studia.womw.misc.Configuration;
import pl.morecraft.dev.studia.womw.misc.LoadFromRes;
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
    private final JButton bClose;

    public ManualDialog(Frame parent) {
        super(parent);

        this.setLayout(new BorderLayout());
        this.setSize(600, 430);
        this.setTitle(Translator.getString("MANUAL_TITLE"));

        this.bClose = new JButton(LoadFromRes.loadImageAsIconImage("icons/apply.png"));
        this.bClose.setPreferredSize(new Dimension(500, 30));
        this.bClose.addActionListener(e -> ManualDialog.this.dispose());

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

        this.add(this.sc1, BorderLayout.CENTER);
        this.add(this.bClose, BorderLayout.PAGE_END);
    }
}

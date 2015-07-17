package pl.morecraft.dev.studia.womw.base.gui;

import pl.morecraft.dev.studia.womw.core.interfaces.Progressable;

import javax.swing.*;
import java.awt.*;

public class ProgressWaitingFrame extends JDialog implements Progressable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final JLabel action;
    // private JFrame parent;
    private final JProgressBar progressBar;

    public ProgressWaitingFrame() {
        this(null, "---");
    }

    public ProgressWaitingFrame(JFrame parent, String action) {

        super(parent);
        // this.parent = parent;
        this.setLayout(null);
        this.setSize(210, 100);
        this.setMinimumSize(new Dimension(210, 100));
        this.setLocationRelativeTo(parent);
        this.setTitle(action);
        this.setResizable(false);
        this.setModal(true);

        this.progressBar = new JProgressBar();
        this.progressBar.setBounds(10, 30, 180, 20);

        this.action = new JLabel(action);
        this.action.setBounds(10, 10, 180, 20);

        this.add(this.progressBar);
        this.add(this.action);

        this.pack();

        SwingUtilities.invokeLater(ProgressWaitingFrame.this::toDoFull);
        this.setVisible(true);
    }

    protected void close() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            // EXCEPTION
        }
        this.dispose();
    }

    @Override
    public int getMaximum() {
        return this.progressBar.getMaximum();
    }

    @Override
    public int getValue() {
        return this.progressBar.getValue();
    }

    @Override
    public void setMaximum(int progress) {
        if (this.progressBar != null)
            this.progressBar.setMaximum(progress);
    }

    @Override
    public void setValue(int progress) {
        if (this.progressBar != null) {
            this.progressBar.setValue(progress);
            if (progress == this.progressBar.getMaximum())
                this.close();
        }
    }

    private void toDoFull() {
        try {
            this.toDo();
        } catch (Exception e) {
            // EXCEPTION
        }
        //close();
    }

    public void toDo() throws Exception {
        // TO OVERRIDE
    }

}

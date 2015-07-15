package pl.morecraft.dev.studia.womw.base.gui;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TextAreaSelectable extends JTextArea implements MouseListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    Action selectLine;
    private boolean isTextChanged = false;
    private String lastSelectedText = "";

    public TextAreaSelectable() {
        this.addMouseListener(this);

        this.selectLine = this.getAction(DefaultEditorKit.selectLineAction);

    }

    public boolean isTextChanged() {
        return this.isTextChanged;
    }

    private Action getAction(String name) {
        Action action = null;
        Action[] actions = this.getActions();

        for (Action actionElem : actions) {
            if (name.equals(actionElem.getValue(Action.NAME).toString())) {
                action = actionElem;
                break;
            }
        }

        return action;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
            this.selectLine.actionPerformed(null);
            if (this.getSelectedText() == null || this.lastSelectedText.equalsIgnoreCase(this.getSelectedText())
                    || this.getSelectedText().contains("\n")) {
                this.isTextChanged = false;
            } else {
                this.isTextChanged = true;
                this.lastSelectedText = this.getSelectedText();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}

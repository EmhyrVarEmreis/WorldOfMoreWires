package pl.morecraft.dev.studia.womw.base.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HandScrollListener extends MouseAdapter {
    private final Cursor defCursor = Cursor.getDefaultCursor();
    private final Cursor hndCursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
    private final JComponent image;
    private final Point pp = new Point();
    private final JScrollPane toScroll;

    public HandScrollListener(JComponent image, JScrollPane toScroll) {
        this.image = image;
        this.toScroll = toScroll;
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        JViewport vport = this.toScroll.getViewport();
        Point cp = e.getPoint();
        Point vp = vport.getViewPosition();
        vp.translate(this.pp.x - cp.x, this.pp.y - cp.y);
        this.image.scrollRectToVisible(new Rectangle(vp, vport.getSize()));
        this.pp.setLocation(cp);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.image.setCursor(this.hndCursor);
        this.pp.setLocation(e.getPoint());

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.image.setCursor(this.defCursor);
        this.image.repaint();

    }
}
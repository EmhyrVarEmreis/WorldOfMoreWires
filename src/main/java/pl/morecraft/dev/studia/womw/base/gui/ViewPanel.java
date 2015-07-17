package pl.morecraft.dev.studia.womw.base.gui;

import pl.morecraft.dev.studia.womw.base.engines.v1.CellsMapV1;
import pl.morecraft.dev.studia.womw.core.CellState;
import pl.morecraft.dev.studia.womw.core.interfaces.CellsMapInterface;
import pl.morecraft.dev.studia.womw.core.interfaces.UpdatableViewer;
import pl.morecraft.dev.studia.womw.misc.Configuration;
import pl.morecraft.dev.studia.womw.misc.OtherStuff;
import pl.morecraft.dev.studia.womw.misc.enums.EditingMode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ViewPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, UpdatableViewer<CellState, Point> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static int getMax(int a, int b) {
        return (a > b) ? a : b;
    }

    private Point currentPoint, lastEdited;
    private EditingMode editingMode;
    private BufferedImage image;
    private boolean isInEditingMode = false;
    private CellsMapInterface<CellState, Point, Map.Entry<Point, CellState>> mapMainFromWorker, mapToHoldStructs;
    private Dimension maximumDimension;

    private long timer1, timer2 = 0;

    private StatusBar statusBar;

    public ViewPanel() {
        this(null);
    }

    public ViewPanel(CellsMapInterface<CellState, Point, Map.Entry<Point, CellState>> map) {
        this.mapMainFromWorker = map;
        this.editingMode = EditingMode.NOTHING;

        this.image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

        this.maximumDimension = new Dimension();

        this.mapToHoldStructs = new CellsMapV1();

        if (Configuration.NONE_COLOR != null)
            this.setBackground(Configuration.NONE_COLOR);

        this.currentPoint = new Point(-1, -1);
        this.lastEdited = new Point(-1, -1);

        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);

    }

    private boolean editPointOnMap(Point p) {

        if ((p.x < 0) || (p.y < 0))
            return false;

        this.rescalePoint(p, Configuration.SCALE);

        if (this.lastEdited.equals(p))
            return false;

        this.lastEdited = (Point) p.clone();

        switch (this.editingMode) {
            case DELETE:
                this.mapMainFromWorker.putCell(p, null);
                break;
            case DRAW_CONDUCTOR:
                this.mapMainFromWorker.putCell(p, CellState.CONDUCTOR);
                break;
            case DRAW_HEAD:
                this.mapMainFromWorker.putCell(p, CellState.HEAD);
                break;
            case DRAW_TAIL:
                this.mapMainFromWorker.putCell(p, CellState.TAIL);
                break;
            case NOTHING:
                break;
            default:
                break;
        }

        if (++p.x > this.maximumDimension.width)
            this.maximumDimension.width = p.x;
        if (++p.y > this.maximumDimension.height)
            this.maximumDimension.height = p.y;

        return true;
    }

    private void endEditingMode() {
        this.isInEditingMode = false;
    }

    public EditingMode getEdtMode() {
        return this.editingMode;
    }

    public int getScale() {
        return Configuration.SCALE;
    }

    private void insertStructureToMapAndDone() {
        this.mapMainFromWorker.insertMap(this.mapToHoldStructs, this.currentPoint.x, this.currentPoint.y);
        this.removeStructureToDraw();
        this.editingMode = EditingMode.NOTHING;
    }

    public boolean isInEdtMode() {
        return this.isInEditingMode;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {
            if (this.editingMode == EditingMode.DRAW_STRUCT)
                this.insertStructureToMapAndDone();
        } else if (e.getModifiers() == MouseEvent.BUTTON3_MASK) {
            if (this.editingMode == EditingMode.DRAW_STRUCT && this.mapToHoldStructs != null) {
                OtherStuff.rotateMap(this.mapToHoldStructs);
                this.refreshView();
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (((e.getModifiersEx() & (MouseEvent.BUTTON1_DOWN_MASK)) == MouseEvent.BUTTON1_DOWN_MASK) && (this.editingMode != EditingMode.NOTHING)) {
            if (this.editPointOnMap(e.getPoint()))
                this.refreshView();
        } else if ((e.getModifiersEx() & (MouseEvent.BUTTON2_DOWN_MASK)) == MouseEvent.BUTTON2_DOWN_MASK)
            if (this.getParent() != null)
                this.getParent().dispatchEvent(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        switch (this.editingMode) {
            case DELETE:
                break;
            case DRAW_CONDUCTOR:
                break;
            case DRAW_HEAD:
                break;
            case DRAW_TAIL:
                break;
            case NOTHING:
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.setCursor(Cursor.getDefaultCursor());
        if (this.statusBar != null)
            this.statusBar.showPoint(0, 0);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (this.statusBar != null) {
            this.currentPoint = (Point) e.getPoint().clone();
            this.rescalePoint(this.currentPoint, Configuration.SCALE);
            this.statusBar.showPoint(this.currentPoint.x, this.currentPoint.y);
        }
        if (this.editingMode == EditingMode.DRAW_STRUCT)
            this.softRefresh();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if ((e.getButton() == MouseEvent.BUTTON1) && (this.editingMode != EditingMode.NOTHING)) {
            this.prepareEditingMode();
            this.editPointOnMap(e.getPoint());
            this.softRefresh();
        } else if (e.getButton() == MouseEvent.BUTTON2)
            if (this.getParent() != null)
                this.getParent().dispatchEvent(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        /*
         * if ( (e.getButton() == MouseEvent.BUTTON1) && (this.editingMode != EditingMode.NOTHING) ) { this.editPointOnMap( e.getPoint() );
		 * this.softRefresh(); } else if ( e.getButton() == MouseEvent.BUTTON2 ) if ( this.getParent() != null ) this.getParent().dispatchEvent( e );
		 */
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int s = e.getWheelRotation();
        if (((s > 0) && (Configuration.SCALE < Configuration.MAX_SCALE)) || ((s < 0) && (Configuration.SCALE > 1))) {
            Configuration.SCALE += s;
            this.refreshView();
        }
        if (this.statusBar != null) {
            Point p = (Point) e.getPoint().clone();
            this.rescalePoint(p, Configuration.SCALE);
            this.statusBar.showPoint(p.x, p.y);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.image, 0, 0, this.image.getWidth() * Configuration.SCALE, this.image.getHeight() * Configuration.SCALE, 0, 0,
                this.image.getWidth(), this.image.getHeight(), null);
    }

    private void prepareEditingMode() {
        if (this.isInEditingMode)
            return;
        this.isInEditingMode = true;
        this.maximumDimension = new Dimension(this.mapMainFromWorker.getSize().width, this.mapMainFromWorker.getSize().height);
    }

    @Override
    public void rebuildView() {
        if (!this.getSize().equals(this.maximumDimension))
            this.resizeView();
        this.reDraw();
    }

    private void reDraw() {
        // System.out.println( "REDRAW" );
        this.timer1 = System.currentTimeMillis();
        this.repaintImage();
        this.repaint();
        this.timer2 = System.currentTimeMillis() - this.timer1;
        this.statusBar.showMilisec(this.timer2);
        // System.out.println();
    }

    private void refreshMax() {
        this.maximumDimension = new Dimension(this.mapMainFromWorker.getSize().width, this.mapMainFromWorker.getSize().height);
    }

    @Override
    public void refreshView() {
        // System.out.println( "REFRESH VIEW" );
        if (this.isInEditingMode)
            this.endEditingMode();
        this.softRefresh();
    }

    public void removeStructureToDraw() {
        this.mapToHoldStructs = new CellsMapV1();
    }

    private void repaintImage() {
        if (this.mapMainFromWorker == null)
            return;

        Iterator<Entry<Point, CellState>> iterator = this.mapMainFromWorker.iterator();

        OtherStuff.drawMapOnBufferedImageFromIterator(this.image, iterator, 0, 0);

        if (this.mapToHoldStructs.getSize().width == 0)
            return;

        iterator = this.mapToHoldStructs.iterator();

        OtherStuff.drawMapOnBufferedImageFromIterator(this.image, iterator, this.currentPoint.x, this.currentPoint.y);
    }

    private void rescalePoint(Point p, int scale) {
        p.x = p.x / scale;
        p.y = p.y / scale;
    }

    private void resizeView() {
        // System.out.println( "RESIZUJE" );
        if ((this.editingMode != EditingMode.DRAW_STRUCT) && (this.maximumDimension.width <= this.image.getWidth())
                && (this.maximumDimension.height <= this.image.getHeight()))
            this.image = new BufferedImage(this.maximumDimension.width + 1, this.maximumDimension.height + 1, BufferedImage.TYPE_INT_ARGB);
        else
            this.image = new BufferedImage(ViewPanel.getMax(this.maximumDimension.width, this.currentPoint.x
                    + this.mapToHoldStructs.getSize().width), ViewPanel.getMax(this.maximumDimension.height, this.currentPoint.y
                    + this.mapToHoldStructs.getSize().height), BufferedImage.TYPE_INT_ARGB);
        this.setPreferredSize(new Dimension(this.maximumDimension.width * Configuration.SCALE, this.maximumDimension.height * Configuration.SCALE));
    }

    public void setEdtMode(EditingMode edtMode) {
        this.editingMode = edtMode;
        this.lastEdited = new Point(-1, -1);
        if (this.editingMode != EditingMode.DRAW_STRUCT) {
            this.removeStructureToDraw();
            this.refreshView();
        }
    }

    public void setMap(CellsMapInterface<CellState, Point, Map.Entry<Point, CellState>> map) {
        this.mapMainFromWorker = map;
    }

    public void setScale(int scale) {
        Configuration.SCALE = scale;
    }

    public void setStatusBar(StatusBar statusBar) {
        this.statusBar = statusBar;
    }

    public StatusBar getStatusBar() {
        return this.statusBar;
    }

    public void setStructureToDraw(CellsMapInterface<CellState, Point, Map.Entry<Point, CellState>> map_pre) {
        if (map_pre.getNumberOfElements() == 0)
            return;
        this.mapToHoldStructs = map_pre;
        this.editingMode = EditingMode.DRAW_STRUCT;
    }

    private void softRefresh() {
        this.refreshMax();
        this.resizeView();
        this.reDraw();
        this.revalidate();
    }

    @Override
    public void updateAll() {
        if (this.isInEditingMode)
            this.endEditingMode();
        this.reDraw();
    }

    @Override
    public void updateCell(Point position, CellState newState) {
        // TODO _ Opcjonalnie
    }

}

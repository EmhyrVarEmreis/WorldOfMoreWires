package pl.morecraft.dev.studia.womw.base.engines.v1;

import pl.morecraft.dev.studia.womw.core.CellState;
import pl.morecraft.dev.studia.womw.core.interfaces.CellsMapInterface;
import pl.morecraft.dev.studia.womw.core.interfaces.CellsProcessorInterface;
import pl.morecraft.dev.studia.womw.core.interfaces.UpdatableViewer;

import java.awt.*;
import java.util.Map;
import java.util.Map.Entry;

public class CellsProcessorV1 implements CellsProcessorInterface<CellState, Point, Map.Entry<Point, CellState>> {

    private Map.Entry<Point, CellState> entry;
    private boolean isInFlexibleMode;
    private CellsMapInterface<CellState, Point, Entry<Point, CellState>> map;
    private Point tmpPoint;
    private UpdatableViewer<CellState, Point> viewer;

    public CellsProcessorV1() {
        this(null, null);
    }

    public CellsProcessorV1(CellsMapInterface<CellState, Point, Entry<Point, CellState>> map, UpdatableViewer<CellState, Point> viewer) {
        this.map = map;
        this.viewer = viewer;
        this.isInFlexibleMode = false;
        this.tmpPoint = new Point(0, 0);
    }

    private int getMooresOfCell(Point p) {
        int i = 0;
        for (this.tmpPoint.y = p.y - 1; this.tmpPoint.y < (p.y + 2); this.tmpPoint.y++)
            for (this.tmpPoint.x = p.x - 1; this.tmpPoint.x < (p.x + 2); this.tmpPoint.x++)
                if (this.map.getCell(this.tmpPoint) == CellState.HEAD) {
                    i++;
                    if (i > 2)
                        return -1;
                }
        return i;
    }

    @Override
    public void makeCycle() {
        for (Entry<Point, CellState> aMap : this.map) {
            this.entry = aMap;
            this.processCell(this.entry.getKey(), this.entry.getValue());
        }
        this.map.cyclePostProcess();
        // viewer.updateAll( map.getIterator() );
    }

    private void processCell(Point p, CellState s) {
        if (s == CellState.HEAD)
            this.processCellOnMap(p, CellState.TAIL);
        else if (s == CellState.TAIL)
            this.processCellOnMap(p, CellState.CONDUCTOR);
        else if (this.getMooresOfCell(p) > 0)
            this.processCellOnMap(p, CellState.HEAD);
        else
            this.processCellOnMap(p, CellState.CONDUCTOR);
    }

    private void processCellOnMap(Point p, CellState s) {
        this.map.processCell(p, s);
        if (this.isInFlexibleMode && (this.viewer != null))
            this.viewer.updateCell(p, s);
    }

    @Override
    public void setCellmap(CellsMapInterface<CellState, Point, Entry<Point, CellState>> map) {
        this.map = map;
    }

    @Override
    public void setUpdatableViever(UpdatableViewer<CellState, Point> viewer) {
        this.viewer = viewer;
    }

}

package pl.morecraft.dev.studia.womw.base;

import pl.morecraft.dev.studia.womw.core.CellState;
import pl.morecraft.dev.studia.womw.core.interfaces.CellsMapInterface;
import pl.morecraft.dev.tool.pointproccessor.PointTranslation;

import java.awt.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class CellsMapV1 implements CellsMapInterface<CellState, Point, Map.Entry<Point, CellState>> {

    private Map<Point, CellState> map, mapClone, mapTMP;
    private Dimension size;

    public CellsMapV1() {
        this.map = new ConcurrentHashMap<>();
        this.mapClone = new ConcurrentHashMap<>();
        this.size = null;
    }

    @Override
    public void clear() {
        this.map.clear();
        this.mapClone.clear();
    }

    @Override
    public void cyclePostProcess() {
        this.switchMap();
        // this.map.putAll( this.mapClone );
        this.mapClone.clear();
    }

    @Override
    public CellState getCell(Point position) {
        return this.map.get(position);
    }

    @Override
    public Dimension getSize() {
        if (this.map.size() == 0) {
            return new Dimension(0, 0);
        }
        if (this.size == null) {
            this.optimizeMap();
        }
        return this.size;
    }

    @Override
    public Iterator<Entry<Point, CellState>> iterator() {
        return this.map.entrySet().iterator();
    }

    private void optimizeMap() {
        Point range = this.getBorderPoint();

        this.size = new Dimension((range.x + 1), (range.y + 1));
    }

    private Point getBorderPoint() {
        Point range = new Point(0, 0);

        for (Map.Entry<Point, CellState> entry : this.map.entrySet()) {
            if (entry.getKey().x > range.x) {
                range.x = entry.getKey().x;
            }
            if (entry.getKey().y > range.y) {
                range.y = entry.getKey().y;
            }
        }

        return range;
    }

    @Override
    public void processCell(Point position, CellState state) {
        this.mapClone.put(position, state);
    }

    @Override
    public void putCell(Point position, CellState state) {
        this.size = null;
        if (state == null || state == CellState.EMPTY) {
            this.map.remove(position);
        } else {
            this.map.put(new Point(position.x, position.y), state);
        }
    }

    @Override
    public void rebuild() {
        this.optimizeMap();
    }

    @Override
    public void setCell(Point position, CellState state) {
        this.size = null;
        if (state == null || state == CellState.EMPTY) {
            this.map.remove(position);
        } else {
            this.map.put(position, state);
        }
    }

    private void switchMap() {
        this.mapTMP = this.map;
        this.map = this.mapClone;
        this.mapClone = this.mapTMP;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("[ ");
        for (Map.Entry<Point, CellState> entry : this.map.entrySet())
            s.append("(").append(entry.getKey().x).append(",").append(entry.getKey().y).append(")-").append(entry.getValue());
        s.append("]");
        return s.toString();
    }

    @Override
    public void packToBorders() {
        Point mRange = this.getBorderPoint();

        this.size = new Dimension(((this.size.width == 0) && (this.size.height == 0)) ? 0 : (this.size.width + 1) - mRange.x,
                ((this.size.width == 0) && (this.size.height == 0)) ? 0 : (this.size.height + 1) - mRange.y);
    }

    @Override
    public void insertMap(CellsMapInterface<CellState, Point, Entry<Point, CellState>> map, int translateX, int translateY) {
        Map.Entry<Point, CellState> entry;
        for (Entry<Point, CellState> aMap : map) {
            entry = aMap;
            this.map.put(PointTranslation.translatePoint(entry.getKey(), translateX, translateY), entry.getValue());
        }
        this.size = null;
    }

    @Override
    public int getNumberOfElements() {
        return this.map.size();
    }

}

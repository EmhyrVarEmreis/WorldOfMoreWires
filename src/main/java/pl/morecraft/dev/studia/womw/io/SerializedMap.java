package pl.morecraft.dev.studia.womw.io;

import pl.morecraft.dev.studia.womw.core.CellState;

import java.awt.*;
import java.io.Serializable;

public class SerializedMap implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Point[] coordinates;
    private CellState[] states;

    public SerializedMap() {
        this(null, null);
    }

    public SerializedMap(Point[] coordinates, CellState[] states) {
        this.coordinates = coordinates;
        this.states = states;
    }

    public Point[] getCoordinates() {
        return this.coordinates;
    }

    public void setCoordinates(Point[] coordinates) {
        this.coordinates = coordinates;
    }

    public CellState[] getStates() {
        return this.states;
    }

    public void setStates(CellState[] states) {
        this.states = states;
    }

}

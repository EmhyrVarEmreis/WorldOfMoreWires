package pl.morecraft.dev.studia.womw.core.interfaces;

import java.awt.*;

public interface CellsMapInterface<S, P, E> extends Iterable<E> {

    void clear();

    void cyclePostProcess();

    S getCell(P position);

    Dimension getSize();

    void processCell(P position, S state);

    // void putCell( P position, S state );

    // void removeCell( P position );

    void putCell(P position, S state);

    void rebuild();

    void packToBorders();

    void setCell(P position, S state);

    void insertMap(CellsMapInterface<S, P, E> map, int translateX, int translateY);

    int getNumberOfElements();

}

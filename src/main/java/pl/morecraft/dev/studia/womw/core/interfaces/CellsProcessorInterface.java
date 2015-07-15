package pl.morecraft.dev.studia.womw.core.interfaces;

public interface CellsProcessorInterface<S, P, E> {

    void makeCycle();

    void setCellmap(CellsMapInterface<S, P, E> map);

    void setUpdatableViever(UpdatableViewer<S, P> viewer);

}

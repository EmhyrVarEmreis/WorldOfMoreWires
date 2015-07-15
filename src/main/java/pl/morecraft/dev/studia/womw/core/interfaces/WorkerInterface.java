package pl.morecraft.dev.studia.womw.core.interfaces;

public interface WorkerInterface<S, P, E> {

    CellsMapInterface<S, P, E> getCellsMap();

    int getCyclesPerViewerUpdate();

    int getCyclesProcessed();

    int getDelayBetweenCycles();

    boolean isProcessing();

    void makeCycle();

    void makeCyclesAndStop(int cycles);

    void setCyclesPerViewerUpdate(int count);

    void setDelayBetweenCycles(int delay);

    void setViewer(UpdatableViewer<S, P> viewer);

    void startProcess();

    void stopProcess();

}

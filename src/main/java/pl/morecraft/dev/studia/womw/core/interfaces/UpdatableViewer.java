package pl.morecraft.dev.studia.womw.core.interfaces;

public interface UpdatableViewer<S, P> {

    void refreshView();

    void rebuildView();

    void updateAll();

    void updateCell(P position, S newState);

}

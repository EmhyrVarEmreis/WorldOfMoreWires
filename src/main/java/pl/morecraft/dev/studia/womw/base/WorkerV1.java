package pl.morecraft.dev.studia.womw.base;

import pl.morecraft.dev.studia.womw.core.CellState;
import pl.morecraft.dev.studia.womw.core.interfaces.CellsMapInterface;
import pl.morecraft.dev.studia.womw.core.interfaces.CellsProcessorInterface;
import pl.morecraft.dev.studia.womw.core.interfaces.UpdatableViewer;
import pl.morecraft.dev.studia.womw.core.interfaces.WorkerInterface;

import java.awt.*;
import java.util.Map;
import java.util.Map.Entry;

public class WorkerV1 extends Thread implements WorkerInterface<CellState, Point, Map.Entry<Point, CellState>> {

    private int cyclesPerViewUpdate;
    private int cyclesProcessed;
    private int cyclesToForceMake;
    private int delayBetweenCycles;
    private boolean isRunning;
    private boolean isStopped;
    private CellsMapInterface<CellState, Point, Entry<Point, CellState>> map;
    private UpdatableViewer<CellState, Point> viewer;
    private CellsProcessorInterface<CellState, Point, Map.Entry<Point, CellState>> worldProcessor;

    public WorkerV1() {
        this(null);
    }

    public WorkerV1(UpdatableViewer<CellState, Point> viewer) {
        this.map = new CellsMapV1();
        this.worldProcessor = new CellsProcessorV1(this.map, viewer);
        this.viewer = viewer;
        this.cyclesPerViewUpdate = 1;
        this.cyclesProcessed = 0;
        this.cyclesToForceMake = 0;
        this.delayBetweenCycles = 100;
        this.isStopped = true;
        this.isRunning = true;
    }

    @Override
    public CellsMapInterface<CellState, Point, Entry<Point, CellState>> getCellsMap() {
        return this.map;
    }

    @Override
    public int getCyclesPerViewerUpdate() {
        return this.cyclesPerViewUpdate;
    }

    @Override
    public int getCyclesProcessed() {
        return this.cyclesProcessed;
    }

    @Override
    public int getDelayBetweenCycles() {
        return this.delayBetweenCycles;
    }

    @Override
    public boolean isProcessing() {
        return !this.isStopped;
    }

    @Override
    public void makeCycle() {
        this.worldProcessor.makeCycle();
        this.cyclesProcessed++;
    }

    @Override
    public void makeCyclesAndStop(int cycles) {
        this.cyclesToForceMake = cycles;
        this.startProcess();
    }

    @Override
    public void run() {
        while (this.isRunning) {

            if (this.isStopped) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // e.printStackTrace();
                }
                continue;
            }

            if (this.cyclesToForceMake != 0) {

                for (; this.cyclesToForceMake > 0; this.cyclesToForceMake--)
                    this.makeCycle();

                this.viewer.updateAll();

            } else
                while (!this.isStopped) {
                    for (int i = 0; i < this.cyclesPerViewUpdate; i++)
                        this.makeCycle();

                    this.viewer.updateAll();

                    try {
                        Thread.sleep(this.delayBetweenCycles);
                    } catch (InterruptedException e) {
                        // e.printStackTrace();
                    }
                }

            this.stopProcess();
        }
    }

    @Override
    public void setCyclesPerViewerUpdate(int count) {
        this.cyclesPerViewUpdate = count;
    }

    @Override
    public void setDelayBetweenCycles(int delay) {
        this.delayBetweenCycles = delay;
    }

    @Override
    public void setViewer(UpdatableViewer<CellState, Point> viewer) {
        this.viewer = viewer;
        this.worldProcessor.setUpdatableViever(viewer);
    }

    @Override
    public void startProcess() {
        this.isStopped = false;
    }

    @Override
    public void stopProcess() {
        this.isStopped = true;
    }

}

package pl.morecraft.dev.studia.womw.base.gui;

public interface Progressable {

    int getMaximum();

    int getValue();

    void setMaximum(int progress);

    void setValue(int progress);

}

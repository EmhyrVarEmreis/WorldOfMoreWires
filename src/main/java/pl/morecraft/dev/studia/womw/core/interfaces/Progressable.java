package pl.morecraft.dev.studia.womw.core.interfaces;

public interface Progressable {

    int getMaximum();

    int getValue();

    void setMaximum(int progress);

    void setValue(int progress);

}

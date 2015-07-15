package pl.morecraft.dev.studia.womw.misc;

import javax.swing.*;
import java.awt.*;

public final class CoreBasicMain {

    public static JFrame getParentFrameofComponent(JComponent component) {
        Window parentWindow = SwingUtilities.windowForComponent(component);
        JFrame parentFrame = null;
        if (parentWindow instanceof JFrame)
            parentFrame = (JFrame) parentWindow;
        return parentFrame;
    }

    public static void setUILookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
    // public static void closingOperation() {
    // System.exit(0);
    // }

}

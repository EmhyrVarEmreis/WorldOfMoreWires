package pl.morecraft.dev.studia.womw.misc;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class LoadFromRes {

    public static String RESOURCES_PREFIX = "";

    public static InputStream loadAsInputStream(String path) {
        InputStream ret = null;
        try {
            ret = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Wystapił błąd podczas ładowania zasobu '" + path + "'!", "Uwaga!", JOptionPane.WARNING_MESSAGE);
        }
        return ret;
    }

    public static ImageIcon loadImageAsIconImage(String path) {
        ImageIcon ret = null;
        try {
            ret = new ImageIcon(loadImageAsImage(path));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Wystapił błąd podczas ładowania zasobu '" + path + "'!", "Uwaga!", JOptionPane.WARNING_MESSAGE);
        }
        return ret;
    }

    public static Image loadImageAsImage(String path) {
        Image ret = null;
        try {
            ret = Toolkit.getDefaultToolkit().createImage(Thread.currentThread().getContextClassLoader().getResource("img/" + path));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Wystapił błąd podczas ładowania zasobu '" + path + "'!", "Uwaga!", JOptionPane.WARNING_MESSAGE);
        }
        return ret;
    }

    public static InputStream loadImageAsInputStream(String path) {
        InputStream ret = null;
        try {
            ret = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Wystapił błąd podczas ładowania zasobu '" + path + "'!", "Uwaga!", JOptionPane.WARNING_MESSAGE);
        }
        return ret;
    }

}

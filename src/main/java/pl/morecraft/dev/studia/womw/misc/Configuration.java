package pl.morecraft.dev.studia.womw.misc;

import pl.morecraft.dev.studia.womw.misc.enums.Language;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class Configuration {

    public static Language LANGUAGE = Language.XX;
    public static Boolean LANGUAGE_CHANGED = false;
    public static Language LANGUAGE_DEFAULT = Language.XX;
    public static Color CONDUCTOR_COLOR = Color.BLACK;
    public static Color HEAD_COLOR = Color.RED;
    public static Color NONE_COLOR = Color.WHITE;
    public static Color TAIL_COLOR = Color.ORANGE;
    public static int DELAY_BETWEEN_CYCLES = 100;
    public static int CYCLES_PER_ROUND = 1;
    public static int MAX_SCALE = 64;
    public static boolean AUTOSAVE = false;

    public static int SCALE = 10;

    public static String defaultSettingsFile = "/womw.properties.xml";

    public static void saveConfiguration(String path) {
        if (path == null) {
            path = System.getProperty("user.dir");
        }
        path += Configuration.getDefaultSettingsFile();

        Properties p = new Properties();
        p.setProperty("LANGUAGE", LANGUAGE.toString());
        p.setProperty("LANGUAGE_CHANGED", LANGUAGE_CHANGED.toString());
        p.setProperty("CONDUCTOR_COLOR", CONDUCTOR_COLOR.getRGB() + "");
        p.setProperty("HEAD_COLOR", HEAD_COLOR.getRGB() + "");
        p.setProperty("TAIL_COLOR", TAIL_COLOR.getRGB() + "");
        p.setProperty("NONE_COLOR", NONE_COLOR.getRGB() + "");
        p.setProperty("CYCLES_PER_ROUND", CYCLES_PER_ROUND + "");
        p.setProperty("DELAY_BETWEEN_CYCLES", DELAY_BETWEEN_CYCLES + "");
        p.setProperty("SCALE", SCALE + "");
        p.setProperty("AUTOSAVE", AUTOSAVE + "");

        try {
            p.storeToXML(new FileOutputStream(path), "World of More Wires Configuration File", "UTF-8");
        } catch (IOException e) {
            e.printStackTrace(); // todo Exception
        }

    }

    public static void readConfiguration(String path) {
        if (path == null) {
            path = System.getProperty("user.dir");
        }
        path += Configuration.getDefaultSettingsFile();

        Properties p = new Properties();

        try {
            p.loadFromXML(new FileInputStream(path));
        } catch (InvalidPropertiesFormatException | FileNotFoundException e) {
            return;
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }

        String tmp;

        tmp = p.getProperty("LANGUAGE");
        if (tmp != null) {
            LANGUAGE = Language.valueOf(tmp);
        }
        tmp = p.getProperty("LANGUAGE_CHANGED");
        if (tmp != null) {
            LANGUAGE_CHANGED = Boolean.valueOf(tmp);
        }
        tmp = p.getProperty("CONDUCTOR_COLOR");
        if (tmp != null) {
            CONDUCTOR_COLOR = new Color(Integer.valueOf(tmp));
        }
        tmp = p.getProperty("HEAD_COLOR");
        if (tmp != null) {
            HEAD_COLOR = new Color(Integer.valueOf(tmp));
        }
        tmp = p.getProperty("TAIL_COLOR");
        if (tmp != null) {
            TAIL_COLOR = new Color(Integer.valueOf(tmp));
        }
        tmp = p.getProperty("NONE_COLOR");
        if (tmp != null) {
            NONE_COLOR = new Color(Integer.valueOf(tmp));
        }
        tmp = p.getProperty("CYCLES_PER_ROUND");
        if (tmp != null) {
            CYCLES_PER_ROUND = Integer.valueOf(tmp);
        }
        tmp = p.getProperty("DELAY_BETWEEN_CYCLES");
        if (tmp != null) {
            DELAY_BETWEEN_CYCLES = Integer.valueOf(tmp);
        }
        tmp = p.getProperty("SCALE");
        if (tmp != null) {
            SCALE = Integer.valueOf(tmp);
        }
        tmp = p.getProperty("AUTOSAVE");
        if (tmp != null) {
            AUTOSAVE = Boolean.valueOf(tmp);
        }

//        try {
//            p.storeToXML(new FileOutputStream(path), "World of More Wires Configuration File", "UTF-8");
//        } catch (IOException e) {
//            e.printStackTrace(); // todo Exception
//        }

    }

    public static String getDefaultSettingsFile() {
        return defaultSettingsFile;
    }

}

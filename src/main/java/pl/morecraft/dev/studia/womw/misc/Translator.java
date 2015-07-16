package pl.morecraft.dev.studia.womw.misc;

import pl.morecraft.dev.studia.womw.misc.enums.Language;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Translator {

    private static Map<String, String> languageMap;

    public static void createMap() {
        Translator.refreshMap();
    }

    public static void refreshMap() {
        Translator.languageMap = new HashMap<>();

        Properties properties = new Properties();

        try {
            if (Configuration.LANGUAGE == null) {
                throw new NullPointerException("Unsupported language");
            }
            properties.loadFromXML(
                    Thread.currentThread().getContextClassLoader().getResourceAsStream(
                            "lng/" + Configuration.LANGUAGE.getCode() + "/strings.xml"));
        } catch (IOException | NullPointerException e) {
            Configuration.LANGUAGE = Configuration.LANGUAGE_DEFAULT;
            // todo replace with EN
        }

        properties.stringPropertyNames().forEach(propertyName ->
                Translator.languageMap.put(propertyName, properties.getProperty(propertyName)));

    }

    public static String getString(String key) {
        if (Configuration.LANGUAGE.equals(Language.XX)) {
            return key;
        }
        return Translator.languageMap.get(key);
    }
}

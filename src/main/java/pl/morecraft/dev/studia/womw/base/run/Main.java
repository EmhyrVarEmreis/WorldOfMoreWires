package pl.morecraft.dev.studia.womw.base.run;

import pl.morecraft.dev.studia.womw.base.gui.MainFrame;
import pl.morecraft.dev.studia.womw.misc.Configuration;
import pl.morecraft.dev.studia.womw.misc.LoadFromRes;
import pl.morecraft.dev.studia.womw.misc.Translator;
import pl.morecraft.dev.studia.womw.misc.enums.Language;

import java.util.Locale;

public class Main {

    public static void main(String[] args) {

		/*
         * Miscellaneous
		 */
        LoadFromRes.RESOURCES_PREFIX = "";

		/*
         * Improving performance
		 */
        System.setProperty("sun.java2d.noddraw", Boolean.TRUE.toString());

		/*
		 * Reading properties
		 */
        Configuration.readConfiguration(null);

		/*
         * Setting locale
		 */
        if (!Configuration.LANGUAGE_CHANGED) {
            Configuration.LANGUAGE = Language.valueOf(Locale.getDefault().getLanguage().toUpperCase());
        }

		/*
         * Reading locale
		 */
        Translator.createMap();

		/*
		 * Initializing Core
		 */
        MainFrame mainAppFrame = new MainFrame();

		/*
         * Translate components
		 */
        Translator.translate();

		/*
		 * Show app
		 */
        mainAppFrame.setVisible(true);

    }
}

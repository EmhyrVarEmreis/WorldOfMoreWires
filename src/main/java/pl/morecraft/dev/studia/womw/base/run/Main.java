package pl.morecraft.dev.studia.womw.base.run;

import pl.morecraft.dev.studia.womw.base.gui.MainFrame;
import pl.morecraft.dev.studia.womw.misc.Configuration;
import pl.morecraft.dev.studia.womw.misc.LoadFromRes;

public class Main {

    public static void main(String[] args) {

		/*
         * Miscellenous
		 */
        LoadFromRes.RESOURCES_PREFIX = "pl/morecraft/dev/studia/womw/";

		/*
         * Improving performance
		 */
        System.setProperty("sun.java2d.noddraw", Boolean.TRUE.toString());
		
		/*
		 * Reading properties
		 */
        Configuration.readConfiguration(null);

		/*
		 * Initializing Core
		 */
        MainFrame mainAppFrame = new MainFrame();

        mainAppFrame.setVisible(true);

        //IOFile.readImageFile( "C:\\Users\\MS\\Desktop\\a.bmp" );

    }
}

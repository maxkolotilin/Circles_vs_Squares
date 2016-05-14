package com.circlesvssquares.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.circlesvssquares.game.Circles_vs_Squares;


public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 720;
        config.width = 1280;
        config.title = "Circles vs Squares";
        config.samples = 8;
        // config.fullscreen = true;

		new LwjglApplication(new Circles_vs_Squares(), config);
	}
}

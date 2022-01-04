package com.dontstopshooting.dontstopshooting.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dontstopshooting.dontstopshooting.MyGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		//config.fullscreen = true;
		config.title = "Don't stop shooting";
		new LwjglApplication(new MyGame(), config);
	}
}

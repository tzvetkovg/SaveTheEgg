package com.saveggs.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.saveggs.game.GameClass;
import com.saveggs.game.Slingshot;
import com.saveggs.game.screens.StageScreen;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 800;
		config.height = 480;


		new LwjglApplication(new GameClass(null), config);
	}
	
}

package com.ricm3.packnight.desktop;

import com.badlogic.gdx.backends.lwjgl.*;
import com.ricm3.packnight.view.GestureDetectorTest;
import com.ricm3.packnight.view.Jeu;
import com.ricm3.packnight.view.screen.LauncherScreen;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();

		cfg.title = Jeu.TITLE;
		cfg.width = Jeu.WIDTH;
		cfg.height = Jeu.HEIGHT;
//		cfg.fullscreen = true;
		
		new LwjglApplication(new LauncherScreen(), cfg);
	}
}

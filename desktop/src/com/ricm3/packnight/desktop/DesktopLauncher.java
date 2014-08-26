package com.ricm3.packnight.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.ricm3.packnight.view.Jeu;
import com.ricm3.packnight.view.screen.LauncherScreen;

public class DesktopLauncher {
	public static void main (String[] arg) {
		new LwjglApplication(new LauncherScreen(), Jeu.TITLE,Jeu.WIDTH,Jeu.HEIGHT);
	}
}

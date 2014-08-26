package com.ricm3.packnight.view;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class LancerDesktop {

//	// set resolution to HD ready (1280 x 720) and set full-screen to true
//	Gdx.graphics.setDisplayMode(1280, 720, true);
//
//	// set resolution to default and set full-screen to true
//	Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, true);
	
	public static void main(String[] args) {
        new LwjglApplication(new GestureDetectorTest(), Jeu.TITLE,Jeu.WIDTH,Jeu.HEIGHT);
	}
}

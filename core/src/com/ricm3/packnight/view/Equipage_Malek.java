package com.ricm3.packnight.view;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.ricm3.packnight.model.personnages.PacKnight;
import com.ricm3.packnight.model.structure_terrain.Direction;

public class Equipage_Malek extends Equipage{
	
	
	public Equipage_Malek() {
		super();
	}
	
	public static void main(String[] args) {
	    new LwjglApplication((ApplicationListener) new Jeu(new Equipage_Malek()),Jeu.TITLE,Jeu.WIDTH,Jeu.HEIGHT);
	}

	@Override
	public void create() {
		PacKnight PACMAN_1= new PacKnight("J1",1,1,Direction.droite, true);
//		PacKnight PACMAN_2= new PacKnight("J1",2,2,Direction.droite, true);
		Joueur PM1 = new Joueur(Sprites.Pacman, PACMAN_1);
//		Joueur PM2 = new Joueur(Sprites.Princess, PACMAN_2);
		Equipage.joueurCamera =  PACMAN_1;
		Equipage.joueurFleche =  PACMAN_1;
	}
}


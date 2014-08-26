package com.ricm3.packnight.view;


import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.ricm3.packnight.controller.Automate;
import com.ricm3.packnight.model.personnages.Ghost;
import com.ricm3.packnight.model.personnages.Pacman;
import com.ricm3.packnight.model.personnages.Personnage;
import com.ricm3.packnight.model.structure_terrain.Direction;

public class Joueur {
	public static List<Joueur> liste = new LinkedList<Joueur>();

	public Animation personnageAnimation ;
	public Sprites personnageSprite ;
	private final int NombreClignotement=20;//pair
	
	Personnage p;
	Automate auto=null;

	public Joueur(String sprite, Personnage p) {
		this.p = p;
		personnageSprite = new Sprites(sprite);
		liste.add(this);
	}

	public Joueur(String sprite, Personnage p, String automate) {
		
		try{
    		this.auto = new Automate(automate, p);
    	}catch(Exception e)  { System.out.println(e); }
		
		this.p = p;
		personnageSprite = new Sprites(sprite);
		
    	liste.add(this);
	}

		
	/**
	 * Methode a executer avant le bach.begin() dans Jeu.java
	 */
	public void render(){
		
		//Permet de choisir l'annimation du personnage parmis les 4 directions possible, sachant que l'axe Y est vers le haut sur libGDX
		if (p.direction==Direction.haut) personnageSprite.direction =  Direction.bas;
		else if (p.direction==Direction.bas) personnageSprite.direction =  Direction.haut;
		else personnageSprite.direction = p.direction;
		
		//Charge l'annimation du personnage
        personnageAnimation = personnageSprite.loadAnimation();

		if ((p instanceof Ghost)){
			if(((Ghost) p).getisAlive()) 
				//Dessine le personnage
		        Jeu.batch.draw(personnageAnimation.getKeyFrame(Jeu.stateTime, true), 
		        		p.coordPix.PixCentre().x - personnageSprite.getWidth()/2, 
		        		p.coordPix.PixCentre().y - personnageSprite.getHeight()/2);
		} 
		else{
			if(!((Pacman) p).isInvincible){
				//Dessine le personnage
		        Jeu.batch.draw(personnageAnimation.getKeyFrame(Jeu.stateTime, true), 
		        		p.coordPix.PixCentre().x - personnageSprite.getWidth()/2, 
		        		p.coordPix.PixCentre().y - personnageSprite.getHeight()/2);
				((Pacman) p).compteurClignotement=0;
			}
			else{
				//On dessine le pacman un certain nombre de frame
				if(((Pacman) p).compteurClignotement<(NombreClignotement/2)){
					//Dessine le personnage
			        Jeu.batch.draw(personnageAnimation.getKeyFrame(Jeu.stateTime, true), 
			        		p.coordPix.PixCentre().x - personnageSprite.getWidth()/2, 
			        		p.coordPix.PixCentre().y - personnageSprite.getHeight()/2);
					((Pacman) p).compteurClignotement++;
				}
				else{
					((Pacman) p).compteurClignotement++;
					((Pacman) p).compteurClignotement=((Pacman) p).compteurClignotement%NombreClignotement;
				}
			}
		}
        
	}
	
	void suivant () throws Exception{
		if(this.auto != null){
			auto.suivant();
		} else {
			if (p.parametrable())
				p.avancer();
			else
				p.avancerAnimation();
		}
	}
	
}

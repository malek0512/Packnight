package com.ricm3.packnight.model.structure_terrain;

public enum Direction {
	haut,
	gauche,
	bas,
	droite;
	
	public Direction opposer(){
		Direction opp = null;
		switch(this){
		case haut: opp=bas;break;
		case bas: opp=haut;break;
		case gauche: opp=droite;break;
		case droite: opp=gauche;break;
		}
		return opp;
	}
	
	public Direction aDroite()
	{
		switch(this)
		{
		case haut : return droite;
		case droite : return bas;
		case bas : return gauche;
		case gauche : return haut;
		default : return null;
		}
	}
	
	public Direction aGauche()
	{
		switch(this)
		{
		case haut : return gauche;
		case droite : return haut;
		case bas : return droite;
		case gauche : return bas;
		default : return null;
		}
	}

}

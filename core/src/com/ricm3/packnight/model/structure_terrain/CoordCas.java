package com.ricm3.packnight.model.structure_terrain;

import com.ricm3.packnight.model.personnages.Personnage;


public class CoordCas extends Coord {

	public CoordCas(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public CoordCas(CoordCas c)
	{
		this.x = c.x;
		this.y = c.y;
	}

	/**
	 * renvoie vrai si l'objet et c represente la meme case
	 */
	public boolean equals(CoordCas c)
	{
		return this.x==c.x && this.y==c.y;
	}

	/**
	 * fait avancer les coordonné du pixel d'une case vers la direction donné
	 * edit : prend en compte le tore 
	 * @param d
	 */
	//coorigé y++ et y--
	public void avancerDansDir(Direction d)
	{
		Terrain t = Personnage.terrain;
		if(t.estCore(this, d))
		{
			switch(d)
			{
			case droite:
				x=0;
				break;
			case bas:
				y=0;
				break;
			case gauche:
				y=t.largeur-1;
				break;
			case haut:
				x=t.hauteur-1;
				break;
			}
		}
		else if (t.caseAcessible(this, d))
		{
			switch(d)
			{
			case haut:
				y++;
				break;
			case bas:
				y--;
				break;
			case droite:
				x++;
				break;
			case gauche:
				x--;
				break;
			}
		}
	}
	
	public String toString()
	{return "Cordonnée en case (x/y) : "+ x + " " + y;}

	public int distance(CoordCas c) {
		return Math.abs(this.x-c.x)+Math.abs(this.y-c.y);
	}
	
	/**
	 * donne la direction pour aller vers la case donnée en argument
	 * @param src : case source
	 * @param dest : case destination
	 * @return la direction a suivre pour aller a dest
	 */
	public Direction directionPourAllerVers(CoordCas dest)
	{
		int vx = dest.x - x;
		int vy = dest.y - y;

		if (vy == 0) {
			if (vx < 0)
				return Direction.gauche;
			else
				return Direction.droite;
		}
		else // vy != 0
		{
			if (vy > 0)
				return Direction.bas;
			else
				return Direction.haut;
		}
	}
	
	//fonction a supprimer quand le code sera valider
	public boolean equals(CoordPix c)
	{
		System.out.println("warning, utilisation nimportenawak de fonction equals");
		return false;
	}
	
}


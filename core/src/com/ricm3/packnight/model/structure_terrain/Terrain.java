package com.ricm3.packnight.model.structure_terrain;

import com.ricm3.packnight.view.Jeu;



public class Terrain {

	public Case[][] terrain;
	public int hauteur;
	public int largeur;
	static public int nb_pacgum;
	static public int nb_pacgum_init; 
	
	/**
	 * Alloue la mémoire pour un terrain de haut largeur donnée
	 * @param hauteur
	 * @param largeur
	 * @require les paramètre sont >= a 1
	 * author : alex
	 */
	public Terrain(int largeur, int hauteur, int nb_pacgum){
		terrain=new Case[largeur][hauteur];
		this.hauteur = hauteur;
		this.largeur = largeur;
		Terrain.nb_pacgum = nb_pacgum;
		Terrain.nb_pacgum_init=nb_pacgum;
	}
	
	/**
	 * Alloue la mémoire pour un terrain de haut largeur donnée
	 * @param hauteur
	 * @param largeur
	 * @require les paramètre sont >= a 1
	 * author : alex
	 */
	public Terrain(Case[][] terrain){
		this.terrain = terrain;
		this.hauteur = terrain.length;
		this.largeur = terrain[0].length;
		Terrain.nb_pacgum = nbPacgum();
		Terrain.nb_pacgum_init=nb_pacgum;
	}
	
	/**
	 * ajoute l'objet au coordonée donnée
	 * @param ligne
	 * @param colonne
	 * @param elt l'objet a mettre
	 * @require : les coordonée sont juste et l'objet est initialiser
	 */
	public void setCase(CoordCas c, int elt){
		if(estDansLeTerrain(c))
			terrain[c.x][c.y]=new Case(elt);
	}

	private int nbPacgum(){
		int cpt = 0;
		for(Case[] i : terrain){
			for(Case j : i){
				if (j.caseValeur()==Case.Pacgum) 
					cpt++;
			}
		}
		return cpt;
	}
	
	/**
	 * affiche le terrain
	 * fonction a ameliorer pour pouvoir voir quelque chose
	 */
	public void afficher(){
		int i,j;

		System.out.print("  ");
		for(i=0; i< this.largeur;i++)
			System.out.print(i);
		System.out.println();
		for(i=0; i < this.hauteur;i++){
			System.out.print(i + " ");
			for(j=0; j < this.largeur;j++)
				System.out.print(terrain[j][i].toString());
			System.out.println();
		}
	}
	
	/**
	 * renvoie vrai si la case est accessible
	 * @param x : cord.x de la case
	 * @param y : cord.y de la case
	 */
	public boolean caseAcessible(CoordCas c)
	{
		if(estDansLeTerrain(c))
			return terrain[c.x][c.y].isAccessable();
		else
			return false;
	}
	
	/**
	 * renvoie vrai si la case dans la direction est accessible
	 * @param x : cord.x de la case
	 * @param y : cord.y de la case
	 * @param direction : direction ou doit etre tester la case
	 * @return vraie si la case dans la direction de la case (x,y) est accessible
	 */
	public boolean caseAcessible(CoordCas c,Direction direction)
	{
		return caseAcessible(c,1,direction);
	}
	
	/**
	 * Renvoie si la case d'une distance distance dans la direction donnée est accessible
	 * @param x : cord.x de la case de reference
	 * @param y : cord.y de la case de reference
	 * @param distance : distance de la case chercher par rapport a la case de depart
	 * si distance = 0, on renvoie si la case (x,y) est accessible
	 * @param direction : direction vers ou on veut connaitre la case
	 * @return vraie si la case de distance distance et dans la direction donné est accessible
	 */
	public boolean caseAcessible(CoordCas c, int distance, Direction direction)
	{
		switch(direction)
		{
		case haut :
			if(estDansLeTerrain(new CoordCas(c.x, c.y-distance)))
				return terrain[c.x][c.y-distance].isAccessable();
		case bas :
			if(estDansLeTerrain(new CoordCas(c.x, c.y+distance)))
				return terrain[c.x][c.y+distance].isAccessable();
		case droite :
			if(estDansLeTerrain(new CoordCas(c.x+distance, c.y)))
				return terrain[(c.x+distance)][c.y].isAccessable();
		case gauche : 
			if(estDansLeTerrain(new CoordCas(c.x-distance, c.y)))
				return terrain[(c.x-distance)][c.y].isAccessable();
		default:
			break; 
		}
	return false;
	}
	
	/**
	 * vraie si les coordonnée sont dans le terrain
	 * @author malek
	 * @param coord
	 * @return vraie si les coordonnée sont dans le terrain
	 *
	 */
	protected boolean estDansLeTerrain(CoordCas c){
		return c.x>-1 && c.x <hauteur && c.y>-1 && c.y<largeur;
	}
	
	/**
	 * Renvoie vrai si les prochaine coordonnées actuelles sont dans le terrain, et les suivante ne le sont pas (tore)
	 * @param x
	 * @param y
	 * @param d
	 * @return !estDansLeTerrain(new CoordCas(tmpX, tmpY)) && estDansLeTerrain(c)
	 */
	public boolean estCore(CoordCas c, Direction d)
	{
		
		int tmpX = c.x;
		int tmpY = c.y;
		switch (d)
		{
		case haut : tmpY-= 1; break;
		case bas : tmpY+= 1; break;
		case droite : tmpX+=1; break;
		case gauche : tmpX-=1; break;
		default : break;
		}
		return !estDansLeTerrain(new CoordCas(tmpX, tmpY)) && estDansLeTerrain(c);
	}
	
	/**
	 * renvoie la valeur du pixel du bord droit 
	 * @return
	 */
	public int pixelBordDroit()
	{
		return Jeu.tuile_size * this.hauteur;
	}
	
	/**
	 * renvoie la valeur du pixel du bord bas
	 * @return
	 */
	public int pixelBordBas()
	{
		return Jeu.tuile_size * this.largeur;
	}
	
	public int ValueCase(CoordCas c)
	{
		if(estDansLeTerrain(c))
			return terrain[c.x][c.y].caseValeur();
		return 0;
	}
	
	public void SetCase(CoordCas c, int v)
	{
		if(estDansLeTerrain(c))
			terrain[c.x][c.y].setAcessCase(v);
	}

	/**
	 * @return Vrai si la case est une intersection
	 */
	public boolean estIntersection(CoordCas c){
		int n=0;
		for(Direction d : Direction.values())
			if(caseAcessible(c,d))
				n++;
		return n>2;
	}
}
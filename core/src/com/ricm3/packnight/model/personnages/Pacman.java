/**
$******** * author : Alex et Rama
 * Class des pacmans
 * WARNING : Pensez à initialiser TERRAIN
 */
package com.ricm3.packnight.model.personnages;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.ricm3.packnight.model.hitBoxManager.HitBoxManager;
import com.ricm3.packnight.model.structure_terrain.CoordCas;
import com.ricm3.packnight.model.structure_terrain.CoordPix;
import com.ricm3.packnight.model.structure_terrain.CoordPix.position;
import com.ricm3.packnight.model.structure_terrain.Direction;

public abstract class Pacman extends Personnage {

	protected CoordPix pointDeRespawn;
	/**
	 * liste des pacmans sur le terrain
	 */
	static public List<Pacman> liste = new LinkedList<Pacman>();
	static final protected int tempsPasserMort = 40;
	public int tempsInvincible = 100;
	public boolean isInvincible=false;
	public static boolean godMode=false;
	public int compteurClignotement=0;
	
	/**
	 * Construit le pacman en initialisant son point de spawn*/
	public Pacman(String nom, int x,int y, Direction d){
		super(nom,x,y,d);
		this.pointDeRespawn = new CoordPix(x*32,y*32,position.hg);
		System.out.println(pointDeRespawn);
		Pacman.liste.add((Pacman) this);
	}
	
	/**
	 * @param position ou on veut savoir si un personnage si trouve
	 * @return renvoie vrai si un objet Personnage se trouve sur la position indiquer
	 */
	static public boolean personnagePresent(CoordCas position)
	{
		Iterator<Pacman> i= Pacman.liste.iterator();
		while(i.hasNext())
		{
			if(position.equals(i.next().coordPix.CasCentre()))
				return true;
		}
		return false;
	}
	
	static public boolean hittingPerso(CoordPix position)
	{
		Iterator<Pacman> i= Pacman.liste.iterator();
		while (i.hasNext()) {
			if (HitBoxManager.personnageHittingPersonnage(i.next().coordPix,position))
				return true;
		}
		return false;
	}
	
	static public int distance(CoordCas c)
	{
		int min = Integer.MAX_VALUE;
		Iterator<Pacman> i = Pacman.liste.iterator();
		while(i.hasNext())
		{
			int aux = i.next().coordPix.CasCentre().distance(c);
			if( aux < min)
				min = aux;
		}
		return min;
	}
	
	/**
	 * @return true si le pacman peut revivre
	 * author : alex
	 */
	public abstract boolean canRespawn();
	
	/**
	 * parametre le pac-man pour qu'il passe en animation de mort
	 */
	public void respawn()
	{
		this.agonise = true;
	}
	
	/**
	 * parametre le pac-man pour qu'il fasse un respawn sans animation
	 * effectuer une fois que l'animation est fini
	 */
	protected void respawnWOA() {
		this.coordPix.setCoord(this.pointDeRespawn);
		this.isInvincible=true;
	}
	
	public static void majTempsInvincible(){
		
		Iterator<Pacman> i = Pacman.liste.iterator();
		while(i.hasNext()){
			Pacman next = i.next();
			if(next.isInvincible){
				if(next.tempsInvincible != 0)
					next.tempsInvincible--;
				else{
					next.isInvincible = false;
					next.tempsInvincible=100;
					}
			}	
		}
		
	}
	public boolean hitting() {
		return !(agonise) && !(godMode) && !(isInvincible);
	}

}

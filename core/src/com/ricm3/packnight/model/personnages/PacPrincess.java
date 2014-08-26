/**
 * author alex
 */

package com.ricm3.packnight.model.personnages;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.ricm3.packnight.model.hitBoxManager.HitBoxManager;
import com.ricm3.packnight.model.structure_terrain.CoordCas;
import com.ricm3.packnight.model.structure_terrain.CoordPix;
import com.ricm3.packnight.model.structure_terrain.Direction;

public class PacPrincess extends Pacman{
	
	/**
	 * liste des PacPrincess sur le terrain
	 */
	static public List<PacPrincess> liste = new LinkedList<PacPrincess>();
	public static int vie = 2;
	public int perimetreSecurite = 10;
	public List<Ghost> violeurs; //Les fantomes qui ose toucher a la princesse. Pour l'instant un violeur a la fois ^^. Par la suite pk pas une liste :D
	static public CoordCas cordDeFuite = new CoordCas(1, 1);
	public PacKnight protecteur=null;
	static public int perimetreSecurite2 = 10;
	
	public PacPrincess(String name, int x, int y, Direction d) {
		super(name,x,y,d);
		PacPrincess.liste.add(this);
		violeurs = new LinkedList<Ghost>();
	}
	
	/**
	 * @param position ou on veut savoir si un personnage si trouve
	 * @return renvoie vrai si un objet Personnage se trouve sur la position indiquer
	 */
	static public boolean personnagePresent(CoordCas position)
	{
		Iterator<PacPrincess> i= PacPrincess.liste.iterator();
		while(i.hasNext())
		{
			if(position.equals(i.next().coordPix.CasCentre()))
				return true;
		}
		return false;
	}
	
	static public boolean hittingPerso(CoordPix position)
	{
		Iterator<PacPrincess> i= PacPrincess.liste.iterator();
		while (i.hasNext()) {
			if (i.next().coordPix.equals(position))
				return true;
		}
		return false;
	}
	
	public boolean canRespawn() {
		return vie !=0;
	}

	public void meurtDansDatroceSouffrance() {
		vie--;
		if(vie != 0)
		{
			respawn();
			//Ghost.central.remove(this);//La mort efface de la centrale
//			MusicManager.play_Dead_Princess();
		}
	}

	public void gererCollision() {
		Iterator<Ghost> i = Ghost.liste.iterator();
		while(this.hitting() && i.hasNext())
		{
			Ghost g = i.next();
			if(g.hitting()&& HitBoxManager.personnageHittingPersonnage(this.coordPix, g.coordPix))
			{
				this.meurtDansDatroceSouffrance();
				break;
			}
		}
	}

	public boolean parametrable() {
		return !(this.agonise);
	}

	public void avancerAnimation() {
		if(agonise)
		{
			if(this.timerAnimation < Pacman.tempsPasserMort)
			{
				this.timerAnimation++;
				//faire avancer d'un cran l'animation
			}
			else
			{
				this.timerAnimation=0;
				this.agonise=false;
				this.respawnWOA();
			}
		}
	}	

}

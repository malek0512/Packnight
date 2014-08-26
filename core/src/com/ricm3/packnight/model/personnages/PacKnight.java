/**
 * author : Alex
 */
package com.ricm3.packnight.model.personnages;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.ricm3.packnight.model.hitBoxManager.HitBoxManager;
import com.ricm3.packnight.model.structure_terrain.Case;
import com.ricm3.packnight.model.structure_terrain.CoordCas;
import com.ricm3.packnight.model.structure_terrain.CoordPix;
import com.ricm3.packnight.model.structure_terrain.Direction;
import com.ricm3.packnight.model.structure_terrain.Terrain;
import com.ricm3.packnight.view.MusicManager;
import com.ricm3.packnight.view.MusicManager.typeSong;

public class PacKnight extends Pacman{

	/**
	 * liste des PacKnight sur le terrain
	 */
	static public List<PacKnight> liste = new LinkedList<PacKnight>();
	public static int vie = 10;	
	private boolean user;
	
	/**
	 * @return the controlable
	 */
	public boolean user() {
		return this.user;
	}

	//La princesse aurait une action, qui permet de signaler sa detresse, en mettant son referenceur dans cette variable
	public PacPrincess princesseEnDetresse = null;
	
	//Contient le fantome apres lequel le knight est a la recherche
	public Ghost ghostEnChasse = null;
	
	public PacKnight(String name, int x, int y, Direction d, boolean userPlaying) {
		super(name,x,y,d);
		PacKnight.liste.add(this);
		this.user = userPlaying;
	}
	
	/**
	 * @param position ou on veut savoir si un personnage si trouve
	 * @return renvoie vrai si un objet Personnage se trouve sur la position indiquer
	 */
	static public boolean personnagePresent(CoordCas position)
	{
		Iterator<PacKnight> i= PacKnight.liste.iterator();
		while(i.hasNext())
		{
			if(position.equals(i.next().coordPix.CasCentre()))
				return true;
		}
		return false;
	}

	static public boolean hittingPerso(CoordPix position)
	{
		Iterator<PacKnight> i= PacKnight.liste.iterator();
		while (i.hasNext()) {
			if (HitBoxManager.personnageHittingPersonnage(i.next().coordPix,position))
				return true;
		}
		return false;
	}
	
	public boolean canRespawn() {
		return vie > 0;
	}

	public void meurtDansDatroceSouffrance() {
		vie--;
		if(vie > 0)
		{
			respawn();
			Ghost.central.remove(this);//La mort efface de la centrale
			MusicManager.playOnce(typeSong.Dead_Knigth);
		}
	}

	public void gererCollision() {
		Iterator<Ghost> i = Ghost.liste.iterator();
		while(this.hitting() && i.hasNext())
		{
			Ghost g = i.next();
			if(g.hitting() && HitBoxManager.personnageHittingPersonnage(this.coordPix, g.coordPix))
			{
				this.meurtDansDatroceSouffrance();
				g.meurtDansDatroceSouffrance(); //vengeance !!!
				break;
			}
		}
		if(Personnage.terrain.ValueCase(this.coordPix.CasCentre()) == Case.Pacgum)
		{
			this.mangePacGomm();
		}
	}
	
	private void mangePacGomm()
	{
		Personnage.terrain.SetCase(this.coordPix.CasCentre(),Case.Vide);
		Terrain.nb_pacgum--;
	}
	
	/**
	 * renvoie vrai si le pac-knight est parametrable
	 */
	public boolean parametrable() {
		return !(this.agonise);
	}


	/**
	 * fait avancer les animations en cours d'un cran
	 */
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
	
	public boolean peutProteger(){
		return ghostEnChasse==null;
	}
}

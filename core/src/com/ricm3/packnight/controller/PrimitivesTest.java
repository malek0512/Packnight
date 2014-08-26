package com.ricm3.packnight.controller;


import java.util.Iterator;
import java.util.List;

import com.ricm3.packnight.model.personnages.*;


/**
 * Classe contenant l'ensemble des primitives de test 
 * @author malek
 */
public class PrimitivesTest extends Primitives {

	public PrimitivesTest(Automate a) {
		super();
		this.auto = a;
	}

	/**
	 * Fonction de test Automate.PM_DANS_RAYON_X
	 * @param d
	 * @return True si un ou plusieurs pacman sont dans le rayon 
	 */
	protected boolean dansRayon(int d) {
		List<Pacman> res=pacmanEstDansRayon(auto.getPersonnage().coordPix.CasCentre(),d);
		return res.size()!=0;
		
	}
	/**
	 * Methode utilisant le centrale contrairement à dansrayon*/
	protected boolean vu(){
		boolean res=false;
		for(Iterator<Pacman> i = Pacman.liste.iterator();i.hasNext();){
			Pacman pac = i.next();
			if((dansRayon(((Ghost)auto.getPersonnage()).getVision()) || (Ghost.central.containsKey(pac) && pac.parametrable() && !pac.isInvincible))){
				res=true;
			}
		}
		return res;
	}
	/**
	 * @return Une ENTREE de l'automate. Selon la configuratoin de la case devant le robot
	 * @author malek
	 */
	public Automate.Entree configCaseDevant() {
		boolean caseDevantDispo = this.auto.getPersonnage().caseDevantDisponible();
		if (!caseDevantDispo)
			return Automate.Entree.CASE_OCCUPEE;
		else
			return Automate.Entree.CASE_LIBRE;
	}
	/**
	 * Specification de la méthode dans primitive
	 * Ne s'applique qu'au personnage de l'automate en cours d'utilisation*/
	public boolean estIntersection(){
		
		return estIntersection(auto.getPersonnage().coordPix.CasCentre());
	}
	/**
	 * utilise la méthode pacman est dans croix de primitive avec déjà l'argument personnage.
	 * 
	 * */
	public boolean dansCroix(){
		return pacmanEstDansCroix(auto.getPersonnage().coordPix.CasCentre());
	}
	/**
	 * @return : Vrai si le déplacment du fantôme s'est fait d'une case*/
	public boolean caseAtteinte(){
		return auto.getPersonnage().coordPix.estSurUneCase();
	}
	
	/**
	 * @return : Vrai si le fantôme est controllé par le fantôme Lord*/
	public boolean isControled(){
		
		return ((Ghost) auto.getPersonnage()).getEntendEtObei();
	}
	
	/**
	 * PacPrincess
	 * Fonction de test Automate.FM_DANS_RAYON
	 * Elle met a jour la liste des agresseurs de la princesse
	 * @param d
	 * @return True si un ou plusieurs FM sont dans le rayon, de princessssse
	 * @author malek
	 */
	protected boolean fmDansRayon() {
		int perimetre = PacPrincess.perimetreSecurite2 ;
		List<Ghost> agresseurs = fantomeEstDansRayon(perimetre);
		if(this.auto.getPersonnage() instanceof PacPrincess)
			((PacPrincess) auto.getPersonnage()).violeurs = agresseurs;
		return agresseurs.size()!=0;
	}

	/**
	 * PacKnight
	 * @return Vrai si knight estime que le fantome est tojours dans le perimetre de la princesse. Et qu'il doit lui porter secour
	 * @require La princesse s'identifie et identifie son agresseur, sinon exception
	 * @author malek
	 * @throws Exception 
	 */
	public boolean Chasse() throws Exception{ 
		//casting des perso
		Personnage gosthEnChasse = ((PacKnight) auto.getPersonnage()).ghostEnChasse;
		PacPrincess princess = ((PacKnight) auto.getPersonnage()).princesseEnDetresse;
		if (gosthEnChasse==null || princess==null)
		{
			//rien a chasse
			return false;
		}
		//Renvoie vrai si ghost dans rayon de princesse
		boolean e = personnageEstDansRayon(princess.perimetreSecurite, princess, gosthEnChasse);
		if(!e)
			((PacKnight) auto.getPersonnage()).ghostEnChasse=null;
		return e;
	}
	/**
	 *PacPrincess
	 *Vérifie le que le nombre de garde autour d'elle est >0*/
	public boolean nombreGardeSuffisant(){
		
		return (nombreGarde((PacPrincess)auto.getPersonnage())>0);
	}
	
	/**
	 * Packnight 
	 * Vérifie que la princess ne l'a pas appelé pour l'escorter*/
	public boolean appelAuDevoir(){
		return ((PacKnight)auto.getPersonnage()).princesseEnDetresse!=null;
	}
	/**
	 * PacPrincess
	 * Vérifie que le nombre de pacman est supérieur ou égal au nombre de fantôme
	 * */
	public boolean safe(){
		int perimetre = PacPrincess.perimetreSecurite2 ;
		List<Ghost> agresseurs = fantomeEstDansRayon(perimetre);
		int nb=nombreGarde(((PacPrincess)auto.getPersonnage()));
		return (nb>=agresseurs.size());
	}
	
}



package com.ricm3.packnight.controller;



import java.util.Iterator;
import java.util.Random;

import com.ricm3.packnight.model.personnages.Ghost;
import com.ricm3.packnight.model.personnages.PacKnight;
import com.ricm3.packnight.model.personnages.PacPrincess;
import com.ricm3.packnight.model.personnages.Pacman;
import com.ricm3.packnight.model.personnages.Personnage;
import com.ricm3.packnight.model.structure_terrain.CoordCas;
import com.ricm3.packnight.model.structure_terrain.Direction;


/**
 * Classe contenant l'ensemble des primitives d'action
 * 
 * @author malek
 */
public class PrimitivesAction extends Primitives {

	public PrimitivesAction(Automate a) {
		super();
		this.auto = a;
	}
	
	//l'action ne rien faire
	public void pass()
	{
	}
	
	/**
	 * Rend une direction aléatoire parmis celle disponible lors de l'arrivée
	 * d'une intersection
	 * 
	 * @param personnage
	 *            auquel on veut changer la direction aléatoirement
	 * */
	public void setDirectionAleatoire(Personnage perso) {
		Random rnd = new Random();
		int i = 0;
		int alea;
		Direction[] direct = new Direction[4];

		for (Direction d : Direction.values()) {
			if (perso.caseDisponible(d)
					&& perso.direction != d.opposer()) {
				direct[i] = d;
				i++;
			}
		}
		alea = rnd.nextInt(i);
		perso.setDirection(direct[alea]);
	}

	/**
	 * Reçoit un ordre du Fantôme Lord et avance vers la case désignée tant
	 * qu'il ne l'a pas atteinte
	 * */
	public void obeir() {
		if(Ghost.powerUp())
		{
			Iterator<Pacman> i = Ghost.central.keySet().iterator();
			if (i.hasNext()) {
				Pacman max = i.next();
				while (i.hasNext()) {
					Pacman next = i.next();
					if (Ghost.central.get(next).timer > Ghost.central.get(max).timer)
						max = next;
				}
				((Ghost) auto.getPersonnage()).donnerDesOrdres(max);
			}
		}
		else
		{
			setDirectionAleatoire(auto.getPersonnage());
			auto.getPersonnage().avancer();
		}
	}

	/**
	 * Bloc le personnage pendant un certain temps (10 cycles actuellement)
	 */
	public void stun() {

		((Ghost) auto.getPersonnage()).stun();
	}

	/**
	 * PACKPRINCESSE : Appelle le plus proche packnight de la princesse Si le
	 * knight est deja au service de la princesse : - on lui renseigne le nouvel
	 * fantome a chasser, si pas mort
	 * 
	 * @author malek
	 * @throws Exception
	 */
	public void auSecours() throws Exception{
		//recuperation du personnage
//		System.out.println("COUCOU auSecours");
		PacPrincess bitch = (PacPrincess) auto.getPersonnage();
		//pour chaque ghost dans le rayons
		int i =0;
		for(Ghost violeur : bitch.violeurs)
		{
			if(violeur.hitting() && !isAlreadyChassed(violeur))
			{
				//selection du héro
				PacKnight p = this.whichHero(bitch);
				if (p!=null) 
				{
//					System.out.println("trouver");
					p.princesseEnDetresse = bitch; //on parametre le packnight
					p.ghostEnChasse = violeur;
					bitch.violeurs.remove(i); //on retire le violeur, il est en cours de "traitement"
				}
			}
			else
				bitch.violeurs.remove(i);
		i++;
		}
	}

	/**
	 * PACKNIGHT
	 * chasse le fantome poursuivant la princesse
	 * @author malek
	 * @throws Exception
	 *             Si ghostEnChasse==null ou princesseEnDetresse==null
	 */
	public void protegerPrincesse() throws Exception
	{
		//cast du perso
		PacKnight knight = ((PacKnight) auto.getPersonnage());
		
		if(knight.ghostEnChasse != null)
		{
			avancerVers((knight.ghostEnChasse.coordPix.CasCentre()));;
		}

	}

	/**
	 * Primitive pour un packnight
	 * Patrouille autour de la princesse*/
	public void patrouiller(){
		
		if(((PacKnight)auto.getPersonnage()).princesseEnDetresse!=null && ((PacKnight)auto.getPersonnage()).parametrable()){
			PacPrincess bitch =((PacKnight)auto.getPersonnage()).princesseEnDetresse;
			if(!personnageEstDansRayon(bitch.perimetreSecurite,bitch,((PacKnight)auto.getPersonnage())))
				avancerVers(bitch.coordPix.CasCentre());
			//else
				//fetch();
		}
		else{
			((PacKnight)auto.getPersonnage()).princesseEnDetresse=null;
		}
	}
	
	/**
	 * Primitive pour une PacPrincess*/
	public void appelPatrouilleur(){
		PacPrincess bitch = (PacPrincess) auto.getPersonnage();
		if(!PacKnight.liste.isEmpty())
		{
			((PacPrincess) auto.getPersonnage()).protecteur=PacKnight.liste.get(0);
			PacKnight p = this.whichHero(bitch);
			if( p!=null && p.parametrable()) 
			{
	//			System.out.println("trouver");
				if(p!=((PacPrincess) auto.getPersonnage()).protecteur){
					p.princesseEnDetresse = bitch;//on parametre le packnight
					((PacPrincess) auto.getPersonnage()).protecteur.princesseEnDetresse=null;
				}
				
				else
					((PacPrincess) auto.getPersonnage()).protecteur.princesseEnDetresse=bitch;
				
				
			}
		}
	} 
		
	public void suivre() {
		Iterator<Pacman> i = Ghost.central.keySet().iterator();
		CoordCas bestChallenger = null;
		while (i.hasNext()) {
			Pacman perso = i.next();
			CoordCas next= perso.coordPix.CasCentre();
			if(perso.parametrable() && !perso.isInvincible){
				if(bestChallenger == null)
				{
					bestChallenger = next;
				}
				else if (next.distance(auto.getPersonnage().coordPix.CasCentre()) 
					< next.distance(bestChallenger))
				{
					bestChallenger = next;
				}
			}
		}
		if(bestChallenger == null)
		{
			System.out.println("pas trouvé de pacman a suivre");
		}
		else
		{
			avancerVers(bestChallenger);
		}
	}
	
	public void intercepter() {
		Iterator<Pacman> i = Ghost.central.keySet().iterator();
		CoordCas bestChallenger = null;
		Pacman min = null;
		while (i.hasNext()) {
			Pacman perso = i.next();
			CoordCas next= perso.coordPix.CasCentre();
			if(perso.parametrable() && !perso.isInvincible){
				if(bestChallenger == null)
				{
					bestChallenger = next;
					min=perso;
				}
				else if (next.distance(auto.getPersonnage().coordPix.CasCentre()) 
					< next.distance(bestChallenger))
				{
					bestChallenger = next;
					min=perso;
				}
			}
		}
		if(bestChallenger == null)
		{
			System.out.println("pas trouvé de pacman a suivre");
		}
		else {
			CoordCas minCord = bestChallenger;
			CoordCas inter = prochaineCasAvantInter(minCord, min.direction.opposer());
			
			if(inter == null)
				avancerVers(minCord);
			else
				avancerVers(minCord,inter);
		}
	}
	
	
	
	/**
	 * Seul pac princesse peut fuir
	 */
/*	
public void fuir(CoordonneesFloat ref)
	{
		if(auto.sneaky == null)
		{
			CoordonneesFloat src = this.auto.getPersonnage().coord.CasCentre();
			Aetoile graph = new Aetoile(src);
			graph.blackList(blist);
			List<CoordonneesFloat> l = graph.algo(new CoordonneesFloat(ref));
			l.remove(0);
			if(l.size()==0)
				setDirectionAleatoire(auto.getPersonnage());
			else
				auto.sneaky = mysteriousFunction(src, l.get(0));
			
		}
		else
		{
			if(auto.getPersonnage().caseDisponible(auto.sneaky))
			{
				auto.getPersonnage().setDirection(auto.sneaky);
				auto.sneaky = null;
			}
			else
			{
				CoordonneesFloat src = this.auto.getPersonnage().coord.CasCentre();
				Aetoile graph = new Aetoile(src);
				graph.blackList(blist);
				List<CoordonneesFloat> l = graph.algo(new CoordonneesFloat(ref));
				l.remove(0);
				if(l.size()==0)
					setDirectionAleatoire(auto.getPersonnage());
				else
					auto.sneaky = mysteriousFunction(src, l.get(0));
			}
		}
		this.auto.getPersonnage().avancer();
	}
	
	public void fuir()
	{
		if(PacPrincess.cordDeFuite != null && this.auto.getPersonnage().coord.CasCentre().distance(PacPrincess.cordDeFuite) < 5)
		{
			//calcule d'une nouvelle cord de fuite
			Random rnd = new Random();
			int i,j;
			Terrain t = Personnage.getTerrain();
			CoordonneesFloat dest = null;
			if(Terrain.nb_pacgum<=40){
				int alea;
				alea = rnd.nextInt(4);
				switch(alea)
				{
				case 0 :
					PacPrincess.cordDeFuite = new CoordonneesFloat(1,1); break;
				case 1 :
					PacPrincess.cordDeFuite = new CoordonneesFloat(1,Personnage.getTerrain().hauteur-2); break;
				case 2 :
					PacPrincess.cordDeFuite = new CoordonneesFloat(Personnage.getTerrain().largeur-2,1); break;
				case 3 :
					PacPrincess.cordDeFuite = new CoordonneesFloat(Personnage.getTerrain().largeur-2,Personnage.getTerrain().hauteur-2); break;
				}
			}
				
			else{ 
				int x=t.largeur; 
				int y=t.hauteur; 
				i = rnd.nextInt(x);
				j = rnd.nextInt(y);
					while (t.ValueCase(i, j) != 2 || ((dest=new CoordonneesFloat(i, j))==PacPrincess.cordDeFuite) ) 
					{
						i = rnd.nextInt(x);
						j = rnd.nextInt(y);
						
					}
					PacPrincess.cordDeFuite=dest;
				}
		}
		fuir(Ghost.listCoord(),PacPrincess.cordDeFuite);
	}
	*/
	/**
	 * envoie le personnage manger des pac-gomm
	 */
	/*
	public void fetch() {
		// 0 : pac-gom
		// 1 : distance
		// 2 : personnage

		// 3 : avenir pac-gom
		// 4 : avenir distance
		// 5 : avenir personnage
		CoordonneesFloat caseDuPerso = new CoordonneesFloat(auto.getPersonnage().coord);
		if (caseDuPerso.CasBG().equals(caseDuPerso.CasHD())&& estIntersection(caseDuPerso)) 
		{
			int tab[][] = laFonctionQuiFaitTout(caseDuPerso.CasCentre(),1);

			int meilleurCandidat = Integer.MIN_VALUE;
			Direction meilleurCandidatDirection = null;
			int cpt = 0;
			for (Direction d : Direction.values()) 
			{// pour chaque direction
				if (tab[cpt][1] != 0) 
				{
					if (tab[cpt][0] + tab[cpt][3] > 0) 
					{
						int candidat = 0;
						for (int k = 0; k < 3; k++)
							candidat += ImportanceRacine * tab[cpt][k];
						for (int k = 3; k < 6; k++)
							candidat += ImportanceBranche * tab[cpt][k];
						if (meilleurCandidat < candidat) {
							meilleurCandidat = candidat;
							meilleurCandidatDirection = d;
						}
					}
				}
				cpt++;
			}
			if (meilleurCandidatDirection != null) 
			{
				this.auto.getPersonnage().setDirection(
						meilleurCandidatDirection);
				this.auto.getPersonnage().avancer();
			} 
			else 
			{
				Terrain t = Personnage.getTerrain();
				CoordonneesFloat dest = null;
				for (int i = 0; i < t.largeur; i++) 
					for (int j = 0; j < t.hauteur; j++) 
						if (t.ValueCase(i, j) == 2) 
						{
							dest = new CoordonneesFloat(i, j);
							i=t.largeur;
							j=t.hauteur;
						}
				if (dest != null)
					this.fuir(Ghost.listCoord(),dest);
			}
		} 
		else 
		{
			setDirectionAleatoire(this.auto.getPersonnage());
			this.auto.getPersonnage().avancer();
		}
	}
	*/
}

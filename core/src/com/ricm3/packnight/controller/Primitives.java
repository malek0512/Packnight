package com.ricm3.packnight.controller;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.ricm3.packnight.model.graph.Aetoile;
import com.ricm3.packnight.model.personnages.AvisDeRecherche;
import com.ricm3.packnight.model.personnages.Ghost;
import com.ricm3.packnight.model.personnages.PacKnight;
import com.ricm3.packnight.model.personnages.PacPrincess;
import com.ricm3.packnight.model.personnages.Pacman;
import com.ricm3.packnight.model.personnages.Personnage;
import com.ricm3.packnight.model.structure_terrain.CoordCas;
import com.ricm3.packnight.model.structure_terrain.Direction;

/**
 * Classe contenant l'ensemble de fonction intermedaire permettant l'elaboration
 * des primitives de test et d'action
 * 
 * @author malek
 */
public class Primitives {
	Automate auto;
	List<CoordCas> chemin; // Utilisé par prochaineCase.
	int nbCout = 0; // Utilisé par prochaineCase.

	/**
	 * TODO : a move dans PacMan Test si un objet est en contact d'un pacman
	 * author : alex
	 * 
	 * @param cord
	 *            : coordonée de l'objet a tester
	 * @return vrai si un pacman ou plus se trouve sur les coordonnée indiquée
	 */
	protected boolean caseEstPM(CoordCas cord) {
		return Pacman.personnagePresent(cord);
	}

	/**
	 * @param position
	 *            : coordonner du fantome
	 * @param rayon
	 *            : rayon de vision du Fantome a la position donnée
	 * @return la liste des pacman de le champ de vision
	 */
	protected List<Pacman> pacmanEstDansRayon(CoordCas position,
			int rayon) {
		List<Pacman> res = new LinkedList<Pacman>();

		for (Iterator<Pacman> i = Pacman.liste.iterator(); i.hasNext();) {
			Pacman pac = i.next();
			if (position.distance(pac.coordPix.CasCentre()) <= rayon && !pac.isInvincible && pac.parametrable()) {
				res.add(pac);
				if (Ghost.central.containsKey(pac)) {
					Ghost.central.get(pac).majAvisDeRecherche(
							pac.coordPix.CasCentre());
				} 
				else
				{
					if(Ghost.central.isEmpty())
					{
//						MusicManager.play_Reperer();
					}
					Ghost.central.put(pac,new AvisDeRecherche(pac.coordPix.CasCentre()));
				}
			}

		}
		return res;
	}

	/**
	 * @param position coordonnée du centre du fantomea croix 
	 * @return vrai si il vois un pacman is la ligne et qu'il n'y a pas de mur entre les deux
	 * @Action met tourne le fantome vers pac-man
	 * @author rama/vivien
	 */
	protected boolean pacmanEstDansCroix(CoordCas position) {
		int j = 0;
		boolean res = false;

		for (Iterator<Pacman> i = Pacman.liste.iterator(); i.hasNext();) {
			Pacman pac = i.next();
			
			CoordCas coord = pac.coordPix.CasCentre();
			if (coord.x == position.x) {
				if (coord.y < position.y) {
					while (mur(position, j, Direction.haut)
							&& coord.y != (position.y - j)) {
						j++;
					}
					if (mur(position, j, Direction.haut)) {
						auto.getPersonnage().setDirection(Direction.haut);
						return true;

					}
				} else {
					while (mur(position, j, Direction.bas)
							&& coord.y != (position.y + j)) {
						j++;

					}
					if (mur(position, j, Direction.bas)) {
						auto.getPersonnage().setDirection(Direction.bas);
						return true;
					}
				}

			} else if (coord.y == position.y) {
				if (coord.x < position.x) {
					while (mur(position, j, Direction.gauche)
							&& coord.x != (position.x - j)) {
						j++;
					}
					if (mur(position, j, Direction.gauche)) {
						auto.getPersonnage().setDirection(Direction.gauche);
						return true;
					}
				}

				else {
					while (mur(position, j, Direction.droite)
							&& coord.x != (position.x + j)) {
						j++;
					}
					if (mur(position, j, Direction.droite)) {
						auto.getPersonnage().setDirection(Direction.droite);
						return true;
					}
				}
			}
		}
		return res;
	}

	/**
	 * @param temp
	 *            : Coordonnees de la case à tester si présence d'un mur, A UNE
	 *            DISTANCE i
	 * @return boolean Vrai si il y a un mur faux sinon
	 * @author vivien
	 * */
	private boolean mur(CoordCas test, int i, Direction d) {
		return Personnage.terrain.caseAcessible(test, i, d);
	}

	/**
	 * @return dans les parametres la case devant le Personnage selon sa
	 *         direction actuelle
	 * @author malek
	 * @param d
	 * @param x
	 * @param y
	 */
	public CoordCas positionDevant() {
		CoordCas coord = this.auto.getPersonnage().coordPix.convertIntoCas();
		coord.avancerDansDir(auto.getPersonnage().direction);
		return coord;
	}

	/**
	 * Pas merci ! :)
	 * 
	 * @return Vrai si la case est une intersection
	 */
	public boolean estIntersection(CoordCas coord) {
		int n = 0;
		for (Direction d : Direction.values()){
			if (Personnage.terrain.caseAcessible(coord, d))
				n++;
		}
		return n > 2;
	}

	/**
	 * TODO A adapter lors de la disponibilité de l'algorithme A etoile Renvoie
	 * les coordonnées de la prochaine case, afin d'atteindre la coordonnée c.
	 * Le chemin est mis a jour tous les 3 couts. A eventuellement modifier afin
	 * de prendre en compte la distance Utilise la variable globale,
	 * List<Coordonnees> chemin, et int nbCout
	 * 
	 * @param c
	 * @author malek
	 */
	protected CoordCas prochaineCase(CoordCas c) {
		// Si nous somme deja sur la case demandé
		if (auto.getPersonnage().coordPix.equals(c))
			return c;

		// On met a jour le chemin vers c, dans l'un des cas stipulé dans la
		// condition
		if (chemin == null || chemin.size() == 0 || nbCout > 3) {
			Aetoile depart = new Aetoile(c);
			chemin = depart.algo(auto.getPersonnage().coordPix.CasCentre()); // case
			nbCout = 0;
		}

		// Declenche une erreur si l'assertion est verifiée
		assert (chemin.size() == 0) : "Erreur fonction (primitives.java) prochaineCase. La chemin.size()==0. "
				+ "N'existe-t-il pas de chemin vers la coordonnées donnée en parametre ?";

		nbCout++;
		CoordCas prochain = chemin.get(0);
		chemin.remove(0);
		return prochain;
	}

	protected boolean isAlreadyChassed(Ghost g){
		boolean res = false;
		for(PacKnight knight : PacKnight.liste){
			res = res || knight.ghostEnChasse==g;
		}
		return res;
	}

	/**
	 * @return le Packnight le plus proche de la princesse
	 * @author malek
	 * @throws Exception
	 */
	protected PacKnight whichHero(PacPrincess bitch) {

		PacKnight captain = null;
		int d_bestFound =Integer.MAX_VALUE;
		
		for(PacKnight knight : PacKnight.liste){
			int d_candidat = knight.coordPix.CasCentre().distance(bitch.coordPix.CasCentre());
			if(!knight.user() && d_candidat < d_bestFound && knight.ghostEnChasse ==null)
			{
				captain = knight;
				d_bestFound =d_candidat;
			}
		}
		return captain;
	}

	/**
	 * PacPrincesse et PacKnight
	 * 
	 * @param rayon
	 *            : rayon de vision du personnage de l'automate
	 * @return la liste des fantomes vivants dans champ de vision de la
	 *         princesse
	 */
	protected List<Ghost> fantomeEstDansRayon(int rayon) {
		List<Ghost> res = new LinkedList<Ghost>();
		CoordCas position = auto.getPersonnage().coordPix.CasCentre();
		
		for(Ghost pac : Ghost.liste)
		{
			if(position.distance(pac.coordPix.CasCentre())<=rayon){
				res.add(pac);
			}
		}
		return res;
	}

	/**
	 * PacPrincesse et PacKnight
	 * 
	 * @param rayon
	 *            : rayon de vision du personnage de l'automate
	 * @return Vrai si p2 est dans le rayon de p1
	 */
	protected boolean personnageEstDansRayon(int rayon, Personnage p1,
			Personnage p2) {

		if(p2.parametrable() && p1.coordPix.CasCentre().distance(p2.coordPix.CasCentre())<=rayon)
				return true;
		return false;
	}

	/**
	 * Renseigne sur le nombre de pacman dans le périmètre*/
	public int nombreGarde(PacPrincess bitch){
		int n=0;
		for(PacKnight knight : PacKnight.liste){
			if(personnageEstDansRayon(bitch.perimetreSecurite,bitch,knight))
				n++;
		}
		return n;
		}
	
	final int Value_pacgom = 15;
	final int Value_distance = -1;
	final int Value_ghost = Integer.MIN_VALUE;
	final int Value_pacKnight = -100;

	final int Value_futur_pacgom = 2;
	final int Value_futur_distance = -1;
	final int Value_futur_ghost = Integer.MIN_VALUE;;
	final int Value_futur_pacKnight = -10;

	final int Value_pacgom2 = 0;
	final int Value_distance2 = -1;
	final int Value_ghost2 = -Integer.MIN_VALUE;;
	final int Value_pacKnight2 = 0;

	final int Value_futur_pacgom2 = 0;
	final int Value_futur_distance2 = -1;
	final int Value_futur_ghost2 = Integer.MIN_VALUE;;
	final int Value_futur_pacKnight2 = 0;

	
	final int ImportanceRacine = 4;
	final int ImportanceBranche = 1;

	public void avancerVers(CoordCas dest)
	{
		if(this.auto.getPersonnage().coordPix.estSurUneCase())
		{
			CoordCas src = this.auto.getPersonnage().coordPix.CasCentre();
			Aetoile graph = new Aetoile(src);
			List<CoordCas> l = graph.algo(new CoordCas(dest));
			l.remove(0);
			if(l.isEmpty())
			{
				System.out.println("Un personnage n'a trouvé aucun chemin 2");
			}
			else
			{
				follow(l);
			}
		}
		else
			this.auto.getPersonnage().avancer();
	}
	
	/**
	 * fait avancer le personnage vers la case dest en évitant la liste renseigner
	 * @param dest
	 * @param CaseAEviter
	 */
	public void avancerVers(CoordCas dest, List<CoordCas> CaseAEviter)
	{
		CoordCas src = this.auto.getPersonnage().coordPix.CasCentre();
		Aetoile graph = new Aetoile(src);
		graph.blackList(CaseAEviter);
		List<CoordCas> l = graph.algo(new CoordCas(dest));
		l.remove(0);
		if(l.isEmpty())
		{
			System.out.println("Un personnage n'a trouvé aucun chemin 1");
		}
		else
		{
			follow(l);
		}
	}
	
	public void avancerVers(CoordCas dest, CoordCas CaseAEviter)
	{
		CoordCas src = this.auto.getPersonnage().coordPix.CasCentre();
		Aetoile graph = new Aetoile(src);
		graph.blackCoord(CaseAEviter);
		List<CoordCas> l = graph.algo(new CoordCas(dest));
		l.remove(0);
		if(l.isEmpty())
		{
			System.out.println("Un personnage n'a trouvé aucun chemin 3");
			avancerVers(dest);
		}
		else
		{
			follow(l);
		}
	}
	
	private void follow(List<CoordCas> l)
	{
		CoordCas src = this.auto.getPersonnage().coordPix.CasCentre();
		CoordCas c = l.get(0);
		Direction d = src.directionPourAllerVers(c);
		if(this.auto.getPersonnage().caseDisponible(d))
			this.auto.getPersonnage().direction=d;
		this.auto.getPersonnage().avancer();
	}
	

	/**
	 * renvoie les coordonée de la prochaine intersection dans la direction donnée
	 */
	public CoordCas prochaineInterCas(CoordCas cord, Direction dir)
	{
		CoordCas c=  new CoordCas(cord);
		Direction dirEx =dir;
		while(!estIntersection(c))
		{
			if(Personnage.terrain.caseAcessible(c, dirEx))
			{
			}
			else if(Personnage.terrain.caseAcessible(c, dirEx.aDroite()))
			{
				dirEx = dirEx.aDroite();
			}
			else
			{
				dirEx = dirEx.aGauche();
			}
			c.avancerDansDir(dirEx);
		}
		return c;
	}
	
	/**
	 * renvoie la case avant la prochain intersection
	 * si on est sur une intersection, renvoi null
	 * @param cord : coordoner ou commencer a chercher
	 * @param dir : direction vers ou chercher
	 * @return la case qui est avant la prochaine intersection dans la direction donné
	 */
	public CoordCas prochaineCasAvantInter(CoordCas cord, Direction dir)
	{
		CoordCas c=  new CoordCas(cord);
		CoordCas res = c;
		Direction dirEx =dir;
		if(estIntersection(c))
			return null;
		while(!estIntersection(c))
		{
			res = new CoordCas(c);
			if(Personnage.terrain.caseAcessible(c, dirEx))
			{
			}
			else if(Personnage.terrain.caseAcessible(c, dirEx.aDroite()))
			{
				dirEx = dirEx.aDroite();
			}
			else
			{
				dirEx = dirEx.aGauche();
			}
			c.avancerDansDir(dirEx);
		}
		return res;
	}
	
	
	
	/**
	 * Fonction qui calcule le poids des toutes les intersection sauf celle de
	 * la source
	 * 
	 * @param argcordDonne
	 *            : coord de l'intersection, exprimer en case
	 * @param src
	 *            : direction vers la source
	 * @return un tableau de int contenant les valeurs pour chaque inter
	 */
	/*
	public int[][] laFonctionQuiFaitPresqueTout(CoordonneesFloat coordo,
			Direction src, int mode) {
		// 0 : pac-gom
		// 1 : distance
		// 2 : personnage
		int cpt=0;
		int tab[][] = new int[4][3];
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 3; j++)
				tab[i][j] = 0;

		int nbInter = -1;

		for (Direction d : Direction.values()) {
			nbInter++;
			if (d != src && Personnage.getTerrain().caseAcessible(coordo.x,coordo.y, d)) 
			{
				CoordonneesFloat coordonne = new CoordonneesFloat(coordo);
				// faire avancer le c dans la direction d
				Direction directionCord = d;
				coordonne.avancerDansDir(d);
				tab[nbInter][1] += Value_distance;
				while (!estIntersectionCas(coordonne) && cpt < 100) {
					if(mode == 1)
					{
						// tester si pac-gom
						if (Personnage.getTerrain().ValueCase(coordonne) == 2)
							tab[nbInter][0] += Value_futur_pacgom;
						// incremente la distance
						tab[nbInter][1] += Value_futur_distance;
						// tester si fantome
						if (Ghost.personnagePresentCas(coordonne))
							tab[nbInter][2] += Value_futur_ghost;
						// tester si pacKnight
						if (PacKnight.personnagePresentCas(coordonne))
							tab[nbInter][2] += Value_futur_pacKnight;
					}
					if(mode==2)
					{
						// tester si pac-gom
						if (Personnage.getTerrain().ValueCase(coordonne) == 2)
							tab[nbInter][0] += Value_futur_pacgom2;
						// incremente la distance
						tab[nbInter][1] += Value_futur_distance2;
						// tester si fantome
						if (Ghost.personnagePresentCas(coordonne))
							tab[nbInter][2] += Value_futur_ghost2;
						// tester si pacKnight
						if (PacKnight.personnagePresentCas(coordonne))
							tab[nbInter][2] += Value_futur_pacKnight2;
					}
					// faire avancer coordCaseEnCours
					for (Direction d2 : Direction.values()) {
						if (directionCord != d2.opposer()
								&& Personnage.getTerrain().caseAcessible(
										coordonne.x, coordonne.y, d2)) {
							coordonne.avancerDansDir(d2);
							directionCord = d2;
							break;
						}
					}
				cpt++;}
			}
		}
		return tab;
	}
	*/
	/**
	 * Fonction qui calcul le poids de toutes les intersection
	 * 
	 * @param cord
	 *            : coord de l'intersection exprimer en case
	 * @return un tableau de int contenant les valeurs pour chaque inter
	 */
	/*
	public int[][] laFonctionQuiFaitTout(CoordonneesFloat cord, int mode) {
		// 0 : pac-gom
		// 1 : distance
		// 2 : personnage

		// 3 : avenir pac-gom
		// 4 : avenir distance
		// 5 : avenir personnage

		int[][] tab = new int[4][6];
		int cpt=0;
		// init du tableau
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 6; j++)
				tab[i][j] = 0;

		int nbInter = -1;
		for (Direction d : Direction.values()) {
			nbInter++;
			if (Personnage.getTerrain().caseAcessible(cord.x, cord.y, d)) {// si
																			// case
																			// accessible
				CoordonneesFloat cordCaseAcutel = new CoordonneesFloat(cord);
				// faire avancer le c dans la direction d
				Direction directionCord = d;
				cordCaseAcutel.avancerDansDir(d);
				tab[nbInter][1] += Value_distance;
				while (!estIntersectionCas(cordCaseAcutel)&& cpt<100) {
					if(mode == 1)
					{
					// tester si pac-gom
					if (Personnage.getTerrain().ValueCase(cordCaseAcutel) == 2)
						tab[nbInter][0] += Value_pacgom;
					// incremente la distance
					tab[nbInter][1] += Value_distance;
					// tester si fantome
					if (Ghost.personnagePresentCas(cordCaseAcutel))
						tab[nbInter][2] += Value_ghost;
					// tester si pacKnight
					if (PacKnight.personnagePresentCas(cordCaseAcutel))
						tab[nbInter][2] += Value_pacKnight;
					}
					if(mode ==2)
					{
						// tester si pac-gom
						if (Personnage.getTerrain().ValueCase(cordCaseAcutel) == 2)
							tab[nbInter][0] += Value_pacgom2;
						// incremente la distance
						tab[nbInter][1] += Value_distance2;
						// tester si fantome
						if (Ghost.personnagePresentCas(cordCaseAcutel))
							tab[nbInter][2] += Value_ghost2;
						// tester si pacKnight
						if (PacKnight.personnagePresentCas(cordCaseAcutel))
							tab[nbInter][2] += Value_pacKnight2;
					}
					// faire avancer cordCaseAcutel
					for (Direction d2 : Direction.values()) {
						if (directionCord != d2.opposer()&& Personnage.getTerrain().caseAcessible(cordCaseAcutel.x, cordCaseAcutel.y, d2))
						{
							cordCaseAcutel.avancerDansDir(d2);
							directionCord = d2;
							break;
						}
					}
				cpt++;}
				int[][] tabaux = laFonctionQuiFaitPresqueTout(
						new CoordonneesFloat(cordCaseAcutel),
						directionCord.opposer(),mode);
				for (int i = 0; i < 4; i++)
					for (int j = 0; j < 3; j++)
						tab[nbInter][3 + j] += tabaux[i][j];
			}
		}
		return tab;
	}
	*/
}

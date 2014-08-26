package com.ricm3.packnight.model.personnages;


import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.ricm3.packnight.model.graph.Aetoile;
import com.ricm3.packnight.model.graph.Graph;
import com.ricm3.packnight.model.hitBoxManager.HitBoxManager;
import com.ricm3.packnight.model.structure_terrain.CoordCas;
import com.ricm3.packnight.model.structure_terrain.CoordPix;
import com.ricm3.packnight.model.structure_terrain.CoordPix.position;
import com.ricm3.packnight.model.structure_terrain.Direction;
import com.ricm3.packnight.model.structure_terrain.Terrain;

public class Ghost extends Personnage {
	
	/**
	 * liste des fantomes du jeux
	 * cette liste est utiliser pour gerer les collisions, et pour
	 * que les fantomes puissent communiquer entre eux
	 * author : alex
	 */
	public static List<Ghost> liste = new LinkedList<Ghost>();

	/**
	 * Le central repertorie l'ensemble des information des PM en fuite
	 */
	public static Map<Pacman, AvisDeRecherche> central=new HashMap<Pacman, AvisDeRecherche>();
	
	private static int rangeMinRespawnMultiMode = 20;
	public static int powerRange = 5;
	public static int timerSepareSortiePrison = 55;
	
		//info divers
	public static int vision = 5;
	private CoordPix pointDeRespawn;
	
		//info pour le pouvoir de controle
	static private int nbInterChercher = 4; //nombre d'inter calculer par fantome lord pour les ganks
	static private int fantomeUp=0; //fantome up pour le pouvoir
	//attribut pour les fantomes qui recoivent des ordres
	private CoordCas caseDOrdre = null; //case en cours
	private List<CoordCas> ordre = null; //liste des case de l'ordre en cours
	
		//boolean des animations
	private boolean prisonner = false; //le fantome est dans la prison
	private boolean stun = false;
	private boolean entendEtObei = false;
	private boolean sortiePrison = false;
	
		//timeur des animation
	static public int tempsPasserEnPrison = 230; //pair
	final static private int tempsStun = 15;
	
	static public boolean modeMulti;

	
	/**
	 * @param position ou on veut savoir si un personnage si trouve
	 * @return renvoie vrai si un objet Personnage se trouve sur la position indiquer
	 */
	static public boolean hittingPerso(CoordPix position) {
		Iterator<Ghost> i = Ghost.liste.iterator();
		while (i.hasNext()) {
			if (HitBoxManager.personnageHittingPersonnage(i.next().coordPix,position))
				return true;
		}
		return false;
	}
	
	static public boolean personnagePresent(CoordCas position)
	{
		Iterator<Ghost> i= Ghost.liste.iterator();
		while(i.hasNext())
		{
			if(position.equals(i.next().coordPix.CasCentre()))
				return true;
		}
		return false;
	}
	
	static public List<CoordCas> listCoord()
	{
		List<CoordCas> listRes = new LinkedList<CoordCas>();
		Iterator<Ghost> i= Ghost.liste.iterator();
		while (i.hasNext()) {
				listRes.add(i.next().coordPix.CasCentre());
		}
		return listRes;
	}

	/**
	 * renvoie la distance du fantome le plus proche de la case
	 * @param c
	 * @return
	 */
	static public int distance(CoordCas c)
	{
		int min = Integer.MAX_VALUE;
		Iterator<Ghost> i = Ghost.liste.iterator();
		while(i.hasNext())
		{
			int aux = i.next().coordPix.CasCentre().distance(c);
			if( aux < min)
				min = aux;
		}
		return min;
	}
	
	static public boolean powerUp()
	{
		return fantomeUp>=nbInterChercher;
	}
	
	static public void init()
	{
		if(modeMulti)
			Ghost.initAlea();
		else
			Ghost.initMap0();
	}
	
	static public void initAlea()
	{
		Iterator<Ghost> i= Ghost.liste.iterator();
		while (i.hasNext()) {
			i.next().respawnWOA();
		}
		Ghost.tempsPasserEnPrison=0;
	}
	
	static public void initMap0()
	{
		Ghost.tempsPasserEnPrison = 230;
		Iterator<Ghost> i= Ghost.liste.iterator();
		int timer = 0;
		int cpt=1;
		int x = 12*32;
		int y = 14*32;
		while (i.hasNext()) {
			Ghost g = i.next();
			g.prisonner=true;
			g.timerAnimation -= timer*timerSepareSortiePrison;
			g.pointDeRespawn = new CoordPix(12*32,14*32,position.hg);
			g.coordPix.x = x;
			g.coordPix.y = y;
			g.direction=Direction.droite;
			timer++;
			if(cpt==4)
				x=12*32;
			else
				x+= 32;
			cpt++;
			
		}
	}
	
	public Ghost(String nom) {
		super(nom,1,1, Direction.droite);
		this.agonise=false;
		Ghost.liste.add(this);
		Ghost.fantomeUp++;
	}
	
	//getter de base
	public boolean getisAlive(){
		return !(agonise);
	}

	/**
	 * Met à jour l'état vivant ou mort du fantome*/
	public void setIsAlive(boolean a){
		agonise=a;
		
	}
	
	public boolean getEntendEtObei(){
		
		return entendEtObei;
	}
	
	/**
	 * Supprime le Pacman de la centrale si le timer est à 0*/
	public static void disparitionPacman(){
		boolean etaitvide = central.isEmpty();
		for(Iterator<Pacman> i = Pacman.liste.iterator();i.hasNext();){
			Pacman pac = i.next();
			if(central.containsKey(pac)){
				if(central.get(pac).timer==0)
					central.remove(pac);
				else{ 
					central.get(pac).timer--;
				}
			
			}
		
		}
//		if(!etaitvide && central.isEmpty())
//			MusicManager.play_PerduDeVue();
	}
	/**
	 * Gère la collision avec les pacmans*/
	public void gererCollision() {
		Iterator<PacKnight> i = PacKnight.liste.iterator();
		while(this.hitting() && i.hasNext() )
		{
			PacKnight g = i.next();
			if(g.hitting() && HitBoxManager.personnageHittingPersonnage(this.coordPix, g.coordPix))
			{
				this.meurtDansDatroceSouffrance();
				g.meurtDansDatroceSouffrance();
				break;
			}
		}		
		Iterator<PacPrincess> j = PacPrincess.liste.iterator();
		while(this.hitting() && j.hasNext())
		{
			PacPrincess p = j.next();
			if(HitBoxManager.personnageHittingPersonnage(this.coordPix, p.coordPix) && p.hitting())
			{
				p.meurtDansDatroceSouffrance();
				break;
			}
		}
	}
	
	/**
	 * etourdi le fantome
	 */
	public void stun()
	{
		this.stun = true;
	}

	public int getVision() {
		return vision;
	}

	/**
	 * Pourquoi ne pas la mettre dans personnage car elle est commune aux pacman et aux Ghost
	 * */
	public void respawn()
	{
		this.agonise = true;
	}
	
	/**
	 * parametre le pac-man pour qu'il fasse un respawn sans animation
	 * effectuer une fois que l'animation est fini
	 */
	protected void respawnWOA() {
		if(!Ghost.modeMulti)
		{
			this.coordPix.setCoord(this.pointDeRespawn);
			this.direction = Direction.droite;
			this.prisonner = true;
		}
		else
		{
			Random r = new Random();
			Terrain t = Personnage.terrain;
			CoordCas c = new CoordCas(0, 0);
			do
			{
			c.x = r.nextInt(t.largeur);
			c.y = r.nextInt(t.hauteur);
			}while(!(t.caseAcessible(c) && (Pacman.distance(c))>(vision+rangeMinRespawnMultiMode)));
			this.coordPix = new CoordPix(c.x*32, c.y*32,position.hg);
		}
	}
	
	/**
	 * fait mourrir le ghost pour un moment restreint :D
	 */
	public void meurtDansDatroceSouffrance() {
		this.agonise = true;
	}

	/**
	 * renvoie vraie si le ghost est parametrable par un automate a un l'instant courant
	 */
	public boolean parametrable() {
		return !(agonise || prisonner || stun || entendEtObei || sortiePrison);
	}

	/**
	 * fait avancer d'un cran les animations en cours
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
		else if(prisonner)
		{
			if(this.timerAnimation < Ghost.tempsPasserEnPrison)
			{
				this.timerAnimation++;
				if(!this.caseDevantDisponible())
					this.direction = this.direction.opposer();
				this.avancer();
			}
			else
			{
				this.timerAnimation = 0;
				this.prisonner = false;
				this.sortiePrison = true;
				this.direction = Direction.haut;
			}
		}
		else if(sortiePrison)
		{
			if(this.timerAnimation < 24)
			{
				this.timerAnimation++;
				this.avancerAux();
			}
			else
			{
				this.timerAnimation = 0;
				this.sortiePrison = false;
				this.direction = Direction.droite;
			}
		}
		else if(stun)
		{
			if(this.timerAnimation < Ghost.tempsStun)
			{
				this.timerAnimation++;
			}
			else
			{
				this.timerAnimation = 0;
				this.stun = false;
			}
		}
		else if(entendEtObei && !prisonner && !stun && !agonise && !sortiePrison  )
		{
				this.executerOrdre();
		}
	}
	
	/**
	 * fait recevoir au fantome un ordre
	 * @param l : la liste des case a parcourir
	 */
	public void recoitOrdre(List<CoordCas> l)
	{
		if(!l.isEmpty())
		{
			this.ordre = l;
			this.entendEtObei = true;
			fantomeUp--;
			this.caseDOrdre = l.get(0);
			this.coordPix = new CoordPix(caseDOrdre,position.hg);
			ordre.remove(0);
			if(!ordre.isEmpty())
			{
				this.caseDOrdre = l.get(0);
				this.direction = coordPix.CasCentre().directionPourAllerVers(caseDOrdre);
				this.avancer();
			}
		}
	}
	
	/**
	 * avance a la prochaine case de l'ordre
	 * cette case doit se situé dans l'index 1 de la liste
	 * si il n'y a pas cette case, on arrete d'executer des ordres
	 */
	private void executerOrdre()
	{
		if(!ordre.isEmpty())
		{
			if(coordPix.estSurUneCase())
			{
				caseDOrdre = ordre.get(0);
				ordre.remove(0);
				direction = coordPix.CasCentre().directionPourAllerVers(caseDOrdre);
				avancer();
			}
			else
			{
				this.avancer();
			}
		}
		else
		{
			this.avancer();
			if(coordPix.estSurUneCase())
			{
				entendEtObei = false;
				fantomeUp++;
			}
		}
	}

	public boolean hitting() {
		return !(agonise) && !(prisonner) && !(sortiePrison);
	}

	/**
	 * donne des ordre au fantomes pour coincé un pacman donné
	 */
	public void donnerDesOrdres(Pacman ref)
	{
		if(Ghost.powerUp())
		{
			CoordCas caseDeLaCible = ref.coordPix.CasCentre();
			
			//reboot du graph
			Graph g = new Graph(Personnage.terrain);
			
			// calcule des intersection a occuper
			List<CoordCas> listeDesInter = g.visiterLargeur(caseDeLaCible,nbInterChercher);
			
			//copie de la liste des fantomes
			List<Ghost> listeDesGhost = new LinkedList<Ghost>(Ghost.liste);
	
			// pour chaque inter
			Iterator<CoordCas> i = listeDesInter.iterator();
			while(i.hasNext())
			{
				CoordCas interEnTraitement = i.next();
				// variable temporaire
				Ghost meilleurCandidat = null;
				int distanceMeilleurCandidat = Integer.MAX_VALUE;
				
				// calcul de la distance max entre le fantome et l'inter
				int dmax = interEnTraitement.distance(caseDeLaCible);
				dmax += Ghost.powerRange; //parceque je suis sadic :3
				
				// calcul du fantome qui doit y aller
				Iterator<Ghost> ig = listeDesGhost.iterator();
				
				int indice = 0; //index de la liste fantome
				int cpt = 0; //cpt pour savoir l'index en cours de test
				while(ig.hasNext())
				{
					//creation du candidat
					Ghost actuelCandidat = ig.next();
					//test si le candidat peut obtenir des ordres
					if(actuelCandidat.parametrable())
					{
						int dactuelCandidat = interEnTraitement.distance(actuelCandidat.coordPix.CasCentre());
						
						if(dactuelCandidat < dmax && dactuelCandidat < distanceMeilleurCandidat)
						{
							//maj du candidat
							meilleurCandidat = actuelCandidat;
							distanceMeilleurCandidat = dactuelCandidat;
							indice = cpt;
						}
					}
					//on incremente notre compteur dans tout les cas !
					cpt++;
				}
				if(meilleurCandidat != null)
				{
					//supprime le fantome de la liste
					listeDesGhost.remove(indice);
					//calcul de l'itinéraire
					Aetoile ga = new Aetoile(meilleurCandidat.coordPix.CasCentre());
					List<CoordCas> ordre = ga.algo(interEnTraitement);
					meilleurCandidat.recoitOrdre(ordre);
				}
			}
//			MusicManager.play_GhostPower_Obey();
		}
	}
	
	static public void donnerDesOrdresGodMod()
	{
		if(Ghost.powerUp())
		{
			PacKnight ref = PacKnight.liste.get(0);
			CoordCas refCasCentre = ref.coordPix.CasCentre();
			
			//reboot du graph
			Graph g = new Graph(Personnage.terrain);
			
			// calcule des intersection a occuper
			List<CoordCas> listeDesInter = g.visiterLargeur(ref.coordPix.CasCentre(),nbInterChercher);
			
			//copie de la liste des fantomes
			List<Ghost> listeDesGhost = new LinkedList<Ghost>(Ghost.liste);
	
			// pour chaque inter
			Iterator<CoordCas> i = listeDesInter.iterator();
			while(i.hasNext())
			{
				CoordCas interEnTraitement = i.next();
				// variable temporaire
				Ghost meilleurCandidat = null;
				int distanceMeilleurCandidat = Integer.MAX_VALUE;
				
				// calcul de la distance max entre le fantome et l'inter
				int dmax = interEnTraitement.distance(refCasCentre);
				
				dmax += Ghost.powerRange; //parceque je suis sadic :3
				//des fantomes se deplaceront meme si ils ne sont pas sur de le coincé, ca fiche le stress
				
				// calcul du fantome qui doit y aller
				Iterator<Ghost> ig = listeDesGhost.iterator();
				
				int indice = 0; //index de la liste fantome
				int cpt = 0; //cpt pour savoir l'index en cours de test
				while(ig.hasNext())
				{
					//creation du candidat
					Ghost actuelCandidat = ig.next();
					//test si le candidat peut obtenir des ordres
					if(actuelCandidat.parametrable())
					{
						int dactuelCandidat = interEnTraitement.distance(actuelCandidat.coordPix.CasCentre());
						
						if(dactuelCandidat < dmax && dactuelCandidat < distanceMeilleurCandidat)
						{
							//maj du candidat
							meilleurCandidat = actuelCandidat;
							distanceMeilleurCandidat = dactuelCandidat;
							indice = cpt;
						}
					}
					//on incremente notre compteur dans tout les cas !
					cpt++;
				}
				if(meilleurCandidat != null)
				{
					//supprime le fantome de la liste
					listeDesGhost.remove(indice);
					//calcul de l'itinéraire
					Aetoile ga = new Aetoile(meilleurCandidat.coordPix.CasCentre());
					List<CoordCas> ordre = ga.algo(interEnTraitement);
					meilleurCandidat.recoitOrdre(ordre);
				}
			}
//			MusicManager.play_GhostPower_Obey();
		}
	}
}
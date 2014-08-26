package com.ricm3.packnight.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ricm3.packnight.controller.TableTransitionSortie.Triplet;
import com.ricm3.packnight.model.parser.Parser;
import com.ricm3.packnight.model.parser.Quad;
import com.ricm3.packnight.model.personnages.Ghost;
import com.ricm3.packnight.model.personnages.Personnage;
import com.ricm3.packnight.model.structure_terrain.Direction;


/**
 * Toute primitive de test doit etre ajoutée dans PrimitivesTest, et se voit attribuée, type enuméré, ci dessous.
	Une fonction à modifier selon si c'est une primitive de test ou d'action :
	Primitive de test, dans la fonction getEntree() : 
		Ajouter : case <le_nom_primitive_Test> : appel de la primitiveTest
	Primitive d'action, dans la fonction suivant() : 
	Ajouter : case <le_nom_primitive_Action> : appel de la primitiveAction
 * @author malek
 */
public class Automate {
	
	PrimitivesTest primitivesTest = new PrimitivesTest(this);
	PrimitivesAction primitivesAction = new PrimitivesAction(this);
	protected Personnage personnage;

	//ENTREES : CASE_OCCUPE, CASE_LIBRE, SORTIE_TERRAIN...
	public static enum Entree{
		CASE_LIBRE, CASE_OCCUPEE, SORTIE_TERRAIN, PM_DANS_RAYON_X, NON_PM_DANS_RAYON_X, PM_DANS_CROIX, 
		NON_PM_DANS_CROIX, INTERSECTION, NON_INTERSECTION, CASE_ATTEINTE, CASE_NON_ATTEINTE, FREE, NON_FREE, ETOILE,
		EN_DETRESSE, NON_EN_DETRESSE, FM_DANS_RAYON, NON_FM_DANS_RAYON, VU, PAS_VU, POSSEDE_UN_GARDE, NE_POSSEDE_PAS_UN_GARDE, EST_APPELER, NON_APPELER, EN_SECURITE, EN_DANGER;
		
		public static boolean contains (String s){
			try{
				Entree.valueOf(s);
			} catch (Exception e){
				return false;
			}
			return true;
		}
	}	

	public static enum Sortie{
		AVANCER, GAUCHE, DROIT, HAUT, BAS, RIEN, DIRECTION_ALEATOIRE, CHEMIN_PLUS_COURT, OBEIR, SPAWN, STUN, PROTEGER_PRINCESSE, AU_SECOURS, SUIVRE, RAMASSER_PACGUM
		,FUIR, APPEL_PATROUILLEUR,PATROUILLER, INTERCEPTER, AVANCER_VERS_PACGUM;
		public static boolean contains (String s){
			try{
				Sortie.valueOf(s);
			} catch (Exception e){
				return false;
			}
			return true;
		}
	}	
	
	TableTransitionSortie tableTransitionSortie;
	
	private String etatCourant;
	private int nbEtat;
//	private int nbEntree;
//	private int nbSortie;
	private int nbTransition;
//	private int nbTransitionMax;
	private String etatInitial;
	private List<String>etatsFinals;
	private List<String>etatsBloquants;
	public Direction sneaky = null;
	
	/*
	 * Prend un fichier XML et remplie les attributs de l'automate
	 */
	public Automate(String fichierXML, Personnage p) throws Exception
	{
		
		Parser parser = new Parser("assets/Automate/"+fichierXML);
		
		Map<String,List<Quad>> liste = parser.parseTableau();
		etatInitial = parser.parseEtatInitiale();
		etatsFinals = parser.parseEtatFinal();
//		System.out.println(etatsFinals);
		etatsBloquants = parser.parseEtatBloquant();
		//Initialisations des attributs
		this.nbEtat  = liste.size();
//		this.nbTransition = 0;
//		this.nbTransitionMax = Integer.MAX_VALUE; //A Virer
		this.personnage = p;
		this.etatCourant = etatInitial;
		
		
		//Initialisation des la table d'entree sortie
		tableTransitionSortie = new TableTransitionSortie();
		tableTransitionSortie.initTransitionSortie(liste);
		System.out.println(tableTransitionSortie.toString());
	}
	
	
	/**
	 * 
	 * @return la Sortie, correspondant a l'etatCourant et l'entree passé en parametre
	 * @param Entree
	 * @author malek
	 * @throws Exception 
	 */
	protected Automate.Sortie effectuerTransition(Automate.Entree entree) throws Exception {
//		if (nbTransition < nbTransitionMax){
//			if (tableTransitionSortie.getValide(this.etatCourant, Entree)){
//		System.out.println("Entreeee " + Entree);
				String RAMA = this.etatCourant;
				this.etatCourant = tableTransitionSortie.getEtatSuiv(this.etatCourant, entree);
				return tableTransitionSortie.getSortie(RAMA, entree);
//			} else
//				throw new Exception("Erreur l'etatCourant n'a pas le droit d'effectuer ce test");
//		}
//		throw new Exception("Erreur le nombre de transition MAX atteint");
	}

	/**
	 * Fonction effectuant l'action suivante, selon l'entree et la sortie de l'automate.
	 * A modifier au fur et a mesure des ajout de fonction d'actions des personnages.
	 * Ne pas oublier de CASTER selon le type de votre personnage, afin d'avoir accès aux actions qui y sont décrites 
	 * @throws Exception
	 * @author malek
	 */
	public void suivant() throws Exception {
		if(this.personnage.parametrable())
		{
			
			do
			{
				Automate.Entree entreeAutomate = getEntree();
				Automate.Sortie sortieAutomate = effectuerTransition(entreeAutomate);
				switch (sortieAutomate) {
				//TODO Ajouter chaque fonction d'action
				case AVANCER: personnage.avancer(); break;
				case DROIT: personnage.setDirection(Direction.droite); break;
				case GAUCHE: personnage.setDirection(Direction.gauche); break;
				case HAUT: personnage.setDirection(Direction.haut); break;
				case BAS: personnage.setDirection(Direction.bas); break;
				case DIRECTION_ALEATOIRE: primitivesAction.setDirectionAleatoire(getPersonnage()); break;
				case SUIVRE: primitivesAction.suivre(); break;
				case RIEN:primitivesAction.pass(); break;
				case STUN:primitivesAction.stun(); break;
				case PROTEGER_PRINCESSE:primitivesAction.protegerPrincesse(); break;
				case AU_SECOURS:primitivesAction.auSecours(); break;
				//case RAMASSER_PACGUM:primitivesAction.fetch();break;
				case OBEIR:primitivesAction.obeir(); break;
				//case FUIR:primitivesAction.fuir();break;
				case PATROUILLER:primitivesAction.patrouiller();break;
				case APPEL_PATROUILLEUR:primitivesAction.appelPatrouilleur();
				case INTERCEPTER:primitivesAction.intercepter(); break;
				//case AVANCER_VERS_PACGUM: primitivesAction.fetch(); break;
				}
			}
			while(this.personnage.parametrable() && !(isEtatBloquant()));
			incrementerTransition();
		}
		else
		{
			if(this.personnage.agonise)
				this.etatCourant = etatInitial;
			this.personnage.avancerAnimation();
		}
	}

	/**
	 * A l'etatCourant X,
	 * Pour chaque entree de l'automate,
	 * 		On evalue appelle la fonction de test
	 * 		  
	 * Rmq : Dans le cas où l'etatCourantX ne possède aucune entree, On leve exception. (Il s'agirait d'un etat Puit)
	 * Pour chaque Entree de l'etatCourantX, nous appelons, dans l'ordre du parcours de la table de 
	 * Hashage (l'ordre des entier "Constante"), chacune des fonctions de test
	 * 
	 * Theoriquement, parmis toute les entrees a prtir de l'etatCourantX, une et seulement une fonction 
	 * est verifiée. La fonction getEntree() ne le verifie pas ! 
	 * 
	 * Elle renvoie l'Entree de l'automate, leve exception si aucune Entree n'est verifiée. 
	 * @return Entree de type automate, fonction a modifier, au fure et a mesure des ajouts de fonction de test
	 * @author malek
	 * @throws Exception 
	 */ 
	public Automate.Entree getEntree() throws Exception{
		Map<Automate.Entree, Triplet> entries = tableTransitionSortie.getEtatAll(this.etatCourant);
		//On parcours l'ensemble des Entree de l'automate, de l'etatCourant
		for (Iterator<Automate.Entree> key = entries.keySet().iterator(); key.hasNext(); ){
			Automate.Entree Entree = key.next();
			//if ( entries.get(Entree).ok ){
				switch ( Entree ){
				//TODO Ajouter chaque fonction de test
				case CASE_LIBRE: if (primitivesTest.configCaseDevant()==Entree.CASE_LIBRE) return Automate.Entree.CASE_LIBRE; break;
				case CASE_OCCUPEE: if (primitivesTest.configCaseDevant()==Entree.CASE_OCCUPEE) return Entree.CASE_OCCUPEE; break;
				case CASE_NON_ATTEINTE:if(!primitivesTest.caseAtteinte()) return Entree.CASE_NON_ATTEINTE; break;
				case CASE_ATTEINTE:if(primitivesTest.caseAtteinte()) return Entree.CASE_ATTEINTE; break;
				case SORTIE_TERRAIN: if (primitivesTest.configCaseDevant()==Automate.Entree.SORTIE_TERRAIN) return Entree.SORTIE_TERRAIN;break;
				case PM_DANS_RAYON_X : if(primitivesTest.dansRayon(((Ghost) getPersonnage()).getVision())) return Entree.PM_DANS_RAYON_X; break;
				case NON_PM_DANS_RAYON_X : if(!primitivesTest.dansRayon(((Ghost) getPersonnage()).getVision())) return Automate.Entree.NON_PM_DANS_RAYON_X; break;
				case INTERSECTION: if(primitivesTest.estIntersection()) return Entree.INTERSECTION; break;
				case NON_INTERSECTION: if(!primitivesTest.estIntersection()) return Entree.NON_INTERSECTION; break;
				case PM_DANS_CROIX: if(primitivesTest.dansCroix()) return Entree.PM_DANS_CROIX; break;
				case NON_PM_DANS_CROIX: if(!primitivesTest.dansCroix()) return Entree.NON_PM_DANS_CROIX; break;
				case ETOILE: return Entree.ETOILE;
				case EN_DETRESSE: if(primitivesTest.Chasse()) return Entree.EN_DETRESSE; break;
				case NON_EN_DETRESSE: if(!primitivesTest.Chasse()) return Entree.NON_EN_DETRESSE; break;
				case FM_DANS_RAYON: if(primitivesTest.fmDansRayon()) return Entree.FM_DANS_RAYON; break;
				case NON_FM_DANS_RAYON: if(!primitivesTest.fmDansRayon()) return Entree.NON_FM_DANS_RAYON; break;
				case VU:if(primitivesTest.vu())return Entree.VU; break;
				case PAS_VU:if(!primitivesTest.vu())return Entree.PAS_VU; break;
				case POSSEDE_UN_GARDE:if(primitivesTest.nombreGardeSuffisant()) return Entree.POSSEDE_UN_GARDE; break;
				case NE_POSSEDE_PAS_UN_GARDE:if(!primitivesTest.nombreGardeSuffisant()) return Entree.NE_POSSEDE_PAS_UN_GARDE; break;
				case EST_APPELER:if(primitivesTest.appelAuDevoir())return Entree.EST_APPELER; break;
				case NON_APPELER:if(!primitivesTest.appelAuDevoir())return Entree.NON_APPELER; break;
				case EN_SECURITE:if(primitivesTest.safe())return Entree.EN_SECURITE; break;
				case EN_DANGER:if(!primitivesTest.safe()) return Entree.EN_DANGER; break;
				//	}
			}
		}
		
		//Affichage des ENTREES dans le cas où : Aucune entree n'est valide  
		String Erreur = "C'est l'ETAT " + this.etatCourant + ", voici toutes mes ENTREES : ";
		for (Iterator<Automate.Entree> key = entries.keySet().iterator(); key.hasNext(); ){
			Automate.Entree Entree = key.next(); Erreur += Entree + ", ";
		}
		System.out.println(Erreur);
		throw new Exception("Erreur dans l'automate. L'etat courant ne possède aucune entree valide. Manque t-il une transition ?");
	}

	/**
	 * Fonction qui test le nombre d'entree valide de l'etatCourantX, a complementer au fure est a mesure 
	 * des ajout de primitive de test 
	 * En theorie return == 1
	 * @return le nombre d'entree valide de l'etatCourantX
	 * @author malek
	 * @throws Exception 
	 */
	public int nbEntreeValide() throws Exception {
		int nb = 0;
		Map<Automate.Entree, Triplet> entries = tableTransitionSortie.getEtatAll(this.etatCourant);
		//On parcout l'ensemble des Entree de l'automate, de l'etatCourant
		for (Iterator<Automate.Entree> key = entries.keySet().iterator(); key.hasNext(); ){
			Automate.Entree Entree = key.next();
			//if ( entries.get(Entree).ok ){ 
				switch ( Entree ){ 
//				case CASE_LIBRE: if (primitivesTest.configCaseDevant()==Entree.CASE_LIBRE) nb++; break;
//				case CASE_OCCUPEE: if (primitivesTest.configCaseDevant()==Entree.CASE_OCCUPEE) nb++; break;
//				case SORTIE_TERRAIN: if (primitivesTest.configCaseDevant()==Entree.SORTIE_TERRAIN) nb++; break;
				case PM_DANS_RAYON_X : if (primitivesTest.dansRayon(3)) nb++; break;
				}
			//}
		}
		return nb;
	}

	/**
	 * Reinitialise l'etat courant de l'automate a l'etat initial 0
	 * @author malek
	 */
	public void reinitialiserAutomate(){
		etatCourant = etatInitial;
	}
	
	/**
	 * @return Etat courant
	 * @author malek
	 */
	public String getEtatCourant() {
		return etatCourant;
	}	

	/**
	 * @return True si l'automate est dans un etat final, False sinon
	 * @author malek
	 */
	public boolean isEtatFinal() {
		return this.etatsFinals.contains(this.etatCourant);
	}

	public boolean isEtatBloquant()
	{
		return this.etatsBloquants.contains(this.etatCourant);
	}
	protected void incrementerTransition(){
		nbTransition++;
	}
	
	public String tableTransSortie(){
		return tableTransitionSortie.toString();
	}
	public String infoAutomate(){
		String res="";
		res += " Etat Courant : " + etatCourant + "\n Nb Transition : " + nbTransition + "\n Etats Finals : " + etatsFinals.toString() + "\n";
		return res;
	}
	
	public Personnage getPersonnage() {
		return this.personnage;
	}

}
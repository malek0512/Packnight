/**
 * Pas de constructeur
 * Si choix de non static : au vue du nombre de fonction, et d'attribut, je prefere
 * le faire par instance, ca permet de pouvoir comparer plusiseurs fichier
 * allez savoir pourquoi, mais ferme pas des possibiliter
 * En plus le gabarging supprimera les donnée une fois le parse fini
 * Si on met en static je crains que la memoire soit conserver :/
 * 
 * author : Alex
 * 
 */
package com.ricm3.packnight.model.parser;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jdom2.*;
import org.jdom2.input.*;


public class Parser {
   

	private final String balise_Etat_Initial = "Etat_Initial"; //balise de l'état initiale
	private final String balise_Etat = "Etat"; //balise de l'état
	private final String balise_Transition = "Transition"; //balise des transitions
	private final String balise_Etat_Finaux = "Etats_Finals"; //balise des état final
	private final String balise_Etat_Bloquant = "Etats_Bloquants";

	private final String attribue_Etat_Numero = "Nom"; //nom de l'attribut de balise_etat
	private final String attribue_Transition_Entree = "Entree"; //nom de l'attribut entree de balise_transition
	private final String attribue_Transition_EtatArriver= "Etat"; //nom de l'attribut etat d'arriver de balise_transition
	private final String attribue_Transition_Sortie = "Action"; //nom de l'attribut sortie de balise_transition

	private Element racine;
   
   /**
    * Fonction qui ouvre un fichier, valeur de retour si erreur
    * 
    * @param file, nom du fichier a ouvrire avec pour chemin par default : /home/../PACKNIGHT/
    * donc pour ouvrire un fichier dans /home/../PLA/fichier.txt faut donnée : "../fichier.txt"
    * vue que ../PACKNIGHT == PLA/
    * @return 0 si l'ouverture du fichier a reussi // 1 sinon
    * 
    * author : Alex
 * @throws Exception 
    */
   public Parser(String file) throws Exception
   {
	   	org.jdom2.Document document = null;
	    //On crée une instance de SAXBuilder
	    SAXBuilder sxb = new SAXBuilder();
	   // try
	   // {
	    	System.out.println("Ouverture du fichier");
	       //On crée un nouveau document JDOM avec en argument le fichier XML
	    	document = sxb.build(new File(file));
	   // }
	   // catch(Exception e) {throw new Exception("Erreur dans l'ouverture du fichier de l'automate");}// return 1;}
	    //On initialise un nouvel élément racine avec l'élément racine du document.
	    racine = document.getRootElement();
	    //return 0;
   }
   
   
   /**
    * Fonction qui parse les tableaux de Entree( ou transition au choix)
    * et de Sortie(valeur de sortie de chaque transition)
    * @param tabTrans : Tableau contenant les Entrees des transition  
    * @param tabSort : Tableau contenant les Sorties des transition
    * 
    * @require La taille des tableaux (les 2) DOIVENT etre initilasier
    * la fonction "int parseNombreEtat()"  et "int parseTransitionMax()"
    * 
    * author : Alex
    */
   public Map<String,List<Quad>> parseTableau() //int tabTrans[][],int tabSort[][])
	{

	   Map<String,List<Quad>>transitionSortie = new HashMap<String,List<Quad>> ();

	   //On crée la liste d'etat
	   List<Element> listEtat = racine.getChildren(balise_Etat);
	   //On crée un Iterator sur la liste d'etat
	   Iterator<Element> i = listEtat.iterator();

	   while(i.hasNext())
	   {
		   //Recup le prochain etat a traiter
		   Element etat = (Element)i.next();
		   //Recup l'indice de l'état pour le tableau Transition
		   String Ietat = (etat.getAttributeValue(attribue_Etat_Numero));

//		   System.out.println(Ietat.intValue()); //Ici erreur car pointeur null
		   //Initialisation d'une liste d'entree de l'Ietat
		   transitionSortie.put(Ietat, new LinkedList<Quad>());

		   //Meme action mais pour transition
		   List<Element> listTransition = etat.getChildren(balise_Transition);
		   Iterator<Element> j = listTransition.iterator(); 
		   while(j.hasNext())
		   {
			   Element transition = (Element)j.next(); 
			   String Ientree = (transition.getAttributeValue(attribue_Transition_Entree));
			   String Iarriver = (transition.getAttributeValue(attribue_Transition_EtatArriver));
			   String Isortie = (transition.getAttributeValue(attribue_Transition_Sortie));

			   transitionSortie.get(Ietat).add(new Quad(Ientree, Iarriver, Isortie));
			   //tabSort[Ietat][Ientree] = Isortie;
			   //tabTrans[Ietat][Ientree] = Iarriver;
		   }
	   }
	return transitionSortie;
	}
   
   /**
    * renvoie le nombre d'état renseigner par le fichier
    * @return : le nombre d'état du fichier, inscrit dans une balise specifique
    * author : Alex
    */
   public int parseNombreEtat()
   {
	   List<Element> tmp = racine.getChildren(balise_Etat);
	   return tmp.size();
   }
   
   /**
    * renvoie le nombre de transition maximum pour un etat
    * author : Alex
    */
   public int parseTransitionMax()
   {

	   int res=0;

	   List<Element> listEtat = racine.getChildren(balise_Etat);
	   //On crée un Iterator sur la liste d'etat
	   Iterator<Element> i = listEtat.iterator();


	   while(i.hasNext())
	   {
		   //Recup le prochain etat a traiter
		   Element etat = (Element)i.next();
		   List<Element> tmp = etat.getChildren(balise_Transition);
		   if (res<tmp.size())
		   {
			   res = tmp.size();
		   }
	   }
	   return res;
   }

   /**
    * renvoie le numero de l'état initiale
    * @return le numero de l'état initiale
    * si l'état initial n'a pas était trouvé renvoie -1
    * author : Alex
    */
   public String parseEtatInitiale()
   {
	   List<String> list = new LinkedList<String>();

	 //On recup la liste des etats finaux
	   List<Element> listEtatFinal = racine.getChildren(balise_Etat_Initial);

	   //On crée un Iterator sur cette liste
	   Iterator<Element> i = listEtatFinal.iterator();

	   while (i.hasNext())
	   {
		   Element etat_finals = (Element)i.next();

		   //on recupere la liste des etats final mis dans la balise "Etats_Finals"
		   List<Element> listeEtat = etat_finals.getChildren(balise_Etat);

		   Iterator<Element> j = listeEtat.iterator();

		   while(j.hasNext())
		   {
			   //Recup le prochain etat a traiter
			   Element etat = (Element)j.next();

			   //Recup l'indice de l'état pour le tableau Transition
			   String Ietat = (etat.getAttributeValue(attribue_Etat_Numero));

			   //on ajoute a la liste d'état final létat trouver
			   list.add(Ietat);
		   }
	   	}
	return list.get(0);
   }
   
 /**
  * ajoute avec la fonction "List.add()" la liste avec le numero des etats finaux
  * @return liste of Integer : la liste des etats finals
  * utiliser la fonction parseNombreEtat() de preference
  */
   public List<String> parseEtatFinal()
   {
	   List<String> liste = new LinkedList<String>();

	 //On recup la liste des etats finaux
	   List<Element> listEtatFinal = racine.getChildren(balise_Etat_Finaux);

	   //On crée un Iterator sur cette liste
	   Iterator<Element> i = listEtatFinal.iterator();

	   while (i.hasNext())
	   {
		   Element etat_finals = (Element)i.next();

		   //on recupere la liste des etats final mis dans la balise "Etats_Finals"
		   List<Element> listeEtat = etat_finals.getChildren(balise_Etat);

		   Iterator<Element> j = listeEtat.iterator();

		   while(j.hasNext())
		   {
			   //Recup le prochain etat a traiter
			   Element etat = (Element)j.next();

			   //Recup l'indice de l'état pour le tableau Transition
			   String Ietat = (etat.getAttributeValue(attribue_Etat_Numero));

			   //on ajoute a la liste d'état final létat trouver
			   liste.add(Ietat);
		   }
	   	}
	return liste;
   }
   
   /**
    * ajoute avec la fonction "List.add()" la liste avec le numero des etats finaux
    * @return liste of Integer : la liste des etats finals
    * utiliser la fonction parseNombreEtat() de preference
    */
     public List<String> parseEtatBloquant()
     {
  	   List<String> liste = new LinkedList<String>();

  	 //On recup la liste des etats finaux
  	   List<Element> listEtatFinal = racine.getChildren(balise_Etat_Bloquant);

  	   //On crée un Iterator sur cette liste
  	   Iterator<Element> i = listEtatFinal.iterator();

  	   while (i.hasNext())
  	   {
  		   Element etat_finals = (Element)i.next();

  		   //on recupere la liste des etats final mis dans la balise "Etats_Finals"
  		   List<Element> listeEtat = etat_finals.getChildren(balise_Etat);

  		   Iterator<Element> j = listeEtat.iterator();

  		   while(j.hasNext())
  		   {
  			   //Recup le prochain etat a traiter
  			   Element etat = (Element)j.next();

  			   //Recup l'indice de l'état pour le tableau Transition
  			 String Ietat = (etat.getAttributeValue(attribue_Etat_Numero));

  			   //on ajoute a la liste d'état final létat trouver
  			   liste.add(Ietat);
  		   }
  	   	}
  	return liste;
     }
}
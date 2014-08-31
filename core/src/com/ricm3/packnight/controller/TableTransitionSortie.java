package com.ricm3.packnight.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TableTransitionSortie {
	// Classe intermediaire de triplet
	static public class Triplet {
		public String EtatSuiv;
		public Automate.Sortie Sortie;

		public Triplet(String etatSuiv, Automate.Sortie sortie) {
			EtatSuiv = etatSuiv;
			Sortie = sortie;
		}
	}

	Map<String, Map<Automate.Entree, Triplet>> table;

	public TableTransitionSortie() {
		table = new HashMap<String, Map<Automate.Entree, Triplet>>();
	}


	/**
	 * Renvoie True, si l'Entree Existe Dans l'Automate donc si l'une des
	 * Hashmap contient l'entree.
	 * 
	 * @param Entree
	 * @return
	 */
	public boolean isEntreeExisteDansAutomate(int Entree) {
		boolean OK = false;
		for (Entry<String, Map<Automate.Entree, Triplet>> s : table.entrySet()) {
			// for(Map<Integer, Triplet> i : s.getValue())
			OK = OK || s.getValue().containsKey(Entree);
		}
		return OK;
	}

	/**
	 * Renvoie True, si l'automate possède le numero de l'état passé en
	 * parametre
	 * 
	 * @param Etat
	 * @return
	 */
	public boolean isEtatExisteDansAutomate(int Etat) {
		return table.containsKey(Etat);
	}

	/**
	 * Renvoie la table de transition associé a l'etat donné en paramtre
	 * 
	 * @param Etat
	 * @throws Exception
	 *             Si l'etat n'existe pas dans l'automate
	 */
	public Map<Automate.Entree, Triplet> getEtatAll(String Etat) throws Exception {
		if (!table.containsKey(Etat))
			throw new Exception("L'etat renseigner n'existe pas dans la table");
		return (Map<Automate.Entree, Triplet>) table.get(Etat);
	}

	public String getEtatSuiv(String Etat, Automate.Entree Entree) throws Exception {
		if (!table.containsKey(Etat))
			throw new Exception("L'etat renseigné n'existe pas dans la table");
		if (!table.get(Etat).containsKey(Entree))
			throw new Exception(
					"L'entree renseignée n'existe pas dans la table");
		return table.get(Etat).get(Entree).EtatSuiv;
	}

	public Automate.Sortie getSortie(String Etat, Automate.Entree Entree) throws Exception {
		if (!table.containsKey(Etat))
			throw new Exception("L'etat renseigné n'existe pas dans la table");
		if (!table.get(Etat).containsKey(Entree))
			throw new Exception(
				"Je a l'ETAT " + Etat + " L'entree " + Entree + "renseignée n'existe pas dans la table");
		return table.get(Etat).get(Entree).Sortie;
	}

	public String toString() {
		String res = "Table De Transition et Sortie \n";
		for (Entry<String, Map<Automate.Entree, Triplet>> i : table.entrySet()) {
			res += "ETAT " + i.getKey() + " : ";
			for (Map.Entry<Automate.Entree, Triplet> e : i.getValue().entrySet()) {
				res += " [ENTREE " + "*" + e.getKey() + "*" + " : etatSuiv "
						+ "*" + e.getValue().EtatSuiv + "*" + ", SORTIE "
						+ "*" + e.getValue().Sortie + "*" + "], ";
			}
			res += "\n";
		}
		return res;
	}
}
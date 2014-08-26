package com.ricm3.packnight.model.parser;

public class Quad {
	//Classe intermediaire de Quad
		public String entree;
		public String sortie; 
		public String EtatSuiv;
		public Quad(String entree, String etatSuiv, String sortie) {
			super();
			this.entree = entree;
			EtatSuiv = etatSuiv;
			this.sortie = sortie;
		}
}

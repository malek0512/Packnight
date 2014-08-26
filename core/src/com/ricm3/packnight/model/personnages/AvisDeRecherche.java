package com.ricm3.packnight.model.personnages;

import com.ricm3.packnight.model.structure_terrain.CoordCas;


/**
 * Structure qui repertorie l'ensemble des information d'un PM en fuite
 * */
public class AvisDeRecherche {
	boolean Mort;
	public CoordCas coord;
	public int timer;

	public AvisDeRecherche(CoordCas c) {
		coord = new CoordCas(c);
		timer=180;
	}
	
	public void majAvisDeRecherche(CoordCas c){
		timer=180;
		coord=c;
	}
}
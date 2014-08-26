	/**
	 * legende :
	 * 
	 * ## Option 1 ##
	 * Pix : les valeurs de retour sont en pixel
	 * 
	 * ## Option 2 ##
	 * B : bas
	 * H : haut
	 * G : gauche
	 * D : droite
	 * C : centre
	 * 
	 * ## Option 3 ##
	 * X : renvoie seulement pour la valeur X
	 * Y : renvoie seulement pour la valeur Y
	 * 
	 * si pas d'option 3, renvoie un objet CoordonneesFloat 
	 * contenant dans X et Y les valeurs calculer avec option 3 X et Y
	 */


package com.ricm3.packnight.model.structure_terrain;

public abstract class Coord {
	public int x;
	public int y;
	
	public void setCoord(Coord c){
		this.x = c.x;
		this.y = c.y;
	}
}

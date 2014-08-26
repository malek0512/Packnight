package com.ricm3.packnight.model.hitBoxManager;

import com.ricm3.packnight.model.structure_terrain.CoordPix;


public class HitBoxManager {

	//taille de la hitBox des personnages
	//on peut voir la valeur comme un cercle 
	static private int hitBox = 1;

	/**
	 * calcul si deux personnages se touche
	 * @param cord1 : les coords du premier personnage
	 * @param cord2 : les coords du second personnage
	 * @return vraie si les deux personnages se touche
	 */
	static public boolean personnageHittingPersonnage(CoordPix cord1,CoordPix cord2)
	{
		CoordPix cordf=cord1.PixCentre();
		CoordPix cordp=cord2.PixCentre();
		return (Math.abs(cordf.x - cordp.x) < 2*hitBox) && (Math.abs(cordf.y - cordp.y) < 2*hitBox);
	}
}

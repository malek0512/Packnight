/*
 * Cette classe est utiliser pour definir les coordonné d'un pixel
 * d'une image
 * On distinguera dans l'image 5 pixel important :
 * Le pixel en haut a droite
 * Le pixel en bas a droite
 * Le pixel en haut a gauche
 * Le pixel en bas a gauche
 */


package com.ricm3.packnight.model.structure_terrain;


public class CoordPix extends Coord {

		int size = 32;
		public enum position {
			hd, //haut droit
			hg, //haut gauche
			bd, //bas droit
			bg, //bas gauche
			centre; //pixel centrale
		}

		private position posPix;
		
		/**
		 * constructeur de base
		 * @param x
		 * @param y
		 * author : alex
		 */
		public CoordPix(int PixX,int PixY,position position)
		{
			this.x = PixX;
			this.y = PixY;
			this.posPix = position;
		}
		
		 /** constructeur de base
		 * @param coord : coord a copier
		 * author : alex
		 */
		public CoordPix(CoordPix coord)
		{
			this.x = coord.x;
			this.y = coord.y;
			this.posPix = coord.posPix;
		}
		
		public CoordPix(CoordCas cord, position position)
		{
			this.x = cord.x*size;
			this.y = cord.y*size;
			this.posPix = position;
		}
		
		/**
		 * renvoie vrai si les coord corresponde a l'objet
		 * @param coord les coord a comparer
		 * @return vraie si les x et y sont identique
		 */
		public boolean equals(CoordPix coord)
		{
			return (coord.x == this.x && coord.y == this.y);
		}
		
		/**
		 * distance en pixel entre l'objet et c
		 * @param c : coord pix
		 * @return
		 */
		public int distancePix(CoordPix c){
			return Math.abs(this.x-c.x)+Math.abs(this.y-c.y);
		}

		/**
		 * distance de case entre les deux pixel
		 * @param c : le pixel a comparer
		 * @return
		 */
		public int distanceCas(CoordPix c)
		{
			return Math.abs(this.x/size - c.x/size) + Math.abs(this.y/size - c.y/size);
		}
		
		/**
		 * renvoie la case correspondant au pixel
		 */
		public CoordCas convertIntoCas(){
			return new CoordCas(x/size, y/size);
		}

		/**
		 * @return la case sur laquel est le pixel haut gauche de l'image de ce pixel
		 */
		public CoordCas CasHG()
		{
			switch(this.posPix)
			{
			case hg : return new CoordCas(this.x/size,this.y/size);
			case hd : return new CoordCas((this.x -size +1)/size, this.y/size);
			case bg : return new CoordCas(this.x/size, this.y/size);
			case bd : return new CoordCas((this.x -size +1)/size, this.y/size);
			case centre : return new CoordCas((this.x + (size/2))/size,(this.y + (size/2))/size);
			}
			return null;
		}
		
		/**
		 * @return la case sur laquel est le pixel haut droite de l'image de ce pixel
		 */
		public CoordCas CasHD()
		{
			switch(this.posPix)
			{
			case hg : return new CoordCas((this.x + size -1)/size, this.y/size);
			case hd : return new CoordCas(this.x/size, this.y/size);
			case bg : return new CoordCas((this.x + size -1)/size, (this.y -size +1)/size);
			case bd : return new CoordCas(this.x/size, (this.y -size +1)/size);
			case centre : return new CoordCas((this.x + (size/2))/size, (y - (size/2))/size);
			}
			return null;
			
		}
		
		/**
		 * @return la case sur laquel est le pixel bas gauche de l'image de ce pixel
		 */
		public CoordCas CasBG()
		{
			switch(this.posPix)
			{
			case hg : return new CoordCas(this.x/size, (this.y + size -1)/size);
			case hd : return new CoordCas((this.x + size -1)/size, (this.y + size -1)/size);
			case bg : return new CoordCas(this.x/size, this.y/size);
			case bd : return new CoordCas((this.x + size -1)/size,this.y/size);
			case centre : return new CoordCas((this.x - (size/2))/size, (y + (size/2))/size);
			}
			return null;
		}

		/**
		 * @return la case sur laquel est le pixel bas droite de l'image de ce pixel
		 */
		public CoordCas CasBD()
		{
			switch(this.posPix)
			{
			case hg : return new CoordCas((this.x + size -1)/size, (this.y + size -1)/size);
			case hd : return new CoordCas(this.x/size, (this.y + size -1)/size);
			case bg : return new CoordCas((this.x + size -1)/size, this.y/size);
			case bd : return new CoordCas(this.x/size,this.y/size);
			case centre : return new CoordCas((this.x + (size/2))/size, (y + (size/2))/size);
			}
			return null;
		}
		
		/**
		 * @return la case sur laquel est le pixel centrale de l'image de ce pixel
		 */
		//TODO Truc bizarre (this.x + (size/2))/size <=> (this.x / size) + 0.5  pk 0.5 et pase size/2=16
		public CoordCas CasCentre()
		{
			switch(this.posPix)
			{
			case hg : return new CoordCas((this.x + (size/2))/size, (this.y + (size/2))/size);
			case hd : return new CoordCas((this.x - (size/2))/size, (this.y + (size/2))/size);
			case bg : return new CoordCas((this.x + (size/2))/size, (this.y - (size/2))/size);
			case bd : return new CoordCas((this.x - (size/2))/size, (this.y - (size/2))/size);
			case centre : return new CoordCas(this.x/size,y/size);
			}
			return null;
		}

		/**
		 * @return la case sur laquel est le pixel haut gauche de l'image de ce pixel
		 */
		public CoordCas PixHG()
		{
			switch(this.posPix)
			{
			case hg : return new CoordCas(this.x/size,this.y/size);
			case hd : return new CoordCas((this.x -size +1)/size, this.y/size);
			case bg : return new CoordCas(this.x/size, this.y/size);
			case bd : return new CoordCas((this.x -size +1)/size, this.y/size);
			case centre : return new CoordCas((this.x + (size/2))/size,(this.y + (size/2))/size);
			}
			return null;
		}
		
		/**
		 * @return la case sur laquel est le pixel haut droite de l'image de ce pixel
		 */
		public CoordCas PixHD()
		{
			switch(this.posPix)
			{
			case hg : return new CoordCas((this.x + size -1)/size, this.y/size);
			case hd : return new CoordCas(this.x/size, this.y/size);
			case bg : return new CoordCas((this.x + size -1)/size, (this.y -size +1)/size);
			case bd : return new CoordCas(this.x/size, (this.y -size +1)/size);
			case centre : return new CoordCas((this.x + (size/2))/size, (y - (size/2))/size);
			}
			return null;
		}
		
		/**
		 * @return la case sur laquel est le pixel bas gauche de l'image de ce pixel
		 */
		public CoordPix PixBG()
		{
			switch(this.posPix)
			{
			case hg : return new CoordPix(this.x, this.y + size -1,position.hg);
			case hd : return new CoordPix(this.x + size -1,this.y + size -1,position.hd);
			case bg : return new CoordPix(this.x, this.y,position.bg);
			case bd : return new CoordPix(this.x + size -1,this.y,position.bd);
			case centre : return new CoordPix(this.x - (size/2), y + (size/2),position.centre);
			}
			return null;
		}

		/**
		 * @return la case sur laquel est le pixel bas droite de l'image de ce pixel
		 */
		public CoordPix PixBD()
		{
			switch(this.posPix)
			{
			case hg : return new CoordPix(this.x + size -1, this.y + size -1,position.hg);
			case hd : return new CoordPix(this.x, this.y + size -1,position.hd);
			case bg : return new CoordPix(this.x + size -1, this.y,position.bg);
			case bd : return new CoordPix(this.x,this.y,position.bd);
			case centre : return new CoordPix(this.x + (size/2), y + (size/2),position.centre);
			}
			return null;
		}
		
		/**
		 * @return la case sur laquel est le pixel centrale de l'image de ce pixel
		 */
		public CoordPix PixCentre()
		{
			switch(this.posPix)
			{
			case hg : return new CoordPix(this.x + (size/2),this.y + (size/2),position.hd);
			case hd : return new CoordPix(this.x - (size/2),this.y + (size/2),position.hg);
			case bg : return new CoordPix(this.x + (size/2),this.y - (size/2),position.bg);
			case bd : return new CoordPix(this.x - (size/2),this.y - (size/2),position.bd);
			case centre : return new CoordPix(this.x,this.y,position.centre);
			}
			return null;
		}
		
		public boolean estSurUneCase()
		{
			return this.CasBD().equals(this.CasHG());
		}
		
		/**
		 * fonction a supprimer quand le code sera valider
		 * @param c
		 * @return
		 */
		public boolean equals(CoordCas c)
		{
			System.out.println("warning, utilisation nimportenawak de fonction equals");
			return false;
		}
		
		public String toString()
		{return "Cordonnée en pixel (x/y/position) : "+ x + " " + y + " " + posPix;}
		
}

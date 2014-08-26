package com.ricm3.packnight.model.graph;

import com.ricm3.packnight.model.structure_terrain.CoordCas;

public class NoeudEtoile {

    public int distance; // 0 blanc // 1 gris // 2 noir
    public NoeudEtoile pere;
    public CoordCas cord;
    
	public NoeudEtoile(int d, NoeudEtoile p, CoordCas c)
	{
		distance =d;
		pere = p;
		cord = c;
	}
	
	public boolean equals(NoeudEtoile p)
	{
		return this.cord.equals(p.cord);
	}
	
	public String toString()
	{
		return "coord : " + this.cord + " d : " + this.distance;
		
	}
}

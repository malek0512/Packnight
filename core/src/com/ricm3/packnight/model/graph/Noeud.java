package com.ricm3.packnight.model.graph;

public class Noeud {

    public int couleur; // 0 blanc // 1 gris // 2 noir
    public Noeud pere;
    
	public Noeud()
	{
		couleur =0;
		pere = null;
	}
	
	public void reset()
	{
		couleur = 0;
		pere = null;
	}
}

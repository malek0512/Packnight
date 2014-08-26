package com.ricm3.packnight.model.graph;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.ricm3.packnight.model.personnages.Personnage;
import com.ricm3.packnight.model.structure_terrain.CoordCas;
import com.ricm3.packnight.model.structure_terrain.Direction;
import com.ricm3.packnight.model.structure_terrain.Terrain;


public class Graph {

	public Noeud table[][];
	private int hauteur;
	private int largeur;
	
	
	public Graph(Terrain terrain)
	{
		largeur =terrain.largeur;
		hauteur =terrain.hauteur;
		table = new Noeud[largeur][hauteur];
		for(int i = 0;i<largeur;i++)
		{
			for(int j =0;j<hauteur;j++)
			{
				table[i][j] = new Noeud();
			}
		}
	}
	
	private int couleur(CoordCas u, Direction d)
	{
		CoordCas tmp = new CoordCas(u);
		tmp.avancerDansDir(d);
		if(Personnage.terrain.caseAcessible(tmp))
				return table[u.x][u.y+1].couleur;
		System.out.println("Error 104");
		return 2;
	}
	
	private int nbAdjacent(CoordCas u)
	{
		int adj = 0;
		for(Direction d : Direction.values())
		{
			if(Personnage.terrain.caseAcessible(u, d) && couleur(u,d) == 0 )
				adj++;
		}
		return adj;
	}
	
	/** Pour remettre en situation initiale chaque sommet **/
    public void reset(){
    	for(int i =0; i<largeur;i++)
    	{
    		for(int j=0;j<hauteur;j++)
    			table[i][j].reset();
    	}
    }
    
    private void removeMG(List<CoordCas> res, CoordCas c)
    {
    	Iterator<CoordCas> i = res.iterator();
    	while(i.hasNext())
    	{
    		int cpt = 0;
    		if(i.next().equals(c))
    		{
    			res.remove(cpt);
    			break;
    		}
    		cpt++;
    	}
    }

    /**
     * @param noeud : coordonnée de la case a gank
     * @return : liste des points statégique pour le gank
     */
    public List<CoordCas> visiterLargeur(CoordCas noeud, int nbInter){
    
    int nbInterFind=0;
    int nbInterSearch = 0;
    
    Noeud init = table[noeud.x][noeud.y]; 
	init.couleur = 2; // noir
	
	List<CoordCas> res =  new LinkedList<CoordCas>();
	List<CoordCas> file = new LinkedList<CoordCas>();

	file.add(noeud);
	//algo de parcours
	while (!file.isEmpty()){
		CoordCas u = file.remove(0);
	    Noeud ncourant = table[u.x][u.y];
	    //calcule du nombre d'adjacent
		int cptAdj = this.nbAdjacent(u);

		if(cptAdj <= nbInter - nbInterFind - nbInterSearch)
		{
			//mis a jours de interSearch
			nbInterSearch = nbInterSearch - 1 + cptAdj;
			//on ajoute tout les adjacent a la liste
			for(Direction d : Direction.values())
			{
				if(Personnage.terrain.caseAcessible(u, d))
				{
					CoordCas v = new CoordCas(u);
					v.avancerDansDir(d);
					if(Personnage.terrain.caseAcessible(v))
					{
					Noeud adj = table[v.x][v.y];
					if (adj.couleur==0) //blanc
					{
					    // adj.pere = ncourant;
					    adj.couleur=1; // gris 
					    file.add(v);
				    }
					else if (adj.couleur==1) //gris
					{
						//remove le adj gris trouver
						removeMG(res,v);
						//mis a jour du nombre d'inter trouver
						nbInterFind--;
						adj.couleur=2; //noir
						file.add(v);
					}
					}
				}
			}
			//fini avec ce noeud
			ncourant.couleur = 2;
		}
		else
		{
			//on passe l'état a gris
			ncourant.couleur = 1; //gris
			//on ajoute les coordone au resultat
			res.add(u);
			//on arrete de chercher une intersection
			nbInterSearch--;
			//on a trouver une intersectio
			nbInterFind++;
		}
	}
    return res;
    }
}

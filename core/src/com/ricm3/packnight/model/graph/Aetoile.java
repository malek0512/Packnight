package com.ricm3.packnight.model.graph;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.ricm3.packnight.model.personnages.Personnage;
import com.ricm3.packnight.model.structure_terrain.CoordCas;
import com.ricm3.packnight.model.structure_terrain.Direction;


public class Aetoile {

	private List<NoeudEtoile> ouvert;
	
	private List<NoeudEtoile> fermer;
	
	static private CoordCas teteDeliste;
	
	private int distance(CoordCas c1, CoordCas c2)
	{
		return Math.abs(c1.x - c2.x) + Math.abs(c1.y - c2.y);
	}
	
	private int distance(CoordCas c1)
	{
		return distance(c1,Aetoile.teteDeliste);
	}
	
	private boolean appartient(List<NoeudEtoile> l, NoeudEtoile fi)
	{
		Iterator<NoeudEtoile> i = l.iterator();
		while(i.hasNext())
		{
			if(fi.equals(i.next()))
				return true;
		}
		return false;
	}
	
	public Aetoile(CoordCas teteDeliste)
	{
		ouvert = new LinkedList<NoeudEtoile>();
		fermer = new LinkedList<NoeudEtoile>();
		Aetoile.teteDeliste = teteDeliste;
	}
	
	/**
	 * reinit le graph pour aetoile
	 */
	public void reinit()
	{
		ouvert = new LinkedList<NoeudEtoile>();
		fermer = new LinkedList<NoeudEtoile>();
	}
	
	/**
	 * interdit l'accès a une liste de coordonné
	 */
	public void blackList(List<CoordCas> l)
	{
		if(l != null)
		{
			Iterator<CoordCas> i = l.iterator();
			while(i.hasNext())
			{
				NoeudEtoile blackStar = new NoeudEtoile(Integer.MAX_VALUE, null, i.next());
				fermer.add(blackStar);
			}
		}
	}
	
	/**
	 * interdit l'accès a une coordonné
	 * @param c
	 */
	public void blackCoord(CoordCas c)
	{
		fermer.add(new NoeudEtoile(Integer.MAX_VALUE, null,c));
	}

	/**
	 * extrait le plus petit element
	 * @param list
	 * @return
	 */
	private NoeudEtoile extract(List<NoeudEtoile> list)
	{
		int min = Integer.MAX_VALUE;
		int indice=0;
		int indiceMin = 0;
		NoeudEtoile res = null;
		Iterator<NoeudEtoile> i = list.iterator();
		while(i.hasNext())
		{
			NoeudEtoile tmp = i.next();
			if(tmp.distance < min)
			{
				min = tmp.distance;
				res = tmp;
				indiceMin = indice;
			}
			indice++;
		}
		list.remove(indiceMin);
		return res;
	}
	
	/**
	 * renvoie la l'itinairaire
	 * @param queueDeListe
	 * @return
	 */
	public List<CoordCas> algo(CoordCas queueDeListe)
	{
		boolean continu = true;
		if(queueDeListe.equals(teteDeliste))
		{
			List<CoordCas>resultat = new LinkedList<CoordCas>();
			resultat.add(queueDeListe);
			resultat.add(queueDeListe);
			return resultat;
		}
		NoeudEtoile init = new NoeudEtoile(distance(queueDeListe), null, queueDeListe);
		ouvert.add(init);
		while(!ouvert.isEmpty() && continu)
		{
			NoeudEtoile m = extract(ouvert);
			fermer.add(m);
			
			for(Direction d : Direction.values())
			{
				if(Personnage.terrain.caseAcessible(m.cord, d))
				{	
					CoordCas cordFi = new CoordCas(m.cord);
					cordFi.avancerDansDir(d);
					
					NoeudEtoile fi = new NoeudEtoile(distance(cordFi), m, cordFi);
					fi.pere = m;
					if (fi.cord.equals(Aetoile.teteDeliste))
					{
						init = fi;
						continu=false;
						break;
					}
					else if(!appartient(ouvert,fi) && !appartient(fermer,fi))
						ouvert.add(fi);
				}
			}
		}
		
		List<CoordCas> res = new LinkedList<CoordCas>();
		//on crée la liste de resultat et on la retourne
		while(init.pere != null)
		{
			res.add(init.cord);
			init = init.pere;
		}
		res.add(queueDeListe);
		return res;
	}
}



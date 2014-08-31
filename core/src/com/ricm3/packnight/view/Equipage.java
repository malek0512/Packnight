package com.ricm3.packnight.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.ricm3.packnight.model.personnages.Personnage;

public abstract class Equipage {

	public static HashMap<String, String[]> automate = new LinkedHashMap<String, String[]>() {
		{put("None", null);}
		{put("Berserk", 	new String[] {"automate/fm_berserk.auto", 	Sprites.Berserk});}
		{put("Aleatoire", 	new String[] {"automate/fm_aleatoire.auto", 	Sprites.Aleatoire});}
		{put("Intercepteur",new String[] {"automate/fm_intersepteur.auto",Sprites.Intercepteur});}
		{put("Spectrum", 	new String[] {"automate/fm_lord.auto", 		Sprites.Spectrum});}
		{put("Suiveur", 	new String[] {"automate/fm_suiveur.auto", 	Sprites.Suiveur});}
	};
	
	public static Personnage joueurCamera;
	public static Personnage joueurFleche;
	public static Personnage joueurZQSD;
	public static Personnage joueurIJKL;
	public static Personnage joueur8456;

	public static List<String> getListOfPersonnage () {
		ArrayList<String> res = new ArrayList<String>();
		for(String k : Equipage.automate.keySet()){
			res.add(k);
		}
		return res;
	}
	
	public void render() {
		for(Joueur j:Joueur.liste)
//			if (j.p.hitting() || j.p instanceof Ghost)
				j.render();
	}
	
	public void suivant() {
		for(Joueur j:Joueur.liste)
			try {
				j.suivant();
			} catch (Exception e) {
				System.out.println(e);
			}
	}
	
	public static void clear(){
		Joueur.liste.clear();
	}
	
	public abstract void create();
	
	
}

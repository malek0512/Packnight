package com.ricm3.packnight.view;

import com.badlogic.gdx.audio.Sound;


public class MusicManager {

	public static boolean mute = true;
	
	static public enum typeSong {
		accueil		    ("songs/Batman.ogg"),
		win			    ("songs/Win.ogg"),      
		gameOver	    ("songs/Game_Over.ogg"),
		allBeat		    ("songs/AllBeat.ogg"),
		selection	    ("songs/Selection.ogg"),
//		frozen		    ("songs/Frozen.ogg"),
//		game		    ("songs/Game.ogg"),     
		Reperer     	("songs/Bruitage_Reperer.ogg"),
		PerduDeVue     	("songs/Bruitage_PerduDeVue.ogg"),
		Dead_Knigth    	("songs/Bruitage_Dead_Knigth.ogg"),
		Dead_Princess   ("songs/Bruitage_Dead_Princess.ogg"),
		GhostPower_Obey ("songs/Bruitage_GhostPower_Obey.ogg");
		
		private String value;
		private typeSong(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}
	}

	static public void loadSongs() {
		for(typeSong s : typeSong.values()) {
			if (s.getValue().contains("Bruitage"))
				Jeu.assets.load(s.getValue(), Sound.class);
		}	
	}
	
	static public void playLoop(typeSong s) {
		if(!mute) {
			if (! Jeu.assets.isLoaded(s.value)) {
				Jeu.assets.load(s.getValue(), Sound.class);
				Jeu.assets.finishLoading();
			}
			
			Jeu.assets.get(s.value, Sound.class).loop();
		}
	}
	
	static public void playOnce(typeSong t) {
		if(!mute) {
			if (! Jeu.assets.isLoaded(t.value)) {
				Jeu.assets.load(t.getValue(), Sound.class);
				Jeu.assets.finishLoading();
			}
			
			Jeu.assets.get(t.value, Sound.class).play();
		}
	}
	
	static public void dispose(typeSong t) {
		if (Jeu.assets.isLoaded(t.value))
			Jeu.assets.unload(t.value);	
	}
	
	static public void pause(typeSong t) {
		if (Jeu.assets.isLoaded(t.value))
			Jeu.assets.get(t.value, Sound.class).pause();;
	}
	
}

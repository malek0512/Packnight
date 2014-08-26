package com.ricm3.packnight.view;

import com.badlogic.gdx.audio.Sound;


public class MusicManager {

	public static boolean mute = false;
	
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
			Jeu.assets.load(s.getValue(), Sound.class);
		}	
	}
	
	static public void playLoop(typeSong allbeat) {
		if(!mute) {
			if (! Jeu.assets.isLoaded(allbeat.value)) {
				Jeu.assets.load(allbeat.getValue(), Sound.class);
				Jeu.assets.finishLoading();
			}
			
			Jeu.assets.get(allbeat.value, Sound.class).loop();
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

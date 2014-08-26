package com.ricm3.packnight.view.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class LauncherScreen extends Game {

//	public static HashMap<typeScreen, Screen> screenList = new HashMap<typeScreen,Screen>();
	public static final String buttons =		 	"pictures/output/buttons.atlas";
	public static final String accueil = 			"pictures/Accueil.jpeg";
	public static final String choix = 				"pictures/Choix.jpg";
	public static final String dead = 				"pictures/Dead.jpg";
	public static final String pause = 				"pictures/Pause.jpeg";
	public static final String win = 				"pictures/Win.jpeg";
	public static final String selectionPersonnage ="pictures/SelectionPerso.jpeg";
	public static final String map1 =				"pictures/Map1.png";
	public static final String map2 =				"pictures/Map2.png";
	
	public static enum typeScreen { //Les referenceur des screen sont implictement static et final
		ACCUEIL (new AccueilScreen()), 
		CHOIX (new ChoixMultiJoueurScreen()), //Aucun interet tant que le multiplayer n'est pas géré
		MENU (new MenuScreen()), 
		REGLAGE (new ReglageScreen()), //TODO
		JEU (null), //InitialisÃ© par MenuScreen
		PERDU (new PerduScreen()), 
		GAGNER (new GagnerScreen()),
		DIFFICULTE (new DifficulteScreen()), //TODO
		PAUSE (new PauseScreen()),
		CHARGEMENT (new LoadingScreen());
		;
		private Screen value; 
		private typeScreen (Screen s) {
			this.value = s;
		}
		
		public void dispose(typeScreen t){
			this.value.dispose();
		}

		public void setScreen(Screen t){
			this.value=t;
		}
	}; 
	
	private static typeScreen screenCourant;
	private static typeScreen screenPrecedent;
	
	@Override
	public void create() {
		screenCourant = typeScreen.CHARGEMENT;
		screenPrecedent = screenCourant;
		setScreen(screenCourant.value);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void render() {
		if (screenCourant != screenPrecedent) {
			screenPrecedent = screenCourant;
			setScreen(screenCourant.value);
		}

		screenCourant.value.render(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	public void dispose() {
		for(typeScreen s : typeScreen.values())
			s.value.dispose();
	}
	
	public static void setNextScreen (typeScreen t){
		screenCourant = t;
	}

}

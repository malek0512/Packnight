package com.ricm3.packnight.view;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.ricm3.packnight.model.personnages.Ghost;
import com.ricm3.packnight.model.personnages.PacKnight;
import com.ricm3.packnight.model.personnages.PacPrincess;
import com.ricm3.packnight.model.personnages.Pacman;
import com.ricm3.packnight.model.personnages.Personnage;
import com.ricm3.packnight.model.structure_terrain.Terrain;
import com.ricm3.packnight.view.MusicManager.typeSong;
import com.ricm3.packnight.view.screen.LauncherScreen;
import com.ricm3.packnight.view.screen.LauncherScreen.typeScreen;


public class Jeu implements Screen, ApplicationListener {
	static public AssetManager assets = new AssetManager();
	public static int WIDTH = 1280;
	public static int HEIGHT = 720; //WIDTH * 9 / 16;
	public static final String TITLE = "PACK-NIGHT : THE RETURN";
	public static final boolean USE_GL30 = false;
	public static final int tuile_size = 32;
	public static SpriteBatch batch;
	public static float stateTime;
	public static int time;
	public Equipage equipe;
	static String message = "";
	private BitmapFont font;
	

	public Jeu(Equipage equipe){
		super();
		this.equipe = equipe;
	}

	public void create() {
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		
		InputHandler inputHandler = new InputHandler();
		InputMultiplexer inputMultiplexer = new InputMultiplexer(inputHandler, new GestureDetector(inputHandler));
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		 font = new BitmapFont();
		//Sprite
		batch = new SpriteBatch(); //Feuille sur laquelle les sprites sont dessinés
		
	    stateTime = 0f;
//	    pacmanSprite = new S		prites(Sprites.Princess);
	    
		//Map
		Map.create();
		
		equipe.create();
		
		//Camera
		Camera.create();
		
		Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public void render() {
		
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);  
		
        // Camera --------------------- /
		Camera.render();
		
        //Map : voir le detail dans la classe Map
        Map.render(Camera.camera);
		
        //Sprite
        stateTime += Gdx.graphics.getDeltaTime(); //Calcul de decalage Delta entre chaque rafraichissement du sprite

        batch.setProjectionMatrix(Camera.camera.combined); //Colle le sprit a la map (ou camera je sais plus)
		batch.begin();
			equipe.render();
			TextBounds tb = font.getBounds(message);
	        float x = WIDTH/2 - tb.width/2;
	        float y = HEIGHT/2 + tb.height/2;
	        
	        font.drawMultiLine(batch, message, x, y);
		batch.end();

//		System.out.println(Terrain.nb_pacgum);
		if (Terrain.nb_pacgum == 0) {
			Personnage.init_personnage();
			Equipage.clear();
			LauncherScreen.setNextScreen(typeScreen.GAGNER);
		}

		if (PacKnight.vie == 0 || PacPrincess.vie == 0) {
			Personnage.init_personnage();
			Equipage.clear();
			LauncherScreen.setNextScreen(typeScreen.PERDU);
		}
		
		time ++;
		if (!(time < 30)) 
		{
			try {
				equipe.suivant();
			} catch (Exception e) {
				System.out.println(e);
			}
			Ghost.disparitionPacman();
			Pacman.majTempsInvincible();
		}
	}

	@Override
	/**
	 * Called when the Application is destroyed. Preceded by a call to pause().
	 */
	public void dispose() {
		Map.dispose();
		Jeu.batch.dispose();
	}

	@Override
	/**
	 * Called when the Application is paused. An Application is paused before it is destroyed, 
	 * when a user pressed the Home button on Android or an incoming call happened. 
	 * On the desktop this will only be called immediately before dispose() is called.
	 */
	public void pause() {
		
	}


	@Override
	public void resize(int width, int height) {
//		camera.viewportHeight = height / 2.5f;
//		camera.viewportWidth = width / 2.5f;
		Camera.camera.setToOrtho(false,width,height);
	}

	@Override
	/**
	 * Called when the Application is resumed from a paused state. On Android this happens when 
	 * the activity gets focus again. On the desktop this method will never be called.
	 */
	public void resume() {
	}


	//Methodes pour l'exectuion en tant que Screen
	
	public void render(float delta) {
		render();
	}

	public void show() {
		MusicManager.playOnce(typeSong.allBeat);
		create();
	}

	public void hide() {
		MusicManager.pause(typeSong.allBeat);
	}

}

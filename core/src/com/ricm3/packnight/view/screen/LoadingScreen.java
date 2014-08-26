package com.ricm3.packnight.view.screen;

import com.ricm3.packnight.view.Jeu;
import com.ricm3.packnight.view.MusicManager;
import com.ricm3.packnight.view.MusicManager.typeSong;
import com.ricm3.packnight.view.screen.LauncherScreen.typeScreen;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class LoadingScreen implements Screen{
	
	Texture fond;
	SpriteBatch batch;
	private BitmapFont font;
	private Texture emptyT;
	private Texture fullT;
	private NinePatch empty;
	private NinePatch full;
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);  
		
		if(Jeu.assets.update()){
			LauncherScreen.setNextScreen(typeScreen.MENU);
		}
		
		batch.begin();
			batch.draw(fond, (Jeu.WIDTH - fond.getWidth())/2, (Jeu.HEIGHT - fond.getHeight())/2);
			empty.draw(batch, 40, 225, 720, 30);
			full.draw(batch, 40, 225, Jeu.assets.getProgress()*720, 30);
			font.drawMultiLine(batch,(int)(Jeu.assets.getProgress()*100)+"% loaded",400,247,0, BitmapFont.HAlignment.CENTER);
		batch.end();
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void show() {

		
		//Barre de chargement
		font=new BitmapFont();
		batch=new SpriteBatch();
		emptyT=new Texture(Gdx.files.internal("pictures/empty.png"));
		fullT=new Texture(Gdx.files.internal( "pictures/full.png"));
		empty=new NinePatch(new TextureRegion(emptyT,24,24),8,8,8,8);
		full=new NinePatch(new TextureRegion(fullT,24,24),8,8,8,8);
		
		//Fond
		fond = new Texture(Gdx.files.internal(LauncherScreen.accueil));
		batch = new SpriteBatch();
		
		//Song 
//		Jeu.assets.load(typeSong.accueil.getValue(), Sound.class);
//		Jeu.assets.finishLoading();
		MusicManager.playLoop(typeSong.accueil);
		
		MusicManager.loadSongs();
	}

	@Override
	public void hide() {
		MusicManager.pause(typeSong.accueil);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
	}

}

package com.ricm3.packnight.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.ricm3.packnight.view.Jeu;
import com.ricm3.packnight.view.MusicManager;
import com.ricm3.packnight.view.MusicManager.typeSong;
import com.ricm3.packnight.view.screen.LauncherScreen.typeScreen;

public class AccueilScreen implements Screen{
	
	Texture fond;
	SpriteBatch batch;
	InputProcessor inputHanlder;
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);  
		batch.begin();
			batch.draw(fond, (Jeu.WIDTH - fond.getWidth())/2, (Jeu.HEIGHT - fond.getHeight())/2);
		batch.end();
		
//		if (Gdx.input.isKeyPressed(Keys.ENTER))
//			LauncherScreen.screenCourant = typeScreen.MENU;
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void show() {
//		Gdx.app.log("Accueil", "Je suis dans l'accueil");
		System.out.println("SHOWWWW_ACCUEIL");
		MusicManager.playLoop(typeSong.accueil);
		fond = new Texture(Gdx.files.internal(LauncherScreen.accueil));
		batch = new SpriteBatch();
		Gdx.input.setInputProcessor(this.inputHandler());
	}

	@Override
	public void hide() {
//		Gdx.app.log("Accueil", "HIIIDE");
		System.out.println("HIIIIDE_ACCUEIL");
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
		MusicManager.dispose(typeSong.accueil);
		batch.dispose();
		fond.dispose();
	}

	private InputProcessor inputHandler(){
		return new InputProcessor() {
			
			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				LauncherScreen.setNextScreen(typeScreen.MENU);
				return false;
			}
			
			@Override
			public boolean scrolled(int amount) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean keyUp(int keycode) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean keyTyped(char character) {
				return false;
			}
			
			@Override
			public boolean keyDown(int keycode) {
				switch (keycode) {
				case  Keys.ENTER :
					LauncherScreen.setNextScreen(typeScreen.MENU); break;
				case Keys.ESCAPE : 
					Gdx.app.exit(); break;
				case Keys.A : 
					System.out.println("pipi");; break;
				}
				return false;
			}
		};
	}
	}

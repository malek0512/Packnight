package com.ricm3.packnight.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.ricm3.packnight.view.MusicManager;
import com.ricm3.packnight.view.MusicManager.typeSong;
import com.ricm3.packnight.view.screen.LauncherScreen.typeScreen;

public class PerduScreen implements Screen {

	private Stage stage; // Contiens l'ensemble des acteur (boutons, fond)
	private Table table;
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw(); 
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
		MusicManager.playLoop(typeSong.gameOver); // Musiques

		stage = new Stage(); // Contiens l'ensemble des boutons : multiplexeur des inputs
		Gdx.input.setInputProcessor(stage); // ** stage is responsive **//

		// Chargement de l'image de fond
		final Image fond = new Image(new Texture(Gdx.files.internal(LauncherScreen.dead)));

		table = new Table();
		table.setBackground(fond.getDrawable());
		table .setFillParent(true);
		
//		table.debug();
//		table.debugTable();
		
		
		stage.addActor(table);
		
		// Chargement du Skin des boutons
		TextureAtlas buttonsAtlas = new TextureAtlas(LauncherScreen.buttons); // ** charge l'image creer avec GDX TEXTURE PACKER **//
		Skin buttonSkin = new Skin();
		buttonSkin.addRegions(buttonsAtlas); // ** La decoupe en up et down**//
		BitmapFont font = new BitmapFont(); // ** font, avec possibilité de renseigner une font ". **//

		// Definition d'un style de bouton
		final TextButtonStyle style = new TextButtonStyle(); // ** Button properties **//
		style.up = buttonSkin.getDrawable("down");
		style.down = buttonSkin.getDrawable("up");
		style.font = font;

		
		//Dessiner bouton Restart
		TextButton restart = new TextButton("Restart", style);
		restart.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				LauncherScreen.setNextScreen(typeScreen.MENU);
				return true;
			}
		});
		
		//Dessiner bouton Quit
		TextButton quit = new TextButton("Quit", style);
		quit.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.exit();
				return true;
			}
		});

		table.add(restart);
		table.row();
		table.add(quit);
		
		
	}

	@Override
	public void hide() {
		MusicManager.pause(typeSong.gameOver);
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
		MusicManager.dispose(typeSong.gameOver);
		stage.dispose();
	}

}

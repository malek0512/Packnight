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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.ricm3.packnight.view.Jeu;
import com.ricm3.packnight.view.MusicManager;
import com.ricm3.packnight.view.MusicManager.typeSong;
import com.ricm3.packnight.view.screen.LauncherScreen.typeScreen;

public class ChoixMultiJoueurScreen implements Screen {

	private Stage stage; // Contiens l'ensemble des acteur (boutons, fond)

	private TextButton buttonSinglePlayer;
	private TextButton buttonQuit;

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void show() {
		MusicManager.playLoop(typeSong.selection); // Musiques

		stage = new Stage(); // Contiens l'ensemble des boutons : multiplexeur des inputs
		Gdx.input.setInputProcessor(stage); // ** stage is responsive **//

		// Chargement de l'image de fond
		Image fond = new Image(new Texture(Gdx.files.internal(LauncherScreen.choix)));
		fond.setCenterPosition(Jeu.WIDTH / 2, Jeu.HEIGHT / 2);
		stage.addActor(fond);

		// Chargement du Skin des boutons
		TextureAtlas buttonsAtlas = new TextureAtlas(LauncherScreen.buttons); // ** charge l'image creer avec GDX TEXTURE PACKER **//
		Skin buttonSkin = new Skin();
		buttonSkin.addRegions(buttonsAtlas); // ** La decoupe en up et down**//
		BitmapFont font = new BitmapFont(); // ** font, avec possibilité de renseigner une font ". **//

		// Definition d'un style de bouton
		TextButtonStyle style = new TextButtonStyle(); // ** Button properties **//
		style.up = buttonSkin.getDrawable("up");
		style.down = buttonSkin.getDrawable("down");
		style.font = font;

		// Bouton singlePlayer
		buttonSinglePlayer = new TextButton("Single Player", style);
		buttonSinglePlayer.setCenterPosition(Jeu.WIDTH / 2 - 250, Jeu.HEIGHT / 2 - 60); // ** Button location **//
		buttonSinglePlayer.setHeight(50); // ** Button Height **//
		buttonSinglePlayer.setWidth(150); // ** Button Width **//
		buttonSinglePlayer.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				LauncherScreen.setNextScreen(typeScreen.MENU);
				return true;
			}
		});

		stage.addActor(buttonSinglePlayer);

		// Bouton Quit
		buttonQuit = new TextButton("Quit", style);
		buttonQuit.setCenterPosition(Jeu.WIDTH / 2 + 250, Jeu.HEIGHT / 2 - 60); // ** Button location **//
		buttonQuit.setHeight(50); // ** Button Height **//
		buttonQuit.setWidth(150); // ** Button Width **//
		buttonQuit.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.exit();
				return true;
			}
		});

		stage.addActor(buttonQuit);
	}

	@Override
	public void hide() {
		MusicManager.pause(typeSong.selection);
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
		MusicManager.dispose(typeSong.selection);
		stage.dispose();
	}

}

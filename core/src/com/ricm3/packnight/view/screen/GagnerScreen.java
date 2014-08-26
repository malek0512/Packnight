package com.ricm3.packnight.view.screen;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.plaf.basic.BasicBorders.ButtonBorder;

import com.ricm3.packnight.model.personnages.Ghost;
import com.ricm3.packnight.model.personnages.PacKnight;
import com.ricm3.packnight.model.structure_terrain.Direction;

import sun.awt.image.PixelConverter;
import com.ricm3.packnight.view.Equipage;
import com.ricm3.packnight.view.Jeu;
import com.ricm3.packnight.view.Joueur;
import com.ricm3.packnight.view.Map;
import com.ricm3.packnight.view.MusicManager;
import com.ricm3.packnight.view.Sprites;
import com.ricm3.packnight.view.MusicManager.typeSong;
import com.ricm3.packnight.view.screen.LauncherScreen.typeScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.MyScrolling;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sun.org.apache.regexp.internal.recompile;

public class GagnerScreen implements Screen {

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
		MusicManager.playLoop(typeSong.win); // Musiques

		stage = new Stage(); // Contiens l'ensemble des boutons : multiplexeur des inputs
		Gdx.input.setInputProcessor(stage); // ** stage is responsive **//

		// Chargement de l'image de fond
		final Image fond = new Image(new Texture(Gdx.files.internal(LauncherScreen.win)));

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

		table.add(restart).expandY().bottom().left().pad(50);
		table.add(quit).bottom().right().pad(50);
		
		
	}

	@Override
	public void hide() {
		MusicManager.pause(typeSong.win);
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
		MusicManager.dispose(typeSong.win);
		stage.dispose();
	}

}

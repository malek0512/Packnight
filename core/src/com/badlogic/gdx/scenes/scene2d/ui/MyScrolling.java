package com.badlogic.gdx.scenes.scene2d.ui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.ricm3.packnight.view.screen.LauncherScreen;

/**
 * @author malek
 */
public class MyScrolling extends WidgetGroup{

	private ArrayList<String> stringList;
	private ArrayList<TextButton> buttonList = new ArrayList<TextButton>();
	final TextButton droite;
	final TextButton gauche;
	private Skin buttonSkin;
	final TextButtonStyle style;
	private int EC = 0;
	private Label title;
	private float x, y, buttonsSelectionWidth, buttonsSelectionHeight, xDroit, yDroit, xGauche, yGauche ;
	
	public MyScrolling(ArrayList<String> list, float x, float y, float width, float height, String Title){
		this.x = x; this.y = y;
		this.stringList = list;
		buttonsSelectionHeight = height;
		buttonsSelectionWidth = width/2;
		xDroit = x + width + 5;
		yDroit = y;
		xGauche = x - width/2 - 5;
		yGauche = y;
		
		// Chargement du Skin des boutons
		TextureAtlas buttonsAtlas = new TextureAtlas(LauncherScreen.buttons); // ** charge l'image creer avec GDX TEXTURE PACKER **//
		buttonSkin = new Skin();
		buttonSkin.addRegions(buttonsAtlas); // ** La decoupe en up et down**//
		BitmapFont font = new BitmapFont(); // ** font, avec possibilité de renseigner une font ". **//

		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = font;
		title = new Label(Title, labelStyle);
		title.setCenterPosition(x + width/2, y + height + 10);
		
		// Definition d'un style de bouton
		style = new TextButtonStyle(); // ** Button properties **//
		style.up = buttonSkin.getDrawable("up");
		style.down = buttonSkin.getDrawable("down");
		style.font = font;

		//Les mouvement de deplacement
//		disapearSmoothlyUp = Actions.parallel(Actions.moveBy(0, 10, 0.5f), Actions.fadeOut(.5f));
//		disapearSmoothlyDown = Actions.parallel(Actions.moveBy(0, -10, 0.5f), Actions.fadeOut(.5f));
//		apearSmoothly = Actions.parallel(Actions.moveTo(x, y, 0.5f), Actions.fadeIn(.5f));   
				
		//Generation des boutons 
		for(String s : stringList){
			final TextButton b = new TextButton(s, style);
			b.setPosition(x,y); 
			b.setHeight(height); 
			b.setWidth(width);
			b.setDisabled(true);
			
			if (s != stringList.get(EC)) //On laisse apparaitre l'element courant
				b.addAction(Actions.fadeOut(0));
			buttonList.add(b);
		}
		
		//Definition des boutons de selection
		droite = new TextButton(">", style);
	  	gauche = new TextButton("<", style);

	  	droite.setPosition(xDroit, yDroit); 
		droite.setHeight(buttonsSelectionHeight); 
		droite.setWidth(buttonsSelectionWidth); 
		
		gauche.setPosition(xGauche, yGauche); 
		gauche.setHeight(buttonsSelectionHeight); 
		gauche.setWidth(buttonsSelectionWidth); 
		
		droite.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				next();
				return super.touchDown(event, x, y, pointer, button);
			}
		});
	
		gauche.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				previous();
				return super.touchDown(event, x, y, pointer, button);
			}
		});
	  	
	}
	
	public void addToStage(Stage stage){
		//Add to stage
	  	for(TextButton b : buttonList){
	  		stage.addActor(b);
	  	}
	  	stage.addActor(title);
	  	stage.addActor(droite);
	  	stage.addActor(gauche);
	}
	
	private void next(){
		if (EC < buttonList.size() - 1){
			buttonList.get(EC).addAction(Actions.parallel(Actions.moveBy(-10, 0, 0.5f), Actions.fadeOut(.5f)));
			EC++;
			buttonList.get(EC).addAction(Actions.moveTo(x, y));
			buttonList.get(EC).addAction(Actions.parallel(Actions.moveTo(x, y, 0.5f), Actions.fadeIn(.5f)));
		}
	}
	
	private void previous(){
		if (EC > 0){
			buttonList.get(EC).addAction(Actions.parallel(Actions.moveBy(10, 0, 0.5f), Actions.fadeOut(.5f)));
			EC--;
			buttonList.get(EC).addAction(Actions.moveTo(x, y));
			buttonList.get(EC).addAction(Actions.parallel(Actions.moveTo(x, y, 0.5f), Actions.fadeIn(.5f)));
		}
	}
	
	public void setPosition(float x, float y){
		//Effectue une translation de vecteur de old(Coordonnées) vers Coordonnées (x,y)
		Vector2 translation = new Vector2(x - this.x, y - this.y);
		for(TextButton t : buttonList)
			t.moveBy(translation.x, translation.y);
		droite.moveBy(translation.x, translation.y);
		gauche.moveBy(translation.x, translation.y);
		title.moveBy(translation.x, translation.y);
		this.x = x; this.y = y;
	}

	public void setCenterPosition(float x, float y){
		//Effectue une translation de vecteur de old(Coordonnées) vers Coordonnées (x,y)
		Vector2 translation = new Vector2(x - this.x, y - this.y);
		for(TextButton t : buttonList)
			t.setCenterPosition(x, y);
		droite.moveBy(translation.x, translation.y);
		gauche.moveBy(translation.x, translation.y);
		title.moveBy(translation.x, translation.y);
		this.x = x; this.y = y;
	}
	
	public String getEC(){
		return stringList.get(EC);
	}
}

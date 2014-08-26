package com.ricm3.packnight.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.ricm3.packnight.model.personnages.Personnage;
import com.ricm3.packnight.model.structure_terrain.Direction;

public class InputHandler implements InputProcessor, GestureListener {
	public static Personnage p = Equipage.joueurCamera;
	static public int tauxDeplacement = Personnage.tauxDeDeplacement;
	
	public boolean keyDown(int arg0) {
		switch (arg0) {
			case Keys.ESCAPE : Gdx.app.exit(); break;
			case Keys.UP :
				Equipage.joueurFleche.setNextDirection(Direction.bas);
				Equipage.joueurCamera.setNextDirection(Direction.bas);
				break;
			case Keys.RIGHT :
				Equipage.joueurFleche.setNextDirection(Direction.droite);
				Equipage.joueurCamera.setNextDirection(Direction.droite);
				break;
			case Keys.LEFT :
				Equipage.joueurFleche.setNextDirection(Direction.gauche);
				Equipage.joueurCamera.setNextDirection(Direction.gauche);
				break;
			case Keys.DOWN :  
				Equipage.joueurFleche.setNextDirection(Direction.haut);
				Equipage.joueurCamera.setNextDirection(Direction.haut);
				break;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int arg0) {
		return false;
	}

	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int pointer) {
		return false;
	}

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		
		return false;
	}

	// => destinÃ©e Android
	public static void manipulerAccelerometre() {
        if(Gdx.input.getAccelerometerY()>1)
        {
        	Equipage.joueurFleche.setNextDirection(Direction.droite);
        }
        if(Gdx.input.getAccelerometerY()<-1)
        {
        	Equipage.joueurFleche.setNextDirection(Direction.gauche);
        }
        if(Gdx.input.getAccelerometerX()>1)
        {
        	Equipage.joueurFleche.setNextDirection(Direction.bas);
        }
        if(Gdx.input.getAccelerometerX()<-1)
        {
        	Equipage.joueurFleche.setNextDirection(Direction.haut);
        }
  }


	
	
	//Methodes liées a GestureListener
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

	public boolean pan(float x, float y, float deltaX, float deltaY) {
		int taux = 6;
	if (deltaX > taux) {
		Equipage.joueurFleche.setNextDirection(Direction.droite);
		Equipage.joueurCamera.setNextDirection(Direction.droite);
	} else if (deltaX < -taux) {
		Equipage.joueurFleche.setNextDirection(Direction.gauche);
		Equipage.joueurCamera.setNextDirection(Direction.gauche);
	} else if (deltaY > -taux) {
		Equipage.joueurFleche.setNextDirection(Direction.haut);
		Equipage.joueurCamera.setNextDirection(Direction.haut);
	} else if (deltaY < taux) {
		Equipage.joueurFleche.setNextDirection(Direction.bas);
		Equipage.joueurCamera.setNextDirection(Direction.bas);
	}
	return true;
}

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
            Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

	@Override
	public boolean panStop(float arg0, float arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	

}

package com.ricm3.packnight.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.ricm3.packnight.view.Demo_Berserk;
import com.ricm3.packnight.view.Jeu;
import com.ricm3.packnight.view.screen.LauncherScreen;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = true;
		config.disableAudio = false;
		initialize(new LauncherScreen(), config);
	}
}

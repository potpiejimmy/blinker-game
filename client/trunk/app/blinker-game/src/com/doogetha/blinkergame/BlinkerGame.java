package com.doogetha.blinkergame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.doogetha.blinkergame.screens.GameScreen;
import com.doogetha.blinkergame.screens.StartScreen;

public class BlinkerGame extends Game {

	public final static int VIRTUAL_TILE_SIZE = 2000;
	public final static int VIEWPORT_SIZE = VIRTUAL_TILE_SIZE / 2;
	public final static int BUTTON_SIZE = 300;
	
	public Assets assets = null;
	
	public Screen startScreen, gameScreen;
	
	@Override
	public void create() {
		assets = new Assets();

		startScreen = new StartScreen(this);
		gameScreen = new GameScreen(this);
		
		setScreen(startScreen);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		assets.dispose();
	}
}

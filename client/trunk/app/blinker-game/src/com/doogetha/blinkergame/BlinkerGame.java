package com.doogetha.blinkergame;

import com.badlogic.gdx.Game;
import com.doogetha.blinkergame.screens.GameScreen;

public class BlinkerGame extends Game {

	public final static int VIRTUAL_TILE_SIZE = 2000;
	public final static int VIEWPORT_SIZE = VIRTUAL_TILE_SIZE / 2;
	public final static int BUTTON_SIZE = 300;
	
	public Assets assets = null;
	
	@Override
	public void create() {
		assets = new Assets();
		
		setScreen(new GameScreen(this));
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		assets.dispose();
	}
}

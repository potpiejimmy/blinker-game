package com.doogetha.blinkergame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.doogetha.blinkergame.screens.EnterBetScreen;
import com.doogetha.blinkergame.screens.GameScreen;
import com.doogetha.blinkergame.screens.StartScreen;

public class BlinkerGame extends Game {

	public final static int VIRTUAL_TILE_SIZE = 2000;
	public final static int VIEWPORT_SIZE = VIRTUAL_TILE_SIZE / 2;
	public final static int BUTTON_SIZE = 300;
	
	public Assets assets = null;
	
	private int bet = 0;
	
	public Screen startScreen, enterBetScreen, gameScreen;
	
	@Override
	public void create() {
		assets = new Assets();

		startScreen = new StartScreen(this);
		enterBetScreen = new EnterBetScreen(this);
		gameScreen = new GameScreen(this);
		
		setScreen(startScreen);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		assets.dispose();
	}

	public int getBet() {
		return bet;
	}

	public void setBet(int bet) {
		this.bet = bet;
	}
}

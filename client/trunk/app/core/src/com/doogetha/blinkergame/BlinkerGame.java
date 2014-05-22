package com.doogetha.blinkergame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.doogetha.blinkergame.screens.EnterBetScreen;
import com.doogetha.blinkergame.screens.GameScreen;
import com.doogetha.blinkergame.screens.StartScreen;

public class BlinkerGame extends Game implements NativeApplication {

	public final static int VIRTUAL_TILE_SIZE = 2000;
	public final static int VIEWPORT_SIZE = VIRTUAL_TILE_SIZE / 2;
	public final static int BUTTON_SIZE = 300;
	
	public NativeApplication nativeApplication = null;
	
	public Assets assets = null;
	
	private int bet = 0;
	
	public Screen startScreen, enterBetScreen, gameScreen;
	
	public BlinkerGame() {
	}
	
	public BlinkerGame(NativeApplication nativeApplication) {
		this.nativeApplication = nativeApplication;
	}
	
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
		
		startScreen.dispose();
		enterBetScreen.dispose();
		gameScreen.dispose();
	}

	public int getBet() {
		return bet;
	}

	public void setBet(int bet) {
		this.bet = bet;
	}
	
	public void startGame(int bet) {
		setBet(bet);
		setScreen(gameScreen);
	}

	@Override
	public void setBannerAdVisible(boolean visible) {
		if (nativeApplication != null) nativeApplication.setBannerAdVisible(visible);
	}
}

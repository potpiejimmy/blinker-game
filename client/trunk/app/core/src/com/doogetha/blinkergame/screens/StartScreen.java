package com.doogetha.blinkergame.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.doogetha.blinkergame.BlinkerGame;
import com.doogetha.blinkergame.Utils;

public class StartScreen extends AbstractScreen {

	private Button startButton, startWithBetButton, exitButton;
	
	public StartScreen(BlinkerGame game) {
		super(game);

		startButton = Utils.newTextButton(app, "Start Game", new Runnable() {
			@Override public void run() { app.startGame(0); }
		});
		startWithBetButton = Utils.newTextButton(app, "Start Game (+Bet)", new Runnable() {
			@Override public void run() { app.setScreen(app.enterBetScreen); }
		});
		exitButton = Utils.newTextButton(app, "Exit", new Runnable() {
			@Override public void run() { Gdx.app.exit(); }
		});

		startButton.setSize(BlinkerGame.VIEWPORT_SIZE, BlinkerGame.VIEWPORT_SIZE / 4);
		startWithBetButton.setSize(BlinkerGame.VIEWPORT_SIZE, BlinkerGame.VIEWPORT_SIZE / 4);
		exitButton.setSize(BlinkerGame.VIEWPORT_SIZE, BlinkerGame.VIEWPORT_SIZE / 4);

		setFixedPositions();
		
		stage.addActor(app.assets.startScreenBackground);
		stage.addActor(app.assets.startScreenCar);
		stage.addActor(startButton);
		stage.addActor(startWithBetButton);
		stage.addActor(exitButton);
	}
	
	protected void setFixedPositions() {
		startButton.setPosition((stage.getViewport().getWorldWidth() - startButton.getWidth())/2, BlinkerGame.BUTTON_SIZE*9/5);
		startWithBetButton.setPosition((stage.getViewport().getWorldWidth() - startWithBetButton.getWidth())/2, BlinkerGame.BUTTON_SIZE);
		exitButton.setPosition((stage.getViewport().getWorldWidth() - exitButton.getWidth())/2, BlinkerGame.BUTTON_SIZE/5);
		app.assets.startScreenBackground.setPosition(-(app.assets.startScreenBackground.getWidth() - stage.getViewport().getWorldWidth())/2, -(app.assets.startScreenBackground.getHeight() - stage.getViewport().getWorldHeight())/2);
		app.assets.startScreenCar.setPosition((stage.getViewport().getWorldWidth()-app.assets.startScreenCar.getWidth())/2, (stage.getViewport().getWorldHeight()-app.assets.startScreenCar.getHeight())/4*3);
	}
	
	@Override
	public void render(float delta) {
	    Gdx.gl.glClearColor(0, 0, 0f, 1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	    stage.act();
		stage.draw(); // stage has its own sprite batch
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		setFixedPositions();
	}
	
	@Override
	public void show() {
		super.show();

		stage.getRoot().setVisible(false);
		Utils.makeAlphaInvisible(stage.getRoot());
		Utils.fadeVisibility(stage.getRoot(), 0f, 0.5f, true);
	}
	
	@Override
	public void hide() {
		super.hide();
	}
}

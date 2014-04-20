package com.doogetha.blinkergame.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.doogetha.blinkergame.BlinkerGame;
import com.doogetha.blinkergame.Utils;

public class StartScreen extends AbstractScreen {

	private Button startButton, startWithBetButton;
	
	public StartScreen(BlinkerGame game) {
		super(game);

		startButton = Utils.newTextButton(app, "Start");
		startWithBetButton = Utils.newTextButton(app, "Start (with bet)");

		startButton.setSize(BlinkerGame.VIEWPORT_SIZE, BlinkerGame.VIEWPORT_SIZE / 4);
		startWithBetButton.setSize(BlinkerGame.VIEWPORT_SIZE, BlinkerGame.VIEWPORT_SIZE / 4);

		setFixedPositions();
		
		stage.addActor(app.assets.startScreenBackground);
		stage.addActor(app.assets.startScreenCar);
		stage.addActor(startButton);
		stage.addActor(startWithBetButton);
		
		startButton.addListener(new InputListener() {
			@Override
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			@Override
		    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				app.startGame(0);;
		    }
		});

		startWithBetButton.addListener(new InputListener() {
			@Override
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			@Override
		    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				app.setScreen(app.enterBetScreen);
		    }
		});
	}
	
	protected void setFixedPositions() {
		startButton.setPosition((stage.getViewport().getWorldWidth() - startButton.getWidth())/2, BlinkerGame.BUTTON_SIZE);
		startWithBetButton.setPosition((stage.getViewport().getWorldWidth() - startWithBetButton.getWidth())/2, BlinkerGame.BUTTON_SIZE/5);
		app.assets.startScreenBackground.setPosition(-(app.assets.startScreenBackground.getWidth() - stage.getViewport().getWorldWidth())/2, -(app.assets.startScreenBackground.getHeight() - stage.getViewport().getWorldHeight())/2);
		app.assets.startScreenCar.setPosition((stage.getViewport().getWorldWidth()-app.assets.startScreenCar.getWidth())/2, (stage.getViewport().getWorldHeight()-app.assets.startScreenCar.getHeight())/2);
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

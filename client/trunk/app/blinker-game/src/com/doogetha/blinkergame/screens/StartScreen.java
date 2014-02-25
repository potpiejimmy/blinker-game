package com.doogetha.blinkergame.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.doogetha.blinkergame.BlinkerGame;
import com.doogetha.blinkergame.Utils;

public class StartScreen extends AbstractScreen {

	public StartScreen(BlinkerGame game) {
		super(game);

		setFixedPositions();
		
		stage.addActor(app.assets.startScreenBackground);
		stage.addActor(app.assets.startScreenCar);
		stage.addActor(app.assets.startButton);
		
		app.assets.startButton.addListener(new InputListener() {
			@Override
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				app.setScreen(app.enterBetScreen);
				return true;
			}
		});
	}
	
	protected void setFixedPositions() {
		app.assets.startButton.setPosition((camera.viewportWidth - app.assets.startButton.getWidth())/2, BlinkerGame.BUTTON_SIZE/5);
		app.assets.startScreenBackground.setPosition(-(app.assets.startScreenBackground.getWidth() - camera.viewportWidth)/2, -(app.assets.startScreenBackground.getHeight() - camera.viewportHeight)/2);
		app.assets.startScreenCar.setPosition((camera.viewportWidth-app.assets.startScreenCar.getWidth())/2, (camera.viewportHeight-app.assets.startScreenCar.getHeight())/2);
	}
	
	@Override
	public void render(float delta) {
	    Gdx.gl.glClearColor(0, 0, 0f, 1);
	    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		stage.getSpriteBatch().setProjectionMatrix(camera.combined);
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

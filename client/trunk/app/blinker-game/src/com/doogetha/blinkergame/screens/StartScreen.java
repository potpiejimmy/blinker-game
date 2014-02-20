package com.doogetha.blinkergame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.doogetha.blinkergame.BlinkerGame;

public class StartScreen extends AbstractScreen {

	public StartScreen(BlinkerGame game) {
		super(game);
		
		app.assets.startButton.addListener(new InputListener() {
			@Override
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
//				System.out.println("Button pressed");
				app.setScreen(app.gameScreen);
				return true;
			}
		});

		app.assets.startButton.setPosition((camera.viewportWidth - app.assets.startButton.getWidth())/2, 0);
		app.assets.road.setPosition(-(app.assets.road.getWidth() - camera.viewportWidth)/2, -(app.assets.road.getHeight() - camera.viewportHeight)/2);
		app.assets.car.setPosition((camera.viewportWidth-app.assets.car.getWidth())/2, (camera.viewportHeight-app.assets.car.getHeight())/2);
		
		stage.addActor(app.assets.road);
		stage.addActor(app.assets.car);
		stage.addActor(app.assets.startButton);
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
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}

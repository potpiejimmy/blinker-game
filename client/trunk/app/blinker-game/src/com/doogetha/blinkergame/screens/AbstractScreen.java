package com.doogetha.blinkergame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.doogetha.blinkergame.BlinkerGame;

public abstract class AbstractScreen implements Screen {

	protected BlinkerGame app;
	
	protected OrthographicCamera camera;
	protected Stage stage;
	
	protected AbstractScreen(BlinkerGame app) {
		this.app = app;
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		constructCamera(w, h);
		
		stage = new Stage(camera.viewportWidth, camera.viewportHeight);
		Gdx.input.setInputProcessor(stage);
		
	}

	protected void constructCamera(float w, float h) {
		if (h > w)
			camera = new OrthographicCamera(BlinkerGame.VIEWPORT_SIZE, BlinkerGame.VIEWPORT_SIZE*h/w);
		else
			camera = new OrthographicCamera(BlinkerGame.VIEWPORT_SIZE*w/h, BlinkerGame.VIEWPORT_SIZE);
	}
	
	@Override
	public void dispose() {
		stage.dispose();
	}

	@Override
	public void resize(int w, int h) {
		constructCamera(w, h);
		stage.setViewport(camera.viewportWidth, camera.viewportHeight);
	}	
}

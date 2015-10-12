package com.doogetha.blinkergame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.doogetha.blinkergame.BlinkerGame;

public abstract class AbstractScreen implements Screen {

	protected BlinkerGame app;
	
	protected Stage stage;
	
	protected AbstractScreen(BlinkerGame app) {
		this.app = app;
		
		stage = new Stage(new ExtendViewport(BlinkerGame.VIEWPORT_SIZE, BlinkerGame.VIEWPORT_SIZE));
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	@Override
	public void resize(int w, int h) {
		stage.getViewport().update(w, h, true);
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}

package com.doogetha.blinkergame.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.doogetha.blinkergame.BlinkerGame;
import com.doogetha.blinkergame.Utils;

public class StartScreen extends AbstractScreen {

	private Button startButton, startWithBetButton, hiscoreButton;
	private Button[] allButtons;
	protected Button[] buttons() {
		return allButtons != null ? allButtons :
			(allButtons = new Button[] {startButton, startWithBetButton, hiscoreButton});
	}
	
	private boolean showStartupAnimation = true;
	
	public StartScreen(BlinkerGame game) {
		super(game);

		startButton = Utils.newTextButton(app, "Start Game", new Runnable() {
			@Override public void run() { app.startGame(0); }
		});
		startWithBetButton = Utils.newTextButton(app, "Start Game (+Bet)", new Runnable() {
			@Override public void run() { app.setScreen(app.enterBetScreen); }
		});
		hiscoreButton = Utils.newTextButton(app, ">> High Scores <<", new Runnable() {
			@Override public void run() { app.invokeLeaderboards(); }
		});

		for (Button button : buttons()) button.setSize(BlinkerGame.VIEWPORT_SIZE * 2 / 3, BlinkerGame.VIEWPORT_SIZE / 6);

		stage.addActor(app.assets.startScreenBackground);
		stage.addActor(app.assets.startScreenCar);
		for (Button button : buttons()) stage.addActor(button);
	}
	
	protected void setFixedPositions() {
		int buttonPos =  buttons().length * 3 - 2;
		for (Button button : buttons()) {
			button.setPosition((stage.getViewport().getWorldWidth() - startButton.getWidth())/2, BlinkerGame.BUTTON_SIZE * buttonPos / 5);
			buttonPos -= 3;
		}
		
		app.assets.startScreenBackground.setPosition(-(app.assets.startScreenBackground.getWidth() - stage.getViewport().getWorldWidth())/2, -(app.assets.startScreenBackground.getHeight() - stage.getViewport().getWorldHeight())/2);
		if (app.assets.startScreenCar.getActions().size == 0) {
			if (showStartupAnimation) {
				showStartupAnimation = false;
				startupAnimation();
			} else {
				app.assets.startScreenCar.setPosition((stage.getViewport().getWorldWidth()-app.assets.startScreenCar.getWidth())/2, (stage.getViewport().getWorldHeight()-app.assets.startScreenCar.getHeight())/4*3);
			}
		}
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
	
	protected void startupAnimation() {
		final float ANIM_DURATION = 1.5f;

		for (Button button : buttons()) {
			button.setVisible(false);
			Utils.makeAlphaInvisible(button);
			button.addAction(Actions.delay(ANIM_DURATION-0.5f, Actions.sequence(Actions.show(), Actions.fadeIn(0.2f))));
		}
		app.assets.startScreenCar.setPosition((stage.getViewport().getWorldWidth()-app.assets.startScreenCar.getWidth())/2, -app.assets.startScreenCar.getHeight());
		app.assets.startScreenCar.addAction(Actions.moveTo((stage.getViewport().getWorldWidth()-app.assets.startScreenCar.getWidth())/2, (stage.getViewport().getWorldHeight()-app.assets.startScreenCar.getHeight())/4*3, ANIM_DURATION, Interpolation.fade));
	}
	
	@Override
	public void show() {
		super.show();

		stage.getRoot().setVisible(false);
		Utils.makeAlphaInvisible(stage.getRoot());
		Utils.fadeVisibility(stage.getRoot(), 0f, 0.5f, true);

		this.showStartupAnimation = true;
	}
	
	@Override
	public void hide() {
		super.hide();
	}
}

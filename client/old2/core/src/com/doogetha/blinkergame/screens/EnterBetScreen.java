package com.doogetha.blinkergame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.doogetha.blinkergame.BlinkerGame;
import com.doogetha.blinkergame.Utils;

public class EnterBetScreen extends AbstractScreen {

	private Image background;
	private TextButton goButton;
	private TextField betField;
	private Label betLabel;
	
	public EnterBetScreen(BlinkerGame game) {
		super(game);

		background = new Image(new TextureRegion(app.assets.textureRoad));
		background.setSize(BlinkerGame.VIRTUAL_TILE_SIZE, BlinkerGame.VIRTUAL_TILE_SIZE);
		
		goButton = Utils.newTextButton(app, "Go !", new Runnable() {
			@Override
		    public void run() {
				int bet = 0;
				try {bet = Integer.parseInt(betField.getText());} catch (NumberFormatException nfe) {}
				app.startGame(bet);
		    }
		});
		goButton.setSize(BlinkerGame.VIEWPORT_SIZE * 2 / 3, BlinkerGame.VIEWPORT_SIZE / 6);
		
		Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pm.setColor(Color.BLACK);
		pm.fill();
		betField = new TextField("", 
				new TextFieldStyle(app.assets.scoreFont, Color.YELLOW, new TextureRegionDrawable(new TextureRegion(new Texture(pm), 6, 90)), null, null));
		pm.dispose();
		betField.setMaxLength(5);
		betField.setSize(BlinkerGame.VIEWPORT_SIZE, 90);

		betLabel = new Label("Enter your bet (min. score):", new Label.LabelStyle(app.assets.font, Color.YELLOW));
		
		setFixedPositions();
		
		stage.addActor(background);
		stage.addActor(betLabel);
		stage.addActor(betField);
		stage.addActor(goButton);
	}

	protected void setFixedPositions() {
		background.setPosition(-(background.getWidth() - stage.getViewport().getWorldWidth())/2, -(background.getHeight() - stage.getViewport().getWorldHeight())/2);
		float buttonPosX = (stage.getViewport().getWorldWidth() - goButton.getWidth())/2;
		float buttonPosY = stage.getViewport().getWorldHeight()/2 + 50;
		goButton.setPosition(buttonPosX, stage.getViewport().getWorldHeight()/2);
		betField.setPosition(buttonPosX, buttonPosY + goButton.getHeight() - 20);
		betLabel.setPosition(buttonPosX, buttonPosY + goButton.getHeight() + betField.getHeight() - 20);
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		setFixedPositions();
	}
	
	@Override
	public void show() {
		super.show();
		Gdx.input.setOnscreenKeyboardVisible(true);
		stage.setKeyboardFocus(betField);
	}

	@Override
	public void hide() {
		super.hide();
		Gdx.input.setOnscreenKeyboardVisible(false);
	}
	
	@Override
	public void render(float delta) {
//	    Gdx.gl.glClearColor(0, 0.5f, 0f, 1);
//	    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
//
	    stage.act();
	    stage.draw();
	}

}

package com.doogetha.blinkergame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.doogetha.blinkergame.BlinkerGame;

public class EnterBetScreen extends AbstractScreen {

	private TextButton goButton;
	private TextField betField;
	private Label betLabel;
	
	public EnterBetScreen(BlinkerGame game) {
		super(game);

		goButton = new TextButton("GO!", new TextButtonStyle(
				new TextureRegionDrawable(new TextureRegion(app.assets.textureStartButton)),
				new TextureRegionDrawable(new TextureRegion(app.assets.textureStartButton)),
				new TextureRegionDrawable(new TextureRegion(app.assets.textureStartButton)),
				app.assets.font));
		
		Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pm.setColor(Color.BLACK);
		pm.fill();
		betField = new TextField("100", 
				new TextFieldStyle(app.assets.scoreFont, Color.YELLOW, new TextureRegionDrawable(new TextureRegion(new Texture(pm), 6, 90)), null, null));
		pm.dispose();
		betField.setMaxLength(5);
		betField.setCursorPosition(betField.getText().length());
		betField.setSize(BlinkerGame.VIEWPORT_SIZE, 90);

		betLabel = new Label("BET:", new Label.LabelStyle(app.assets.font, Color.YELLOW));
		
		setFixedPositions();
		
		stage.addActor(betLabel);
		stage.addActor(betField);
		stage.addActor(goButton);
		
		goButton.addListener(new InputListener() {
			@Override
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				try {app.setBet(Integer.parseInt(betField.getText()));} catch (NumberFormatException nfe) {}
				app.setScreen(app.gameScreen);
				return true;
			}
		});
	}

	protected void setFixedPositions() {
		goButton.setPosition((camera.viewportWidth - goButton.getWidth())/2, camera.viewportHeight/2);
		betField.setPosition((camera.viewportWidth - betField.getPrefWidth())/2, camera.viewportHeight/2 + goButton.getHeight() + 100);
		betLabel.setPosition((camera.viewportWidth - goButton.getPrefWidth())/2 + 80, camera.viewportHeight/2 + goButton.getHeight() + 90);
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
	    Gdx.gl.glClearColor(0, 0.5f, 0f, 1);
	    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

	    stage.act();
	    stage.draw();
	}

}

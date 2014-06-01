package com.doogetha.blinkergame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Assets {
	
	public Texture textureRoad, textureCar, textureCarLeft, textureCarRight;
	public Texture buttonLeftOff,buttonLeftOn,buttonRightOff,buttonRightOn;
	public Texture textureGetReady, textureDrive, textureGameOver, texturePlusOne;
	public Texture textureButtonUp, textureButtonDown;
	public Button buttonLeft, buttonRight;
	public Image road;
	public AnimatedImage car;
	public Image startScreenBackground, startScreenCar;
	public BitmapFont font, scoreFont;
	public Image getready, drive, gameover;
	public Label scoreLabel, plusLabel, betLabel;
	public Sound soundCoin, soundTurnSignal;
	public Animation carBlinkLeftAnim, carBlinkRightAnim;
	
	public Assets() {
		textureRoad = new Texture(Gdx.files.internal("data/road.png"));
		textureRoad.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		textureRoad.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);

		textureCar = new Texture(Gdx.files.internal("data/car.png"));
		textureCar.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		textureCarLeft = new Texture(Gdx.files.internal("data/carleft.png"));
		textureCarLeft.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		textureCarRight = new Texture(Gdx.files.internal("data/carright.png"));
		textureCarRight.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		textureGetReady = new Texture(Gdx.files.internal("data/getready.png"));
		textureGetReady.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		textureDrive = new Texture(Gdx.files.internal("data/driveroute.png"));
		textureDrive.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		textureGameOver = new Texture(Gdx.files.internal("data/gameover.png"));
		textureGameOver.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		texturePlusOne = new Texture(Gdx.files.internal("data/plusone.png"));
		texturePlusOne.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		road = new Image(new TextureRegion(textureRoad, 0, 0, textureRoad.getWidth() * 3, textureRoad.getHeight() * 3));
		road.setSize(3f * BlinkerGame.VIRTUAL_TILE_SIZE, 3f * BlinkerGame.VIRTUAL_TILE_SIZE);
		
		car = new AnimatedImage(null, new TextureRegion(textureCar));
		car.setSize(0.4f * BlinkerGame.VIEWPORT_SIZE, 0.4f * BlinkerGame.VIEWPORT_SIZE);
		car.setOrigin(car.getWidth()/2, car.getHeight()/2);
		
		carBlinkLeftAnim = new Animation(0.2f, new TextureRegion(textureCarLeft), new TextureRegion(textureCar));
		carBlinkRightAnim = new Animation(0.2f, new TextureRegion(textureCarRight), new TextureRegion(textureCar));
		
		startScreenBackground = new Image(new TextureRegion(textureRoad));
		startScreenBackground.setSize(BlinkerGame.VIRTUAL_TILE_SIZE, BlinkerGame.VIRTUAL_TILE_SIZE);

		startScreenCar = new Image(new TextureRegion(textureCar));
		startScreenCar.setSize(0.4f * BlinkerGame.VIEWPORT_SIZE, 0.4f * BlinkerGame.VIEWPORT_SIZE);
		startScreenCar.setOrigin(car.getWidth()/2, car.getHeight()/2);
		
		getready = newScreenImageActor(textureGetReady, BlinkerGame.VIEWPORT_SIZE);
		drive = newScreenImageActor(textureDrive, BlinkerGame.VIEWPORT_SIZE);
		gameover = newScreenImageActor(textureGameOver, BlinkerGame.VIEWPORT_SIZE);
		
		font = new BitmapFont(Gdx.files.internal("data/menufont.fnt")); // size 90
		scoreFont = new BitmapFont(Gdx.files.internal("data/scorefont.fnt")); // size 90
		
		soundCoin = Gdx.audio.newSound(Gdx.files.internal("data/coin.wav"));
		soundTurnSignal = Gdx.audio.newSound(Gdx.files.internal("data/turnsignal.wav"));
		
		buttonLeftOff = new Texture(Gdx.files.internal("data/buttonleft_off.png"));
		buttonLeftOn = new Texture(Gdx.files.internal("data/buttonleft_on.png"));
		buttonRightOff = new Texture(Gdx.files.internal("data/buttonright_off.png"));
		buttonRightOn = new Texture(Gdx.files.internal("data/buttonright_on.png"));
		textureButtonUp = new Texture(Gdx.files.internal("data/mbuttonu.png"));
		textureButtonDown = new Texture(Gdx.files.internal("data/mbuttond.png"));

		buttonLeft = new Button(new TextureRegionDrawable(new TextureRegion(buttonLeftOff)), new TextureRegionDrawable(new TextureRegion(buttonLeftOn)), new TextureRegionDrawable(new TextureRegion(buttonLeftOn)));
		buttonLeft.setSize(BlinkerGame.BUTTON_SIZE, BlinkerGame.BUTTON_SIZE);
		buttonRight = new Button(new TextureRegionDrawable(new TextureRegion(buttonRightOff)), new TextureRegionDrawable(new TextureRegion(buttonRightOn)), new TextureRegionDrawable(new TextureRegion(buttonRightOn)));
		buttonRight.setSize(BlinkerGame.BUTTON_SIZE, BlinkerGame.BUTTON_SIZE);
		
		scoreLabel = new Label(" ", new Label.LabelStyle(scoreFont, Color.YELLOW));
		plusLabel = new Label("+1", new Label.LabelStyle(scoreFont, Color.YELLOW));
		betLabel = new Label("+100", new Label.LabelStyle(scoreFont, Color.YELLOW));
		betLabel.setFontScale(0.7f);
		Utils.makeAlphaInvisible(plusLabel);
	}
	
	public void dispose() {
		textureRoad.dispose();
		textureCar.dispose();
		textureCarLeft.dispose();
		textureCarRight.dispose();
		textureGetReady.dispose();
		textureDrive.dispose();
		textureGameOver.dispose();
		texturePlusOne.dispose();
		textureButtonUp.dispose();
		textureButtonDown.dispose();
		buttonLeftOff.dispose();
		buttonLeftOn.dispose();
		buttonRightOff.dispose();
		buttonRightOn.dispose();
		font.dispose();
		scoreFont.dispose();
		soundCoin.dispose();
		soundTurnSignal.dispose();
	}
	
	protected Image newScreenImageActor(Texture texture, float size) {
		Image img = new Image(new TextureRegion(texture));
		img.setSize(size, size);
		Utils.makeAlphaInvisible(img);
		img.setTouchable(Touchable.disabled);
		return img;
	}
}

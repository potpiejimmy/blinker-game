package com.doogetha.blinkergame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Assets {
	
	public Texture textureRoad, textureCar;
	public Texture buttonLeftOff,buttonLeftOn,buttonRightOff,buttonRightOn;
	public Texture textureGetReady, textureDrive, textureGameOver, texturePlusOne;
	public Button buttonLeft, buttonRight;
	public Image road, car;
	public BitmapFont font;
	public Image getready, drive, gameover;
	public Label scoreLabel, plusLabel;
	
	public Assets() {
		textureRoad = new Texture(Gdx.files.internal("data/road.png"));
		textureRoad.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		textureRoad.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);

		textureCar = new Texture(Gdx.files.internal("data/car.png"));
		textureCar.setFilter(TextureFilter.Linear, TextureFilter.Linear);
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

		car = new Image(new TextureRegion(textureCar));
		car.setSize(0.4f * BlinkerGame.VIEWPORT_SIZE, 0.4f * BlinkerGame.VIEWPORT_SIZE);
		car.setOrigin(car.getWidth()/2, car.getHeight()/2);
		
		getready = newScreenImageActor(textureGetReady, BlinkerGame.VIEWPORT_SIZE);
		drive = newScreenImageActor(textureDrive, BlinkerGame.VIEWPORT_SIZE);
		gameover = newScreenImageActor(textureGameOver, BlinkerGame.VIEWPORT_SIZE);
		
		font = new BitmapFont(Gdx.files.internal("data/scorefont.fnt")); // size 90
		
		buttonLeftOff = new Texture(Gdx.files.internal("data/buttonleft_off.png"));
		buttonLeftOn = new Texture(Gdx.files.internal("data/buttonleft_on.png"));
		buttonRightOff = new Texture(Gdx.files.internal("data/buttonright_off.png"));
		buttonRightOn = new Texture(Gdx.files.internal("data/buttonright_on.png"));

		buttonLeft = new Button(new TextureRegionDrawable(new TextureRegion(buttonLeftOff)), new TextureRegionDrawable(new TextureRegion(buttonLeftOn)), new TextureRegionDrawable(new TextureRegion(buttonLeftOn)));
		buttonLeft.setSize(BlinkerGame.BUTTON_SIZE, BlinkerGame.BUTTON_SIZE);
		
		buttonRight = new Button(new TextureRegionDrawable(new TextureRegion(buttonRightOff)), new TextureRegionDrawable(new TextureRegion(buttonRightOn)), new TextureRegionDrawable(new TextureRegion(buttonRightOn)));
		buttonRight.setSize(BlinkerGame.BUTTON_SIZE, BlinkerGame.BUTTON_SIZE);
		
		scoreLabel = new Label(" ", new Label.LabelStyle(font, Color.YELLOW));
		plusLabel = new Label("+1", new Label.LabelStyle(font, Color.YELLOW));
		makeAlphaInvisible(plusLabel);
	}
	
	public void dispose() {
		textureRoad.dispose();
		textureCar.dispose();
		textureGetReady.dispose();
		textureDrive.dispose();
		textureGameOver.dispose();
		texturePlusOne.dispose();
		buttonLeftOff.dispose();
		buttonLeftOn.dispose();
		buttonRightOff.dispose();
		buttonRightOn.dispose();
		font.dispose();
	}
	
	protected Image newScreenImageActor(Texture texture, float size) {
		Image img = new Image(new TextureRegion(texture));
		img.setSize(size, size);
		makeAlphaInvisible(img);
		img.setTouchable(Touchable.disabled);
		return img;
	}
	
	protected static void makeAlphaInvisible(Widget widget) {
		Color c = widget.getColor();
		widget.setColor(c.r, c.g, c.b, 0f); // invisible alpha
	}
	

}

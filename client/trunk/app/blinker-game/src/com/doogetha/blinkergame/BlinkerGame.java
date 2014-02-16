package com.doogetha.blinkergame;

import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class BlinkerGame implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture textureRoad, textureCar;
	private Texture buttonLeftOff,buttonLeftOn,buttonRightOff,buttonRightOn;
	private Button buttonLeft, buttonRight;
	private Sprite road, car;
	private Stage stage;
	
	protected final static Random random = new Random();
	
	protected final static int VIRTUAL_TILE_SIZE = 200;
	protected final static int VIEWPORT_SIZE = VIRTUAL_TILE_SIZE / 2;
	private int direction = 0;
	private int nextDirection = -1;
	
	float centerX, centerY;
	float x = 0, y = VIRTUAL_TILE_SIZE / 2;
	long firstRender = 0;
	long lastRender = 0;
	
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		if (h > w)
			camera = new OrthographicCamera(VIEWPORT_SIZE, VIEWPORT_SIZE*h/w);
		else
			camera = new OrthographicCamera(VIEWPORT_SIZE*w/h, VIEWPORT_SIZE);
		batch = new SpriteBatch();
		
		textureRoad = new Texture(Gdx.files.internal("data/road.png"));
		textureRoad.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		textureRoad.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);

		textureCar = new Texture(Gdx.files.internal("data/car.png"));
		textureCar.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		road = new Sprite(new TextureRegion(textureRoad, 0, 0, textureRoad.getWidth() * 3, textureRoad.getHeight() * 3));
		road.setSize(3f * VIRTUAL_TILE_SIZE, 3f * VIRTUAL_TILE_SIZE);
		road.setOrigin(-(centerX = -road.getWidth()/2), -(centerY = -road.getHeight()/2));
		road.setPosition(centerX + x, centerY + y);

		car = new Sprite(new TextureRegion(textureCar));
		car.setSize(0.4f * VIEWPORT_SIZE, 0.4f * VIEWPORT_SIZE);
		car.setOrigin(car.getWidth()/2, car.getHeight()/2);
		car.setPosition(-car.getWidth()/2, -car.getHeight()/2);
		
		// -------------------
		
		stage = new Stage(camera.viewportWidth, camera.viewportHeight);
		Gdx.input.setInputProcessor(stage);
		
		buttonLeftOff = new Texture(Gdx.files.internal("data/buttonleft_off.png"));
		buttonLeftOn = new Texture(Gdx.files.internal("data/buttonleft_on.png"));
		buttonRightOff = new Texture(Gdx.files.internal("data/buttonright_off.png"));
		buttonRightOn = new Texture(Gdx.files.internal("data/buttonright_on.png"));

		int buttonSize = 30;
		
		buttonLeft = new Button(new TextureRegionDrawable(new TextureRegion(buttonLeftOff)), new TextureRegionDrawable(new TextureRegion(buttonLeftOn)), new TextureRegionDrawable(new TextureRegion(buttonLeftOn)));
		buttonLeft.setPosition(0, 0);
		buttonLeft.setSize(buttonSize, buttonSize);
		buttonLeft.addListener(new InputListener() {
			@Override
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		        return true;
		    }

			@Override
		    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if (!buttonLeft.isDisabled()) {
					buttonLeft.setDisabled(true);
					buttonRight.setVisible(false);
				}
		    }
		});
		stage.addActor(buttonLeft);
		
		buttonRight = new Button(new TextureRegionDrawable(new TextureRegion(buttonRightOff)), new TextureRegionDrawable(new TextureRegion(buttonRightOn)), new TextureRegionDrawable(new TextureRegion(buttonRightOn)));
		buttonRight.setPosition(camera.viewportWidth - buttonSize, 0);
		buttonRight.setSize(buttonSize, buttonSize);
		buttonRight.addListener(new InputListener() {
			@Override
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		        return true;
		    }

			@Override
		    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if (!buttonRight.isDisabled()) {
					buttonLeft.setVisible(false);
					buttonRight.setDisabled(true);
				}
		    }
		});
		stage.addActor(buttonRight);
	}

	@Override
	public void dispose() {
		batch.dispose();
		textureRoad.dispose();
		textureCar.dispose();
		buttonLeftOff.dispose();
		buttonLeftOn.dispose();
		buttonRightOff.dispose();
		buttonRightOn.dispose();
		stage.dispose();
	}

	protected void moveScreen(float offset) {
		switch (direction) {
		case 0: // up
			y -= offset;
			break;
		case 1: // right
			x -= offset;
			break;
		case 2: // down
			y += offset;
			break;
		case 3: // left
			x += offset;
			break;
		}

		road.setX(centerX + x);
		road.setY(centerY + y);

		float combinedOffset = Math.abs(x+y);
		if (combinedOffset >= 0.9f * VIRTUAL_TILE_SIZE && nextDirection == -1) {
			// choose next direction
//			int turn = random.nextInt(3); // 0=left, 1=ahead, 2=right
			int turn = buttonLeft.isChecked() ? 0 : buttonRight.isChecked() ? 2 : 1;
			nextDirection = (direction + turn + 3) % 4;

			buttonLeft.setDisabled(false);
			buttonRight.setDisabled(false);
			buttonLeft.setVisible(true);
			buttonRight.setVisible(true);
			buttonLeft.setChecked(false);
			buttonRight.setChecked(false);
		}
		if (combinedOffset >= VIRTUAL_TILE_SIZE) {
			x = 0; y = 0;
			direction = nextDirection;
			nextDirection = -1;
		}
		
		car.setRotation(-direction * 90);

		if (nextDirection >= 0) {
			int turnDir = nextDirection - direction;
			if (turnDir == -3) turnDir = 1;
			else if (turnDir == 3) turnDir = -1;
			float crossRoadsOffset = VIRTUAL_TILE_SIZE - combinedOffset;
			car.rotate(-turnDir * (1.0f - crossRoadsOffset / 0.1f / VIRTUAL_TILE_SIZE) * 90f);
		}
	}
	
	@Override
	public void render() {
		if (firstRender == 0) firstRender = System.currentTimeMillis();
		long now = System.currentTimeMillis();

		float speed = 0.2f * Math.min(1000f, Math.max(0f,  now - firstRender - 500)) / 1000;
		
		if (lastRender > 0) {
			int elapsed = Math.min((int)(now - lastRender), 200);
			float offset = (float)elapsed * speed;
			moveScreen(offset);
		}
		lastRender = now;

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		road.draw(batch);
		car.draw(batch);
		batch.end();
		
		stage.act();
		stage.draw(); // stage has its own sprite batch
	}

	@Override
	public void resize(int width, int height) {
		lastRender = 0;
		dispose();
		create();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}

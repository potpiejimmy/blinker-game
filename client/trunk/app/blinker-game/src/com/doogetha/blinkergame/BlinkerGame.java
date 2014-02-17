package com.doogetha.blinkergame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
	private Texture textureGetReady, textureDrive;
	private Button buttonLeft, buttonRight;
	private Sprite road, car, getready, drive;
	private Stage stage;
	
	protected static enum GameState {
		memorize,
		drive
	}
	
	protected final static Random random = new Random();
	
	private GameState gameState = GameState.memorize; 
	
	protected final static int VIRTUAL_TILE_SIZE = 200;
	protected final static int VIEWPORT_SIZE = VIRTUAL_TILE_SIZE / 2;
	protected final static int BUTTON_SIZE = 30;
	protected final static int START_DELAY = 2000;
	
	private int direction = 0;
	private int nextDirection = -1;
	
	private List<Integer> directionHistory = new ArrayList<Integer>();
	int historyIndex = 0;
	long stopCarTime = 0;
	boolean gameOver = false;
	
	float centerX, centerY;
	float x, y;
	long firstRender = 0;
	long lastRender = 0;
	
	protected class DirectionButtonListener extends InputListener {
		
		private Button me, other;
		
		public DirectionButtonListener(Button me, Button other) {
			this.me = me;
			this.other = other;
		}
		
		@Override
	    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
			if (!me.isDisabled()) {
				me.setDisabled(true);
				me.setChecked(true);
				me.setPosition((camera.viewportWidth - BUTTON_SIZE)/2, 0);
				other.setVisible(false);
			}
			return true;
	    }

		@Override
	    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
			me.setDisabled(false);
	    }
	}
	
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		constructCamera(w, h);
		
		batch = new SpriteBatch();
		
		textureRoad = new Texture(Gdx.files.internal("data/road.png"));
		textureRoad.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		textureRoad.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);

		textureCar = new Texture(Gdx.files.internal("data/car.png"));
		textureGetReady = new Texture(Gdx.files.internal("data/getready.png"));
		textureDrive = new Texture(Gdx.files.internal("data/driveroute.png"));
		
		road = new Sprite(new TextureRegion(textureRoad, 0, 0, textureRoad.getWidth() * 3, textureRoad.getHeight() * 3));
		road.setSize(3f * VIRTUAL_TILE_SIZE, 3f * VIRTUAL_TILE_SIZE);
		road.setOrigin(-(centerX = -road.getWidth()/2), -(centerY = -road.getHeight()/2));
		road.setPosition(centerX + x, centerY + y);

		car = new Sprite(new TextureRegion(textureCar));
		car.setSize(0.4f * VIEWPORT_SIZE, 0.4f * VIEWPORT_SIZE);
		car.setOrigin(car.getWidth()/2, car.getHeight()/2);
		car.setPosition(-car.getWidth()/2, -car.getHeight()/2);
		
		getready = new Sprite(new TextureRegion(textureGetReady));
		getready.setSize(VIEWPORT_SIZE, VIEWPORT_SIZE);
		getready.setOrigin(getready.getWidth()/2, getready.getHeight()/2);
		getready.setPosition(-getready.getWidth()/2, -getready.getHeight()/2 + 10);
		
		drive = new Sprite(new TextureRegion(textureDrive));
		drive.setSize(VIEWPORT_SIZE, VIEWPORT_SIZE);
		drive.setOrigin(drive.getWidth()/2, drive.getHeight()/2);
		drive.setPosition(-drive.getWidth()/2, -drive.getHeight()/2 + 10);
		
		// -------------------

		setStartPosition();
		
		// -------------------
		
		stage = new Stage(camera.viewportWidth, camera.viewportHeight);
		Gdx.input.setInputProcessor(stage);
		
		buttonLeftOff = new Texture(Gdx.files.internal("data/buttonleft_off.png"));
		buttonLeftOn = new Texture(Gdx.files.internal("data/buttonleft_on.png"));
		buttonRightOff = new Texture(Gdx.files.internal("data/buttonright_off.png"));
		buttonRightOn = new Texture(Gdx.files.internal("data/buttonright_on.png"));

		buttonLeft = new Button(new TextureRegionDrawable(new TextureRegion(buttonLeftOff)), new TextureRegionDrawable(new TextureRegion(buttonLeftOn)), new TextureRegionDrawable(new TextureRegion(buttonLeftOn)));
		buttonLeft.setSize(BUTTON_SIZE, BUTTON_SIZE);
		
		buttonRight = new Button(new TextureRegionDrawable(new TextureRegion(buttonRightOff)), new TextureRegionDrawable(new TextureRegion(buttonRightOn)), new TextureRegionDrawable(new TextureRegion(buttonRightOn)));
		buttonRight.setSize(BUTTON_SIZE, BUTTON_SIZE);
		
		buttonLeft.addListener(new DirectionButtonListener(buttonLeft, buttonRight));
		buttonRight.addListener(new DirectionButtonListener(buttonRight, buttonLeft));
		
		stage.addActor(buttonLeft);
		stage.addActor(buttonRight);

		resetDirectionButtons();
	}
	
	protected void setStartPosition() {
		x = 0;
		y = -car.getHeight();
		car.setRotation(0);
		direction = 0;
	}
	
	protected void constructCamera(float w, float h) {
		if (h > w)
			camera = new OrthographicCamera(VIEWPORT_SIZE, VIEWPORT_SIZE*h/w);
		else
			camera = new OrthographicCamera(VIEWPORT_SIZE*w/h, VIEWPORT_SIZE);
	}
	
	protected void resetDirectionButtons() {
		buttonLeft.setPosition(0, 0);
		buttonRight.setPosition(camera.viewportWidth - BUTTON_SIZE, 0);
		buttonLeft.setVisible(true);
		buttonRight.setVisible(true);
		buttonLeft.setChecked(false);
		buttonRight.setChecked(false);
	}

	@Override
	public void dispose() {
		batch.dispose();
		textureRoad.dispose();
		textureCar.dispose();
		textureGetReady.dispose();
		textureDrive.dispose();
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
			int turn;
			if (gameState.equals(GameState.memorize)) {
				if (historyIndex < directionHistory.size())
					turn = directionHistory.get(historyIndex++);
				else {
				    turn = directionHistory.size() > 0 ? random.nextInt(3) : random.nextInt(2) * 2;
					directionHistory.add(turn); // 0=left, 1=ahead, 2=right
					stopCarTime = System.currentTimeMillis();
				}
			} else {
				turn = buttonLeft.isChecked() ? 0 : buttonRight.isChecked() ? 2 : 1;
				if (turn != directionHistory.get(historyIndex)) {
					gameOver = true;
					stopCarTime = System.currentTimeMillis();
				} else {
					// good, go on
					historyIndex++;
					if (historyIndex == directionHistory.size()) {
						// end reached
						stopCarTime = System.currentTimeMillis();
					}
				}
			}
			nextDirection = (direction + turn + 3) % 4;
			resetDirectionButtons();
		}
		if (combinedOffset >= VIRTUAL_TILE_SIZE) {
			if (nextDirection == direction) {
				x %= VIRTUAL_TILE_SIZE; y %= VIRTUAL_TILE_SIZE; // smoothly scroll ahead
			} else {
				x = 0; y = 0;
			}
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
	
	protected void enterNewState(GameState newState) {
		gameState = newState;
		firstRender = 0;
		stopCarTime = 0;
		historyIndex = 0;
		setStartPosition();
	}
	
	protected void checkEnterNewState() {
		// enter new phase:
		switch (gameState) {
			case memorize:
				enterNewState(GameState.drive);
				break;
			case drive:
				if (!gameOver) {
					enterNewState(GameState.memorize);
				}
				break;
		}
	}
	
	@Override
	public void render() {
		if (firstRender == 0) firstRender = System.currentTimeMillis();
		long now = System.currentTimeMillis();
		int appTime = (int)(now - firstRender);

		float speed = 0.2f * Math.min(1000f, Math.max(0f,  appTime - START_DELAY)) / 1000;
		if (stopCarTime > 0) {
			speed = 0.2f * Math.max(0f,  1000 - (now - stopCarTime)) / 1000;
			if (speed < 0.01f) checkEnterNewState();
		}
		
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
		if (appTime > 500 && appTime < START_DELAY) drawGetReady(appTime);
		batch.end();
		
		if (gameState.equals(GameState.drive) && !gameOver) {
			stage.act();
			stage.draw(); // stage has its own sprite batch
		}
	}
	
	protected void drawGetReady(int time) {
		Sprite sprite;
		switch (gameState) {
			case memorize: sprite = getready;
				break;
			case drive: sprite = drive;
				break;
			default:
				return;
		}
		if (time > START_DELAY/2) time = START_DELAY - time;
		else time = Math.max(0,  time - 500);
		Color c = sprite.getColor();
		sprite.setColor(c.r, c.g, c.b, Math.min(1.0f, time / 250f));
		sprite.draw(batch);
	}

	@Override
	public void resize(int w, int h) {
		lastRender = 0;

		constructCamera(w, h);
		stage.setViewport(camera.viewportWidth, camera.viewportHeight);
		
		resetDirectionButtons();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}

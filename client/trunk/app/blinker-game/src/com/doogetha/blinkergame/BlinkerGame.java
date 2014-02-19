package com.doogetha.blinkergame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.VisibleAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class BlinkerGame implements ApplicationListener {
	private OrthographicCamera camera;
	private Texture textureRoad, textureCar;
	private Texture buttonLeftOff,buttonLeftOn,buttonRightOff,buttonRightOn;
	private Texture textureGetReady, textureDrive, textureGameOver, texturePlusOne;
	private Button buttonLeft, buttonRight;
	private Image road, car;
	private BitmapFont font;
	private Image getready, drive, gameover;
	private Label scoreLabel, plusLabel;
	private Stage stage;
	
	protected static enum GameState {
		memorize,
		drive
	}
	
	protected final static Random random = new Random();
	
	private GameState gameState = GameState.memorize; 
	
	protected final static int VIRTUAL_TILE_SIZE = 2000;
	protected final static int VIEWPORT_SIZE = VIRTUAL_TILE_SIZE / 2;
	protected final static int BUTTON_SIZE = 300;
	
	protected final static int START_DELAY = 2500;
	protected final static float SPEED_BASE = 1.5f;
	
	private int direction = 0;
	private int nextDirection = -1;
	
	private int score = 0;
	
	private List<Integer> directionHistory = new ArrayList<Integer>();
	int historyIndex = 0;
	long stopCarTime = 0;
	boolean gameOver = false;
	
	float roadCenterX, roadCenterY;
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
		road.setSize(3f * VIRTUAL_TILE_SIZE, 3f * VIRTUAL_TILE_SIZE);

		car = new Image(new TextureRegion(textureCar));
		car.setSize(0.4f * VIEWPORT_SIZE, 0.4f * VIEWPORT_SIZE);
		car.setOrigin(car.getWidth()/2, car.getHeight()/2);
		
		getready = newScreenImageActor(textureGetReady, VIEWPORT_SIZE);
		drive = newScreenImageActor(textureDrive, VIEWPORT_SIZE);
		gameover = newScreenImageActor(textureGameOver, VIEWPORT_SIZE);
		
		font = new BitmapFont(Gdx.files.internal("data/scorefont.fnt")); // size 90
		
		// -------------------

		directionHistory.add(random.nextInt(2) * 2); // left or right only
		directionHistory.add(random.nextInt(2) * 2); // left or right only
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
		
		scoreLabel = new Label(" ", new Label.LabelStyle(font, Color.YELLOW));
		plusLabel = new Label("+1", new Label.LabelStyle(font, Color.YELLOW));
		makeAlphaInvisible(plusLabel);
		
		setFixedScreenPositions();
		
		stage.addActor(road);
		stage.addActor(car);
		stage.addActor(buttonLeft);
		stage.addActor(buttonRight);
		stage.addActor(scoreLabel);
		stage.addActor(plusLabel);
		stage.addActor(getready);
		stage.addActor(drive);
		stage.addActor(gameover);

		resetDirectionButtons();
	}
	
	protected void setFixedScreenPositions() {
		roadCenterX = -(road.getWidth() - camera.viewportWidth)/2;
		roadCenterY = -(road.getHeight() - camera.viewportHeight)/2;
		road.setPosition(roadCenterX + x, roadCenterY + y);
		car.setPosition((camera.viewportWidth-car.getWidth())/2, (camera.viewportHeight-car.getHeight())/2);
		scoreLabel.setPosition(50, camera.viewportHeight - 150);
		centerOnScreen(getready);
		centerOnScreen(drive);
		centerOnScreen(gameover);
		centerOnScreen(plusLabel);
	}
	
	protected void centerOnScreen(Widget widget) {
		widget.setPosition((camera.viewportWidth-widget.getWidth())/2, (camera.viewportHeight-widget.getHeight())/2);
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
		setDirectionButtonsVisible(true);
		buttonLeft.setChecked(false);
		buttonRight.setChecked(false);
	}

	protected void setDirectionButtonsVisible(boolean visible) {
		buttonLeft.setVisible(visible);
		buttonRight.setVisible(visible);
	}
	
	@Override
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
		stage.dispose();
		font.dispose();
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

		road.setX(roadCenterX + x);
		road.setY(roadCenterY + y);

		float combinedOffset = Math.abs(x+y);
		if (combinedOffset >= 0.9f * VIRTUAL_TILE_SIZE && nextDirection == -1) {
			// choose next direction
			int turn;
			if (gameState.equals(GameState.memorize)) {
				if (historyIndex < directionHistory.size())
					turn = directionHistory.get(historyIndex++);
				else {
				    do { turn = random.nextInt(3); } // 0=left, 1=ahead, 2=right
				    while ( turn == directionHistory.get(historyIndex-1) &&
				    		turn == directionHistory.get(historyIndex-2));
					directionHistory.add(turn);
					endOfRound();
				}
			} else {
				turn = buttonLeft.isChecked() ? 0 : buttonRight.isChecked() ? 2 : 1;
				if (turn != directionHistory.get(historyIndex)) {
					gameOver();
				} else {
					// good, go on
					goodTurn();
					historyIndex++;
					if (historyIndex == directionHistory.size()) {
						// end reached
						endOfRound();
						setDirectionButtonsVisible(false);
					} else {
						resetDirectionButtons();
					}
				}
			}
			nextDirection = (direction + turn + 3) % 4;
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

	protected void endOfRound() {
		stopCarTime = System.currentTimeMillis();
		fadeVisibility(stage.getRoot(), 1.5f, 0.25f, false);
	}
	
	protected void goodTurn() {
		score += historyIndex + 1;
		scoreLabel.setText(""+score);
		plusLabel.setText("+"+(historyIndex+1));
		plusLabel.needsLayout();
		plusLabel.setPosition((camera.viewportWidth-plusLabel.getPrefWidth())/2, (camera.viewportHeight-plusLabel.getPrefHeight())/2);
		plusLabel.addAction(newMoveByAction(0.6f, 0f, 200));
		plusLabel.addAction(new SequenceAction(newFadeAction(0.1f, 1f), newDelayAction(0.25f, newFadeAction(0.25f, 0f))));
	}
	
	protected void gameOver() {
		gameOver = true;
		stopCarTime = System.currentTimeMillis();
		setDirectionButtonsVisible(false);
		gameover.addAction(newFadeAction(1f, 1f));
	}
	
	protected void enterNewState(GameState newState) {
		gameState = newState;
		firstRender = 0;
		stopCarTime = 0;
		historyIndex = 0;
		setStartPosition();
		resetDirectionButtons();
		setDirectionButtonsVisible(newState.equals(GameState.drive));
		fadeVisibility(stage.getRoot(), 0f, 0.5f, true);
	}
	
	protected static void fadeVisibility(Actor actor, float delay, float duration, boolean fadeIn) {
		actor.addAction(fadeIn ?
			new SequenceAction(
				newVisibleAction(true),
				newDelayAction(delay, newFadeAction(duration, 1f))) :
			new SequenceAction(
				newDelayAction(delay, newFadeAction(duration, 0f)),
				newVisibleAction(false)));
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
	    Gdx.gl.glClearColor(0, 0, 0f, 1);
	    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

	    if (firstRender == 0) {
			firstRender = System.currentTimeMillis();
			setupGetReady();
		}
		long now = System.currentTimeMillis();
		int appTime = (int)(now - firstRender);

		float maxSpeed = SPEED_BASE + SPEED_BASE * Math.min(1f, directionHistory.size() / 7f);
		float speed = maxSpeed * Math.min(1f, Math.max(0f,  appTime - START_DELAY) / 1000f);
		if (stopCarTime > 0) {
			speed = maxSpeed * Math.max(0f,  750 - (now - stopCarTime)) / 750;
			if (!stage.getRoot().isVisible()) checkEnterNewState();
		}
		
		if (lastRender > 0) {
			int elapsed = Math.min((int)(now - lastRender), 200);
			//System.out.println(1000f/elapsed + "fps");
			float offset = (float)elapsed * speed;
			moveScreen(offset);
		}
		lastRender = now;

		stage.getSpriteBatch().setProjectionMatrix(camera.combined);
		stage.act();
		stage.draw(); // stage has its own sprite batch
	}
	
	protected void setupGetReady() {
		Image image;
		switch (gameState) {
			case memorize: image = getready;
				break;
			case drive: image = drive;
				break;
			default:
				return;
		}
		image.addAction(new SequenceAction(
				newFadeAction(0.25f, 1f),
				newDelayAction(2f, newFadeAction(0.25f, 0f))));
		setDirectionButtonsVisible(gameState.equals(GameState.drive));
	}
	
	protected static DelayAction newDelayAction(float delay, Action action) {
		DelayAction da = new DelayAction(delay);
		da.setAction(action);
		return da;
	}

	protected static AlphaAction newFadeAction(float duration, float alpha) {
		AlphaAction fadeAction = new AlphaAction();
		fadeAction.setDuration(duration);
		fadeAction.setAlpha(alpha);
		return fadeAction;
	}
	
	protected static MoveByAction newMoveByAction(float duration, float x, float y) {
		MoveByAction mba = new MoveByAction();
		mba.setDuration(duration);
		mba.setAmount(x, y);
		return mba;
	}
	
	protected static VisibleAction newVisibleAction(boolean visible) {
		VisibleAction va = new VisibleAction();
		va.setVisible(visible);
		return va;
	}
	
	@Override
	public void resize(int w, int h) {
		lastRender = 0;

		constructCamera(w, h);
		stage.setViewport(camera.viewportWidth, camera.viewportHeight);
		
		setFixedScreenPositions();
		resetDirectionButtons();
		
		if (gameOver || !gameState.equals(GameState.drive)) {
			setDirectionButtonsVisible(false);
		}
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
		lastRender = System.currentTimeMillis();
	}
}

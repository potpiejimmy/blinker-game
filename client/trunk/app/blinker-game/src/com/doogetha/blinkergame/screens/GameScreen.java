package com.doogetha.blinkergame.screens;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.doogetha.blinkergame.BlinkerGame;
import com.doogetha.blinkergame.Utils;

public class GameScreen extends AbstractScreen {
	
	protected static enum GameState {
		memorize,
		drive
	}
	
	protected final static Random random = new Random();
	
	private GameState gameState = GameState.memorize; 
	
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
				me.setPosition((camera.viewportWidth - BlinkerGame.BUTTON_SIZE)/2, 0);
				other.setVisible(false);
			}
			return true;
	    }

		@Override
	    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
			me.setDisabled(false);
	    }
	}
	
	public GameScreen(BlinkerGame game) {
		super(game);
		
		app.assets.buttonLeft.addListener(new DirectionButtonListener(app.assets.buttonLeft, app.assets.buttonRight));
		app.assets.buttonRight.addListener(new DirectionButtonListener(app.assets.buttonRight, app.assets.buttonLeft));
		app.assets.restartButton.addListener(new InputListener() {
			@Override
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				Utils.fadeVisibility(app.assets.road, 0f, 0.5f, false);
				Utils.fadeVisibility(stage.getRoot(), 0f, 0.5f, false);
				return false;
			}
		});
		
		setFixedScreenPositions();
		
		stage.addActor(app.assets.road);
		stage.addActor(app.assets.car);
		stage.addActor(app.assets.buttonLeft);
		stage.addActor(app.assets.buttonRight);
		stage.addActor(app.assets.scoreLabel);
		stage.addActor(app.assets.betLabel);
		stage.addActor(app.assets.plusLabel);
		stage.addActor(app.assets.getready);
		stage.addActor(app.assets.drive);
		stage.addActor(app.assets.gameover);
		stage.addActor(app.assets.restartButton);
	}
	
	protected void setFixedScreenPositions() {
		roadCenterX = -(app.assets.road.getWidth() - camera.viewportWidth)/2;
		roadCenterY = -(app.assets.road.getHeight() - camera.viewportHeight)/2;
		app.assets.road.setPosition(roadCenterX + x, roadCenterY + y);
		app.assets.car.setPosition((camera.viewportWidth-app.assets.car.getWidth())/2, (camera.viewportHeight-app.assets.car.getHeight())/2);
		app.assets.scoreLabel.setPosition(50, camera.viewportHeight - 150);
		app.assets.betLabel.setPosition(50, camera.viewportHeight - 250);
		centerOnScreen(app.assets.getready);
		centerOnScreen(app.assets.drive);
		centerOnScreen(app.assets.gameover);
		centerOnScreen(app.assets.restartButton);
		centerOnScreen(app.assets.plusLabel);
	}
	
	protected void centerOnScreen(Actor actor) {
		actor.setPosition((camera.viewportWidth-actor.getWidth())/2, (camera.viewportHeight-actor.getHeight())/2);
	}
	
	protected void setStartPosition() {
		x = 0;
		y = -app.assets.car.getHeight();
		app.assets.car.setRotation(0);
		direction = 0;
	}
	
	protected void resetDirectionButtons() {
		app.assets.buttonLeft.setPosition(0, 0);
		app.assets.buttonRight.setPosition(camera.viewportWidth - BlinkerGame.BUTTON_SIZE, 0);
		setDirectionButtonsVisible(true);
		app.assets.buttonLeft.setChecked(false);
		app.assets.buttonRight.setChecked(false);
	}

	protected void setDirectionButtonsVisible(boolean visible) {
		app.assets.buttonLeft.setVisible(visible);
		app.assets.buttonRight.setVisible(visible);
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

		app.assets.road.setX(roadCenterX + x);
		app.assets.road.setY(roadCenterY + y);

		float combinedOffset = Math.abs(x+y);
		if (combinedOffset >= 0.9f * BlinkerGame.VIRTUAL_TILE_SIZE && nextDirection == -1) {
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
				turn = app.assets.buttonLeft.isChecked() ? 0 : app.assets.buttonRight.isChecked() ? 2 : 1;
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
		if (combinedOffset >= BlinkerGame.VIRTUAL_TILE_SIZE) {
			if (nextDirection == direction) {
				x %= BlinkerGame.VIRTUAL_TILE_SIZE; y %= BlinkerGame.VIRTUAL_TILE_SIZE; // smoothly scroll ahead
			} else {
				x = 0; y = 0;
			}
			direction = nextDirection;
			nextDirection = -1;
		}
		
		app.assets.car.setRotation(-direction * 90);

		if (nextDirection >= 0) {
			int turnDir = nextDirection - direction;
			if (Math.abs(turnDir)==3) turnDir /= -3;
			float crossRoadsOffset = BlinkerGame.VIRTUAL_TILE_SIZE - combinedOffset;
			app.assets.car.rotate(-turnDir * (1.0f - crossRoadsOffset / 0.1f / BlinkerGame.VIRTUAL_TILE_SIZE) * 90f);
		}
	}

	protected void endOfRound() {
		stopCarTime = System.currentTimeMillis();
		Utils.fadeVisibility(app.assets.road, 1.5f, 0.25f, false);
	}
	
	protected void fadeBetLabel(boolean up) {
		Utils.fadeVisibility(app.assets.betLabel, 0f, 0.5f, false);
		app.assets.betLabel.addAction(Utils.newMoveByAction(0.5f, 0, up?90:-90));
	}
	
	protected void updateScore(int newScore) {
		int bet = app.getBet();
		if (score < bet && newScore >= bet) {
			newScore += bet;
			fadeBetLabel(true);
		}
		score = newScore;
		app.assets.scoreLabel.setText(""+score);
	}
	
	protected void goodTurn() {
		app.assets.soundCoin.play();
		updateScore(score + historyIndex+1);
		app.assets.plusLabel.setText("+"+(historyIndex+1));
		app.assets.plusLabel.needsLayout();
		app.assets.plusLabel.setPosition((camera.viewportWidth-app.assets.plusLabel.getPrefWidth())/2, (camera.viewportHeight-app.assets.plusLabel.getPrefHeight())/2);
		app.assets.plusLabel.addAction(Utils.newMoveByAction(0.6f, 0f, 200));
		app.assets.plusLabel.addAction(new SequenceAction(Utils.newFadeAction(0.1f, 1f), Utils.newDelayAction(0.25f, Utils.newFadeAction(0.25f, 0f))));
	}
	
	protected void gameOver() {
		gameOver = true;
		stopCarTime = System.currentTimeMillis();
		setDirectionButtonsVisible(false);
		app.assets.gameover.addAction(Utils.newFadeAction(1f, 1f));
		app.assets.restartButton.addAction(Utils.newDelayAction(2f, Utils.newVisibleAction(true)));
		if (score < app.getBet()) {
			updateScore(0);
			fadeBetLabel(false);
		}
	}
	
	protected void enterNewState(GameState newState) {
		gameState = newState;
		firstRender = 0;
		stopCarTime = 0;
		historyIndex = 0;
		setStartPosition();
		resetDirectionButtons();
		setDirectionButtonsVisible(newState.equals(GameState.drive));
		Utils.fadeVisibility(app.assets.road, 0f, 0.5f, true);
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
				} else {
					app.setScreen(app.startScreen);
				}
				break;
		}
	}
	
	@Override
	public void render(float delta) {
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
			if (!app.assets.road.isVisible()) checkEnterNewState();
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
			case memorize: image = app.assets.getready;
				break;
			case drive: image = app.assets.drive;
				break;
			default:
				return;
		}
		image.addAction(new SequenceAction(
				Utils.newFadeAction(0.25f, 1f),
				Utils.newDelayAction(2f, Utils.newFadeAction(0.25f, 0f))));
		setDirectionButtonsVisible(gameState.equals(GameState.drive));
	}
	
	@Override
	public void resize(int w, int h) {
		super.resize(w, h);
		
		lastRender = 0;

		setFixedScreenPositions();
		resetDirectionButtons();
		
		if (gameOver || !gameState.equals(GameState.drive)) {
			setDirectionButtonsVisible(false);
		}
	}

	@Override
	public void show() {
		super.show();
		resetDirectionButtons();
		setDirectionButtonsVisible(false);
		gameOver = false;
		Utils.makeAlphaInvisible(app.assets.gameover);
		app.assets.restartButton.setVisible(false);
		updateScore(0);
		app.assets.betLabel.setText(app.getBet()>0 ? "+"+app.getBet() : "");
		Utils.fadeVisibility(app.assets.betLabel, 0f, 0f, true);
		lastRender = 0;
		directionHistory.clear();
		directionHistory.add(random.nextInt(2) * 2); // left or right only
		directionHistory.add(random.nextInt(2) * 2); // left or right only
		setStartPosition();
		Utils.fadeVisibility(stage.getRoot(), 0f, 0f, true);
	}
	
	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
		lastRender = System.currentTimeMillis();
	}
}

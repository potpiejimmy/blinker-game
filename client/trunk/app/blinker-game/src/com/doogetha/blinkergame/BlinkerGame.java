package com.doogetha.blinkergame;

import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BlinkerGame implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture textureRoad, textureCar;
	private Sprite road, car;
	
	protected final static Random random = new Random();
	
	protected final static int VIRTUAL_TILE_SIZE = 200;
	protected final static int VIEWPORT_SIZE = VIRTUAL_TILE_SIZE / 2;
	private int direction = 0;
	private int nextDirection = -1;
	
	float centerX, centerY;
	float x, y;
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
		
		// start position:
		x = 0;
		y = -VIRTUAL_TILE_SIZE / 2; 
		
		road = new Sprite(new TextureRegion(textureRoad, 0, 0, textureRoad.getWidth() * 3, textureRoad.getHeight() * 3));
		road.setSize(3f * VIRTUAL_TILE_SIZE, 3f * VIRTUAL_TILE_SIZE);
		road.setOrigin(-(centerX = -road.getWidth()/2), -(centerY = -road.getHeight()/2));
		road.setPosition(centerX + x, centerY + y);

		car = new Sprite(new TextureRegion(textureCar));
		car.setSize(0.4f * VIEWPORT_SIZE, 0.4f * VIEWPORT_SIZE);
		car.setOrigin(car.getWidth()/2, car.getHeight()/2);
		car.setPosition(-car.getWidth()/2, -car.getHeight()/2);
	}

	@Override
	public void dispose() {
		batch.dispose();
		textureRoad.dispose();
		textureCar.dispose();
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
			int turn = random.nextInt(3); // 0=left, 1=ahead, 2=right
			nextDirection = (direction + turn + 3) % 4;
		}
		if (combinedOffset >= VIRTUAL_TILE_SIZE) {
			x = 0;
			y = 0;
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
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		float speed = 0.3f;
		
		long now = System.currentTimeMillis();
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
	}

	@Override
	public void resize(int width, int height) {
		create();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}

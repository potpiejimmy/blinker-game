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
	
	protected final static int VIEWPORT_PIXELS = 100;
	private int direction = 0;
	
	float x,y = 0;
	long lastRender = 0;
	
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		if (h > w)
			camera = new OrthographicCamera(VIEWPORT_PIXELS, VIEWPORT_PIXELS*h/w);
		else
			camera = new OrthographicCamera(VIEWPORT_PIXELS*w/h, VIEWPORT_PIXELS);
		batch = new SpriteBatch();
		
		textureRoad = new Texture(Gdx.files.internal("data/road.png"));
		textureRoad.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		textureRoad.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);

		textureCar = new Texture(Gdx.files.internal("data/car.png"));
		textureCar.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		road = new Sprite(new TextureRegion(textureRoad, 0, 0, 6144, 6144));
		road.setSize(6f * VIEWPORT_PIXELS, 6f * VIEWPORT_PIXELS);
		road.setOrigin(road.getWidth()/2, road.getHeight()/2);
		road.setPosition(x = -road.getWidth()/2, y = -road.getHeight()/2 - 50);

		car = new Sprite(new TextureRegion(textureCar, 0, 0, 512, 512));
		car.setSize(40, 40);
		car.setOrigin(car.getWidth()/2, car.getHeight()/2);
		car.setPosition(x = -car.getWidth()/2, y = -car.getHeight()/2);
	}

	@Override
	public void dispose() {
		batch.dispose();
		textureRoad.dispose();
		textureCar.dispose();
	}

	protected void moveScreen(float offset) {
		switch (direction) {
		case 0:
			y -= offset;
			break;
		case 1:
			x -= offset;
			break;
		case 2:
			y += offset;
			break;
		case 3:
			x += offset;
			break;
		}

		road.setX(x);
		road.setY(y);

		if (y <= -road.getHeight()/2 - 6f/3f*VIEWPORT_PIXELS ||
		    x <= -road.getWidth()/2 - 6f/3f*VIEWPORT_PIXELS ||
		    y >= -road.getHeight()/2 + 6f/3f*VIEWPORT_PIXELS ||
		    x >= -road.getWidth()/2 + 6f/3f*VIEWPORT_PIXELS) {
				x = -road.getWidth()/2;
				y = -road.getHeight()/2;
				int turn = random.nextInt(3);
				direction = (direction + turn + 3) % 4;
				if (turn == 2) car.rotate90(true);
				else if (turn == 0) car.rotate90(false);
		}
	}
	
	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		float speed = 0.2f;
		
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

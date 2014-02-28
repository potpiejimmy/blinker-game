package com.doogetha.blinkergame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "blinker-game";
		cfg.useGL20 = false;
		cfg.width = 360;
		cfg.height = 600;
		
		new LwjglApplication(new BlinkerGame(), cfg);
	}
}

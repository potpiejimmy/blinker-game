package com.doogetha.blinkergame;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class AnimatedImage extends Image {
	
	protected Animation animation = null;
	protected TextureRegion defaultRegion = null;
	private float stateTime = 0;
	
	public AnimatedImage(Animation animation) {
		super(animation.getKeyFrame(0));
		this.animation = animation;
	}
	
	public AnimatedImage(Animation animation, TextureRegion defaultRegion) {
		super(defaultRegion);
		this.defaultRegion = defaultRegion;
		this.animation = animation;
	}
	
	public void setAnimation(Animation animation) {
		this.animation = animation;
	}
	
	public void reset() {
		stateTime = 0;
	}
	
	@Override
	public void act(float delta)
	{
		((TextureRegionDrawable)getDrawable()).setRegion(animation!=null ? animation.getKeyFrame(stateTime+=delta, true) : defaultRegion);
	    super.act(delta);
	}
}

package com.doogetha.blinkergame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.VisibleAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Utils {
	
	public static void fadeVisibility(Actor actor, float delay, float duration, boolean fadeIn) {
		actor.addAction(fadeIn ?
			new SequenceAction(
				newVisibleAction(true),
				newDelayAction(delay, newFadeAction(duration, 1f))) :
			new SequenceAction(
				newDelayAction(delay, newFadeAction(duration, 0f)),
				newVisibleAction(false)));
	}
	
	public static DelayAction newDelayAction(float delay, Action action) {
		DelayAction da = new DelayAction(delay);
		da.setAction(action);
		return da;
	}

	public static AlphaAction newFadeAction(float duration, float alpha) {
		AlphaAction fadeAction = new AlphaAction();
		fadeAction.setDuration(duration);
		fadeAction.setAlpha(alpha);
		return fadeAction;
	}
	
	public static MoveByAction newMoveByAction(float duration, float x, float y) {
		MoveByAction mba = new MoveByAction();
		mba.setDuration(duration);
		mba.setAmount(x, y);
		return mba;
	}
	
	public static VisibleAction newVisibleAction(boolean visible) {
		VisibleAction va = new VisibleAction();
		va.setVisible(visible);
		return va;
	}
	
	public static RunnableAction newRunnableAction(Runnable runnable) {
		RunnableAction ra = new RunnableAction();
		ra.setRunnable(runnable);
		return ra;
	}
	
	public static void makeAlphaInvisible(Actor widget) {
		Color c = widget.getColor();
		widget.setColor(c.r, c.g, c.b, 0f); // invisible alpha
	}
	
	public static Button newTextButton(BlinkerGame app, String text, final Runnable action) {
		TextButton b = new TextButton(text, new TextButtonStyle(
				new TextureRegionDrawable(new TextureRegion(app.assets.textureButtonUp)),
				new TextureRegionDrawable(new TextureRegion(app.assets.textureButtonDown)),null,
				app.assets.font));
		b.addListener(new InputListener() {
			@Override public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { return true; }
			@Override public void touchUp (InputEvent event, float x, float y, int pointer, int button) { action.run(); }
		});
		return b;
	}
}

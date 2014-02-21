package com.doogetha.blinkergame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.VisibleAction;

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
	
	public static void makeAlphaInvisible(Actor widget) {
		Color c = widget.getColor();
		widget.setColor(c.r, c.g, c.b, 0f); // invisible alpha
	}
}

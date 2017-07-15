package com.doogetha.blinkergame.android;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;

public class GoogleGameActivity extends AndroidApplication implements GameHelper.GameHelperListener {

	public final static String LEADERBOARD_ID_HIGHSCORES = "CgkImfPJsIoJEAIQAQ";
	
	protected GameHelper mHelper;

	protected boolean mDebugLog = false;
	
	private boolean invokingLeaderboards = false;

	public GameHelper getGameHelper() {
		if (mHelper == null) {
			mHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
			mHelper.setMaxAutoSignInAttempts(0); // log on only if high score button pressed
			mHelper.enableDebugLog(mDebugLog);
		}
		return mHelper;
	}

	@Override
	protected void onCreate(Bundle b) {
		super.onCreate(b);
		if (mHelper == null) {
			getGameHelper();
		}
		mHelper.setup(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		mHelper.onStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		mHelper.onStop();
	}

	@Override
	protected void onActivityResult(int request, int response, Intent data) {
		super.onActivityResult(request, response, data);
		mHelper.onActivityResult(request, response, data);
	}

	protected GoogleApiClient getApiClient() {
		return mHelper.getApiClient();
	}

	protected boolean isSignedIn() {
		return mHelper.isSignedIn();
	}

	protected void beginUserInitiatedSignIn() {
		mHelper.beginUserInitiatedSignIn();
	}

	protected void signOut() {
		mHelper.signOut();
	}

	protected void showAlert(String message) {
		mHelper.makeSimpleDialog(message).show();
	}

	protected void showAlert(String title, String message) {
		mHelper.makeSimpleDialog(title, message).show();
	}

	protected void enableDebugLog(boolean enabled) {
		mDebugLog = true;
		if (mHelper != null) {
			mHelper.enableDebugLog(enabled);
		}
	}

	protected String getInvitationId() {
		return mHelper.getInvitationId();
	}

	protected void reconnectClient() {
		mHelper.reconnectClient();
	}

	protected boolean hasSignInError() {
		return mHelper.hasSignInError();
	}

	protected GameHelper.SignInFailureReason getSignInError() {
		return mHelper.getSignInError();
	}
	
	public void invokeLeaderboards() {
		if (!getGameHelper().isSignedIn()) {
			invokingLeaderboards = true;
			getGameHelper().beginUserInitiatedSignIn();
		} else {
			displayHighscoreLeaderboard();
		}
	}
	
	public void submitScore(int score) {
		if (getGameHelper().isSignedIn()) {
			// only submit if signed in
			Games.Leaderboards.submitScore(getApiClient(), LEADERBOARD_ID_HIGHSCORES, score);
		}
	}
	
	protected void displayHighscoreLeaderboard() {
		startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient(), LEADERBOARD_ID_HIGHSCORES), 1);
	}
	
	// -------------------------------------------
	
	@Override
	public void onSignInFailed() {
		invokingLeaderboards = false;
	}

	@Override
	public void onSignInSucceeded() {
		if (invokingLeaderboards) {
			displayHighscoreLeaderboard();
		}
		invokingLeaderboards = false;
	}
}

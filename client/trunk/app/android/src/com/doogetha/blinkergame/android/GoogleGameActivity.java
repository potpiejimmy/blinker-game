package com.doogetha.blinkergame.android;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.example.games.basegameutils.GameHelper;

public class GoogleGameActivity extends AndroidApplication implements GameHelper.GameHelperListener {

	// The game helper object. This class is mainly a wrapper around this object.
	protected GameHelper mHelper;

	protected boolean mDebugLog = false;

	public GameHelper getGameHelper() {
		if (mHelper == null) {
			mHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
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

	@Override
	public void onSignInFailed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSignInSucceeded() {
		// TODO Auto-generated method stub
		
	}
}

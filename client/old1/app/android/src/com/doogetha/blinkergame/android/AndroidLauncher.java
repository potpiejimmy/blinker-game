package com.doogetha.blinkergame.android;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.doogetha.blinkergame.BlinkerGame;
import com.doogetha.blinkergame.NativeApplication;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class AndroidLauncher extends GoogleGameActivity implements NativeApplication {

	private RelativeLayout layout = null;
	private View gameView;
	private AdView adView = null;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		gameView = initializeForView(new BlinkerGame(this), config);

        initView(createLayout());
	}
	
	protected void createAdView() {
        // (Re-)create and setup the AdMob view
		if (adView != null) adView.destroy();
		
        adView = new AdView(this);
        adView.setAdUnitId("ca-app-pub-9069641916384503/7433452077");
        adView.setAdSize(AdSize.SMART_BANNER);
        //adView.setBackgroundColor(Color.BLACK);

        adView.loadAd(new AdRequest.Builder().build());
	}
	
	protected View createLayout() {
        // Create the layout
		layout = new RelativeLayout(this);
		layout.setBackgroundColor(Color.BLACK);

        // Add the libgdx view
        layout.addView(gameView);

		// Add the AdMob view
		addAdView(true);
        
        return layout;
	}

	protected void addAdView(boolean hidden) {
		createAdView(); // must be recreated each time
		
        RelativeLayout.LayoutParams adParams = 
            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        layout.addView(adView, adParams);

        if (hidden) adView.setVisibility(View.GONE);
	}
	
	protected void readdAdView() {
		boolean wasHidden = (adView == null || adView.getVisibility() == View.GONE);
		layout.removeView(adView);
		addAdView(wasHidden);
	}
	
	@Override
	public void onConfigurationChanged (Configuration config) {
		super.onConfigurationChanged(config);
		readdAdView();
	}
	
	protected void initView(View content) {
		// Do the stuff that initialize() would do for you
		try {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		} catch (Exception ex) {
			log("AndroidApplication", "Content already displayed, cannot request FEATURE_NO_TITLE", ex);
		}
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		setContentView(content, createLayoutParams());
	}

	@Override
	public void setBannerAdVisible(final boolean visible) {
		this.runOnUiThread(new Runnable() {
			public void run() {
				adView.setVisibility(visible ? View.VISIBLE : View.GONE);
			}
		});
	}
	
	@Override
	public void invokeLeaderboards() {
		super.invokeLeaderboards();
	}
	
	@Override
	public void submitScore(int score) {
		super.submitScore(score);
	}
}

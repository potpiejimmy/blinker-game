package com.doogetha.blinkergame.android;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.doogetha.blinkergame.BlinkerGame;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class AndroidLauncher extends AndroidApplication {

	private LinearLayout layout = null;
	private View gameView;
	private AdView adView = null;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		gameView = initializeForView(new BlinkerGame(), config);

        initView(createLayout());
	}
	
	protected void createAdView() {
        // (Re-)create and setup the AdMob view
		if (adView != null) adView.destroy();
		
        adView = new AdView(this);
        adView.setAdUnitId("ca-app-pub-9069641916384503/7433452077");
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.loadAd(new AdRequest.Builder().build());
	}
	
	protected View createLayout() {
        // Create the layout
		layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackgroundColor(Color.BLACK);

		// Add the AdMob view
		addAdView();
        
        // Add the libgdx view
        layout.addView(gameView);

        return layout;
	}

	protected void addAdView() {
		createAdView(); // must be recreated each time
        RelativeLayout.LayoutParams adParams = 
            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layout.addView(adView, 0, adParams);
	}
	
	protected void readdAdView() {
		layout.removeViewAt(0);
		addAdView();
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
}

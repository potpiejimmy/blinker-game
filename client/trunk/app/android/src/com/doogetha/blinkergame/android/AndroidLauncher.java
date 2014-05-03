package com.doogetha.blinkergame.android;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.doogetha.blinkergame.BlinkerGame;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		View gameView = initializeForView(new BlinkerGame(), config);

        initView(createLayout(gameView));
	}
	
	protected View createLayout(View gameView) {
        // Create the layout
        RelativeLayout layout = new RelativeLayout(this);
		
        // Create and setup the AdMob view
        AdView adView = new AdView(this);
        adView.setAdUnitId("ca-app-pub-9069641916384503/7433452077");
        adView.setAdSize(AdSize.BANNER);
        adView.loadAd(new AdRequest.Builder()
        .build());
        
        // Add the libgdx view
        layout.addView(gameView);

        // Add the AdMob view
        RelativeLayout.LayoutParams adParams = 
            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        layout.addView(adView, adParams);
        
        return layout;
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

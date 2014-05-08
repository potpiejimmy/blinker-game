package com.doogetha.blinkergame;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSDictionary;
import org.robovm.apple.foundation.NSString;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.bindings.admob.GADAdSizeManager;
import org.robovm.bindings.admob.GADBannerView;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

public class IOSLauncher extends IOSApplication.Delegate {
	private IOSApplication app;
	
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
		config.orientationLandscape = true;
		config.orientationPortrait = true;
        return app = new IOSApplication(new BlinkerGame(), config);
    }

	@Override
	public boolean didFinishLaunching (UIApplication application, NSDictionary<NSString, ?> launchOptions) {
		boolean result = super.didFinishLaunching(application, launchOptions);
		
		GADBannerView bannerView = new GADBannerView(GADAdSizeManager.smartBannerPortrait());
		bannerView.setAdUnitID("ca-app-pub-9069641916384503/7433452077");
	    bannerView.setRootViewController(app.getUIViewController());
	    app.getUIWindow().addSubview(bannerView);
	    
	    bannerView.loadRequest(null);
		
		return result;
	}

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
}
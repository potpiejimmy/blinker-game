package com.doogetha.blinkergame;

import org.robovm.apple.coregraphics.CGPoint;
import org.robovm.apple.coregraphics.CGSize;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSDictionary;
import org.robovm.apple.foundation.NSString;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIDevice;
import org.robovm.apple.uikit.UIInterfaceOrientation;
import org.robovm.apple.uikit.UIScreen;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.bindings.admob.GADAdSizeManager;
import org.robovm.bindings.admob.GADBannerView;
import org.robovm.bindings.admob.GADRequest;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

public class IOSLauncher extends IOSApplication.Delegate {
	
	protected GADBannerView bannerView = null;
	
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
		config.orientationLandscape = true;
		config.orientationPortrait = true;
        return new IOSApplication(new BlinkerGame(), config);
    }

	@Override
	public boolean didFinishLaunching (UIApplication application, NSDictionary<NSString, ?> launchOptions) {
		boolean result = super.didFinishLaunching(application, launchOptions);
		
		UIDevice.getCurrentDevice().beginGeneratingDeviceOrientationNotifications();
		setupAdvertisements(application);
		updateBannerPosition(UIApplication.getSharedApplication().getStatusBarOrientation());
		return result;
	}
	
	protected void setupAdvertisements(UIApplication application) {
	    UIViewController rootViewController = application.getKeyWindow().getRootViewController();
	    UIViewController adsViewController = new AdsViewController();
	    
	    rootViewController.addChildViewController(adsViewController);

        bannerView = new GADBannerView(GADAdSizeManager.smartBannerPortrait());
		bannerView.setAdUnitID("ca-app-pub-9069641916384503/6810683273");
	    bannerView.setRootViewController(adsViewController);
//	    bannerView.setBackgroundColor(new UIColor(0,0,0,1));
	    
//	    UIView libgdxView = app.getUIViewController().getView();
//	    UIView rootView = libgdxView.getSuperview();
//	    
//	    libgdxView.setFrame(new CGRect(0, bannerView.getBounds().getHeight()+ 1, UIScreen.getMainScreen().getBounds().size().width(), UIScreen.getMainScreen().getBounds().size().height() - 1 - bannerView.getBounds().getHeight()));
	    
	    rootViewController.getView().addSubview(bannerView);
	    
//	    application.getKeyWindow().setRootViewController(rootViewController);
//	    application.getKeyWindow().addSubview(rootViewController.getView());
//	    application.getKeyWindow().makeKeyAndVisible();	   
	}
	
	protected void updateBannerPosition(UIInterfaceOrientation orientation) {
		boolean landscape = UIInterfaceOrientation.LandscapeLeft.equals(orientation) || UIInterfaceOrientation.LandscapeRight.equals(orientation);

		bannerView.setAdSize(landscape ? GADAdSizeManager.smartBannerLandscape() : GADAdSizeManager.smartBannerPortrait());
        CGSize AD_SIZE = new CGSize(bannerView.getFrame().getWidth(), bannerView.getFrame().getHeight());
        CGSize DEVICE_SCREEN_SIZE = UIScreen.getMainScreen().getBounds().size();
        CGSize SCREEN_SIZE = landscape ? new CGSize(DEVICE_SCREEN_SIZE.height(), DEVICE_SCREEN_SIZE.width()) :
                                         new CGSize(DEVICE_SCREEN_SIZE.width(), DEVICE_SCREEN_SIZE.height());
        bannerView.setCenter(new CGPoint(SCREEN_SIZE.width()/2,SCREEN_SIZE.height()-AD_SIZE.height()/2));
		System.out.println("updated banner");

		bannerView.loadRequest(GADRequest.request());
	}

	@Override
	public void didBecomeActive (UIApplication application) {

		super.didBecomeActive(application);
	}

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
    
    public class AdsViewController extends UIViewController
    {
    	@Override
    	public void willRotate(UIInterfaceOrientation toInterfaceOrientation, double duration) {   
    		System.out.println("screen orientation changed");
    		updateBannerPosition(toInterfaceOrientation);
    	}
    }
}
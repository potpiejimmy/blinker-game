package com.doogetha.blinkergame;

import java.util.ArrayList;

import org.robovm.apple.coregraphics.CGPoint;
import org.robovm.apple.coregraphics.CGSize;
import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSDictionary;
import org.robovm.apple.foundation.NSError;
import org.robovm.apple.foundation.NSString;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIDevice;
import org.robovm.apple.uikit.UIInterfaceOrientation;
import org.robovm.apple.uikit.UIScreen;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.bindings.admob.GADAdSizeManager;
import org.robovm.bindings.admob.GADBannerView;
import org.robovm.bindings.admob.GADRequest;
import org.robovm.bindings.gpp.GPPSignIn;
import org.robovm.bindings.gpp.GPPSignInDelegate;
import org.robovm.bindings.gt.GTMOAuth2Authentication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

public class IOSLauncher extends IOSApplication.Delegate implements NativeApplication, GPPSignInDelegate {
	
	public final static String GPGS_CLIENT_ID = "312023873945-4ijovu4c1899i8i821ho0fhirklj7cva.apps.googleusercontent.com";
	
	protected GADBannerView bannerView = null;
	
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
		config.orientationLandscape = true;
		config.orientationPortrait = true;
        return new IOSApplication(new BlinkerGame(this), config);
    }

	@Override
	public boolean didFinishLaunching (UIApplication application, NSDictionary<NSString, ?> launchOptions) {
		boolean result = super.didFinishLaunching(application, launchOptions);
		
		setupGoogleSignIn();
		
		UIDevice.getCurrentDevice().beginGeneratingDeviceOrientationNotifications();
		setupAdvertisements(application);
		updateBannerPosition(UIApplication.getSharedApplication().getStatusBarOrientation());
		return result;
	}
	
	protected void setupGoogleSignIn() {
		// set google plus settings
		GPPSignIn signIn = GPPSignIn.sharedInstance();
		signIn.setClientID(GPGS_CLIENT_ID);

		ArrayList<NSString> scopes = new ArrayList<NSString>();
		scopes.add(new NSString("https://www.googleapis.com/auth/games"));
		scopes.add(new NSString("https://www.googleapis.com/auth/appstate"));
		signIn.setScopes(new NSArray<NSString>(scopes));

		signIn.setDelegate(this);
		signIn.setShouldFetchGoogleUserID(true);
		signIn.setShouldFetchGoogleUserEmail(false);
		signIn.setShouldFetchGooglePlusUser(false);

		// try to sign in silently
		//signIn.trySilentAuthentication();
	}
	
	protected void setupAdvertisements(UIApplication application) {
	    UIViewController rootViewController = application.getKeyWindow().getRootViewController();
	    UIViewController adsViewController = new UIViewController();
	    
	    rootViewController.addChildViewController(adsViewController);

        bannerView = new GADBannerView(GADAdSizeManager.smartBannerPortrait());
		bannerView.setAdUnitID("ca-app-pub-9069641916384503/6810683273");
	    bannerView.setRootViewController(adsViewController);
//	    bannerView.setBackgroundColor(new UIColor(0,0,0,1));
	    bannerView.setHidden(true); // hide until setBannderAdVisible is called
	    
	    rootViewController.getView().addSubview(bannerView);
	}
	
	protected void updateBannerPosition(UIInterfaceOrientation orientation) {
		boolean landscape = UIInterfaceOrientation.LandscapeLeft.equals(orientation) || UIInterfaceOrientation.LandscapeRight.equals(orientation);

		bannerView.setAdSize(landscape ? GADAdSizeManager.smartBannerLandscape() : GADAdSizeManager.smartBannerPortrait());
        CGSize AD_SIZE = bannerView.getFrame().size();
        CGSize DEVICE_SCREEN_SIZE = UIScreen.getMainScreen().getBounds().size();
        CGSize SCREEN_SIZE = landscape ? new CGSize(DEVICE_SCREEN_SIZE.height(), DEVICE_SCREEN_SIZE.width()) :
                                         new CGSize(DEVICE_SCREEN_SIZE.width(), DEVICE_SCREEN_SIZE.height());
        bannerView.setCenter(new CGPoint(SCREEN_SIZE.width()/2,SCREEN_SIZE.height()-AD_SIZE.height()/2));

		// reload ad on orientation change
		bannerView.loadRequest(GADRequest.request());
	}

    @Override
    public void willChangeStatusBarOrientation(UIApplication application, UIInterfaceOrientation newStatusBarOrientation, double duration) {
    	updateBannerPosition(newStatusBarOrientation);
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

	@Override
	public void setBannerAdVisible(boolean visible) {
		bannerView.setHidden(!visible);
	}
	
	@Override
	public void invokeLeaderboards() {
		// TODO XXX TESTING
		GPPSignIn.sharedInstance().authenticate();
	}

	@Override
	public void submitScore(int score) {
		// TODO
	}

	@Override
	public void finishedWithAuth(GTMOAuth2Authentication arg0, NSError arg1) {
		// TODO Auto-generated method stub
		
	}
}
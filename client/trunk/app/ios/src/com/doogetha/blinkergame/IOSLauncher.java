package com.doogetha.blinkergame;

import java.util.ArrayList;

import org.robovm.apple.coregraphics.CGPoint;
import org.robovm.apple.coregraphics.CGSize;
import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSError;
import org.robovm.apple.foundation.NSPropertyList;
import org.robovm.apple.foundation.NSString;
import org.robovm.apple.foundation.NSURL;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIApplicationLaunchOptions;
import org.robovm.apple.uikit.UIDevice;
import org.robovm.apple.uikit.UIInterfaceOrientation;
import org.robovm.apple.uikit.UIScreen;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.bindings.admob.GADAdSizeManager;
import org.robovm.bindings.admob.GADBannerView;
import org.robovm.bindings.admob.GADRequest;
import org.robovm.bindings.gpgs.GPGLeaderboardController;
import org.robovm.bindings.gpgs.GPGLeaderboardControllerDelegate;
import org.robovm.bindings.gpgs.GPGManager;
import org.robovm.bindings.gpgs.GPGReAuthenticationBlock;
import org.robovm.bindings.gpp.GPPSignIn;
import org.robovm.bindings.gpp.GPPSignInDelegate;
import org.robovm.bindings.gpp.GPPURLHandler;
import org.robovm.bindings.gt.GTMOAuth2Authentication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

public class IOSLauncher extends IOSApplication.Delegate implements NativeApplication, GPPSignInDelegate, GPGLeaderboardControllerDelegate {
	
	public final static String GPGS_CLIENT_ID = "312023873945-4ijovu4c1899i8i821ho0fhirklj7cva.apps.googleusercontent.com";
	public final static String LEADERBOARD_ID_HIGHSCORES = "CgkImfPJsIoJEAIQAQ";
	
	private boolean signedIn = false;
	private GPGReAuthenticationBlock gamesAuthBlock;

	private boolean invokingLeaderboards = false;
	
	protected GADBannerView bannerView = null;
	
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
		config.orientationLandscape = true;
		config.orientationPortrait = true;
        return new IOSApplication(new BlinkerGame(this), config);
    }

	@Override
	public boolean didFinishLaunching (UIApplication application, UIApplicationLaunchOptions launchOptions) {
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
		signIn.trySilentAuthentication();
	}
	
	@Override
	public boolean openURL (UIApplication application, NSURL url, String sourceApplication, NSPropertyList annotation) {
	    return GPPURLHandler.handleURL(url, sourceApplication, annotation);
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
	
	public boolean isSignedIn() {
		GPPSignIn signIn = GPPSignIn.sharedInstance();
		GTMOAuth2Authentication auth = signIn.getAuthentication();
		return (auth != null);
	}
	
	@Override
	public void invokeLeaderboards() {
		// TODO XXX TESTING
		if (!signedIn) {
			invokingLeaderboards = true;
			GPPSignIn.sharedInstance().authenticate();
		} else {
			displayHighscoreLeaderboard();
		}
	}

	@Override
	public void submitScore(int score) {
		// TODO
	}

	protected void displayHighscoreLeaderboard() {
		// create the view controller
		GPGLeaderboardController leadController = new GPGLeaderboardController(LEADERBOARD_ID_HIGHSCORES);
		leadController.setLeaderboardDelegate(this);
		// you can choose the default time scope to display in the view controller.
		//leadController.setTimeScope(GPGLeaderboardTimeScope.GPGLeaderboardTimeScopeThisWeek);

		// present the leaderboard view controller
		UIApplication.getSharedApplication().getKeyWindow().getRootViewController().presentViewController(leadController, true, null);
	}
	
	@Override
	public void finishedWithAuth (GTMOAuth2Authentication auth, NSError error) {
		if (error == null) {
			System.out.println("logged in succesfully.");
			
			// after the google+ sign-in is done, we must continue the sign-in of 'games'.
			startGoogleGamesSignIn();
			
			if (invokingLeaderboards) displayHighscoreLeaderboard();
		} else {
			System.out.println("error during login: " + error.description());
			signedIn = false;
		}
		invokingLeaderboards = false;
	}
	
	private void startGoogleGamesSignIn () {
		final GPPSignIn s = GPPSignIn.sharedInstance();
		GPGManager m = GPGManager.sharedInstance();
		
		gamesAuthBlock = new GPGReAuthenticationBlock() {
			@Override
			public void invoke (boolean requiresKeychainWipe, NSError error) {
				// If you hit this, auth has failed and you need to authenticate.
				// Most likely you can refresh behind the scenes
				if (requiresKeychainWipe) {
					s.signOut();
				}
				s.authenticate();
				signedIn = false;
			}
		};
		
		// pass the GPPSignIn to the GPGManager.
		m.signIn(s, gamesAuthBlock);
		signedIn = true;
	}

	@Override
	public void leaderboardViewControllerDidFinish(GPGLeaderboardController viewController) {
		viewController.dismissViewController(true, null);
	}
}
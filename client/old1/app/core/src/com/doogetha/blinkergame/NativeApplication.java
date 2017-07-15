package com.doogetha.blinkergame;

/**
 * Interface for accessing native features of the underlying platform
 */
public interface NativeApplication
{
	/**
	 * Shows / hides the banner advertisement
	 * @param visible true or false
	 */
	public void setBannerAdVisible(boolean visible);
	
	/**
	 * Invoke high score (signing into Google Game Services)
	 */
	public void invokeLeaderboards();
	
	/**
	 * Submits the given score
	 */
	public void submitScore(int score);
}

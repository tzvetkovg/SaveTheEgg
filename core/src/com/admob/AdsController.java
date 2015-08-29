package com.admob;


public interface AdsController {
	public void showBannerAd();
	public void hideBannerAd();
	public boolean isWifiConnected();
	public void showInterstitialAd (Runnable then);
	public boolean isMobileDataEnabled();
	public void loadInterstitialAd();
}

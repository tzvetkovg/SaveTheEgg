package com.admob;

import java.io.IOException;
import java.net.UnknownHostException;


public interface AdsController {
	public void showBannerAd();
	public void hideBannerAd();
	public boolean isWifiConnected();
	public void showInterstitialAd (Runnable then);
	public boolean isMobileDataEnabled();
	public void loadInterstitialAd();
}

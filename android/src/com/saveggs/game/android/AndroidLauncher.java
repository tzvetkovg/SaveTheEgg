package com.saveggs.game.android;

import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.admob.AdsController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.saveggs.game.GameClass;
import com.saveggs.game.Slingshot;
import com.saveggs.game.screens.LevelScreen;
import com.saveggs.game.screens.StageScreen;

public class AndroidLauncher extends AndroidApplication  implements AdsController{
	private static final String BANNER_AD_UNIT_ID = "ca-app-pub-4679885100031064/5478775838";
	private static final String INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-4679885100031064/7252900235"; 
	
	public InterstitialAd interstitialAd; 
	AdView bannerAd;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
				
		View gameView = initializeForView(new GameClass(this), config);
		setupAds();
		
		RelativeLayout layout = new RelativeLayout(this);
		layout.addView(gameView, ViewGroup.LayoutParams.MATCH_PARENT,
		        ViewGroup.LayoutParams.MATCH_PARENT);
		         
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
		        ViewGroup.LayoutParams.MATCH_PARENT,
		        ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_TOP);
		layout.addView(bannerAd, params);
		 
		setContentView(layout);
	}


	public void setupAds() {
	    bannerAd = new AdView(this);
	    bannerAd.setVisibility(View.INVISIBLE);
	    bannerAd.setBackgroundColor(0xff000000); // black
	    bannerAd.setAdUnitId(BANNER_AD_UNIT_ID);
	    bannerAd.setAdSize(AdSize.SMART_BANNER);
	    
	    //interstitial
	    interstitialAd = new InterstitialAd(this);
	    interstitialAd.setAdUnitId(INTERSTITIAL_AD_UNIT_ID);
	     
	    AdRequest.Builder builder = new AdRequest.Builder().addTestDevice("8025DA1E2D0A9AF4AF06E4AE3DEA9257");
	    AdRequest ad = builder.build();
	    interstitialAd.loadAd(ad);
	}

	@Override
	public void showBannerAd() {
	    runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	            bannerAd.setVisibility(View.VISIBLE);
	            AdRequest.Builder builder = new AdRequest.Builder().addTestDevice("8025DA1E2D0A9AF4AF06E4AE3DEA9257");;
	            AdRequest ad = builder.build();
	            bannerAd.loadAd(ad);
	        }
	    });
	}

	@Override
	public void hideBannerAd() {
	    runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	            bannerAd.setVisibility(View.INVISIBLE);
	        }
	    });
	}

	ConnectivityManager cm;
	NetworkInfo ni;
	Class cmClass;
	Method method;
	
	@Override
	public boolean isWifiConnected() {
		cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		 
		return (ni != null && ni.isConnected());
	}

	
	@Override
	public boolean isMobileDataEnabled(){
		boolean mobileDataEnabled = false; // Assume disabled
		cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    
	    try {
	    	cmClass = Class.forName(cm.getClass().getName());
			method = cmClass.getDeclaredMethod("getMobileDataEnabled");
	        method.setAccessible(true); // Make the method callable
	        // get the setting for "mobile data"
	        mobileDataEnabled = (Boolean)method.invoke(cm);
	    } catch (Exception e) {}
	    return mobileDataEnabled;
	}
	
	
   @Override
   public void showInterstitialAd(final Runnable then) {
       runOnUiThread(new Runnable() {
           @Override
           public void run() {
                   interstitialAd.setAdListener(new AdListener() {
                       @Override
                       public void onAdClosed() {
                    	   interstitialAdNotLoadedOrClosed(then);
                       }
                   });
                if(interstitialAd.isLoaded())
                	interstitialAd.show(); 
                else
                	interstitialAdNotLoadedOrClosed(then);
           }
       });
   }
   
   
   private void interstitialAdNotLoadedOrClosed(Runnable then){
       AdRequest.Builder builder = new AdRequest.Builder().addTestDevice("8025DA1E2D0A9AF4AF06E4AE3DEA9257");;
       AdRequest ad = builder.build();
       interstitialAd.loadAd(ad);
       Gdx.app.postRunnable(then);
   }
   
   @Override
   public void loadInterstitialAd(){
	   runOnUiThread(new Runnable() {
			@Override
			public void run() {
				   AdRequest.Builder builder = new AdRequest.Builder().addTestDevice("8025DA1E2D0A9AF4AF06E4AE3DEA9257");;
				   AdRequest ad = builder.build();
				   interstitialAd.loadAd(ad);
			}	   
	   });
   }
   
      
}

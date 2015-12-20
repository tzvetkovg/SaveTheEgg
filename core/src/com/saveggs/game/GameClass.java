package com.saveggs.game;

import java.util.Map;

import assets.Assets;

import com.admob.AdsController;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.pay.Information;
import com.badlogic.gdx.pay.Offer;
import com.badlogic.gdx.pay.OfferType;
import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.badlogic.gdx.pay.PurchaseObserver;
import com.badlogic.gdx.pay.Transaction;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.gdxPay.PlatformResolver;
import com.saveggs.game.screens.LevelScreen;
import com.saveggs.game.screens.MainMenuScreen;
import com.saveggs.game.screens.SplashScreen;
import com.saveggs.game.screens.StageScreen;
import com.saveggs.utils.Constants;


public class GameClass extends Game {
	
	private AdsController adsController;
	private Map<String,Object> worldBodies;
	private World world;
	
	public SplashScreen splash;
	public MainMenuScreen mainMenu;
	public LevelScreen levelScreen;
	public StageScreen stageScreen;
	
	SpriteBatch batch;
	Stage stage;
	Texture img;

	
	// ----- app stores -------------------------
		public static final int APPSTORE_UNDEFINED	= 0;
		public static final int APPSTORE_GOOGLE 	= 1;
		public static final int APPSTORE_OUYA 		= 2;
		public static final int APPSTORE_AMAZON 	= 3;
		public static final int APPSTORE_DESKTOP 	= 4;

		private int isAppStore = APPSTORE_UNDEFINED;



		public final static String productID_fullVersion = "fullversion";
		public final static String automaticdestruction = "automaticdestruction";
		public final static String fastball = "fastball";
		public final static String morelevels = "morelevels";
		public final static String slowdown = "slowdown";
		public final static String ball_limit = "ball_limit";
		
		static PlatformResolver m_platformResolver;
		public PurchaseManagerConfig purchaseManagerConfig;
	
	
	public GameClass(AdsController adsController){
	    this.adsController = adsController; 

	    setAppStore(APPSTORE_GOOGLE);	// change this if you deploy to another platform

		// ---- IAP: define products ---------------------
		purchaseManagerConfig = new PurchaseManagerConfig();
		// ---- IAP: define products ---------------------
		purchaseManagerConfig = new PurchaseManagerConfig();
		purchaseManagerConfig.addOffer(new Offer().setType(OfferType.ENTITLEMENT).setIdentifier(productID_fullVersion));
		purchaseManagerConfig.addOffer(new Offer().setType(OfferType.ENTITLEMENT).setIdentifier(automaticdestruction));
		purchaseManagerConfig.addOffer(new Offer().setType(OfferType.ENTITLEMENT).setIdentifier(fastball));
		purchaseManagerConfig.addOffer(new Offer().setType(OfferType.ENTITLEMENT).setIdentifier(morelevels));
		purchaseManagerConfig.addOffer(new Offer().setType(OfferType.ENTITLEMENT).setIdentifier(slowdown));
		purchaseManagerConfig.addOffer(new Offer().setType(OfferType.ENTITLEMENT).setIdentifier(ball_limit));

	}
	
	@Override
	public void create() {
		
/*		Assets asset = new Assets();
		
		Assets.manager.load(Assets.class);		
		
		while(!Assets.manager.update()){
			System.out.println(Assets.manager.getProgress());
		}
		Assets.manager.finishLoading();
		Constants constant = new Constants();*/
		boolean internetEnabled = false;
		if(adsController != null){			
			internetEnabled = (adsController.isMobileDataEnabled() || adsController.isWifiConnected());			
		}
		splash = new SplashScreen(adsController,this,internetEnabled);
		this.setScreen(splash);
				
		//getPlatformResolver().requestPurchaseRestore();	// check for purchases in the past

/*		Image image = new Image(Assets.manager.get(Assets.gameTitle, Texture.class));
		image.setX(350);
		image.setY(200);
		image.addListener(new ClickListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				//Information info = getPlatformResolver().getInformation(productID_fullVersion);
				//String getName = info.getLocalName() + "";
				//if(getName.equals("null"))
					getPlatformResolver().requestPurchase(productID_fullVersion);
				//else{
					//to do
				//}
				return true;
			}
		});*/

/*		Image image2 = new Image(Assets.manager.get(Assets.gameTitle, Texture.class));
		image2.setX(350);
		image2.setY(350);
		image2.addListener(new ClickListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{	
				getPlatformResolver().requestPurchaseRestore();
				return true;
			}
		});*/
		
		
		
		//stage.addActor(image);
		//stage.addActor(image2);
		
		
		//Assets.manager.load("data/sounds/loop.ogg", Music.class);
		//this.setScreen(new MainMenuScreen(adsController,this));
		//this.setScreen(new LevelScreen(null,this));
	}
	
	@Override
	public void render() {
		super.render();
		
		//Gdx.gl.glClearColor(0, 0, 0, 1);
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//stage.act();
		//stage.draw();
		
	}
	
	@Override
	public void dispose() {
		super.dispose();
		//System.out.println("goes here");
		Assets.manager.dispose();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	
		public PurchaseObserver purchaseObserver = new PurchaseObserver() {
			@Override
			public void handleRestore (Transaction[] transactions) {
				for (int i = 0; i < transactions.length; i++) {
					System.out.println("transaction is " + transactions[i].getIdentifier());
					if (automaticdestruction.equals(transactions[i].getIdentifier()) ) {
						Constants.shopPreferences.putBoolean(automaticdestruction, true);
						Constants.shopPreferences.flush();
					}		
					if (fastball.equals(transactions[i].getIdentifier()) ) {
						System.out.println("fastball bought");
						Constants.shopPreferences.putBoolean(fastball, true);
						Constants.shopPreferences.flush();
					}
					if (slowdown.equals(transactions[i].getIdentifier()) ) {
						Constants.shopPreferences.putBoolean(slowdown, true);
						Constants.shopPreferences.flush();
					}
					if (productID_fullVersion.equals(transactions[i].getIdentifier()) ) {
						Constants.shopPreferences.putBoolean(productID_fullVersion, true);
						Constants.shopPreferences.flush();
					}
					if (morelevels.equals(transactions[i].getIdentifier()) ) {
						Constants.shopPreferences.putBoolean(morelevels, true);
						Constants.shopPreferences.flush();
					}
					if (ball_limit.equals(transactions[i].getIdentifier()) ) {
						Constants.shopPreferences.putBoolean(ball_limit, true);
						Constants.shopPreferences.flush();
					}
				}
			}
			@Override
			public void handleRestoreError (Throwable e) {
				throw new GdxRuntimeException(e);
			}
			@Override
			public void handleInstall () {	}

			@Override
			public void handleInstallError (Throwable e) {
				Gdx.app.log("ERROR", "PurchaseObserver: handleInstallError!: " + e.getMessage());
				throw new GdxRuntimeException(e);
			}
			@Override
			public void handlePurchase (Transaction transaction) {
				checkTransaction(transaction.getIdentifier(), false);
			}
			@Override
			public void handlePurchaseError (Throwable e) {	//--- Amazon IAP: this will be called for cancelled
				throw new GdxRuntimeException(e);
			}
			@Override
			public void handlePurchaseCanceled () {	//--- will not be called by amazonIAP
			}
		};

		protected boolean checkTransaction (String ID, boolean isRestore) {
			boolean returnbool = false;

			if (automaticdestruction.equals(ID) ) {
				Constants.shopPreferences.putBoolean(automaticdestruction, true);
				Constants.shopPreferences.flush();
				returnbool = true;
			}		
			if (fastball.equals(ID) ) {
				System.out.println("fastball bought");
				Constants.shopPreferences.putBoolean(fastball, true);
				Constants.shopPreferences.flush();
				returnbool = true;
			}
			if (slowdown.equals(ID) ) {
				Constants.shopPreferences.putBoolean(slowdown, true);
				Constants.shopPreferences.flush();
				returnbool = true;
			}
			if (productID_fullVersion.equals(ID) ) {
				Constants.shopPreferences.putBoolean(productID_fullVersion, true);
				Constants.shopPreferences.flush();
				returnbool = true;
			}
			if (morelevels.equals(ID) ) {
				Constants.shopPreferences.putBoolean(morelevels, true);
				Constants.shopPreferences.flush();
				returnbool = true;
			}
			if (ball_limit.equals(ID) ) {
				Constants.shopPreferences.putBoolean(ball_limit, true);
				Constants.shopPreferences.flush();
			}
			return returnbool;
		}
	
	
		public PlatformResolver getPlatformResolver() {
			return m_platformResolver;
		}
		public static void setPlatformResolver (PlatformResolver platformResolver) {
			m_platformResolver = platformResolver;
		}

		public int getAppStore () {
			return isAppStore;
		}
		public void setAppStore (int isAppStore) {
			this.isAppStore = isAppStore;
		}
	
	
}

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
import com.badlogic.gdx.pay.PurchaseSystem;
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
		static PlatformResolver m_platformResolver;
		public PurchaseManagerConfig purchaseManagerConfig;
	
	
	public GameClass(AdsController adsController){
	    this.adsController = adsController; 

	    setAppStore(APPSTORE_GOOGLE);	// change this if you deploy to another platform

		// ---- IAP: define products ---------------------
		purchaseManagerConfig = new PurchaseManagerConfig();
		purchaseManagerConfig.addOffer(new Offer().setType(OfferType.ENTITLEMENT).setIdentifier(productID_fullVersion));

	}
	
	@Override
	public void create() {
		splash = new SplashScreen(adsController,this);
		this.setScreen(splash);
				
		
		//getPlatformResolver().requestPurchaseRestore();	// check for purchases in the past
		
/*		Assets asset = new Assets();
		Constants constant = new Constants();
		Assets.manager.load(Assets.class);		
		
		while(!Assets.manager.update()){
			//System.out.println(Assets.manager.getProgress());
		}
		Assets.manager.finishLoading();*/
		
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

/*		Image image = new Image(Assets.manager.get(Assets.gameTitle, Texture.class));
		image.setX(350);
		image.setY(200);
		image.addListener(new ClickListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				Information info = getPlatformResolver().getInformation(productID_fullVersion);
				String getName = info.getLocalName() + "";
				if(getName.equals("null"))
					getPlatformResolver().requestPurchase(productID_fullVersion);
				else{
					//to do
				}
				return true;
			}
		});

		Image image2 = new Image(Assets.manager.get(Assets.gameTitle, Texture.class));
		image2.setX(350);
		image2.setY(350);
		image2.addListener(new ClickListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{	
				getPlatformResolver().requestPurchaseRestore();
				return true;
			}
		});
		
		
		
		stage.addActor(image);
		stage.addActor(image2);
		*/
		
		//Assets.manager.load("data/sounds/loop.ogg", Music.class);
		//this.setScreen(new MainMenuScreen(adsController,this));
		//this.setScreen(new LevelScreen(null,this));
	}
	
	@Override
	public void render() {
		super.render();
		
		//Gdx.gl.glClearColor(0, 0, 0, 1);
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

/*		stage.act();
		stage.draw();*/
		
	}
	
	@Override
	public void dispose() {
		super.dispose();
		System.out.println("goes here");
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
					if (checkTransaction(transactions[i].getIdentifier(), true) == true) break;
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
			
			if (productID_fullVersion.equals(ID) ) {
				Gdx.app.log("checkTransaction", "full version found!");

				//----- put your logic for full version here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

				returnbool = true;
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

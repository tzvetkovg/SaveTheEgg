package com.gdxPay;


import com.badlogic.gdx.Gdx;
import com.saveggs.game.GameClass;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.pay.Information;
import com.badlogic.gdx.pay.PurchaseManager;
import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.badlogic.gdx.pay.PurchaseObserver;
import com.badlogic.gdx.pay.PurchaseSystem;

public abstract class PlatformResolver {

    public GameClass game;
    protected PurchaseManager mgr;
    PurchaseObserver purchaseObserver;
    PurchaseManagerConfig config;

    public PlatformResolver (GameClass game) {
        this.game = game;
    }

    public void initializeIAP (PurchaseManager mgr, PurchaseObserver purchaseObserver, PurchaseManagerConfig config) {
        this.mgr = mgr;
        this.purchaseObserver = purchaseObserver;
        this.config = config;
    }

    public void installIAP() {
        // set and install the manager manually
        if (mgr != null) {
            PurchaseSystem.setManager(mgr);
            mgr.install(purchaseObserver, config,true);	// dont call PurchaseSystem.install() because it may bind openIAB!
            Gdx.app.log("gdx-pay", "calls purchasemanager.install() manually");
        }
        else {
            Gdx.app.log("gdx-pay", "initializeIAP(): purchaseManager == null => call PurchaseSystem.hasManager()");
            if (PurchaseSystem.hasManager()) { // install and get the manager automatically via reflection
                this.mgr = PurchaseSystem.getManager();
                Gdx.app.log("gdx-pay", "calls PurchaseSystem.install() via reflection");
                PurchaseSystem.install(purchaseObserver, config); // install the observer
                Gdx.app.log("gdx-pay", "installed manager: " + this.mgr.toString());
            }
        }
    }

    public void requestPurchase (String productString) {
        if (mgr != null) {
            mgr.purchase(productString);	// dont call PurchaseSystem... because it may bind openIAB!
            Gdx.app.log("gdx-pay", "calls purchasemanager.purchase()");
        } else {
            Gdx.app.log("gdx-pay", "ERROR: requestPurchase(): purchaseManager == null");
        }
    }

    public void requestPurchaseRestore () {
        if (mgr != null) {
            mgr.purchaseRestore();	// dont call PurchaseSystem.purchaseRestore(); because it may bind openIAB!
            Gdx.app.log("gdx-pay", "calls purchasemanager.purchaseRestore()");
        } else {
            Gdx.app.log("gdx-pay", "ERROR: requestPurchaseRestore(): purchaseManager == null");
        }
    }

    public PurchaseManager getPurchaseManager () {
        return mgr;
    }
    
    public Information getInformation(String identifier){
    	return mgr.getInformation(identifier);
    }
    

    public void dispose () {
        if (mgr != null) {
            Gdx.app.log("gdx-pay", "calls purchasemanager.dispose()");
            mgr.dispose();		// dont call PurchaseSystem... because it may bind openIAB!
            mgr = null;
        }
    }
}
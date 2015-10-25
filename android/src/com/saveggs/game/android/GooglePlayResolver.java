package com.saveggs.game.android;

import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.saveggs.game.GameClass;
import com.saveggs.game.screens.LevelScreen;

public class GooglePlayResolver extends com.gdxPay.PlatformResolver {


    public static final String GOOGLE_PLAY_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtwOr2vBNmugztLP+1F4fa9fN9KOQDdsFJ9vCbFMU70qvm1laBZHBsa4s7j0dQ7kLVXpQ2bell84O5PAydDJH0PlS8/HcXSzQ3oupnGD2UUL4S/jKWjzqDa5MnNgep/zW2wHO1I6UurN2iQgTFjDXXm5AtcGyrPyP7gzLEA3NawOEr9PnKCDbyeTaxYhZLDRjj8uiVr3rI7au9XToRNJvPxAjBK0rRA6siuVMUW+Bil7dMolnOtdD/bGRXne69aXoMfWth0cRzRByQFJJGEiuwKNNsI3CJnXSq3IwUGDQ0BznxXAWLdz9QSV1cuOpRUhOWad0k636MmVybhd8zOAV0QIDAQAB";
    static final int RC_REQUEST = 10001;	// (arbitrary) request code for the purchase flow

    public GooglePlayResolver(GameClass game) {
        super(game);

        PurchaseManagerConfig config = game.purchaseManagerConfig;
        config.addStoreParam(PurchaseManagerConfig.STORE_NAME_ANDROID_GOOGLE, GOOGLE_PLAY_KEY);
        initializeIAP(null, game.purchaseObserver, config);
    }
}
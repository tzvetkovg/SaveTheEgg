package com.saveggs.game.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import assets.Assets;

import com.admob.AdsController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.saveggs.game.GameClass;
import com.saveggs.utils.Constants;

public class SplashScreen implements Screen {
	
	GameClass game;
	private Stage stage;
	private Image image;
	private OrthographicCamera camera;
	private AdsController adsController;
	
	public SplashScreen(final AdsController adsController,GameClass game){
		
		Assets asset = new Assets();
		Constants constant = new Constants();
		Assets.manager.load(Assets.class);	
		
		this.game = game;
		
		this.stage = new Stage(new ExtendViewport(Constants.SCENE_WIDTH / 35, Constants.SCENE_HEIGHT / 35));
		this.adsController = adsController;
		camera = new OrthographicCamera();
		stage.getViewport().setCamera(camera);
		
		Gdx.input.setInputProcessor(stage);
		Sprite splash = new Sprite(new Texture(Gdx.files.internal("data/logo.png")));
		
		image = new Image(splash);
		image.setSize(splash.getWidth() / 100, splash.getHeight() / 100);
		image.setOrigin(splash.getWidth()/ 35 / 2, splash.getHeight()/ 35 / 2);
		image.addAction(sequence(alpha(0),scaleTo(.1f,.1f),
				parallel(fadeIn(2f, Interpolation.pow2),
						scaleTo(2f,2f,2.5f, Interpolation.pow5),
						moveTo(1f,1f, 2f ,Interpolation.swing)),
				delay(2.5f), fadeOut(1.25f)));
		stage.addActor(image);
	


/*		while(!Assets.manager.update()){
			System.out.println(Assets.manager.getProgress());
		}
		Assets.manager.finishLoading();*/
		
		//load interstitial ad
		if(adsController != null && (adsController.isWifiConnected() || adsController.isMobileDataEnabled())){					
			adsController.loadInterstitialAd();
		}
	}
	
	@Override
	public void show() {}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(!Assets.manager.update()) {
			Assets.manager.update();
			//System.out.println(Assets.manager.getProgress());
	     }
		
		stage.act(delta);
		stage.draw();
		
		if(image.getActions().size == 0 && Assets.manager.update()){
			game.setScreen(new MainMenuScreen(adsController,game));
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height,false);
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose() {
		stage.dispose();
	}

}

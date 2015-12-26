package com.saveggs.game.screens;

import assets.Assets;

import com.admob.AdsController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.pay.Offer;
import com.badlogic.gdx.pay.OfferType;
import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.badlogic.gdx.pay.PurchaseObserver;
import com.badlogic.gdx.pay.Transaction;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.gdxPay.PlatformResolver;
import com.saveggs.game.GameClass;
import com.saveggs.game.screens.MainMenuScreen.MyActor;
import com.saveggs.utils.Constants;

public class Shop implements Screen{
	private Stage stage;
	private Table table;
	private TextButton weapon1, weapon2, weapon3, weapon4, weapon5,weapon6;
	private Image backgroundImage;
	private BitmapFont font;
	private Texture destroyArrow;
	private Texture slowDown;
	private Texture fastBall;
	private Texture removeAds;
	private Texture moreLevels;
	private Texture slingShots;
	private Image gameTitle;
	private static AdsController adsController;
	private boolean internetEnabled = false;
	private TextureAtlas atlas;
	
	public Shop(final AdsController adsController,final GameClass game){
		this.stage = new Stage(new ExtendViewport(Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT));
		atlas = Assets.manager.get(Assets.gameAtlas, TextureAtlas.class);
		//font
		font = Assets.manager.get(Assets.bitmapfont, BitmapFont.class);
		backgroundImage = new Image(Assets.manager.get(Assets.levels, Texture.class));
		backgroundImage.setPosition(Constants.SCENE_WIDTH / 2 - backgroundImage.getWidth() / 2, Constants.SCENE_HEIGHT / 2 - backgroundImage.getHeight() / 2);

		stage.addActor(backgroundImage);
		gameTitle = new Image(new TextureRegionDrawable(new TextureRegion(atlas.findRegion("vulture"))));

		// arrow
		TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle();
		tbs.font = font;
		tbs.up = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("testweapon1")));
		tbs.font.getData().setScale(0.01f);
		
		
		// slowDown
		TextButton.TextButtonStyle tbs2 = new TextButton.TextButtonStyle();
		tbs2.font = font;
		tbs2.up = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("testweapon2")));
		tbs2.font.getData().setScale(0.01f);
		
		// super fast ball
		TextButton.TextButtonStyle tbs3 = new TextButton.TextButtonStyle();
		tbs3.font = font;
		tbs3.up = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("testweapon3")));
		tbs3.font.getData().setScale(0.01f);
		
		//remove ads
		TextButton.TextButtonStyle tbs4 = new TextButton.TextButtonStyle();
		tbs4.font = font;
		tbs4.up = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("testweapon4")));
		tbs4.font.getData().setScale(0.01f);
		
		// super fast ball
		TextButton.TextButtonStyle tbs5 = new TextButton.TextButtonStyle();
		tbs5.font = font;
		tbs5.up = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("testweapon5")));
		tbs5.font.getData().setScale(0.01f);
		
		//limit of slingshot shots
		// super fast ball
		TextButton.TextButtonStyle tbs6 = new TextButton.TextButtonStyle();
		tbs6.font = font;
		tbs6.up = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("testweapon6")));
		tbs6.font.getData().setScale(0.01f);
		
		//go back
        //back button
		TextButton.TextButtonStyle tbs7 = new TextButton.TextButtonStyle();
		tbs7.font =  Assets.manager.get(Assets.bitmapfont, BitmapFont.class);
		tbs7.font.getData().setScale(0.001f);
		tbs7.up = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("backbutton")));
		tbs7.down = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("backbuttonClicked")));
		TextButton button = new TextButton("", tbs7);
		tbs7.font.getData().setScale(2f);
		button.setPosition(0, 0);
		button.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
			   game.setScreen(game.mainMenu);
			}
		});

		
		//Define buttons
		weapon1 = new TextButton("", tbs);
		weapon1.setPosition(Constants.SCENE_WIDTH /  7 , Constants.SCENE_HEIGHT / 2);
		weapon2 = new TextButton("",tbs2);
		weapon2.setPosition(Constants.SCENE_WIDTH / 2.3f , Constants.SCENE_HEIGHT / 2);
		weapon3 = new TextButton("",tbs3);
		weapon3.setPosition(Constants.SCENE_WIDTH / 1.4f , Constants.SCENE_HEIGHT / 2);
		weapon4 = new TextButton("",tbs4);
		weapon4.setPosition(Constants.SCENE_WIDTH / 7f , Constants.SCENE_HEIGHT / 8);
		weapon5 = new TextButton("",tbs5);
		weapon5.setPosition(Constants.SCENE_WIDTH /2.3f , Constants.SCENE_HEIGHT / 8);
		weapon6 = new TextButton("",tbs6);
		weapon6.setPosition(Constants.SCENE_WIDTH / 1.4f , Constants.SCENE_HEIGHT / 8);
		
		weapon1.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				if(!Constants.shopPreferences.contains(GameClass.automaticdestruction)){
					game.getPlatformResolver().requestPurchase(GameClass.automaticdestruction);
				}
				return super.touchDown(event, x, y, pointer, button);
			}
		});
		
		weapon2.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {			
				if(!Constants.shopPreferences.contains(GameClass.slowdown)){					
					game.getPlatformResolver().requestPurchase(GameClass.slowdown);
				}
				return super.touchDown(event, x, y, pointer, button);
			}
		});
		
		weapon3.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {	
				if(!Constants.shopPreferences.contains(GameClass.fastball)){						
					game.getPlatformResolver().requestPurchase(GameClass.fastball);
				}
				return super.touchDown(event, x, y, pointer, button);
			}
		});
		
		weapon4.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {		
				if(!Constants.shopPreferences.contains(GameClass.productID_fullVersion)){							
					game.getPlatformResolver().requestPurchase(GameClass.productID_fullVersion);
				}
				return super.touchDown(event, x, y, pointer, button);
			}
		});
		
		weapon5.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {	
				if(!Constants.shopPreferences.contains(GameClass.morelevels)){							
					game.getPlatformResolver().requestPurchase(GameClass.morelevels);
				}
				return super.touchDown(event, x, y, pointer, button);
			}
		});

		weapon6.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {	
				if(!Constants.shopPreferences.contains(GameClass.ball_limit)){							
					game.getPlatformResolver().requestPurchase(GameClass.ball_limit);
				}
				return super.touchDown(event, x, y, pointer, button);
			}
		});
		
		
		// Set table structure
		table = new Table();
		table.setFillParent(true);

		table.add(gameTitle).size(gameTitle.getWidth() , gameTitle.getHeight() /2 ).expand().top();
		stage.addActor(table);
		stage.addActor(weapon1);
		stage.addActor(weapon2);
		stage.addActor(weapon3);
		stage.addActor(weapon4);
		stage.addActor(weapon5);
		stage.addActor(weapon6);
		stage.addActor(button);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
		if(Gdx.input.isKeyJustPressed(Keys.UP)){
			((OrthographicCamera) this.stage.getCamera() ).zoom -= 0.1f;
			((OrthographicCamera) this.stage.getCamera() ).update();
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.DOWN)){
			((OrthographicCamera) this.stage.getCamera() ).zoom += 0.1f;
			((OrthographicCamera) this.stage.getCamera() ).update();
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height,false);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
	
	

	
}

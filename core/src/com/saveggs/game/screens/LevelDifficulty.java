package com.saveggs.game.screens;

import assets.Assets;

import com.admob.AdsController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.saveggs.game.GameClass;
import com.saveggs.utils.Constants;

public class LevelDifficulty implements Screen {
	private GameClass game;
	private AdsController _adsController;
	private Stage stage;
	private Texture buttonUpTex;
	private Texture buttonOverTex;
	private Texture buttonDownTex;
	private TextButton easy, hard;
	
	public LevelDifficulty(final AdsController adsController,final GameClass game){
		this.game = game;
		this._adsController = adsController;
		this.stage = new Stage(new ExtendViewport(Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT));
		Gdx.input.setInputProcessor(stage);
		//back button
		TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle();
		tbs.font =  Assets.manager.get(Assets.bitmapfont, BitmapFont.class);
		tbs.font.getData().setScale(0.001f);
		tbs.up = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.backbutton, Texture.class)));
		tbs.down = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.backbuttonClicked, Texture.class)));
		TextButton button = new TextButton("", tbs);
		tbs.font.getData().setScale(2f);
		button.setPosition(0, 0);
		button.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
			   game.setScreen(new MainMenuScreen(adsController,game));
			   dispose();
			}
		});
		
		
		// Set buttons' style
		buttonUpTex = Assets.manager.get(Assets.normalButton, Texture.class);
		buttonOverTex = Assets.manager.get(Assets.buttonOverTex, Texture.class);
		buttonDownTex = Assets.manager.get(Assets.buttonDownTex, Texture.class);
		TextButton.TextButtonStyle tbs2 = new TextButton.TextButtonStyle();
		tbs2.up = new TextureRegionDrawable(new TextureRegion(buttonUpTex));
		tbs2.over = new TextureRegionDrawable(new TextureRegion(buttonOverTex));
		tbs2.down = new TextureRegionDrawable(new TextureRegion(buttonDownTex));
		tbs2.font.getData().setScale(1.3f);
		
		easy = new TextButton("EASY", tbs);
		hard = new TextButton("DIFFICULT",tbs);
		
	}
	
	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}
	
}

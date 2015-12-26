package com.saveggs.game.screens;

import assets.Assets;

import com.admob.AdsController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.saveggs.game.GameClass;
import com.saveggs.utils.Constants;

public class CreditsScreen implements Screen {
	
	private Stage stage;
	private GameClass games;
	private Image image;
	private AdsController _adsController;
	private TextureAtlas atlas;
	
	public CreditsScreen(final AdsController adsController,final GameClass game){
		this.games = game;
		this._adsController = adsController;
		atlas = Assets.manager.get(Assets.gameAtlas, TextureAtlas.class);
        //back button
		TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle();
		tbs.font =  Assets.manager.get(Assets.bitmapfont, BitmapFont.class);
		tbs.font.getData().setScale(0.001f);
		tbs.up = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("backbutton")));
		tbs.down = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("backbuttonClicked")));
		TextButton button = new TextButton("", tbs);
		tbs.font.getData().setScale(2f);
		button.setPosition(0, 0);
		button.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
			   game.setScreen(games.mainMenu);
			}
		});
		
		this.stage = new Stage(new ExtendViewport(Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT));

		image = new Image(Assets.manager.get(Assets.credits, Texture.class));
		image.setSize(Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
		image.setOrigin(Constants.SCENE_WIDTH * 0.5f, Constants.SCENE_HEIGHT * 0.5f);
		stage.addActor(image);
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

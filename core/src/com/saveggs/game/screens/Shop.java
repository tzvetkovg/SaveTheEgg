package com.saveggs.game.screens;

import assets.Assets;

import com.admob.AdsController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.saveggs.game.GameClass;
import com.saveggs.game.screens.MainMenuScreen.MyActor;
import com.saveggs.utils.Constants;

public class Shop implements Screen{
	private Stage stage;
	private Table table;
	private TextButton weapon1, weapon2, weapon3, removeAdsButton, moveLevelsButton;
	private Image backgroundImage;
	private BitmapFont font;
	private Texture destroyArrow;
	private Texture destroyArrowClicked;
	
	private Texture slowDown;
	private Texture slowDownClicked;
	
	private Texture fastBall;
	private Texture fastBallClicked;
	
	private Texture removeAds;
	private Texture removeAdsClicked;
	
	private Texture moreLevels;
	private Texture moreLevelsClicked;
	
	private Image gameTitle;
	private MyActor ac;
	
	public Shop(){
		this.stage = new Stage(new ExtendViewport(Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT));
		Gdx.input.setInputProcessor(stage);
		//font
		font = Assets.manager.get(Assets.bitmapfont, BitmapFont.class);
		backgroundImage = new Image(Assets.manager.get(Assets.levels, Texture.class));
		backgroundImage.setPosition(Constants.SCENE_WIDTH / 2 - backgroundImage.getWidth() / 2, Constants.SCENE_HEIGHT / 2 - backgroundImage.getHeight() / 2);

		stage.addActor(backgroundImage);
		gameTitle = new Image(new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.gameTitle, Texture.class))));

		// arrow
		destroyArrow = Assets.manager.get(Assets.destroyEnemyArrow, Texture.class);
		destroyArrowClicked = Assets.manager.get(Assets.destroyEnemyArrowClicked, Texture.class);
		TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle();
		tbs.font = font;
		tbs.up = new TextureRegionDrawable(new TextureRegion(destroyArrow));
		tbs.down = new TextureRegionDrawable(new TextureRegion(destroyArrowClicked));
		tbs.font.getData().setScale(0.01f);
		
		
		// slowDown
		slowDown = Assets.manager.get(Assets.slowDownBird, Texture.class);
		slowDownClicked = Assets.manager.get(Assets.slowDownBirdClicked, Texture.class);
		TextButton.TextButtonStyle tbs2 = new TextButton.TextButtonStyle();
		tbs2.font = font;
		tbs2.up = new TextureRegionDrawable(new TextureRegion(slowDown));
		tbs2.down = new TextureRegionDrawable(new TextureRegion(slowDownClicked));
		tbs2.font.getData().setScale(0.01f);
		
		
		//Define buttons
		weapon1 = new TextButton("", tbs);
		weapon2 = new TextButton("",tbs2);
		
		// Set table structure
		table = new Table();
		table.setFillParent(true);

		table.add(gameTitle).size(gameTitle.getWidth() , gameTitle.getHeight() /2).expandX();
		table.row();
		table.add(weapon1).size(Constants.SCENE_WIDTH /  5.5f , Constants.SCENE_HEIGHT / 3).padLeft(100f).expand().top().left();;
		table.row();
		table.add(weapon2).padTop(5f).size(Constants.SCENE_WIDTH / 5.5f , Constants.SCENE_HEIGHT / 12);
		table.row();
		//table.debug();
		stage.addActor(table);

	}

	@Override
	public void show() {	
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

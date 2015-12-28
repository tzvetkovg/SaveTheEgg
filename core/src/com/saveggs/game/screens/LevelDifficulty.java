package com.saveggs.game.screens;

import assets.Assets;

import com.admob.AdsController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
	private TextureRegion buttonUpTex;
	private TextureRegion buttonOverTex;
	private TextureRegion buttonDownTex;
	private TextButton easy, medium, hard;
	private Image backgroundImage;
	private TextButton.TextButtonStyle easyLevel;
	private TextButton.TextButtonStyle hardLevel;
	private TextButton.TextButtonStyle mediumLevel;
	private TextureAtlas atlas;
	
	public LevelDifficulty(final AdsController adsController,final GameClass game){
		this.game = game;
		this._adsController = adsController;
		this.stage = new Stage(new ExtendViewport(Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT));
		
		atlas = Assets.manager.get(Assets.gameAtlas, TextureAtlas.class);
		
		backgroundImage = new Image(Assets.manager.get(Assets.levels, Texture.class));
		backgroundImage.setPosition(Constants.SCENE_WIDTH / 2 - backgroundImage.getWidth() / 2, Constants.SCENE_HEIGHT / 2 - backgroundImage.getHeight() / 2);

		stage.addActor(backgroundImage);
		
		// Set buttons' style
		buttonUpTex = atlas.findRegion("normalButton");
		buttonOverTex = atlas.findRegion("normalButton");
		buttonDownTex = atlas.findRegion("clickedButton");
		
		hardLevel = new TextButton.TextButtonStyle();
		hardLevel.up = new TextureRegionDrawable(new TextureRegion(buttonUpTex));
		hardLevel.over = new TextureRegionDrawable(new TextureRegion(buttonOverTex));
		hardLevel.down = new TextureRegionDrawable(new TextureRegion(buttonDownTex));
		
		easyLevel = new TextButton.TextButtonStyle();
		easyLevel.up = new TextureRegionDrawable(new TextureRegion(buttonUpTex));
		easyLevel.over = new TextureRegionDrawable(new TextureRegion(buttonOverTex));
		easyLevel.down = new TextureRegionDrawable(new TextureRegion(buttonDownTex));
		
		mediumLevel = new TextButton.TextButtonStyle();
		mediumLevel.up = new TextureRegionDrawable(new TextureRegion(buttonUpTex));
		mediumLevel.over = new TextureRegionDrawable(new TextureRegion(buttonOverTex));
		mediumLevel.down = new TextureRegionDrawable(new TextureRegion(buttonDownTex));
		
		FreeTypeFontGenerator fontGenerator = Assets.manager.get(Assets.myFont, FreeTypeFontGenerator.class);
		FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter =
		      new FreeTypeFontGenerator.FreeTypeFontParameter();
		freeTypeFontParameter.size = 20;
		freeTypeFontParameter.borderColor = Color.BLACK;
		freeTypeFontParameter.borderWidth = 1;
		fontGenerator.generateData(freeTypeFontParameter); 
		
		hardLevel.font = fontGenerator.generateFont(freeTypeFontParameter);
		hardLevel.font.getData().setScale(1f);
		easyLevel.font = fontGenerator.generateFont(freeTypeFontParameter);
		easyLevel.font.getData().setScale(1f);
		mediumLevel.font =  fontGenerator.generateFont(freeTypeFontParameter);
		mediumLevel.font.getData().setScale(1f);
		
		easy = new TextButton("EASY", easyLevel);
		medium = new TextButton("MEDIUM",mediumLevel);
		hard = new TextButton("INSANE",hardLevel);
		easy.setPosition(Constants.SCENE_WIDTH / 2.5f, Constants.SCENE_HEIGHT / 2);
		medium.setPosition(Constants.SCENE_WIDTH / 2.5f, Constants.SCENE_HEIGHT / 2.8f);
		hard.setPosition(Constants.SCENE_WIDTH / 2.5f, Constants.SCENE_HEIGHT / 4.6f);
		
		updateButtonLook();
		
		easy.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				   Constants.gameDifficulty.putString("levels", Constants.easyLevels);
				   Constants.gameDifficulty.flush();
				   updateButtonLook();
			}
		});

		
		medium.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				   Constants.gameDifficulty.putString("levels", Constants.mediumLevels);
				   Constants.gameDifficulty.flush();
				   updateButtonLook();
			}
		});
		
		hard.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
			   Constants.gameDifficulty.putString("levels", Constants.difficultLevels);
			   Constants.gameDifficulty.flush();
			   updateButtonLook();
			}
		});
		
		stage.addActor(easy);
		stage.addActor(medium);
		stage.addActor(hard);
		
		
        //back button
		TextButton.TextButtonStyle tbs3 = new TextButton.TextButtonStyle();
		tbs3.font =  fontGenerator.generateFont(freeTypeFontParameter);
		tbs3.font.getData().setScale(0.001f);
		tbs3.up = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("backbutton")));
		tbs3.down = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("backbuttonClicked")));
		TextButton button2 = new TextButton("", tbs3);
		tbs3.font.getData().setScale(2f);
		button2.setPosition(0, 0);
		button2.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
			   game.setScreen(game.mainMenu);
			}
		});

        stage.addActor(button2);
		
		
	}
	
	public void updateButtonLook(){
		if(Constants.gameDifficulty.getString("levels").equals(Constants.easyLevels)){
			easyLevel.up = easyLevel.down;
			hardLevel.up = hardLevel.over;
			mediumLevel.up = mediumLevel.over;
		}
		else if(Constants.gameDifficulty.getString("levels").equals(Constants.mediumLevels)){
			mediumLevel.up = mediumLevel.down;
			hardLevel.up = hardLevel.over;
			easyLevel.up = easyLevel.over;
		}
		else{
			hardLevel.up = hardLevel.down;
			easyLevel.up = easyLevel.over;
			mediumLevel.up = mediumLevel.over;
		}
	}
	
	
	@Override	public void show() {
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

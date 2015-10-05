package com.saveggs.game.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;

import java.util.Map;

import assets.Assets;

import com.admob.AdsController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.saveggs.game.GameClass;
import com.saveggs.utils.Constants;
import com.saveggs.utils.WorldUtils;

public class MainMenuScreen implements Screen{
	
	
	private Stage stage;
	private Table table;
	private TextButton play, exit,tutorial,credits;
	private Image image;
	private BitmapFont font;
	private Texture buttonUpTex;
	private Texture buttonOverTex;
	private Texture buttonDownTex;
	private Image gameTitle;
	private AdsController adsController;
	private final GameClass games;
	
	public MainMenuScreen(final AdsController adsController,final GameClass game){
		
		
		this.games = game;
		
		this.stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		this.adsController = adsController;
		
		Gdx.input.setInputProcessor(stage);
		//font
		font = Assets.manager.get(Assets.bitmapfont, BitmapFont.class);
		
		gameTitle = new Image(new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.gameTitle, Texture.class))));
		
		// Set buttons' style
		buttonUpTex = Assets.manager.get(Assets.normalButton, Texture.class);
		buttonOverTex = Assets.manager.get(Assets.buttonOverTex, Texture.class);
		buttonDownTex = Assets.manager.get(Assets.buttonDownTex, Texture.class);
		TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle();
		tbs.font = font;
		tbs.up = new TextureRegionDrawable(new TextureRegion(buttonUpTex));
		tbs.over = new TextureRegionDrawable(new TextureRegion(buttonOverTex));
		tbs.down = new TextureRegionDrawable(new TextureRegion(buttonDownTex));
		tbs.font.getData().setScale(2f);
		//Define buttons
		play = new TextButton("PLAY", tbs);
		tutorial = new TextButton("TUTORIAL", tbs);
		credits = new TextButton("CREDITS", tbs);
		exit = new TextButton("EXIT", tbs);


		// Play button listener
		play.addListener( new ClickListener() {
			@Override
		    public void clicked (InputEvent event, float x, float y) {
				game.setScreen(new LevelScreen(adsController,game));
			};
		});

		// Exit button listener
		exit.addListener( new ClickListener() {             
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			};
		});
		
		// Set table structure
		table = new Table();
		//table.debug();
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		table.add(gameTitle).expandX().spaceBottom(80f);
		table.row();
		table.row();
		table.add(play).padTop(5f).size(Gdx.graphics.getWidth() / 5 , Gdx.graphics.getHeight() / 10);
		table.row();
		table.add(credits).padTop(5f).size(Gdx.graphics.getWidth() / 5 , Gdx.graphics.getHeight() / 10);
		table.row();
		table.add(tutorial).padTop(5f).size(Gdx.graphics.getWidth() / 5 , Gdx.graphics.getHeight() / 10);
		table.row();
		table.add(exit).padTop(5f).size(Gdx.graphics.getWidth() / 5 , Gdx.graphics.getHeight() / 10);;

		// Set table's alpha to 0
		table.getColor().a = 0f;
		
		// Adds created table to stage
		stage.addActor(table);
		//
		//table.debug();
		// To make the table appear smoothly
		table.addAction(fadeIn(2f));
		
		table.setBackground(new NinePatchDrawable(Constants.getNinePatch()));
		
	}
	


	
	
	@Override
	public void show() {}

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
		table.setClip(true);
		table.setSize(width, height);
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

package com.saveggs.game.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import assets.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.saveggs.game.GameClass;
import com.saveggs.utils.Constants;
import com.saveggs.utils.WorldUtils;

public class MainMenuScreen implements Screen{
	
	GameClass game;
	private Stage stage;
	private Table table;
	private TextButton play, exit;
	private Image image;
	private BitmapFont font;
	private Texture buttonUpTex;
	private Texture buttonOverTex;
	private Texture buttonDownTex;
	private Image gameTitle;
	private WorldUtils utils;
	
	public MainMenuScreen(final GameClass game,final WorldUtils utils){
		this.game = game;
		this.stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		this.utils = utils;
		Gdx.input.setInputProcessor(stage);
		//font
		font = Assets.manager.get(Assets.bitmapfont, BitmapFont.class);
		
		gameTitle = new Image(new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.gameTitle, Texture.class))));
		
		// Set buttons' style
		buttonUpTex = Assets.manager.get(Assets.buttonUpTex, Texture.class);
		buttonOverTex = Assets.manager.get(Assets.buttonOverTex, Texture.class);
		buttonDownTex = Assets.manager.get(Assets.buttonDownTex, Texture.class);
		TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle();
		tbs.font = font;
		tbs.up = new TextureRegionDrawable(new TextureRegion(buttonUpTex));
		tbs.over = new TextureRegionDrawable(new TextureRegion(buttonOverTex));
		tbs.down = new TextureRegionDrawable(new TextureRegion(buttonDownTex));
		
		//Define buttons
		play = new TextButton("PLAY", tbs);
		exit = new TextButton("EXIT", tbs);

		
		// Play button listener
		play.addListener( new ClickListener() {             
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println(play.getText());
				game.setScreen(new LevelScreen(game,utils));
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
		
		table.add(gameTitle).expandX().spaceBottom(200f);
		table.row();
		table.row();
		table.add(play).padTop(5f);
		table.row();
		table.add(exit).padTop(5f);

		// Set table's alpha to 0
		table.getColor().a = 0f;
		
		// Adds created table to stage
		stage.addActor(table);
		//
		table.debug();
		// To make the table appear smoothly
		table.addAction(fadeIn(4f));
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

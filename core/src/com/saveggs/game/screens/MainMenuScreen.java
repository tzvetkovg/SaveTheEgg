package com.saveggs.game.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;

import java.util.Map;

import net.dermetfan.gdx.graphics.g2d.AnimatedBox2DSprite;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import assets.Assets;

import com.admob.AdsController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.saveggs.game.GameClass;
import com.saveggs.game.screens.LevelScreen.MyActor;
import com.saveggs.utils.Constants;
import com.saveggs.utils.WorldUtils;

public class MainMenuScreen implements Screen{
	
	
	private Stage stage;
	private Table table;
	private TextButton play, exit,tutorial,credits,becomePro,difficulty;
	private Image backgroundImage;
	private BitmapFont font;
	private Texture buttonUpTex;
	private Texture buttonOverTex;
	private Texture buttonDownTex;
	private Image gameTitle;
	private AdsController _adsController;
	private final GameClass games;
	private MoveToAction moveAction;
	private MyActor ac;
	private Sound sound;
	
	public MainMenuScreen(final AdsController adsController,final GameClass game){
		
		this.games = game;
		this.stage = new Stage(new ExtendViewport(Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT));
		this._adsController = adsController;
		
		
		//font
		FreeTypeFontGenerator fontGenerator = Assets.manager.get(Assets.myFont, FreeTypeFontGenerator.class);
		FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter =
		      new FreeTypeFontGenerator.FreeTypeFontParameter();
		freeTypeFontParameter.size = 20;
		freeTypeFontParameter.borderColor = Color.BLACK;
		freeTypeFontParameter.borderWidth = 1;
		fontGenerator.generateData(freeTypeFontParameter); 
		font = fontGenerator.generateFont(freeTypeFontParameter);
		
		gameTitle = new Image(new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.gameTitle, Texture.class))));
		
		backgroundImage = new Image(Assets.manager.get(Assets.levels, Texture.class));
		backgroundImage.setPosition(Constants.SCENE_WIDTH / 2 - backgroundImage.getWidth() / 2, Constants.SCENE_HEIGHT / 2 - backgroundImage.getHeight() / 2);

		stage.addActor(backgroundImage);
		
		// Set buttons' style
		buttonUpTex = Assets.manager.get(Assets.normalButton, Texture.class);
		buttonOverTex = Assets.manager.get(Assets.buttonOverTex, Texture.class);
		buttonDownTex = Assets.manager.get(Assets.buttonDownTex, Texture.class);
		TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle();
		tbs.font = font;
		tbs.up = new TextureRegionDrawable(new TextureRegion(buttonUpTex));
		tbs.over = new TextureRegionDrawable(new TextureRegion(buttonOverTex));
		tbs.down = new TextureRegionDrawable(new TextureRegion(buttonDownTex));
		tbs.font.getData().setScale(1f);
		//Define buttons
		play = new TextButton("PLAY", tbs);
		becomePro = new TextButton("BECOME PRO",tbs);
		tutorial = new TextButton("TUTORIAL", tbs);
		credits = new TextButton("CREDITS", tbs);
		exit = new TextButton("EXIT", tbs);
		difficulty = new TextButton("DIFFICULTY", tbs);

		// Play button listener
		play.addListener( new ClickListener() {
			@Override
		    public void clicked (InputEvent event, float x, float y) {
				game.setScreen(games.levelScreen);
			};
		});

		// Exit button listener
		exit.addListener( new ClickListener() {             
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			};
		});
		
		// shop
		becomePro.addListener( new ClickListener() {             
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(games.shop);
			};
		});
		
		//tutorial
		credits.addListener( new ClickListener() {             
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(games.credits);
			};
		});
		
		//tutorial
		tutorial.addListener( new ClickListener() {             
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(game.tutorial);
			};
		});
		
		//tutorial
		difficulty.addListener( new ClickListener() {             
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(games.levelDiffulty);
			};
		});
		
		// Set table structure
		table = new Table();
		table.setFillParent(true);

        moveAction = new MoveToAction();
	    moveAction.setPosition(-Constants.SCENE_WIDTH, MathUtils.random(Constants.SCENE_HEIGHT * 0.1f, Constants.SCENE_HEIGHT * 0.9f));
	    moveAction.setDuration(15f);
		ac = new MyActor();
		ac.addAction(moveAction);
		table.addActor(ac);
		
		table.add(gameTitle).expandX().spaceBottom(120f);
		table.row();
		table.row();
		table.row();
		table.add(play).padTop(5f).size(Constants.SCENE_WIDTH /  4.5f , Constants.SCENE_HEIGHT / 12);
		table.row();
		table.add(becomePro).padTop(5f).size(Constants.SCENE_WIDTH / 4.5f , Constants.SCENE_HEIGHT / 12);
		table.row();
		table.add(credits).padTop(5f).size(Constants.SCENE_WIDTH / 4.5f , Constants.SCENE_HEIGHT / 12);
		table.row();
		table.add(difficulty).padTop(5f).size(Constants.SCENE_WIDTH / 4.5f , Constants.SCENE_HEIGHT / 12);
		table.row();
		table.add(tutorial).padTop(5f).size(Constants.SCENE_WIDTH / 4.5f ,Constants.SCENE_HEIGHT / 12);
		table.row();
		table.add(exit).padTop(5f).size(Constants.SCENE_WIDTH / 4.5f , Constants.SCENE_HEIGHT / 12);;


		// Set table's alpha to 0
		table.getColor().a = 0f;
		table.addAction(fadeIn(2f));
		

		// Adds created table to stage
		stage.addActor(table);
		
	}
	
	@Override
	public void show() {Gdx.input.setInputProcessor(stage);}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
		
		if(ac.getActions().size == 0){
			 ac.setPosition(Constants.SCENE_WIDTH, MathUtils.random(Constants.SCENE_HEIGHT * 0.1f, Constants.SCENE_HEIGHT * 0.9f));
			 moveAction.reset();
		     moveAction.setPosition(-MathUtils.random(150, 200), MathUtils.random(Constants.SCENE_HEIGHT * 0.1f, Constants.SCENE_HEIGHT * 0.9f));
		     moveAction.setDuration(15f);
			 ac.addAction(moveAction);
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
	
	public class MyActor extends Actor{
		private Texture texture;
		private TextureRegion[] animationFrames;
		private AnimatedSprite animatedSprite;
		public AnimatedBox2DSprite animatedBox2DSprite;
		private Animation animation;
		private float stateTime = 0f;
		TextureRegion  currentFrame;
		public MyActor(){
			texture = Assets.manager.get(Assets.pticheta2, Texture.class);	
			splitAnimation();
			animation = new Animation(1f/14f, animationFrames);
			setPosition(Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT * 0.5f);	
			setSize(getWidth(), getHeight());
		}
		
		 public void splitAnimation(){
			 TextureRegion[][] tmpFrames = TextureRegion.split(texture, 512, 256);
			 animationFrames = new TextureRegion[8];
			 int index = 0;		 
			 for (int i = 0; i < 4; ++i){
				 for (int j = 0; j < 2; ++j){
					 animationFrames[index++] = tmpFrames[i][j];
				 }
			 }
		 }
		@Override
		public void act(float delta) {
			// TODO Auto-generated method stub
			super.act(delta);
	        stateTime += delta;           // #15
	        currentFrame = animation.getKeyFrame(stateTime, true);  // #16
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			super.draw(batch, parentAlpha);
			// TODO Auto-generated method stub                        // #14
	        batch.draw(currentFrame,getX(),getY(),66.5f,43f);
		}
	}
	
}

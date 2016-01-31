package com.saveggs.game.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import java.io.IOException;
import java.util.Map;

import net.dermetfan.gdx.graphics.g2d.AnimatedBox2DSprite;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import assets.Assets;

import com.admob.AdsController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Action;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.sageggs.actors.CurrentMap;
import com.sageggs.actors.GameActor;
import com.saveggs.game.GameClass;
import com.saveggs.game.GameStage;
import com.saveggs.utils.Constants;

public class LevelScreen implements Screen{
	
	private Stage stage;
	private Table container;
	public final GameClass game;
	private Image gameTitle;
	GameStage gameStage;
	private AdsController adsController;
	private Image sprite;
	private MoveToAction moveAction;
	private MyActor ac;
	private TextureAtlas atlas;
	
	public LevelScreen(final AdsController adsController,final GameClass game){		
		this.game = game;
		this.adsController = adsController;
		atlas = Assets.manager.get(Assets.gameAtlas, TextureAtlas.class);
		initializeScreen();				
	}

	public class MyActor extends Actor{
		private Texture texture;
		private TextureRegion[] animationFrames;
		private AnimatedSprite animatedSprite;
		public AnimatedBox2DSprite animatedBox2DSprite;
		private Animation animation;
		private float stateTime = 0f;
		TextureRegion  currentFrame;
		private TextureAtlas atlas;
		public MyActor(){
			
			atlas = Assets.manager.get(Assets.allEnemies, TextureAtlas.class);
			animationFrames = new TextureRegion[4];
			animationFrames[0] = atlas.findRegion("e1");
			animationFrames[1] = atlas.findRegion("e2");
			animationFrames[2] = atlas.findRegion("e3");
			animationFrames[3] = atlas.findRegion("e4");
			animation = new Animation(1f/10f, animationFrames);
			setPosition(Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT * 0.5f);	
			setSize(getWidth(), getHeight());
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
	        batch.draw(currentFrame,getX(),getY(),72.3f,54.45f);
		}
	}
	
	
	public void initializeScreen() {
		this.stage = new Stage(new ExtendViewport(Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT));
		//gameTitle = new Image(new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.levels, Texture.class))));
		gameTitle = new Image(Assets.manager.get(Assets.levels, Texture.class));
		//gameTitle.setPosition(Constants.SCENE_WIDTH / 2 - gameTitle.getWidth() / 2, Constants.SCENE_HEIGHT - 120);
		gameTitle.setPosition(Constants.SCENE_WIDTH / 2 - gameTitle.getWidth() / 2, Constants.SCENE_HEIGHT / 2 - gameTitle.getHeight() / 2);
				
		

		container = new Table();
		
		container.setFillParent(true);		
		
		
         moveAction = new MoveToAction();
	     moveAction.setPosition(-150, MathUtils.random(Constants.SCENE_HEIGHT * 0.1f, Constants.SCENE_HEIGHT * 0.9f));
	     moveAction.setDuration(15f);
		 ac = new MyActor();
		 ac.addAction(moveAction);
         container.addActor(ac);
		
        stage.addActor(gameTitle);
         
		PagedScrollPane scroll = new PagedScrollPane();
		scroll.setFlingTime(0.1f);
		scroll.setPageSpacing(25);
		int c = 1;
		int bound = 2;
		//if purchased
		if(Constants.shopPreferences.contains(GameClass.morelevels)){
			bound = 3;
			Constants.preferences.putBoolean("Level25", true);
			Constants.preferences.putBoolean("Level26", true);
			Constants.preferences.putBoolean("Level27", true);
		}
		for (int l = 0; l < bound; l++) {

			Table levels = new Table().pad(50);
			levels.defaults().pad(20, 50, 20, 50).size(70);

			if(l == 2){
				for (int y = 0; y < 1; y++) {
					levels.row();
					for (int x = 0; x < 3; x++) {
						levels.defaults().pad(20, 80, 20, 60).size(70);
						levels.add(getLevelButton(c++)).expand().fill().top();
					}
				}
			}
			else{				
				for (int y = 0; y < 3; y++) {
					levels.row();
					for (int x = 0; x < 4; x++) {
						levels.add(getLevelButton(c++)).expand().fill();
					}
				}
			}
			scroll.addPage(levels);
		}
		container.add(scroll).expand().fill();

		
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
			   game.setScreen(game.mainMenu);
			}
		});

        container.addActor(button);
        container.addAction(fadeIn(2f));
        
        stage.addActor(container);
	}
	
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		if(!Constants.isPlaying)
		{
			Constants.setPlaying();
		}
    }

	public TextButton getLevelButton(int level) {
		        
		TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle();
		tbs.font =  Assets.manager.get(Assets.bitmapfont, BitmapFont.class);
		
		TextButton button = new TextButton("", tbs);
		tbs.font.getData().setScale(2f);
		
		String map = null;
		switch (level) {
        case 1:  map = Assets.map1;//Assets.manager.get(Assets.map1, TiledMap.class);
        		 break;
        case 2:  map = Assets.map3;//Assets.manager.get(Assets.map2, TiledMap.class);
                 break;
        case 3:  map = Assets.map5;//Assets.manager.get(Assets.map3, TiledMap.class);
                 break;
        case 4:  map = Assets.map6;//Assets.manager.get(Assets.map4, TiledMap.class);
                 break;
        case 5:  map = Assets.map9;//Assets.manager.get(Assets.map5, TiledMap.class);
                 break;
        case 6:  map = Assets.map27;//Assets.manager.get(Assets.map6, TiledMap.class);
                 break;
        case 7:  map = Assets.map10;//Assets.manager.get(Assets.map7, TiledMap.class);
                 break;
        case 8:  map = Assets.map12;//Assets.manager.get(Assets.map8, TiledMap.class);
                 break;
        case 9:  map = Assets.map26;//Assets.manager.get(Assets.map9, TiledMap.class);
                 break;
        case 10: map = Assets.map23;//Assets.manager.get(Assets.map10, TiledMap.class);
                 break;
        case 11: map = Assets.map21;//Assets.manager.get(Assets.map11, TiledMap.class);
                 break;
        case 12: map = Assets.map18;//Assets.manager.get(Assets.map12, TiledMap.class);
                 break;
        case 13: map = Assets.map19;//Assets.manager.get(Assets.map13, TiledMap.class);
        		 break;
        case 14: map = Assets.map20;//Assets.manager.get(Assets.map14, TiledMap.class);
        		 break;
        case 15: map = Assets.map15;//Assets.manager.get(Assets.map15, TiledMap.class);
        		 break;
        case 16: map = Assets.map22;//Assets.manager.get(Assets.map16, TiledMap.class);
        		 break;
        case 17: map = Assets.map24;//Assets.manager.get(Assets.map17, TiledMap.class);
        		 break;
        case 18: map = Assets.map25;//Assets.manager.get(Assets.map18, TiledMap.class);
        		 break;
        case 19: map = Assets.map7;//Assets.manager.get(Assets.map19, TiledMap.class);
        		break;
        case 20: map = Assets.map13;//Assets.manager.get(Assets.map20, TiledMap.class);
        		break;
        case 21: map = Assets.map14;//Assets.manager.get(Assets.map21, TiledMap.class);
				break;
        case 22: map = Assets.map2;//Assets.manager.get(Assets.map22, TiledMap.class);
				break;
        case 23: map = Assets.map4;//Assets.manager.get(Assets.map23, TiledMap.class);
				break;
        case 24: map = Assets.map17;//Assets.manager.get(Assets.map24, TiledMap.class);
				break;
        case 25: map = Assets.map8;//Assets.manager.get(Assets.map25, TiledMap.class);
				break;
        case 26: map = Assets.map11;//Assets.manager.get(Assets.map26, TiledMap.class);
				break;
        case 27: map = Assets.map16;//Assets.manager.get(Assets.map27, TiledMap.class);
				break;
		}
		
		boolean solvedLevel = false;
		//nivoto sushtestvuva vuv faila
		if(Constants.preferences.contains("Level" + level)){			
			solvedLevel = Constants.preferences.getBoolean("Level" + level) == true ? true : false;
			if(solvedLevel){
				tbs.up = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("solved")));
				tbs.down = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("solvedClick")));
				//TiledMap 
				button.addListener(populateMaps(map,level));
			}
			//nivoto moje da se minava
			else{
				tbs.up = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("readyToBeSolved")));
				tbs.down = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("closedClick")));
				//TiledMap 
				button.addListener(populateMaps(map,level));
			}
		}
		//nivoto oshte go nqma vuv faila
		else{
			tbs.up = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("closedBlack")));
			tbs.down = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("closedClick")));
		}
		
        // Create the label to show the level number
        return button;
	}

	public ClickListener populateMaps(final String levelMap,final int level){		
		ClickListener levelClickListener = new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				//Constants.shopPreferences.remove(GameClass.productID_fullVersion);
				//Constants.shopPreferences.flush();
				//change to specific map
				if(adsController != null && (adsController.isWifiConnected() || adsController.isMobileDataEnabled())
					&& !Constants.shopPreferences.contains(GameClass.productID_fullVersion)){
					adsController.showInterstitialAd(new Runnable() {
						@Override
						public void run() {
							try {
								game.setScreen(new StageScreen(adsController,game,true,levelMap,level));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				}
				//desktop only or no ads
				else{					
					try {
						game.setScreen(new StageScreen(adsController,game,false,levelMap,level));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		return levelClickListener;
	}

	
	@Override
	public void render(float delta) {
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
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		stage.dispose();
	}	
}

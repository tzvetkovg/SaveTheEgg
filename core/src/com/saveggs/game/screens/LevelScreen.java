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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Interpolation;
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
	private Image gameTitle2;
	private Image arrow;
	GameStage gameStage;
	private AdsController adsController;
	private Image sprite;
	private MoveToAction moveAction;
	private MyActor ac;
	
	public LevelScreen(final AdsController adsController,final GameClass game){		
		this.game = game;
		this.adsController = adsController;
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
		public MyActor(){
			texture = Assets.manager.get(Assets.pticheta, Texture.class);	
			splitAnimation();
			animation = new Animation(1f/15f, animationFrames);
			setPosition(Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT * 0.5f);	
			setSize(getWidth(), getHeight());
		}
		
		 public void splitAnimation(){
			 TextureRegion[][] tmpFrames = TextureRegion.split(texture, 117, 88);
			 animationFrames = new TextureRegion[8];
			 int index = 0;		 
			 for (int i = 0; i < 1 ; ++i){
				 for (int j = 0; j < 8; ++j){
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
	        batch.draw(currentFrame,getX(),getY(),72.3f,54.45f);
		}
	}
	
	
	public void initializeScreen() {
		this.stage = new Stage(new ExtendViewport(Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT));
		//gameTitle = new Image(new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.levels, Texture.class))));
		gameTitle = new Image(Assets.manager.get(Assets.levels, Texture.class));
		//gameTitle.setPosition(Constants.SCENE_WIDTH / 2 - gameTitle.getWidth() / 2, Constants.SCENE_HEIGHT - 120);
		gameTitle.setPosition(Constants.SCENE_WIDTH / 2 - gameTitle.getWidth() / 2, Constants.SCENE_HEIGHT / 2 - gameTitle.getHeight() / 2);
		
		gameTitle2 = new Image(new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.levels, Texture.class))));
		gameTitle2.setPosition(Constants.SCENE_WIDTH / 2 - gameTitle2.getWidth() / 2, Constants.SCENE_HEIGHT - 120);
		
		Gdx.input.setInputProcessor(stage);

		container = new Table();
		
		container.setFillParent(true);		
		
		
         moveAction = new MoveToAction();
	     moveAction.setPosition(-Constants.SCENE_WIDTH, 30f);
	     moveAction.setDuration(15f);
		 ac = new MyActor();
		 ac.addAction(moveAction);
         container.addActor(ac);
		
        stage.addActor(gameTitle);
         
		PagedScrollPane scroll = new PagedScrollPane();
		scroll.setFlingTime(0.1f);
		scroll.setPageSpacing(25);
		int c = 1;
		for (int l = 0; l < 2; l++) {

			Table levels = new Table().pad(50);
			levels.defaults().pad(20, 50, 20, 50).size(70);
			//game titles
			if(l == 0){
            	//levels.addActor(gameTitle);
            }
            if(l == 1){
            	//levels.addActor(gameTitle2);
            }
			
			for (int y = 0; y < 3; y++) {
				levels.row();
				for (int x = 0; x < 4; x++) {
					System.out.println("c is " + c);
					levels.add(getLevelButton(c++)).expand().fill();
				}
			}
			scroll.addPage(levels);
		}
		container.add(scroll).expand().fill();
		
		
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
			}
		});

        container.addActor(button);
        //container.setBackground(new NinePatchDrawable(Constants.getNinePatch()));
        container.addAction(fadeIn(2f));
        //container.setScale(1);
        
         stage.addActor(container);
	}
	
	
	@Override
	public void show() {

    }

	public TextButton getLevelButton(int level) {
		        
		TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle();
		tbs.font =  Assets.manager.get(Assets.bitmapfont, BitmapFont.class);
		
		TextButton button = new TextButton("", tbs);
		tbs.font.getData().setScale(2f);
		
		TiledMap map = null;
		switch (level) {
        case 1:  map = Assets.manager.get(Assets.map1, TiledMap.class);
        		 break;
        case 2:  map = Assets.manager.get(Assets.map20, TiledMap.class);
                 break;
        case 3:  map = Assets.manager.get(Assets.map3, TiledMap.class);
                 break;
        case 4:  map = Assets.manager.get(Assets.map4, TiledMap.class);
                 break;
        case 5:  map = Assets.manager.get(Assets.map5, TiledMap.class);
                 break;
        case 6:  map = Assets.manager.get(Assets.map6, TiledMap.class);
                 break;
        case 7:  map = Assets.manager.get(Assets.map7, TiledMap.class);
                 break;
        case 8:  map = Assets.manager.get(Assets.map8, TiledMap.class);
                 break;
        case 9:  map = Assets.manager.get(Assets.map9, TiledMap.class);
                 break;
        case 10: map = Assets.manager.get(Assets.map10, TiledMap.class);
                 break;
        case 11: map = Assets.manager.get(Assets.map11, TiledMap.class);
                 break;
        case 12: map = Assets.manager.get(Assets.map12, TiledMap.class);
                 break;
        case 13: map = Assets.manager.get(Assets.map13, TiledMap.class);
        		 break;
        case 14: map = Assets.manager.get(Assets.map14, TiledMap.class);
        		 break;
        case 15: map = Assets.manager.get(Assets.map15, TiledMap.class);
        		 break;
        case 16: map = Assets.manager.get(Assets.map16, TiledMap.class);
        		 break;
        case 17: map = Assets.manager.get(Assets.map17, TiledMap.class);
        		 break;
        case 18: map = Assets.manager.get(Assets.map18, TiledMap.class);
        		 break;
        case 19: map = Assets.manager.get(Assets.map19, TiledMap.class);
        		break;
        case 20: map = Assets.manager.get(Assets.map2, TiledMap.class);
        		break;
		}
		
		boolean solvedLevel = false;
		//nivoto sushtestvuva vuv faila
		if(Constants.preferences.contains("Level" + level)){			
			solvedLevel = Constants.preferences.getBoolean("Level" + level) == true ? true : false;
			if(solvedLevel){
				tbs.up = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.solvedLevel, Texture.class)));
				tbs.down = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.solvedLevelClicked, Texture.class)));
				//TiledMap 
				button.addListener(populateMaps(map,level));
			}
			//nivoto moje da se minava
			else{
				tbs.up = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.readyToBeSolved, Texture.class)));
				tbs.down = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.levelButtonClicked, Texture.class)));
				//TiledMap 
				button.addListener(populateMaps(map,level));
			}
		}
		//nivoto oshte go nqma vuv faila
		else{
			tbs.up = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.levelButton, Texture.class)));
			tbs.down = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.levelButtonClicked, Texture.class)));
		}
		
        // Create the label to show the level number
        return button;
	}

	public ClickListener populateMaps(final TiledMap levelMap,final int level){		
		ClickListener levelClickListener = new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				//change to specific map
				if(adsController != null && (adsController.isWifiConnected() || adsController.isMobileDataEnabled())){
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
			 ac.setPosition(Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT * 0.5f);
			 moveAction.reset();
		     moveAction.setPosition(-Constants.SCENE_WIDTH, 30f);
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

	
	/*		this.stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
	Gdx.input.setInputProcessor(stage);
	container = new Table();
	gameTitle = new Image(new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.levels, Texture.class))));
	gameTitle.setPosition(Gdx.graphics.getWidth() / 2 - gameTitle.getWidth() / 2 + 50f, Gdx.graphics.getHeight() - gameTitle.getHeight() / 2 - 53f);
	
	gameTitle2 = new Image(new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.levels, Texture.class))));
	gameTitle2.setPosition(Gdx.graphics.getWidth() / 2 - gameTitle.getWidth() / 2 + 50f, Gdx.graphics.getHeight() - gameTitle.getHeight() / 2 - 53f);
	
	arrow = new Image(new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.arrow, Texture.class))));
	arrow.setPosition(Gdx.graphics.getWidth() - gameTitle.getWidth() / 4 , Gdx.graphics.getHeight() /2 - gameTitle.getHeight() / 2 - 150f);
	
    stage.addActor(container);

    container.setFillParent(true);
    
    PagedScrollPane scroll = new PagedScrollPane();

    scroll.setFlingTime(0.1f);

    int c = 1;

    for (int l = 0; l < 2; l++) {
    	
        Table levels = new Table().pad(50, Gdx.graphics.getWidth() / 3.5f, 50, Gdx.graphics.getWidth() / 4);
        levels.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
        if(l == 0){
        	levels.addActor(gameTitle);
        	levels.addActor(arrow);
        }
        if(l == 1){
        	levels.addActor(gameTitle2);
        	
        }
        //levels.add(gameTitle).expandX();
        //levels.addActor(gameTitle);

        for (int y = 0; y < 3; y++) {

            levels.row();
            for (int x = 0; x < 3; x++) {
                levels.add(getLevelButton(c++)).size(Gdx.graphics.getWidth() / 10 , Gdx.graphics.getHeight() / 10).pad(30f);
            }
        }
        scroll.addPage(levels);
    }
    
    
    container.add(scroll).expand().fill(); */
	
}

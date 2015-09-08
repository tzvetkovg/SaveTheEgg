package com.saveggs.game.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;

import java.util.Map;

import assets.Assets;

import com.admob.AdsController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.saveggs.game.GameClass;
import com.saveggs.game.GameStage;
import com.saveggs.utils.Constants;
import com.saveggs.utils.WorldUtils;

public class LevelScreen implements Screen{
	
	private Stage stage;
	private Table container;
	public final GameClass game;
	private Image gameTitle;
	private Image gameTitle2;
	private Image arrow;
	GameStage gameStage;
	private AdsController adsController;
		
	public LevelScreen(final AdsController adsController,final GameClass game){		
		this.game = game;
		this.adsController = adsController;
		
		this.stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
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
        
        
        container.add(scroll).expand().fill(); 
        
        //back button
		TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle();
		tbs.font =  Assets.manager.get(Assets.bitmapfont, BitmapFont.class);
		
		tbs.up = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.buttonUpTex, Texture.class)));
		tbs.over = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.buttonOverTex, Texture.class)));
		tbs.down = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.buttonDownTex, Texture.class)));

		TextButton button = new TextButton("back", tbs);
		tbs.font.getData().setScale(2f);
		button.setPosition(0, 0);
		button.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
			   game.setScreen(new MainMenuScreen(adsController,game));
			}
		});
		
        container.addActor(button);
        container.setBackground(new NinePatchDrawable(Constants.getNinePatch()));
        container.addAction(fadeIn(2f));
	}
		
	
	@Override
	public void show() {

    }

	public TextButton getLevelButton(int level) {
		        
		TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle();
		tbs.font =  Assets.manager.get(Assets.bitmapfont, BitmapFont.class);
		
		tbs.up = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.buttonUpTex, Texture.class)));
		tbs.over = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.buttonOverTex, Texture.class)));
		tbs.down = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.buttonDownTex, Texture.class)));

		TextButton button = new TextButton(level + "", tbs);
		tbs.font.getData().setScale(2f);
		
			TiledMap map = null;
			switch (level) {
	        case 1: map = Assets.manager.get(Assets.map4, TiledMap.class);
	        		break;
	        case 2:  map = Assets.manager.get(Assets.map2, TiledMap.class);
	                 break;
	        case 3:  map = Assets.manager.get(Assets.map3, TiledMap.class);
	                 break;
	        case 4:  map = Assets.manager.get(Assets.map4, TiledMap.class);
	                 break;
	        case 5:  map = Assets.manager.get(Assets.map4, TiledMap.class);
	                 break;
	        case 6:  map = Assets.manager.get(Assets.map4, TiledMap.class);
	                 break;
	        case 7:  map = Assets.manager.get(Assets.map4, TiledMap.class);
	                 break;
	        case 8:  map = Assets.manager.get(Assets.map4, TiledMap.class);
	                 break;
	        case 9:  map = Assets.manager.get(Assets.map4, TiledMap.class);
	                 break;
	        case 10: map = Assets.manager.get(Assets.map4, TiledMap.class);
	                 break;
	        case 11: map = Assets.manager.get(Assets.map4, TiledMap.class);
	                 break;
	        case 12: map = Assets.manager.get(Assets.map4, TiledMap.class);
	                 break;
	        case 13: map = Assets.manager.get(Assets.map4, TiledMap.class);
	        		 break;
	        case 14: map = Assets.manager.get(Assets.map4, TiledMap.class);
	        		 break;
	        case 15: map = Assets.manager.get(Assets.map4, TiledMap.class);
	        		 break;
	        case 16: map = Assets.manager.get(Assets.map4, TiledMap.class);
	        		 break;
	        case 17: map = Assets.manager.get(Assets.map4, TiledMap.class);
	        		 break;
	        case 18: map = Assets.manager.get(Assets.map4, TiledMap.class);
	        		 break;
			}
		
		//TiledMap 
		button.addListener(populateMaps(map));
		
        // Create the label to show the level number
        return button;
	}

	public ClickListener populateMaps(final TiledMap levelMap){		
		ClickListener levelClickListener = new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				//change to specific map
				if(adsController != null && (adsController.isWifiConnected() || adsController.isMobileDataEnabled())){
					adsController.showInterstitialAd(new Runnable() {
						@Override
						public void run() {
							game.setScreen(new StageScreen(adsController,game,true,levelMap));
						}
					});
				}
				//desktop only or no ads
				else
					game.setScreen(new StageScreen(adsController,game,false,levelMap));
			}
		};
		return levelClickListener;
	}

	
	@Override
	public void render(float delta) {
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

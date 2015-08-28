package com.saveggs.game.screens;

import java.util.Map;

import assets.Assets;

import com.admob.AdsController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.saveggs.game.GameClass;
import com.saveggs.game.GameStage;
import com.saveggs.utils.WorldUtils;

public class LevelScreen implements Screen{
	
	private Stage stage;
	private Table container;
	public GameClass game;
	private Image gameTitle;
	GameStage gameStage;
	private Map<String,Object> mapBodies;
	private World world;
	private AdsController adsController;
		
	public LevelScreen(AdsController adsController,GameClass game,Map<String,Object> mapBodies,World world){		
		this.game = game;

		this.world = world;
		this.mapBodies = mapBodies;
		this.adsController = adsController;
		
		this.stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		Gdx.input.setInputProcessor(stage);
		container = new Table();
		gameTitle = new Image(new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.gameTitleL, Texture.class))));
		
        stage.addActor(container);

        container.setFillParent(true);
        
        PagedScrollPane scroll = new PagedScrollPane();

        scroll.setFlingTime(0.1f);

        //scroll.setPageSpacing(200f);

        int c = 1;

        for (int l = 0; l < 2; l++) {
        	
            Table levels = new Table().pad(50, 130, 50, 150);
            levels.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            
            //levels.add(gameTitle).expandX().spaceBottom(10f);
            //levels.row();
            
            //levels.defaults().pad(20, 40, 20, 40);


            for (int y = 0; y < 3; y++) {

                levels.row();

                for (int x = 0; x < 4; x++) {
                        
                    levels.add(getLevelButton(c++)).size(Gdx.graphics.getWidth() / 4 - 100f , Gdx.graphics.getHeight() / 3  - 100f).pad(20f);
                }
            }
            scroll.addPage(levels);
        }
        container.add(scroll).expand().fill(); 

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
		button.addListener(levelClickListener);  
        // Create the label to show the level number
        return button;
	}
	//public  GameClass gameche = new GameClass(adsController);
	
	public ClickListener levelClickListener = new ClickListener() {
	    @Override
	    public void clicked (InputEvent event, float x, float y) {
			adsController.showInterstitialAd(new Runnable() {
		        @Override
		        public void run() {
		        	game.setScreen(new StageScreen(adsController,game,mapBodies,world));
		        }
		    });
	    }
	};

	
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

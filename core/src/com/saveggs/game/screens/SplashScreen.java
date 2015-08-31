package com.saveggs.game.screens;

import java.util.HashMap;
import java.util.Map;

import assets.Assets;

import com.admob.AdsController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.TideMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.sageggs.actors.CreateMesh;
import com.sageggs.actors.CreateMesh2;
import com.sageggs.actors.DynamicBall;
import com.sageggs.actors.Slingshot;
import com.sageggs.actors.enemies.Enemy;
import com.sageggs.actors.enemies.EnemyOtherSide;
import com.sageggs.actors.flyingbirds.FlyingBirds;
import com.sageggs.actors.flyingbirds.FlyingBirds2;
import com.sageggs.actors.particles.ParticleEffectAn;
import com.sageggs.actors.particles.ParticleEffectBall;
import com.sageggs.actors.particles.ParticleEffectFlyingBird;
import com.sageggs.actors.particles.ParticleIzlupvane;
import com.sageggs.actors.particles.PruskaneQice;
import com.saveggs.game.GameClass;
import com.saveggs.utils.Constants;
import com.saveggs.utils.ShaderSpec;
import com.saveggs.utils.WorldUtils;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class SplashScreen implements Screen {
	
	GameClass game;
	private Stage stage;
	private Image image;
	private OrthographicCamera camera;
	private WorldUtils utils;
	private Map<String,Object> worldBodies;
	private ShaderProgram shader;
	private World world;
	private DynamicBall staticBall;
	private FlyingBirds2 flyingBird2;
	private AdsController adsController;
	private Skin skin;
	
	public SplashScreen(final AdsController adsController,GameClass game){

		this.game = game;
		
		this.stage = new Stage(new ExtendViewport(Constants.SCENE_WIDTH / 35, Constants.SCENE_HEIGHT / 35));
		this.adsController = adsController;
		camera = new OrthographicCamera();
		stage.getViewport().setCamera(camera);
		
		Gdx.input.setInputProcessor(stage);
		Sprite splash = new Sprite(new Texture(Gdx.files.internal("data/caveman.png")));
		
		image = new Image(splash);
		image.setSize(splash.getWidth() / 100, splash.getHeight() / 100);
		image.setOrigin(splash.getWidth()/ 35 / 2, splash.getHeight()/ 35 / 2);
		image.addAction(sequence(alpha(0),scaleTo(.1f,.1f),
				parallel(fadeIn(2f, Interpolation.pow2),
						scaleTo(2f,2f,2.5f, Interpolation.pow5),
						moveTo(1f,1f, 2f ,Interpolation.swing)),
				delay(1.5f), fadeOut(1.25f)));
		stage.addActor(image);
		
		
		Assets asset = new Assets();
		Assets.manager.load(Assets.class);		
		
		while(!Assets.manager.update()){
			System.out.println(Assets.manager.getProgress());
		}
		Assets.manager.finishLoading();
				
		
/*		worldBodies = new HashMap<String,Object>();
		
		//skin 
		//worldBodies.put("skin",new Skin(Gdx.files.internal("data/uiskin.json")));
		
		//shaderstaticBall
		shader = new ShaderProgram(ShaderSpec.vertexShader, ShaderSpec.fragmentShader);	
		//world
		world = new World(new Vector2(0,-9.8f), true);
		//mesh1
		worldBodies.put("mesh", new CreateMesh(WorldUtils.createMesh(),shader));
		
		//static ball
		staticBall = new DynamicBall(WorldUtils.createDynamicBall(world));
		worldBodies.put("staticBall", new DynamicBall(WorldUtils.createDynamicBall(world)));

		//draw 2nd mesh
		worldBodies.put("mesh2", new CreateMesh2(WorldUtils.createMesh2(),shader));
	
		//slingshot
		worldBodies.put("slingshot",  new Slingshot(WorldUtils.createSlingshot(world)));
		
		//bird1
		worldBodies.put("flyingBird",  new FlyingBirds(WorldUtils.createFlyingBird(world)));
		
		//bird2
		flyingBird2 = new FlyingBirds2(WorldUtils.createFlyingBird2(world));
		flyingBird2.animatedBox2DSprite.flipFrames(true, false);
		worldBodies.put("flyingBird2",  flyingBird2);
		
		
		//PArticle effects
		worldBodies.put("particleEffect",  new ParticleEffectAn());
		worldBodies.put("particleBall",  new ParticleEffectBall());
		worldBodies.put("particleIzlupvane",  new ParticleIzlupvane());
		worldBodies.put("flyingBirdParticle",  new ParticleEffectFlyingBird());
		worldBodies.put("pruskane",  new PruskaneQice());*/
		
		//load interstitial ad
		if(adsController != null && (adsController.isWifiConnected() || adsController.isMobileDataEnabled())){					
			adsController.loadInterstitialAd();
		}
		//initialize screens
	}
	
	@Override
	public void show() {}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
		
		if(image.getActions().size == 0){
			game.setScreen(new MainMenuScreen(adsController,game));
			//game.setScreen(new MainMenuScreen(game));
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

}

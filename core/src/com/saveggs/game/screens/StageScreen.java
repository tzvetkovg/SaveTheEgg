package com.saveggs.game.screens;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import assets.Assets;

import com.admob.AdsController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.sageggs.actors.CreateMesh;
import com.sageggs.actors.CreateMesh2;
import com.sageggs.actors.DynamicBall;
import com.sageggs.actors.flyingbirds.FlyingBirds;
import com.sageggs.actors.flyingbirds.FlyingBirds2;
import com.sageggs.actors.particles.ParticleEffectAn;
import com.sageggs.actors.particles.ParticleEffectBall;
import com.sageggs.actors.particles.ParticleEffectFlyingBird;
import com.sageggs.actors.particles.ParticleIzlupvane;
import com.sageggs.actors.particles.PruskaneQice;
import com.saveggs.game.GameClass;
import com.saveggs.game.GameStage;
import com.saveggs.utils.Constants;
import com.saveggs.utils.ShaderSpec;
import com.saveggs.utils.WorldUtils;


public class StageScreen implements Screen {

	public GameClass eggSaver;
	private Stage stage = null;
	public AdsController adsController;
	private TiledMap map;
	private Map<String,Object> worldBodies;
	private ShaderProgram shader;
	private World world;
	private FlyingBirds2 flyingBird2;
	private int currentLevel;
	private Table table;
	private Slider slider;
	boolean internetUse = false;
	boolean weapon1 = false;
	boolean weapon2 = false;
	boolean weapon3 = false;
	GameClass game;
	float enemySpeed = 0;
	int ads = 0;
	int numberOfKillings = 0;
	String currentMap;
	
	public StageScreen(final AdsController adsController, final GameClass game,final boolean internetEnabled,String map,int level) throws IOException{
		this.eggSaver = game;
		
		
		this.game = game;
		currentMap = map;
		//this.map = map;
		this.currentLevel = level;
		
		this.worldBodies = new HashMap<String,Object>();
		
		//set level data
		Element root =  new XmlReader().parse(Gdx.files.internal("data/gameLevels.xml"));
		Element levelDetails = root.getChildByName("Level" + level);
		Constants.ENEMYSPEED = Float.parseFloat(levelDetails.getChildByName("enemyspeed").getText());
		Constants.FlYINGBIRDVELOCITY = Float.parseFloat(levelDetails.getChildByName("flyingenemyspeed").getText());
		/**
		 * initialize some of the world objects
		 */
		//skin
		this.worldBodies.put("skin", Assets.manager.get(Assets.skin, Skin.class));
		//shaderstaticBall
		shader = new ShaderProgram(ShaderSpec.vertexShader, ShaderSpec.fragmentShader);	
		//world
		this.world = new World(new Vector2(0,-9.8f), true);
		//mesh1
		this.worldBodies.put("mesh", new CreateMesh(WorldUtils.createMesh(),shader));
		
		//draw 2nd mesh
		this.worldBodies.put("mesh2", new CreateMesh2(WorldUtils.createMesh2(),shader));
		
		//static ball
		this.worldBodies.put("staticBall", new DynamicBall(WorldUtils.createDynamicBall(this.world)));
		
		//slingshot
		//this.worldBodies.put("slingshot",  new Slingshot(WorldUtils.createSlingshot(this.world)));
		
		//bird1
		this.worldBodies.put("flyingBird",  new FlyingBirds(WorldUtils.createFlyingBird(this.world)));
		
		//bird2
		flyingBird2 = new FlyingBirds2(WorldUtils.createFlyingBird2(this.world));
		flyingBird2.animatedBox2DSprite.flipFrames(true, false);
		this.worldBodies.put("flyingBird2",  flyingBird2);
		
		//PArticle effects
		this.worldBodies.put("particleEffect",  new ParticleEffectAn());
		this.worldBodies.put("particleBall",  new ParticleEffectBall());
		this.worldBodies.put("particleIzlupvane",  new ParticleIzlupvane());
		this.worldBodies.put("flyingBirdParticle",  new ParticleEffectFlyingBird());
		this.worldBodies.put("pruskane",  new PruskaneQice());
		
		this.adsController = adsController;	
		Constants.sound.stop();
		
		enemySpeed = Float.parseFloat(levelDetails.getChildByName("enemyspeed").getText());
		ads = Integer.parseInt(levelDetails.getChildByName("ads").getText());
		numberOfKillings = Integer.parseInt(levelDetails.getChildByName("launchbothenemies").getText());
		
		boolean internetUse = (internetEnabled && !Constants.shopPreferences.contains(GameClass.productID_fullVersion));
		boolean weapon1 = Constants.shopPreferences.contains(GameClass.automaticdestruction);
		boolean weapon2 = Constants.shopPreferences.contains(GameClass.slowdown);
		boolean weapon3 = Constants.shopPreferences.contains(GameClass.fastball);
		
		System.out.println("weapon1 " + weapon1 );
		System.out.println("weapon2 " + weapon2 );
		System.out.println("weapon3 " + weapon3 );
		
		Assets.manager.load(map, TiledMap.class);
		Assets.manager.finishLoadingAsset(map);;
		
		//if((currentMap, TiledMap.class)){
			this.stage = new GameStage(adsController,this.worldBodies,this.world,internetUse,game,Assets.manager.get(map, TiledMap.class),currentLevel,enemySpeed,
					weapon1,weapon2,weapon3,ads,map,numberOfKillings);
		//}
		
		
		
/*		
		this.stage = new GameStage(adsController,this.worldBodies,this.world,internetUse,game,this.map,currentLevel,Float.parseFloat(levelDetails.getChildByName("enemyspeed").getText()),
									weapon1,weapon2,weapon3,Integer.parseInt(levelDetails.getChildByName("ads").getText()));	*/
	}


	@Override
	public void show() {}


	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
		this.stage.act(delta);
		this.stage.draw();

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
	public void resize(int width, int height) {;
		this.stage.getViewport().update(width, height,false);
		this.stage.getCamera().viewportWidth = Constants.SCENE_WIDTH / 30f;
		this.stage.getCamera().viewportHeight = Constants.SCENE_HEIGHT / 30f;
		this.stage.getCamera().position.set(this.stage.getCamera().viewportWidth / 2f, this.stage.getCamera().viewportHeight / 2f, 0); 
		this.stage.getCamera().update();	
		
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

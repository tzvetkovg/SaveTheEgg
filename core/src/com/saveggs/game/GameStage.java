package com.saveggs.game;

import java.util.HashMap;
import java.util.Map;

import assets.Assets;

import com.admob.AdsController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sageggs.actors.CreateMesh;
import com.sageggs.actors.CreateMesh2;
import com.sageggs.actors.CurrentMap;
import com.sageggs.actors.DynamicBall;
import com.sageggs.actors.DynamicBall.DynamicBallPool;
import com.sageggs.actors.Qice;
import com.sageggs.actors.Slingshot;
import com.sageggs.actors.enemies.Enemy;
import com.sageggs.actors.enemies.EnemyOtherSide;
import com.sageggs.actors.flyingbirds.FlyingBirds;
import com.sageggs.actors.flyingbirds.FlyingBirds2;
import com.sageggs.actors.loadingscreen.LoadingScreen;
import com.sageggs.actors.particles.ParticleEffectAn;
import com.sageggs.actors.particles.ParticleEffectBall;
import com.sageggs.actors.particles.ParticleEffectFlyingBird;
import com.sageggs.actors.particles.ParticleIzlupvane;
import com.sageggs.actors.particles.PruskaneQice;
import com.saveggs.game.screens.LevelScreen;
import com.saveggs.utils.BodyUtils;
import com.saveggs.utils.Constants;
import com.saveggs.utils.WorldUtils;

public class GameStage extends Stage implements ContactListener{
	public Vector2 middlePoint,clickedPoint;
	public Viewport viewport;
	public Box2DDebugRenderer debugRenderer;
	public World world;
	private FPSLogger logger;
	private Qice qice;
	private DynamicBall dynamicBall;
	private DynamicBall dynamicBall2;
	private DynamicBall staticBall;
	private Slingshot slingshot;
	private Enemy enemy;
	private EnemyOtherSide enemyOtherSide;
	private OrthographicCamera camera;
	private CreateMesh mesh;
	private CreateMesh2 mesh2;
	//Touch position
	private	Vector3 position = new Vector3();
	private Vector3 touchDown = new Vector3();
	private Array<Body> bodies;
	private Array<Body> bodiesOfWorld;
	private Array<Qice> eggs;
	private Map<String,Vector2> worldBodies;
	public Array<DynamicBall> sleeepingBalls;
	public Array<FlyingBirds> destroyFlyingBirds;
	public Array<FlyingBirds2> destroyFlyingBirds2;
	private ParticleEffectAn particleEffect;
	private ParticleEffectBall particleBall;
	private ParticleEffectFlyingBird flyingBirdParticle;
	private ParticleIzlupvane particleIzlupvane;
	private DynamicBallPool pool;
	private FlyingBirds flyingBird;
	private FlyingBirds2 flyingBird2;
	private PruskaneQice pruskane;
	private Map<String,Object> mapBodies;
	private AdsController adsController;
	private int timeIntervalAds = 0,timeAds = 32,numberOfEnemyKillings = 0,launchBothEnemies = 0,dialogAppearTimes = 1;
	public boolean showGame = false, internetEnabled = false, showAd = false;
	private LoadingScreen loading;
	private Skin skin;
	private GameClass game;
	private TiledMap map;
	private Dialog dialog,pauseMenu;
	private Array<Qice> allEggs,izlupeni,vzeti,uduljavane;
	private Array<Vector2> positionsOfQica;
	private Label label;
	private float distance, myX,myY;
	private int currentLevel;
	private Slider slider;
	private Table table;
	private boolean buttonClicked = false,musicMuted = false,weaponOne = false,weaponOneTimeExpired = false,weaponTwoTimeExpiredweaponTwo = false, weaponThreeTimeExpiredweaponThree = false,weapon1Enabled = false,weapon2Enabled= false,weapon3Enabled = false;
	private Music music1;
	private Sound breakingEgg,destroyEnemey;
	private TextButton button,pause,weaponButton1,weaponButton2,weaponButton3;
	private float enemyLevelSpeed;
	final TextureRegionDrawable weaponOneStyle = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.destroyEnemyArrow, Texture.class)));
	final TextureRegionDrawable weaponOneClicked = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.destroyEnemyArrowClicked, Texture.class)));
	final TextureRegionDrawable weaponTwoStyle = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.slowDownBird, Texture.class)));
	final TextureRegionDrawable weaponTwoClicked = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.slowDownBirdClicked, Texture.class)));
	final TextureRegionDrawable weaponThreeStyle = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.fastBallSpeed, Texture.class)));
	final TextureRegionDrawable weaponThreeClicked = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.fastBallSpeedClicked, Texture.class)));
	final TextButton.TextButtonStyle weaponButtonOneStyle = new TextButton.TextButtonStyle();
	final TextButton.TextButtonStyle weaponButtonTwoStyle = new TextButton.TextButtonStyle();
	final TextButton.TextButtonStyle weaponButtonThreeStyle = new TextButton.TextButtonStyle();
	String mapPath;
	
	public GameStage(AdsController adsController,Map<String,Object> mapBodies,World world,boolean internetEnabled,GameClass game,TiledMap map, int currentLevel,float enemyLevelSpeed,boolean weapon1, boolean weapon2, boolean weapon3,int timeAds,String mapPath,int numberOfKillings){
		super(new ExtendViewport(Constants.SCENE_WIDTH / 35, Constants.SCENE_HEIGHT / 35, new OrthographicCamera()));
		this.game = game;
		this.mapBodies = mapBodies;
		this.world = world;
		this.map = map;
		this.internetEnabled = internetEnabled;
		this.adsController = adsController;		
		this.currentLevel = currentLevel;
		this.enemyLevelSpeed = enemyLevelSpeed;
		this.weapon1Enabled = weapon1;
		this.weapon2Enabled = weapon2;
		this.weapon3Enabled = weapon3;
		this.timeAds = timeAds;
		this.launchBothEnemies = numberOfKillings;
		this.mapPath = mapPath;
		setupCamera();
		getViewport().setCamera(camera);
		setUtils();
		setupWorld();
		Gdx.input.setInputProcessor(this);
		//GLProfiler.enable();
		
		//display loading screen initially
		Timer.schedule(new Task(){
            @Override
            public void run() {
            	showGame = true;
            	loading.draw = false;
            	adjustMusic(1);
            	music1.play();
        		music1.setLooping(true);
            }
        },3);
		
	}
		
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		camera.unproject(touchDown.set(screenX, screenY, 0));
		//System.out.println("touchpos" + " " +touchDown.x + " " + touchDown.y);		
		return super.touchDown(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		
		if(showGame && !buttonClicked){			
			camera.unproject(position.set(screenX, screenY, 0));

			clickedPoint.x = position.x;
			clickedPoint.y = position.y;
			myX = position.x;
			myY = position.y;
			//not allow lastik da se raztqga sled opredeleno polojenie
	        if(clickedPoint.sub(middlePoint).len() > 2.1){	        	
	        	float angleFromMiddlePoint = (float) Math.toDegrees(Math.atan2(myY - Constants.middleY, myX - Constants.middleX));
	        	float rad = (float) angleFromMiddlePoint * MathUtils.degreesToRadians;
	        	//System.out.println(distance);
	        	myX = (float) (Constants.middleX + distance * (Math.cos(rad)));
	        	myY = (float) (Constants.middleY + distance * (Math.sin(rad)));
	        }
	        
        	//Update mesh coordss
        	mesh.calculateXYLastikOne(myX,myY);
        	mesh.drawBody = true;
        	//Update mesh2 coords
        	mesh2.calculateXYLastikTwo(myX,myY);
        	mesh2.drawBody = true;
        	//Move the static ball on lastik - 0.4f
        	staticBall.setBodyPosOnLastik(myX, myY);
		}

		return super.touchDragged(screenX, screenY, pointer);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(showGame && !buttonClicked){			
			//remove lasticite
			mesh.drawBody = false;
			mesh2.drawBody = false;
			
			if(weaponOne){				
				Body enemyBody1 = enemy.enemyDraw ? enemy.body : null;
				if(enemyBody1 != null){
					applyForceToBall(dynamicBall, enemyBody1);
				}
				
				Body enemyBody2 = enemyOtherSide.enemyDraw ? enemyOtherSide.body : null;
				if(enemyBody2 != null){
					applyForceToBall(dynamicBall2, enemyBody2);
				}
			}
			else
			{
				applyForceToBall(dynamicBall, null);
			}
/*			dynamicBall = pool.obtain();
			sleeepingBalls.add(dynamicBall);
			//add to stage	    
			dynamicBall.body.setTransform(staticBall.body.getPosition(), 0);
			dynamicBall.draw = true;
			
			//Apply force
			dynamicBall.applyForceToNewBody(null);			
			addActor(dynamicBall);*/
			//transform old body to initial position
			staticBall.body.setTransform(Constants.middleX, Constants.middleY, 0);
		}
	    
		return super.touchUp(screenX, screenY, pointer, button);
	}

	//apply force to the ball
	public void applyForceToBall(DynamicBall ball, Body enemy){
		ball = pool.obtain();
		sleeepingBalls.add(ball);
		//add to stage	    
		ball.body.setTransform(staticBall.body.getPosition(), 0);
		ball.draw = true;
		
		//Apply force
		ball.applyForceToNewBody(enemy);	
		if(enemy != null)
			ball.body.setSleepingAllowed(false);
		addActor(ball);
	}
	
	
	@Override
	public void act(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		super.act(delta);
		
		if(showGame){
			world.step(1 / 60f,6, 2);
			//destroy bodies if out of range
			destroyFlyingBirds();
			destroySleepingBalls();
			resetEnemyIfOutOfBounds();
			removeIzlupeniQica();
			displayScreen();
			izpuleniQica();
			logger.log();
			//System.out.println("texture bindings " + GLProfiler.textureBindings);
			//System.out.println("draw calls " + GLProfiler.drawCalls);
			//GLProfiler.reset();
    		//System.out.println("internetEnabled " + internetEnabled);
		}
		//debugRenderer.render(world,camera.combined);

		
	}
	
	
	//Set up camera
	public void setupCamera(){
        camera = new OrthographicCamera();
	}
	
	//Set up world
	public void setupWorld(){
		
		world.setContactListener(this);

		CurrentMap map = new CurrentMap(this.map,world);
		addActor(map);		
		
		bodiesOfWorld = new Array<Body>();
		world.getBodies(bodiesOfWorld);
		int timeOfIzlupvane = 35;
		//map of all bodies
		for (Body bodyLoop : bodiesOfWorld) {
			//get bodies
			worldBodies.put(bodyLoop.getUserData().toString(), bodyLoop.getPosition());
			//get qica
			if(bodyLoop.getUserData().equals(Constants.GROUND)) {
				//System.out.println("ground is registered");
			}
			
			if(bodyLoop.getUserData().equals(Constants.QICE)) {
				//neizplupeno qice
				bodyLoop.setAwake(false);
				bodyLoop.setBullet(false);
				//get initial position of bodies
				positionsOfQica.add(new Vector2(bodyLoop.getPosition()));
				qice = new Qice(bodyLoop,timeOfIzlupvane);
				
				eggs.add(qice);
				allEggs.add(qice);
				timeOfIzlupvane += 35;
				addActor(qice);
			
			}
			//slingshot position
			if(bodyLoop.getUserData().equals("sling")){
				slingshot = new Slingshot(bodyLoop);//(Slingshot)mapBodies.get("slingshot");
			}
			//mesh1 position
			if(bodyLoop.getUserData().equals("middleSlingshot1")){
				Constants.mesh1MiddleY = bodyLoop.getPosition().y;
				Constants.mesh1MiddleX = bodyLoop.getPosition().x;
			}
			//mesh2 position
			if(bodyLoop.getUserData().equals("middleSlingshot2")){
				Constants.mesh2MiddleY = bodyLoop.getPosition().y;
				Constants.mesh2MiddleX = bodyLoop.getPosition().x;
			}
			//ball position
			if(bodyLoop.getUserData().equals("middleBall")){
				Constants.middleY = bodyLoop.getPosition().y;
				Constants.middleX = bodyLoop.getPosition().x;
				//substract clicked pos - middle position
		        middlePoint.x = Constants.middleX;
		        middlePoint.y = Constants.middleY;
		        distance = middlePoint.dst(middlePoint) + 2.1f;
			}
		}
		
		mesh = (CreateMesh)mapBodies.get("mesh");
		addActor(mesh);
		//static ball

		staticBall = (DynamicBall)mapBodies.get("staticBall");
		staticBall.body.setGravityScale(0f);
		staticBall.body.setTransform(Constants.middleX, Constants.middleY,0);
		addActor(staticBall);
		
		//draw 2nd mesh
		mesh2 = (CreateMesh2)mapBodies.get("mesh2");
		addActor(mesh2);
		//slingshot
		//slingshot = (Slingshot)mapBodies.get("slingshot");
		addActor(slingshot);
		
		//enemy
		enemyOtherSide = new EnemyOtherSide(WorldUtils.createEnemyOtherSide(world),eggs,worldBodies);
		addActor(enemyOtherSide);
		enemy = new Enemy(WorldUtils.createEnemy(world),eggs,worldBodies);
		addActor(enemy);
		startEnemyTwo();

		//bird1
		flyingBird = (FlyingBirds)mapBodies.get("flyingBird");
		addActor(flyingBird);
		FlyingBirds.pointBodyToAngle(135f, flyingBird.body);
		destroyFlyingBirds.add(flyingBird);
		
		//bird2
		flyingBird2 = (FlyingBirds2)mapBodies.get("flyingBird2");
		addActor(flyingBird2);
		FlyingBirds2.pointBodyToAngle(135f, flyingBird2.body);
		destroyFlyingBirds2.add(flyingBird2);
		
		//PArticle effects
		particleEffect = (ParticleEffectAn)mapBodies.get("particleEffect");
		addActor(particleEffect);
		particleBall = (ParticleEffectBall)mapBodies.get("particleBall");
		addActor(particleBall);
		particleIzlupvane =  (ParticleIzlupvane)mapBodies.get("particleIzlupvane");
		addActor(particleIzlupvane);
		flyingBirdParticle = (ParticleEffectFlyingBird)mapBodies.get("flyingBirdParticle");
		addActor(flyingBirdParticle);
		pruskane = (PruskaneQice)mapBodies.get("pruskane");
		addActor(pruskane);
		
		//slider
		addActor(table);
		//music
		addActor(button);
		//pause
		addActor(pause);
		
		//weapons
		if(weapon1Enabled){			
			addActor(weaponButton1);
		}
		if(weapon2Enabled){			
			addActor(weaponButton2);
		}
		if(weapon3Enabled){			
			addActor(weaponButton3);
		}
		
		//loading screen
		loading = new LoadingScreen();
		addActor(loading);
	}
	
	//replay/reset the stage
	public void resetStage(){
		eggs.clear();
		izlupeni.clear();
		vzeti.clear();
		uduljavane.clear();
		
		//reset eggs initial position
		int i = 0;
		for (Vector2 position: positionsOfQica){
			allEggs.get(i).resetInitialPositionQice(position);
			eggs.add(allEggs.get(i));
//			System.out.println("i " + i);
			i++;
		}

		for (DynamicBall ball : sleeepingBalls) {
			ball.body.setAwake(false);
		}
		
		// reset
		numberOfEnemyKillings = 0;
		
		//weapons
		weaponOne = false;
		weaponOneTimeExpired = false;
		weaponTwoTimeExpiredweaponTwo = false;
		weaponThreeTimeExpiredweaponThree = false;
		weaponButtonOneStyle.up = weaponOneStyle;
		weaponButtonTwoStyle.up = weaponTwoStyle;
		weaponButtonThreeStyle.up = weaponThreeStyle;
		
		
		//reset ball speed
		slider.setValue(3);
		Constants.ballSpeed = 14f;
		
		//reset enemies
		enemyOtherSide.anyEggsLeft=true;
		enemy.anyEggsLeft = true;
		launchEnemy();
	}
	
	//utils
	public void setUtils(){
		debugRenderer = new Box2DDebugRenderer();
		bodies = new Array<Body>();
		sleeepingBalls = new Array<DynamicBall>();
		destroyFlyingBirds = new Array<FlyingBirds>();
		destroyFlyingBirds2 = new Array<FlyingBirds2>();
		pool = new DynamicBallPool();
		logger = new FPSLogger();
		eggs = new Array<Qice>();
		worldBodies = new HashMap<String,Vector2>();
		allEggs = new Array<Qice>();
		izlupeni = new Array<Qice>();
		vzeti = new Array<Qice>();
		uduljavane = new Array<Qice>();
		positionsOfQica = new Array<Vector2>();
		skin = (Skin)mapBodies.get("skin");
		middlePoint = new Vector2();
		clickedPoint = new Vector2();
		/**
		 * music
		 */
		music1 = Assets.manager.get(Assets.birdScream, Music.class);
		destroyEnemey = Assets.manager.get(Assets.dyingBird, Sound.class);
		breakingEgg = Assets.manager.get(Assets.breakingEgg, Sound.class);
		adjustMusic(0);
		
		/*
		 * pause button
		 */
		final TextButton.TextButtonStyle tbs1 = new TextButton.TextButtonStyle();
		tbs1.font =  Assets.manager.get(Assets.bitmapfont, BitmapFont.class);
		tbs1.font.getData().setScale(0.01f);
		final TextureRegionDrawable continueBut = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.pause, Texture.class)));
		final TextureRegionDrawable continueButDown = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.pauseDown, Texture.class)));
		tbs1.up = continueBut;
		tbs1.down = continueButDown;
		pause = new TextButton("back", tbs1);
		pause.setSize(1.5f, 1.5f);
		pause.setOrigin(Constants.SCENE_WIDTH / 30f * 0.5f, Constants.SCENE_HEIGHT / 30f * 0.5f);
		pause.setPosition(pause.getX() + 1, pause.getY() + 14.7f);
		
		
		pause.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				
				for (Qice qice : allEggs) {
					qice.pause();
		        }
				
				buttonClicked = true;
				showGame = false;
				adjustMusic(0);
				pauseMenu.setScale(.03333f);
				pauseMenu.show(pause.getStage());
				pauseMenu.setOrigin(pause.getStage().getWidth() * 0.4f , 
									pause.getStage().getHeight() / 2);

				return super.touchDown(event, x, y, pointer, button);
			}
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				buttonClicked = false;
				super.touchUp(event, x, y, pointer, button);
			}
		});
		
		/**
		 * Weapons
		 */
		weaponButtonOneStyle.font =  Assets.manager.get(Assets.bitmapfont, BitmapFont.class);
		weaponButtonOneStyle.font.getData().setScale(0.01f);
		weaponButtonOneStyle.up = weaponOneStyle;
		weaponButtonOneStyle.down = weaponOneClicked;
		weaponButton1 = new TextButton("", weaponButtonOneStyle);
		weaponButton1.setSize(1.25f, 1.25f);
		weaponButton1.setOrigin(0, 0);
		weaponButton1.setPosition(0, Constants.SCENE_HEIGHT / 30f * 0.84f);
		weaponButton1.addListener(new ClickListener() {
			@Override
			
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				buttonClicked = true;
				if(!weaponOneTimeExpired){		
					weaponButtonOneStyle.up = weaponOneClicked;
					GameStage.this.weaponOne = true;
				}
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				buttonClicked = false;
				//display loading screen initially
				if(!weaponOneTimeExpired){					
					Timer.schedule(new Task(){
						@Override
						public void run() {
							GameStage.this.weaponOne = false;
							weaponOneTimeExpired = true;
						}
					},20);
				}
				super.touchUp(event, x, y, pointer, button);
			}
		});
		
		weaponButtonTwoStyle.font =  Assets.manager.get(Assets.bitmapfont, BitmapFont.class);
		weaponButtonTwoStyle.font.getData().setScale(0.01f);
		weaponButtonTwoStyle.up = weaponTwoStyle;
		weaponButtonTwoStyle.down = weaponTwoClicked;
		weaponButton2 = new TextButton("", weaponButtonTwoStyle);
		weaponButton2.setSize(1.25f, 1.25f);
		weaponButton2.setOrigin(0, 0);
		weaponButton2.setPosition(0, Constants.SCENE_HEIGHT / 30f * 0.75f);
		weaponButton2.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				buttonClicked = true;
				if(!weaponTwoTimeExpiredweaponTwo){		
					weaponButtonTwoStyle.up = weaponTwoClicked;
					Constants.ENEMYSPEED = 90;
				}
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				buttonClicked = false;
				//display loading screen initially
				if(!weaponTwoTimeExpiredweaponTwo){					
					Timer.schedule(new Task(){
						@Override
						public void run() {
							Constants.ENEMYSPEED = enemyLevelSpeed;
							weaponTwoTimeExpiredweaponTwo = true;
						}
					},20);
				}
				super.touchUp(event, x, y, pointer, button);
			}
		});
		
		final float[] ballSpeed = new float[1];
		weaponButtonThreeStyle.font =  Assets.manager.get(Assets.bitmapfont, BitmapFont.class);
		weaponButtonThreeStyle.font.getData().setScale(0.01f);
		weaponButtonThreeStyle.up = weaponThreeStyle;
		weaponButtonThreeStyle.down = weaponThreeClicked;
		weaponButton3 = new TextButton("", weaponButtonThreeStyle);
		weaponButton3.setSize(1.25f, 1.25f);
		weaponButton3.setOrigin(0, 0);
		weaponButton3.setPosition(0, Constants.SCENE_HEIGHT / 30f * 0.65f);
		weaponButton3.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				buttonClicked = true;
				if(!weaponThreeTimeExpiredweaponThree){		
					weaponButtonThreeStyle.up = weaponThreeClicked;
					ballSpeed[0] = Constants.ballSpeed;
					Constants.ballSpeed = 20f;
				}
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				buttonClicked = false;
				//display loading screen initially
				if(!weaponThreeTimeExpiredweaponThree){					
					Timer.schedule(new Task(){
						@Override
						public void run() {
							weaponThreeTimeExpiredweaponThree = true;
							Constants.ballSpeed = ballSpeed[0];
						}
					},20);
				}
				super.touchUp(event, x, y, pointer, button);
			}
		});
		
		/**
		 * Music button
		 */
		final TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle();
		tbs.font =  Assets.manager.get(Assets.bitmapfont, BitmapFont.class);
		tbs.font.getData().setScale(0.01f);
		final TextureRegionDrawable musicEnabled = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.music, Texture.class)));
		final TextureRegionDrawable musicDisabled = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.noMusic, Texture.class)));
		tbs.up = musicEnabled;
		button = new TextButton("back", tbs);
		button.setSize(1.5f, 1.5f);
		button.setOrigin(Constants.SCENE_WIDTH / 30f * 0.5f, Constants.SCENE_HEIGHT / 30f * 0.5f);
		button.setPosition(button.getX(), button.getY() + 14.7f);
		
		button.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				buttonClicked = true;
				if(tbs.up == musicEnabled){
					musicMuted = true;
					tbs.up = musicDisabled;
					adjustMusic(0);
				}
				else{
					tbs.up = musicEnabled;
					musicMuted = false;
					adjustMusic(1);
				}
				
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				buttonClicked = false;
				super.touchUp(event, x, y, pointer, button);
			}
	
		});
		
		/**
		 * end of game dialog
		 */
		//label and dialog
		final TextButton.TextButtonStyle continueButton = new TextButton.TextButtonStyle();
		continueButton.font =  Assets.manager.get(Assets.bitmapfont, BitmapFont.class);
		continueButton.font.getData().setScale(0.001f);
		continueButton.up = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.continueButton, Texture.class)));
		continueButton.down = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.continueButtonDown, Texture.class)));

		
		final TextButton.TextButtonStyle replay = new TextButton.TextButtonStyle();
		replay.font =  Assets.manager.get(Assets.bitmapfont, BitmapFont.class);
		replay.font.getData().setScale(0.001f);
		replay.up = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.replay, Texture.class)));
		replay.down = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.replayDown, Texture.class)));
		
		final TextButton.TextButtonStyle myMenu = new TextButton.TextButtonStyle();
		myMenu.font =  Assets.manager.get(Assets.bitmapfont, BitmapFont.class);
		myMenu.font.getData().setScale(0.001f);
		myMenu.up = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.myMenu, Texture.class)));
		myMenu.down = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.myMenuDown, Texture.class)));
		
		 label = new Label("text",skin);
		 dialog = new Dialog("please confirm", skin) {
			{
				button("text","replay",replay);
				button("text", "menu",myMenu);
			}

			@Override
			protected void result(Object object) {

				dialogAppearTimes++;
				
				if(object.equals("replay")){
					
					if(internetEnabled && dialogAppearTimes % 2 != 0){
						adsController.showInterstitialAd(new Runnable() {
							@Override
							public void run() {
							}
						});
					}
					if(musicMuted){						
						adjustMusic(0);
					}
					else{
						adjustMusic(1);
					}
					
					for (Qice qice : allEggs) {
						qice.pause();
			        }
					resetStage();
					showGame = true;
				}		
				else if(object.equals("menu"))
				{
					if(internetEnabled){
						adsController.showInterstitialAd(new Runnable() {
							@Override
							public void run() {
							}
						});
					}
					Constants.sound.loop();
					stopMusic();
					getStage().dispose();
					world.dispose();
					disposeAllAssets();
					game.setScreen(new LevelScreen(adsController,game));
				}
				
			}
		};		
		dialog.text(label);		

		/**
		 * pause menu
		 */
		 pauseMenu = new Dialog("please confirm", skin) {
			{
				button("text","continue",continueButton);
				button("text","replay",replay);
				button("text", "menu",myMenu);
			}

			@Override
			protected void result(Object object) {
				
				dialogAppearTimes++;
				
				if(object.equals("continue")){
					if(internetEnabled && dialogAppearTimes % 2 != 0){
						adsController.showInterstitialAd(new Runnable() {
							@Override
							public void run() {
							}
						});
					}

					if(!musicMuted){						
						adjustMusic(1);
					}
					for (Qice qice : allEggs) {
						qice.resume();
			        }
					showGame = true;
				}
				if(object.equals("replay")){
					
					if(internetEnabled && dialogAppearTimes % 2 != 0){
						adsController.showInterstitialAd(new Runnable() {
							@Override
							public void run() {
							}
						});
					}
					if(musicMuted){						
						adjustMusic(0);
					}
					else{
						adjustMusic(1);
					}
					resetStage();
					showGame = true;
				}		
				else if(object.equals("menu"))
				{
					if(internetEnabled){
						adsController.showInterstitialAd(new Runnable() {
							@Override
							public void run() {
							}
						});
					}
					Constants.sound.loop();
					stopMusic();
					getStage().dispose();
					world.dispose();
					disposeAllAssets();
					game.setScreen(new LevelScreen(adsController,game));
				}
			}
		};			
		
		/**
		 * slider
		 */
		Slider.SliderStyle ss = new Slider.SliderStyle();
		ss.background = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.slider, Texture.class)));
		ss.knob = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.sliderKnob, Texture.class)));
		
		slider = new Slider(0f, 4, 1f, false, ss);
		
		slider.setPosition(0, 0);
		slider.setValue(3);
		
		slider.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				//Gdx.app.log(TAG, "slider changed to: " + slider.getValue());
				buttonClicked = false;
			}
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				switch ((int)slider.getValue()) {
		        case 0:  Constants.ballSpeed = 9f;
		        		 break;
		        case 1:  Constants.ballSpeed = 12f;
       		 			break;
		        case 2:  Constants.ballSpeed = 13f;
		        	break;
		        case 3:  Constants.ballSpeed = 14f;
		        	break;
		        case 4:  Constants.ballSpeed = 14.3f;
		        	break;
				}
				buttonClicked = true;
				return true;
			}
			@Override
			public void touchDragged(InputEvent event, float x, float y,
					int pointer) {
				switch ((int)slider.getValue()) {
		        case 0:  Constants.ballSpeed = 9f;
		        		 break;
		        case 1:  Constants.ballSpeed = 12f;
       		 			break;
		        case 2:  Constants.ballSpeed = 13f;
		        	break;
		        case 3:  Constants.ballSpeed = 14f;
		        	break;
		        case 4:  Constants.ballSpeed = 14.3f;
		        	break;
				}
				buttonClicked = true;
				super.touchDragged(event, x, y, pointer);
			};
		});

		// Set table structure
		table = new Table();

		table.add(slider).width(150).height(50);
		addActor(table);
		table.setClip(true);
		table.setWidth(150);
		table.setHeight(50);
		table.setScale(0.03333f);
	}
	
	//Destroy bodies if out of range
	public void destroyFlyingBirds(){
        for (FlyingBirds bird : destroyFlyingBirds) {
        	if(!BodyUtils.bodyInBounds(bird.body,camera)){
        		bird.resetBody(bird.body);
        		FlyingBirds.pointBodyToAngle(MathUtils.random(130f, 170f),  bird.body);              		
        	}
        }
        for (FlyingBirds2 bird : destroyFlyingBirds2) {
        	if(!BodyUtils.bodyInBounds(bird.body,camera)){
        		bird.resetBody(bird.body);
        		FlyingBirds2.pointBodyToAngle(MathUtils.random(130f, 170f),  bird.body);        		
        	}
        }
	}
	
	//reset enemies out of range
	public void resetEnemyIfOutOfBounds(){
    	if(!BodyUtils.bodyInBounds(enemy.body,camera)){
    		if(enemy.pribirane){
    			//fail dve s koito pticata e otletqla
				showGame = false;
				adjustMusic(0);
				label.setText("Lost! You have one egg taken.");
				//dialog.text(label);
				//skin
				dialog.setScale(.03333f);
				dialog.show(this);
				dialog.setOrigin( this.getWidth() * 0.35f , 
								  this.getHeight() / 2);
    		}
			//((Qice)enemy.body.getFixtureList().first().getUserData()).vzeto = true;
    		enemy.resetBody();            		
    	}
    	if(!BodyUtils.bodyInBounds(enemyOtherSide.body,camera)){
    		//((Qice)enemyOtherSide.body.getFixtureList().first().getUserData()).vzeto = true;
    		if(enemyOtherSide.pribirane){
    			//fail dve s koito pticata e otletqla
				showGame = false;
				adjustMusic(0);
				label.setText("Lost! You have 1 egg taken.");
				//dialog.text(label);
				//skin
				dialog.setScale(.03333f);
				dialog.show(this);
				dialog.setOrigin( this.getWidth() * 0.35f , 
								  this.getHeight() / 2);
    		}
    		enemyOtherSide.resetBody();            		
    	}
	}
	
	//remove eggs which have already been izlupeni
	public void removeIzlupeniQica(){
		for (Qice qice : eggs) {
        	if(qice.body.isAwake()){
        		eggs.removeValue(qice, true);
        	}
        }
	}
	
	//remove eggs which have already been izlupeni
	public void izpuleniQica(){
		for (Qice qice : allEggs) {	
			
			if(qice.uduljavaneIgra && !uduljavane.contains(qice, true))	
				uduljavane.add(qice);
				
        	if(qice.izlupenoQice && !izlupeni.contains(qice, true)){
        		izlupeni.add(qice);
        	}
        	if(qice.vzeto && !vzeti.contains(qice, true)){ 
        		vzeti.add(qice);
        	}
        }
	}
	
	//Destroy sleeping bodies
	public void destroySleepingBalls(){
		
		for (DynamicBall ball : sleeepingBalls) {	
				
				if(!ball.body.isAwake()){
					ball.draw = false;
		        	particleBall.effect.setPosition(ball.body.getPosition().x, ball.body.getPosition().y);
		        	particleBall.showEffect = true;
		        	
					sleeepingBalls.removeValue(ball, true);
					pool.free(ball);
				}
				else if(!BodyUtils.bodyInBounds(ball.body,camera))  {	
						ball.body.setAwake(false);
				}
		}
	}

	/**
	 * music
	 */
	public void adjustMusic(int level){
    	music1.setVolume(level);	
    	for (Qice qice : allEggs) {
			qice.pilence.setVolume(level);
        }
	}
	
	public void stopMusic(){
		music1.stop();
	}
	
	public void dyingEnemey(){
		if(music1.getVolume() > 0){			
			destroyEnemey.play();
		}
	}
		
	public void resetMusic(){

/*		music1.stop();
		music1.play();
		music1.setLooping(true);*/
	}
	/**
	 * launch single enemy
	 */
	public void startEnemyOne(){
		resetMusic();
		
		enemy.enemyDraw = false;
		enemy.setSpeed(0);
		enemy.body.setAwake(false);

		enemy.resetBody();
		//reset the other enemy
		enemyOtherSide.enemyDraw = true;
		enemyOtherSide.setSpeed(Constants.ENEMYSPEED);
		enemyOtherSide.body.setAwake(true);
		enemyOtherSide.resetBody();
		//launch bird scream
	}
	
	public void startEnemyTwo(){
		resetMusic();

		//launch enemy2
		enemyOtherSide.enemyDraw = false;
		enemyOtherSide.setSpeed(0);
		enemyOtherSide.body.setAwake(false);
		enemyOtherSide.resetBody();
		//reset enemy1
		enemy.enemyDraw = true;
		enemy.setSpeed(Constants.ENEMYSPEED);
		enemy.body.setAwake(true);
		enemy.resetBody();
		//launch music
	}
	
	public void launchEnemy(){
    	if(MathUtils.random(1, 2) == 1)
    		startEnemyTwo();
    	else
    		startEnemyOne();
	}
	
	/**
	 * launch both enemies independently
	 */
	//launch both enemies
	public void launchEnemyOne(){
		resetMusic();
		//reset enemy1
		enemy.enemyDraw = true;
		enemy.setSpeed(Constants.ENEMYSPEED);
		enemy.body.setAwake(true);
		enemy.resetBody();
	}
	
	public void launchEnemyTwo(){
		resetMusic();
		//reset the other enemy
		enemyOtherSide.enemyDraw = true;
		enemyOtherSide.setSpeed(Constants.ENEMYSPEED);
		enemyOtherSide.body.setAwake(true);
		enemyOtherSide.resetBody();
	}
	
	/*
	 * Contact Listener
	 */
	@Override
	public void beginContact(Contact contact) {
        
		/**
		 * Enemy and qice
		 */
		//Otvarqne na krakata na pticata
        if( (contact.getFixtureA().getUserData().equals(Constants.QICE) && contact.getFixtureB().getUserData().equals(Constants.DOKOSVANESQICE ))
            || (contact.getFixtureA().getUserData().equals(Constants.DOKOSVANESQICE) && contact.getFixtureB().getUserData().equals(Constants.QICE)) ){ 
            Body body1 = null;
            if(contact.getFixtureA().getBody().getUserData().equals(Constants.Enemy))
            	body1 = contact.getFixtureA().getBody();
            else if(contact.getFixtureB().getBody().getUserData().equals(Constants.Enemy))
            	body1 = contact.getFixtureB().getBody();
            
            Body body2 = null;
            if(contact.getFixtureA().getBody().getUserData().equals(Constants.Enemy2))
            	body2 = contact.getFixtureA().getBody();
            else if(contact.getFixtureB().getBody().getUserData().equals(Constants.Enemy2))
            	body2 = contact.getFixtureB().getBody();
            
			final Body qice = contact.getFixtureA().getBody().getUserData().equals(Constants.QICE) ?
					  contact.getFixtureA().getBody() : contact.getFixtureB().getBody();
					  
            //make sure qiceto e  tova za koeto se e zaputila pticata
            if(body1 != null && (((Qice)body1.getFixtureList().first().getUserData()).body == qice)){            	
            	enemy.naOtivaneDraw = false;
            	enemy.hvashtane = true;
            }
            if(body2 != null && (((Qice)body2.getFixtureList().first().getUserData()).body == qice)){            	
            	enemyOtherSide.naOtivaneDraw = false;
            	enemyOtherSide.hvashtane = true;
            }
        }
        //Hvashtane na qiceto i pribirane na krakata
        if( (contact.getFixtureA().getUserData().equals(Constants.QICE) && contact.getFixtureB().getUserData().equals( Constants.SENSORzaDOKOSVANE))
                || (contact.getFixtureA().getUserData().equals( Constants.SENSORzaDOKOSVANE) && contact.getFixtureB().getUserData().equals(Constants.QICE)) ){ 
        	
	            Body body1 = null;
	            if(contact.getFixtureA().getBody().getUserData().equals(Constants.Enemy))
	            	body1 = contact.getFixtureA().getBody();
	            else if(contact.getFixtureB().getBody().getUserData().equals(Constants.Enemy))
	            	body1 = contact.getFixtureB().getBody();
	            
	            Body body2 = null;
	            if(contact.getFixtureA().getBody().getUserData().equals(Constants.Enemy2))
	            	body2 = contact.getFixtureA().getBody();
	            else if(contact.getFixtureB().getBody().getUserData().equals(Constants.Enemy2))
	            	body2 = contact.getFixtureB().getBody();
				
				final Body qice = contact.getFixtureA().getBody().getUserData().equals(Constants.QICE) ?
						  contact.getFixtureA().getBody() : contact.getFixtureB().getBody();

				
				//make sure qiceto ne se izlupva i qiceto e  tova za koeto se e zaputila pticata
				if(body1 != null && !qice.isAwake() && (((Qice)body1.getFixtureList().first().getUserData()).body == qice)){
					enemy.hvashtane = false;
					if(((Qice)body1.getFixtureList().first().getUserData()).animationDraw == false){
						((Qice)body1.getFixtureList().first().getUserData()).vzetoQice = true;
						
						//max 2 razbiti qica
						if(uduljavane.size == 2) {
							showGame = false;
							adjustMusic(0);
							//skin
							label.setText("Lost! For 2 broken eggs you must save min 3!");
							//dialog.text(label);
							dialog.setScale(.03333f);
							dialog.show(this);
							dialog.setOrigin( this.getWidth() * 0.25f , 
											  this.getHeight() / 2);
						}
					}
	        		enemy.pribirane = true;
				}
				//make sure qiceto ne se izlupva i qiceto e  tova za koeto se e zaputila pticata
				else if(body2 != null && !qice.isAwake() && (((Qice)body2.getFixtureList().first().getUserData()).body == qice)){
					enemyOtherSide.hvashtane = false;
					if(((Qice)body2.getFixtureList().first().getUserData()).animationDraw == false){
						((Qice)body2.getFixtureList().first().getUserData()).vzetoQice = true;
						
						//dve sa razbiti veche
						if(uduljavane.size == 2) {
							showGame = false;
							adjustMusic(0);
							label.setText("Lost! For 2 broken eggs you must save min 3!");
							//dialog.text(label);
							dialog.setScale(.03333f);
							dialog.show(this);
							dialog.setOrigin( this.getWidth() * 0.25f , 
											  this.getHeight() / 2);
						}

					}
					enemyOtherSide.pribirane = true;
				}
        			  
         }
        //unishtojavane na enemy i create a new one 
        if( (contact.getFixtureA().getBody().getUserData().equals(Constants.DynamicBall) && contact.getFixtureB().getUserData().equals( Constants.EnemyHitArea ))
                || (contact.getFixtureA().getUserData().equals(Constants.EnemyHitArea) && contact.getFixtureB().getBody().getUserData().equals(Constants.DynamicBall)) ){
        	
            Body body1 = null;
            if(contact.getFixtureA().getBody().getUserData().equals(Constants.Enemy))
            	body1 = contact.getFixtureA().getBody();
            else if(contact.getFixtureB().getBody().getUserData().equals(Constants.Enemy))
            	body1 = contact.getFixtureB().getBody();
            final Body toRemove = body1;	
            
            Body body2 = null;
            if(contact.getFixtureA().getBody().getUserData().equals(Constants.Enemy2))
            	body2 = contact.getFixtureA().getBody();
            else if(contact.getFixtureB().getBody().getUserData().equals(Constants.Enemy2))
            	body2 = contact.getFixtureB().getBody();
            final Body toRemove2 = body2;
            
            final Body ball = contact.getFixtureA().getBody().getUserData().equals(Constants.DynamicBall) ?
					  		  contact.getFixtureA().getBody() : contact.getFixtureB().getBody();				
			//dying bird particle
			dyingEnemey();
			ball.setSleepingAllowed(true);
			
			if(ball.isAwake()){
				numberOfEnemyKillings++;
				//if enemy1 is hit
				if(toRemove != null && toRemove.isAwake()){					
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run () {
							ball.setAwake(false);
	
							//launch particle effect
							if(enemy.enemyDraw){								
								particleEffect.effect.setPosition(enemy.body.getPosition().x, enemy.body.getPosition().y);
								particleEffect.showEffect = true;
							}

							//puskane na qiceto
							if(enemy.pribirane){
								((Qice)toRemove.getFixtureList().first().getUserData()).drawing = true;
								((Qice)toRemove.getFixtureList().first().getUserData()).body.setTransform(toRemove.getPosition(), 0);
								((Qice)toRemove.getFixtureList().first().getUserData()).vzetoQice = false;
								((Qice)toRemove.getFixtureList().first().getUserData()).razmazanoQice = true;
								
							}
							
							//start launching both enemies
							if(numberOfEnemyKillings > launchBothEnemies){								
								//launch both enemies
								if(internetEnabled && timeIntervalAds >= timeAds){
									for(Qice qice: allEggs){
										qice.pause();
									}
									adsController.showInterstitialAd(new Runnable() {
										@Override
										public void run() {
											timeIntervalAds = 0;
											for(Qice qice: allEggs){
												qice.resume();
											}
										}
									});
								}						
								timeIntervalAds++;
								if(!enemyOtherSide.enemyDraw){
									launchEnemyTwo();
									launchEnemyOne();
								}
								else
									launchEnemyOne();
								//reset
								if(timeIntervalAds > timeAds)
									timeIntervalAds = 0;
							}
							//launch only one enemy
							else
							{								
								//if mobile internet and limit reached
								if(internetEnabled && timeIntervalAds >= timeAds){
									for(Qice qice: allEggs){
										qice.pause();
									}
									adsController.showInterstitialAd(new Runnable() {
										@Override
										public void run() {
											timeIntervalAds = 0;
											for(Qice qice: allEggs){
												qice.resume();
											}
										}
									});
								}
								timeIntervalAds++;
								launchEnemy();
								//reset
								if(timeIntervalAds > timeAds)
									timeIntervalAds = 0;
							}
						}
					});
				}
				//if enemy2 is hit
				else if (toRemove2 != null && toRemove2.isAwake()){
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run () {
							ball.setAwake(false);
	
							if(enemyOtherSide.enemyDraw){								
								particleEffect.effect.setPosition(enemyOtherSide.body.getPosition().x, enemyOtherSide.body.getPosition().y);
								particleEffect.showEffect = true;
							}
							//puskane na qiceto
							if(enemyOtherSide.pribirane){
								((Qice)toRemove2.getFixtureList().first().getUserData()).drawing = true;
								((Qice)toRemove2.getFixtureList().first().getUserData()).body.setTransform(toRemove2.getPosition(), 0);
								((Qice)toRemove2.getFixtureList().first().getUserData()).vzetoQice = false;
								((Qice)toRemove2.getFixtureList().first().getUserData()).razmazanoQice = true;
							}
							
							// launching both enemies
							if(numberOfEnemyKillings > launchBothEnemies){								
								//launch both enemies
								if(internetEnabled && timeIntervalAds >= timeAds){
									for(Qice qice: allEggs){
										qice.pause();
									}
									adsController.showInterstitialAd(new Runnable() {
										@Override
										public void run() {
											timeIntervalAds = 0;
											for(Qice qice: allEggs){
												qice.resume();
											}
										}
									});
								}
								timeIntervalAds++;
								if(!enemy.enemyDraw){
									launchEnemyTwo();
									launchEnemyOne();
								}
								else
									launchEnemyTwo();
								//reset
								if(timeIntervalAds > timeAds)
									timeIntervalAds = 0;					
							}
							//one enemy
							else
							{								
								//if mobile enabled
								if(internetEnabled && timeIntervalAds >= timeAds){
									for(Qice qice: allEggs){
										qice.pause();
									}
									adsController.showInterstitialAd(new Runnable() {
										@Override
										public void run() {
											timeIntervalAds = 0;
											for(Qice qice: allEggs){
												qice.resume();
											}
										}
									});
								}
								timeIntervalAds++;
								launchEnemy();
								//reset
								if(timeIntervalAds > timeAds)
									timeIntervalAds = 0;
							}
							
						}
					});
				}
			}
        }
        
        /**
         * razmazano qice na zemqta
         */
        if( (contact.getFixtureA().equals(Constants.QICE) && contact.getFixtureB().getBody().getUserData().equals(Constants.GROUND))
                || (contact.getFixtureA().getBody().getUserData().equals(Constants.GROUND) && contact.getFixtureB().getUserData().equals(Constants.QICE)) ){
        	
			final Body qice = contact.getFixtureA().getUserData().equals(Constants.QICE) ?
					  contact.getFixtureA().getBody() : contact.getFixtureB().getBody();
			
			
			if(qice.isSleepingAllowed()){
				pruskane.effect.setPosition(qice.getPosition().x, qice.getPosition().y);
				pruskane.showEffect = true;
				qice.setAwake(true);
				qice.setSleepingAllowed(false);
				if(music1.getVolume() > 0){					
					breakingEgg.play();
				}
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						//world.destroyBody(qice);
					}
				});
			}
        }
        
        
        /**
         * razrushavane na topka ako se udari v qice
         */
        if( (contact.getFixtureA().getUserData().equals(Constants.QICE) && contact.getFixtureB().getBody().getUserData().equals(Constants.DynamicBall))
                || (contact.getFixtureA().getBody().getUserData().equals(Constants.DynamicBall) && contact.getFixtureB().getUserData().equals(Constants.QICE)) ){
        	
            final Body ball = contact.getFixtureA().getBody().getUserData().equals(Constants.DynamicBall) ?
					  		  contact.getFixtureA().getBody() : contact.getFixtureB().getBody();
			final Body qice = contact.getFixtureA().getBody().getUserData().equals(Constants.QICE) ?
					  contact.getFixtureA().getBody() : contact.getFixtureB().getBody();	
			
			ball.setSleepingAllowed(true);
			if(!qice.isBullet()){
	        	if(ball.isAwake()){
	        		ball.setAwake(false);
	        	}
			}
        }
        
        /**
         * FlyingBird 1
         */
        if( (contact.getFixtureA().getBody().getUserData().equals(Constants.LINE1) && 
        	 contact.getFixtureB().getUserData().equals(Constants.FLYINGBIRDHITAREA)) 
        	 ||
        	 (contact.getFixtureA().getUserData().equals(Constants.FLYINGBIRDHITAREA) && 
              contact.getFixtureB().getBody().getUserData().equals(Constants.LINE1)) ){
        	final Body myBody = contact.getFixtureA().getUserData().equals(Constants.FLYINGBIRDHITAREA) ?
					  contact.getFixtureA().getBody() : contact.getFixtureB().getBody();
				
			  Gdx.app.postRunnable(new Runnable() {
			  		@Override
			  		public void run () {
			  			FlyingBirds.pointBodyToAngle(MathUtils.random(130f, 170f), myBody);
			  		}
			  	});
        }
        if( (contact.getFixtureA().getBody().getUserData().equals(Constants.LINE2) && 
        		  contact.getFixtureB().getUserData().equals(Constants.FLYINGBIRDHITAREA) ) 
        	 ||
        	 	(contact.getFixtureA().getUserData().equals(Constants.FLYINGBIRDHITAREA) &&
        	 	 contact.getFixtureB().getBody().getUserData().equals(Constants.LINE2)) ){
        	final Body myBody = contact.getFixtureA().getUserData().equals(Constants.FLYINGBIRDHITAREA) ?
					  contact.getFixtureA().getBody() : contact.getFixtureB().getBody();
			  Gdx.app.postRunnable(new Runnable() {
			  		@Override
			  		public void run () {
			  			FlyingBirds.pointBodyToAngle(MathUtils.random(-130f, -170f), myBody);
			  		}
			  	});
        }
        
        //reset flying bird
        if( (contact.getFixtureA().getBody().getUserData().equals(Constants.DynamicBall) && contact.getFixtureB().getUserData().equals( Constants.FLYINGBIRDHITAREA ))
                || (contact.getFixtureA().getUserData().equals( Constants.FLYINGBIRDHITAREA) && contact.getFixtureB().getBody().getUserData().equals(Constants.DynamicBall)) ){
        	final Body ball = contact.getFixtureA().getBody().getUserData().equals(Constants.DynamicBall) ?
        			contact.getFixtureA().getBody() : contact.getFixtureB().getBody();
        	if(ball.isAwake()){        		
        		flyingBirdParticle.effect.setPosition(flyingBird.body.getPosition().x, flyingBird.body.getPosition().y);
        		flyingBirdParticle.showEffect = true;
        		
        		ball.setSleepingAllowed(true);
        		
        		Gdx.app.postRunnable(new Runnable() {
        			@Override
        			public void run () {
        				ball.setAwake(false);
        				flyingBird.resetBody(flyingBird.body);
        			}
        		});
        	}
        }
        
        /**
         * FlyingBird 2
         */
        if( (contact.getFixtureA().getBody().getUserData().equals(Constants.LINE1) && 
        	 contact.getFixtureB().getUserData().equals(Constants.FLYINGBIRDHITAREA2)) 
        	 ||
        	 (contact.getFixtureA().getUserData().equals(Constants.FLYINGBIRDHITAREA2) && 
              contact.getFixtureB().getBody().getUserData().equals(Constants.LINE1)) ){
        	final Body myBody = contact.getFixtureA().getUserData().equals(Constants.FLYINGBIRDHITAREA2) ?
					  contact.getFixtureA().getBody() : contact.getFixtureB().getBody();
			  Gdx.app.postRunnable(new Runnable() {
			  		@Override
			  		public void run () {
			  			FlyingBirds2.pointBodyToAngle(MathUtils.random(-130f, -170f), myBody);
			  		}
			  	});
        }
        if( (contact.getFixtureA().getBody().getUserData().equals(Constants.LINE2) && 
        		  contact.getFixtureB().getUserData().equals(Constants.FLYINGBIRDHITAREA2) ) 
        	 ||
        	 	(contact.getFixtureA().getUserData().equals(Constants.FLYINGBIRDHITAREA2) &&
        	 	 contact.getFixtureB().getBody().getUserData().equals(Constants.LINE2)) ){
        	final Body myBody = contact.getFixtureA().getUserData().equals(Constants.FLYINGBIRDHITAREA2) ?
					  contact.getFixtureA().getBody() : contact.getFixtureB().getBody();
			  Gdx.app.postRunnable(new Runnable() {
			  		@Override
			  		public void run () {
			  			FlyingBirds2.pointBodyToAngle(MathUtils.random(130f, 170f), myBody);
			  		}
			  	});
        }
        
        //reset flying bird
        if( (contact.getFixtureA().getBody().getUserData().equals(Constants.DynamicBall) && contact.getFixtureB().getUserData().equals( Constants.FLYINGBIRDHITAREA2 ))
                || (contact.getFixtureA().getUserData().equals( Constants.FLYINGBIRDHITAREA2) && contact.getFixtureB().getBody().getUserData().equals(Constants.DynamicBall)) ){
        	
        	final Body ball = contact.getFixtureA().getBody().getUserData().equals(Constants.DynamicBall) ?
        			contact.getFixtureA().getBody() : contact.getFixtureB().getBody();
        	if(ball.isAwake()){        		
        		flyingBirdParticle.effect.setPosition(flyingBird2.body.getPosition().x, flyingBird2.body.getPosition().y);
        		flyingBirdParticle.showEffect = true;
        			
        		ball.setSleepingAllowed(true);
        		
        		Gdx.app.postRunnable(new Runnable() {
        			@Override
        			public void run () {
        				ball.setAwake(false);
        				flyingBird2.resetBody(flyingBird2.body);
        			}
        		});
        	}
        }
        
      }

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

	@Override
	public void endContact(Contact contact) {}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {}	
		
	public void displayScreen(){
	
		//success 3 izlupeni
		if(izlupeni.size == 3){
			showGame = false;
			adjustMusic(0);
			//level solved
			//skin
			label.setText("Congratulations! You won!");
			//dialog.text(label);
			dialog.setScale(.03333f);
			dialog.show(this);
			dialog.setOrigin( this.getWidth() * 0.4f , 
							  this.getHeight() / 2);
			Constants.preferences.putBoolean("Level" + currentLevel, true);
			int nextLevel = currentLevel + 1;
			boolean solvedLevel = Constants.preferences.getBoolean("Level" + nextLevel) == true ? true : false;
			if(!solvedLevel)
				Constants.preferences.putBoolean("Level" + nextLevel, false);
			Constants.preferences.flush();			
		}
	}
	
	public void disposeAllAssets(){
		Assets.manager.unload(mapPath);
		map.dispose();
/*		music1.dispose();
		breakingEgg.dispose();
		destroyEnemey.dispose();
		map.dispose();
		skin.dispose();
		debugRenderer.dispose();*/
	}
}

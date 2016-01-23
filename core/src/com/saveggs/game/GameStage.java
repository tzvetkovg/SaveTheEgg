package com.saveggs.game;

import java.util.HashMap;
import java.util.Map;

import assets.Assets;

import com.admob.AdsController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
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
import com.sageggs.actors.Text;
import com.sageggs.actors.enemies.Enemy;
import com.sageggs.actors.enemies.EnemyOtherSide;
import com.sageggs.actors.flyingbirds.FlyingBirdAnimations;
import com.sageggs.actors.flyingbirds.FlyingBirds;
import com.sageggs.actors.flyingbirds.FlyingBirds2;
import com.sageggs.actors.loadingscreen.LoadingScreen;
import com.sageggs.actors.particles.Explosion;
import com.sageggs.actors.particles.Fire;
import com.sageggs.actors.particles.GroundExplosion;
import com.sageggs.actors.particles.MaskaBurst;
import com.sageggs.actors.particles.MaskaBurst2;
import com.sageggs.actors.particles.ParticleEffectAn;
import com.sageggs.actors.particles.ParticleEffectBall;
import com.sageggs.actors.particles.ParticleEffectFlyingBird;
import com.sageggs.actors.particles.ParticleIzlupvane;
import com.sageggs.actors.particles.PruskaneQice;
import com.sageggs.actors.particles.Smoke;
import com.sageggs.actors.runnables.RunnableEnemy1;
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
	private int timeIntervalAds = 0,timeAds = 32,numberOfEnemyKillings = 0,launchBothEnemies = 0,dialogAppearTimes = 0,whenToAppearAdd = 2;
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
	private int maskaKillings,eggHatch = 0;
	private int defaultMaskKillingsFrequencyMaska1 = 2;
	private int defaultMaskKillingsFrequencyMaska2 = 5;
	private int maskaAppearingKillings = defaultMaskKillingsFrequencyMaska1;
	private int maskaAppearingKillings2 = defaultMaskKillingsFrequencyMaska2;
	private Slider slider;
	private Table table;
	private boolean buttonClicked = false,musicMuted = false,weaponOne = false,weaponOneTimeExpired = false,weaponTwoTimeExpiredweaponTwo = false, weaponThreeTimeExpiredweaponThree = false,weapon1Enabled = false,weapon2Enabled= false,weapon3Enabled = false;
	private Music music1;
	private Sound breakingEgg,destroyEnemey,rubber,shot;
	private TextButton button,pause,weaponButton1,weaponButton2,weaponButton3;
	private float enemyLevelSpeed;
	private TextureRegionDrawable weaponOneStyle,weaponOneClicked,weaponTwoStyle,weaponTwoClicked,weaponThreeStyle,weaponThreeClicked;
    private TextButton.TextButtonStyle weaponButtonOneStyle,weaponButtonTwoStyle,weaponButtonThreeStyle;
	private String mapPath;
	private Text score;
	int ballLimit;
	boolean slinshotShots;
	private TextureAtlas atlas;
	public CurrentMap myMap; 
	private MaskaBurst maskBurst;
	private MaskaBurst2 maskBurst2;
	private Smoke smoke;
	private Fire fire;
	private Explosion explosion;
	private GroundExplosion groundExplosion;
	
	public GameStage(AdsController adsController,Map<String,Object> mapBodies,World world,boolean internetEnabled,GameClass game,TiledMap map, int currentLevel,float enemyLevelSpeed,boolean weapon1, boolean weapon2, boolean weapon3,int timeAds,String mapPath,int numberOfKillings,int ballLimit,boolean slinshotShots,int eggHatch){
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
		this.ballLimit = ballLimit;
		this.slinshotShots = slinshotShots;
		this.eggHatch = eggHatch;
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
		playRubberSound();
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
				if(score.remainingBalls != -1){					
					Body enemyBody1 = enemy.enemyDraw ? enemy.body : null;
					if(enemyBody1 != null){
						applyForceToBall(dynamicBall, enemyBody1);
					}
					
					Body enemyBody2 = enemyOtherSide.enemyDraw ? enemyOtherSide.body : null;
					if(enemyBody2 != null){
						applyForceToBall(dynamicBall2, enemyBody2);
					}
					if(!slinshotShots){						
						score.remainingBalls--;
					}
					playShot();
					slingshot.bounceEffect=true;
				}
			}
			else
			{
				if(score.remainingBalls != -1){							
					applyForceToBall(dynamicBall, null);
					if(!slinshotShots){						
						score.remainingBalls--;
					}
					playShot();
					slingshot.bounceEffect=true;
				}
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
			//System.out.println("size " + uduljavane.size);
			//System.out.println("texture bindings " + GLProfiler.textureBindings);
			//System.out.println("draw calls " + GLProfiler.drawCalls);
			//GLProfiler.reset();
    		//System.out.println("internetEnabled " + internetEnabled);
		}
		debugRenderer.render(world,camera.combined);

		
	}
	
	
	//Set up camera
	public void setupCamera(){
        camera = new OrthographicCamera();
	}
	
	//Set up world
	public void setupWorld(){
		
		world.setContactListener(this);

		myMap = new CurrentMap(this.map,world);
		addActor(myMap);		
		
		bodiesOfWorld = new Array<Body>();
		world.getBodies(bodiesOfWorld);
		//updateTime
		int timeOfIzlupvane = eggHatch;
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
				timeOfIzlupvane += eggHatch;
				addActor(qice);
			
			}
			//slingshot position
			if(bodyLoop.getUserData().equals("sling")){
				bodyLoop.setType(BodyType.DynamicBody);
				bodyLoop.setGravityScale(0);
				bodyLoop.getFixtureList().first().setSensor(true);
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
		flyingBird.pointBodyToAngle(135f, flyingBird.body);
		destroyFlyingBirds.add(flyingBird);
		
		//bird2
		flyingBird2 = (FlyingBirds2)mapBodies.get("flyingBird2");
		addActor(flyingBird2);
		flyingBird2.pointBodyToAngle(135f, flyingBird2.body);
		destroyFlyingBirds2.add(flyingBird2);
		
		//PArticle effects
		//smoke
		smoke = new Smoke(slingshot.body.getPosition().x + 1f,slingshot.body.getPosition().y);
		addActor(smoke);
		fire = new Fire(slingshot.body.getPosition().x + 1f,slingshot.body.getPosition().y);
		addActor(fire);
		particleBall = (ParticleEffectBall)mapBodies.get("particleBall");
		addActor(particleBall);
		particleIzlupvane =  (ParticleIzlupvane)mapBodies.get("particleIzlupvane");
		pruskane = (PruskaneQice)mapBodies.get("pruskane");
		addActor(pruskane);
		explosion = (Explosion)mapBodies.get("explosion");
		addActor(explosion);
		groundExplosion = (GroundExplosion)mapBodies.get("groundExplosion");
		addActor(groundExplosion);
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
		if(!slinshotShots)
			addActor(score);
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
			i++;
		}

		for (DynamicBall ball : sleeepingBalls) {
			ball.body.setAwake(false);
		}
		
		// reset
		numberOfEnemyKillings = 0;
		maskaAppearingKillings = defaultMaskKillingsFrequencyMaska1;
		score.remainingBalls = ballLimit;
		//weapons
		weaponOne = false;
		weaponOneTimeExpired = false;
		weaponTwoTimeExpiredweaponTwo = false;
		weaponThreeTimeExpiredweaponThree = false;
		weaponButtonOneStyle.up = weaponOneStyle;
		weaponButtonTwoStyle.up = weaponTwoStyle;
		weaponButtonThreeStyle.up = weaponThreeStyle;
		
		
		//reset ball speed
		slider.setValue(4);
		Constants.ballSpeed = 14.3f;
		
		//reset enemies
		/*enemy.restartMask();
		enemyOtherSide.restartMask();
		enemy.restartMask2();
		enemyOtherSide.restartMask2();*/
		maskaKillings = 0;
		score.font.setColor(Color.ORANGE);
		
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
		score = new Text(ballLimit);
		atlas = Assets.manager.get(Assets.gameAtlas, TextureAtlas.class);
		
		/**
		 * button styles
		 */
		weaponOneStyle = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("destroyEnemyArrow")));
		weaponOneClicked = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("destroyEnemyArrowClicked")));
		weaponTwoStyle  = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("slowDownBird")));
	    weaponTwoClicked = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("slowDownBirdClicked")));
		weaponThreeStyle = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("fastBallSpeed")));
		weaponThreeClicked = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("fastBallSpeedClicked")));
		weaponButtonOneStyle = new TextButton.TextButtonStyle();
		weaponButtonTwoStyle = new TextButton.TextButtonStyle();
		weaponButtonThreeStyle = new TextButton.TextButtonStyle();

		/**
		 * music
		 */
		music1 = Assets.manager.get(Assets.birdScream, Music.class);
		destroyEnemey = Assets.manager.get(Assets.dyingBird, Sound.class);
		breakingEgg = Assets.manager.get(Assets.breakingEgg, Sound.class);
		rubber = Assets.manager.get(Assets.rubber, Sound.class);
		shot =Assets.manager.get(Assets.shot, Sound.class);
		adjustMusic(0);
		
		/*
		 * pause button
		 */
		final TextButton.TextButtonStyle tbs1 = new TextButton.TextButtonStyle();
		tbs1.font =  Assets.manager.get(Assets.bitmapfont, BitmapFont.class);
		tbs1.font.getData().setScale(0.01f);
		final TextureRegionDrawable continueBut = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("pause1")));
		final TextureRegionDrawable continueButDown = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("pause2")));
		tbs1.up = continueBut;
		tbs1.down = continueButDown;
		pause = new TextButton("back", tbs1);
		pause.setSize(1.5f, 1.5f);
		pause.setOrigin(Constants.SCENE_WIDTH / 30f * 0.5f, Constants.SCENE_HEIGHT / 30f * 0.5f);
		pause.setPosition(pause.getX() + 1.4f, pause.getY() + 14.5f);
		
		
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
					Constants.ENEMYSPEED = 70;
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
		final TextureRegionDrawable musicEnabled = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("music1")));
		final TextureRegionDrawable musicDisabled = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("music2")));
		tbs.up = musicEnabled;
		button = new TextButton("back", tbs);
		button.setSize(1.5f, 1.5f);
		button.setOrigin(Constants.SCENE_WIDTH / 30f * 0.5f, Constants.SCENE_HEIGHT / 30f * 0.5f);
		button.setPosition(button.getX(), button.getY() + 14.5f);
		
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
		continueButton.up = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("continue1")));
		continueButton.down = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("continue2")));

		
		final TextButton.TextButtonStyle replay = new TextButton.TextButtonStyle();
		replay.font =  Assets.manager.get(Assets.bitmapfont, BitmapFont.class);
		replay.font.getData().setScale(0.001f);
		replay.up = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("replay1")));
		replay.down = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("replay2")));
		
		final TextButton.TextButtonStyle myMenu = new TextButton.TextButtonStyle();
		myMenu.font =  Assets.manager.get(Assets.bitmapfont, BitmapFont.class);
		myMenu.font.getData().setScale(0.001f);
		myMenu.up = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("menu1")));
		myMenu.down = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("menu2")));
		
		 label = new Label("",skin);
		 dialog = new Dialog("please confirm", skin) {
			{
				button("","replay",replay);
				button("", "menu",myMenu);
			}

			@Override
			protected void result(Object object) {

				dialogAppearTimes++;
				
				
				if(object.equals("replay")){
					
					if(internetEnabled && dialogAppearTimes == whenToAppearAdd){
						adsController.showInterstitialAd(new Runnable() {
							@Override
							public void run() {
								whenToAppearAdd += 4;
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
					game.setScreen(game.levelScreen);
				}
				
			}
		};		
		dialog.text(label);		

		/**
		 * pause menu
		 */
		 pauseMenu = new Dialog("please confirm", skin) {
			{
				button("","continue",continueButton);
				button("","replay",replay);
				button("", "menu",myMenu);
			}

			@Override
			protected void result(Object object) {
				
				dialogAppearTimes++;
				
				if(object.equals("continue")){
					if(internetEnabled && dialogAppearTimes == whenToAppearAdd){
						adsController.showInterstitialAd(new Runnable() {
							@Override
							public void run() {
								whenToAppearAdd += 4;
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
					
					if(internetEnabled && dialogAppearTimes == whenToAppearAdd){
						adsController.showInterstitialAd(new Runnable() {
							@Override
							public void run() {
								whenToAppearAdd += 4;
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
					game.setScreen(game.levelScreen);
				}
			}
		};			
		
		/**
		 * slider
		 */
		Slider.SliderStyle ss = new Slider.SliderStyle();
		ss.background = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("PowerBar")));
		ss.knob = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("ArrowDown")));
		
		slider = new Slider(0f, 4, 1f, false, ss);
		
		slider.setPosition(0,0f);
		slider.setValue(4);
		
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
		        case 1:  Constants.ballSpeed = 11f;
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
		        case 1:  Constants.ballSpeed = 11f;
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

		table.add(slider).width(150).height(50).padTop(18f);
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
        		flyingBird.pointBodyToAngle(MathUtils.random(130f, 170f),  bird.body);
        		flyingBird.switchAnimations();
        	}
        }
        for (FlyingBirds2 bird : destroyFlyingBirds2) {
        	if(!BodyUtils.bodyInBounds(bird.body,camera)){
        		bird.resetBody(bird.body);
        		flyingBird2.pointBodyToAngle(MathUtils.random(130f, 170f),  bird.body); 
        		flyingBird2.switchAnimations();
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
		  //show smoke near slingshot
		if(score.remainingBalls < 70){
			score.font.setColor(Color.RED);
			smoke.showEffect = false;
			fire.showEffect = true;
		}
		else if(score.remainingBalls <= 90 && score.remainingBalls >= 70){
			smoke.showEffect = true;
		}
		else{
			smoke.showEffect = false;
			fire.showEffect = false;
		 }
		
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
	
	public void playRubberSound(){
		boolean hasFinished = false;
		if(music1.getVolume() > 0){	
			if(!hasFinished)
				rubber.play();
			hasFinished = true;
		}
	}
	
	public void playShot(){
		boolean hasFinished = false;
		if(music1.getVolume() > 0){			
			if(!hasFinished)
				shot.play();
			hasFinished = true;
		}
	}
	/**
	 * launch single enemy
	 */
	public void startEnemyOne(){
		
		enemy.enemyDraw = false;
		enemy.setSpeed(0);
		enemy.body.setAwake(false);
		
		numberOfEnemyKillings++;
		
		if(numberOfEnemyKillings == 2)
		{
			System.out.println("padasht Losh");
			enemyOtherSide.pokajiPadashto = true;
			numberOfEnemyKillings = 2;
		}
		
		if(numberOfEnemyKillings == 4)
		{
			System.out.println("losh");
			enemyOtherSide.actualAnimation = enemyOtherSide.losh;
			enemyOtherSide.pokajiLossh = true;
			numberOfEnemyKillings = 0;
		}

		
		enemy.resetBody();
		//reset the other enemy
		enemyOtherSide.enemyDraw = true;
		enemyOtherSide.setSpeed(Constants.ENEMYSPEED);
		enemyOtherSide.body.setAwake(true);
		enemyOtherSide.resetBody();
		//launch bird scream
	}
	
	public void startEnemyTwo(){

		//launch enemy2
		enemyOtherSide.enemyDraw = false;
		enemyOtherSide.setSpeed(0);
		enemyOtherSide.body.setAwake(false);
		enemyOtherSide.resetBody();
		//reset enemy1
		enemy.enemyDraw = true;
/*		if(numberOfEnemyKillings == maskaAppearingKillings){
			enemy.switchToMask1();
		}
		else if(numberOfEnemyKillings == maskaAppearingKillings2){
			enemy.switchToMask2();
		}*/
		enemy.setSpeed(Constants.ENEMYSPEED);
		enemy.body.setAwake(true);
		enemy.resetBody();
		//launch music
	}
	
	public void launchEnemy(){
    	if(MathUtils.random(1, 2) == 1)
    		startEnemyOne();
    	else
    		startEnemyOne();
	}
		
	/**
	 * launch both enemies independently
	 */
	//launch both enemies
	public void launchEnemyOne(){

		//reset enemy1
/*		if(numberOfEnemyKillings == maskaAppearingKillings){
			enemy.mask = true;
		}*/
		enemy.enemyDraw = true;
		enemy.setSpeed(Constants.ENEMYSPEED);
		enemy.body.setAwake(true);
		enemy.resetBody();
	}
	
	public void launchEnemyTwo(){
		//reset the other enemy
		enemyOtherSide.enemyDraw = true;
		enemyOtherSide.setSpeed(Constants.ENEMYSPEED);
		enemyOtherSide.body.setAwake(true);
		enemyOtherSide.resetBody();
	}
	
	/*
	 * Contact Listener
	 */
	int loshCount = 0;
	@Override
	public void beginContact(Contact contact) {
        
        /**
         * Razbivane na enemy v Zemqta
         */
        if( (contact.getFixtureA().equals(Constants.EnemyHitArea) && contact.getFixtureB().getUserData().equals(Constants.GROUND))
                || (contact.getFixtureA().getUserData().equals(Constants.GROUND) && contact.getFixtureB().getUserData().equals(Constants.EnemyHitArea)) ){
        	
			final Body enemyBody = contact.getFixtureA().getUserData().equals(Constants.EnemyHitArea) ?
					  contact.getFixtureA().getBody() : contact.getFixtureB().getBody();
					  
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					if(enemyBody != null && enemyBody.isAwake()){
						if(enemyBody == enemyOtherSide.body){
							System.out.println("goes through ground");
							if(enemyOtherSide.pribirane)
							{		
								((Qice)enemyBody.getFixtureList().first().getUserData()).vzetoQice = true;
								uduljavane.add(((Qice)enemyBody.getFixtureList().first().getUserData()));
							}
							groundExplosion.playAnimation(enemyBody);
							myMap.earthQuake = true;
							enemyOtherSide.resetBodyInitalStage();
							launchEnemy();
							return;
						}
					}	
				}						  
			});
        }
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
			//myMap.earthQuake = true;
        	
			ball.setSleepingAllowed(true);

			if(ball.isAwake()){
				
				//if enemy1 is hit
				if(toRemove != null && toRemove.isAwake()){
					Gdx.app.postRunnable(new RunnableEnemy1(ball, enemy, enemyOtherSide, 
															explosion, toRemove, numberOfEnemyKillings, 
															launchBothEnemies, timeIntervalAds, timeAds, 
															allEggs, internetEnabled, adsController));

				}
				//if enemy2 is hit
				else if (toRemove2 != null && toRemove2.isAwake()){
					
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run () {
							ball.setAwake(false);
							//mask handling
							if(enemyOtherSide.getCurrentAnimation() == enemyOtherSide.zamaqnoPilence){
								enemyOtherSide.padaneNaDoly();
								return;
							}
							//losh
							if(enemyOtherSide.getCurrentAnimation() == enemyOtherSide.zamaqnLosh){
								loshCount+=1;
								if(loshCount ==2)
								{									
									enemyOtherSide.padashtLosh();
									loshCount = 0;								
								}
								return;
							}
							
							if(enemyOtherSide.enemyDraw){
								//normal
								if(enemyOtherSide.pokajiPadashto)
								{
									enemyOtherSide.actualAnimation = enemyOtherSide.zamaqnoPilence;
									return;
								}
								
								//losh
								if(enemyOtherSide.pokajiLossh)
								{
									enemyOtherSide.actualAnimation = enemyOtherSide.zamaqnLosh;
									return;
								}
								explosion.playAnimation(enemyOtherSide.body);
							}
							
							//puskane na qiceto
							if(enemyOtherSide.pribirane){
								((Qice)toRemove2.getFixtureList().first().getUserData()).drawing = true;
								((Qice)toRemove2.getFixtureList().first().getUserData()).body.setTransform(toRemove2.getPosition(), 0);
								((Qice)toRemove2.getFixtureList().first().getUserData()).vzetoQice = false;
								((Qice)toRemove2.getFixtureList().first().getUserData()).razmazanoQice = true;
							}							
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
				myMap.earthQuake = true;
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
			  			flyingBird.pointBodyToAngle(MathUtils.random(130f, 170f), myBody);

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
			  			flyingBird.pointBodyToAngle(MathUtils.random(-130f, -170f), myBody);
			  		}
			  	});
        }
        
        //reset flying bird
        if( (contact.getFixtureA().getBody().getUserData().equals(Constants.DynamicBall) && contact.getFixtureB().getUserData().equals( Constants.FLYINGBIRDHITAREA ))
                || (contact.getFixtureA().getUserData().equals( Constants.FLYINGBIRDHITAREA) && contact.getFixtureB().getBody().getUserData().equals(Constants.DynamicBall)) ){
        	final Body ball = contact.getFixtureA().getBody().getUserData().equals(Constants.DynamicBall) ?
        			contact.getFixtureA().getBody() : contact.getFixtureB().getBody();
        	if(ball.isAwake()){      
        		
        		//flyingBirdParticle.effect.setPosition(flyingBird.body.getPosition().x, flyingBird.body.getPosition().y);
        		//flyingBirdParticle.showEffect = true;
        		
        		ball.setSleepingAllowed(true);
        		
        		Gdx.app.postRunnable(new Runnable() {
        			@Override
        			public void run () {
        				explosion.playAnimation(flyingBird.body);
        				ball.setAwake(false);
			  			flyingBird.switchAnimations();
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
			  			flyingBird2.pointBodyToAngle(MathUtils.random(-130f, -170f), myBody);
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
			  			flyingBird2.pointBodyToAngle(MathUtils.random(130f, 170f), myBody);
			  		}
			  	});
        }
        
        //reset flying bird
        if( (contact.getFixtureA().getBody().getUserData().equals(Constants.DynamicBall) && contact.getFixtureB().getUserData().equals( Constants.FLYINGBIRDHITAREA2 ))
                || (contact.getFixtureA().getUserData().equals( Constants.FLYINGBIRDHITAREA2) && contact.getFixtureB().getBody().getUserData().equals(Constants.DynamicBall)) ){
        	
        	final Body ball = contact.getFixtureA().getBody().getUserData().equals(Constants.DynamicBall) ?
        			contact.getFixtureA().getBody() : contact.getFixtureB().getBody();
        	if(ball.isAwake()){ 
        		
        		//flyingBirdParticle.effect.setPosition(flyingBird2.body.getPosition().x, flyingBird2.body.getPosition().y);
        		//flyingBirdParticle.showEffect = true;
        			
        		ball.setSleepingAllowed(true);
        		
        		Gdx.app.postRunnable(new Runnable() {
        			@Override
        			public void run () {
        				explosion.playAnimation(flyingBird2.body);
        				ball.setAwake(false);
        				flyingBird2.switchAnimations();
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
			game.levelScreen = new LevelScreen(adsController, game);
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

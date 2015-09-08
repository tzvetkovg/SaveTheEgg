package com.saveggs.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import assets.Assets;

import com.admob.AdsController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
import com.sageggs.actors.Ground;
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
import com.saveggs.game.screens.MainMenuScreen;
import com.saveggs.game.screens.StageScreen;
import com.saveggs.utils.BodyUtils;
import com.saveggs.utils.Constants;
import com.saveggs.utils.WorldUtils;

public class GameStage extends Stage implements ContactListener{

	public Viewport viewport;
	public Box2DDebugRenderer debugRenderer;
	public World world;
	private FPSLogger logger;
	private Qice qice;
	private DynamicBall dynamicBall;
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
	private int timeIntervalAds = 0,timeAds = 7,numberOfEnemyKillings = 0,launchBothEnemies = 30;
	public boolean showGame = false, internetEnabled = false, showAd = false;
	private LoadingScreen loading;
	private Skin skin;
	private GameClass game;
	private TiledMap map;
	private Dialog dialog;
	private Array<Qice> allEggs,izlupeni,vzeti,uduljavane;
	private Array<Vector2> positionsOfQica;
	private Label label;
	
	public GameStage(AdsController adsController,Map<String,Object> mapBodies,World world,boolean internetEnabled,GameClass game,TiledMap map){
		super(new ExtendViewport(Constants.SCENE_WIDTH / 35, Constants.SCENE_HEIGHT / 35, new OrthographicCamera()));
		this.game = game;
		this.mapBodies = mapBodies;
		this.world = world;
		this.map = map;
		this.internetEnabled = internetEnabled;
		setupCamera();
		getViewport().setCamera(camera);
		setUtils();
		setupWorld();
		Gdx.input.setInputProcessor(this);
		this.adsController = adsController;		
		
		//display loading screen initially
		Timer.schedule(new Task(){
            @Override
            public void run() {
            	showGame = true;
            	loading.draw = false;
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
		if(showGame){			
			camera.unproject(position.set(screenX, screenY, 0));
			
			//Update mesh coordss
			mesh.calculateXYLastikOne(position.x,position.y);
			mesh.drawBody = true;
			//Update mesh2 coords
			mesh2.calculateXYLastikTwo(position.x,position.y);
			mesh2.drawBody = true;
			//Move the static ball on lastik - 0.4f
			staticBall.setBodyPosOnLastik(position.x, position.y);
		}

		return super.touchDragged(screenX, screenY, pointer);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(showGame){			
			//remove lasticite
			mesh.drawBody = false;
			mesh2.drawBody = false;
			
			//Create a new body
			dynamicBall = pool.obtain();
			sleeepingBalls.add(dynamicBall);
			//add to stage	    
			dynamicBall.body.setTransform(staticBall.body.getPosition(), 0);
			dynamicBall.draw = true;
			
			//Apply force
			dynamicBall.applyForceToNewBody();
			addActor(dynamicBall);
			
			//transform old body to initial position
			staticBall.body.setTransform(Constants.middleX, Constants.middleY, 0);
		}
	    
		return super.touchUp(screenX, screenY, pointer, button);
	}

	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
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
		int timeOfIzlupvane = 30;
		//map of all bodies
		for (Body bodyLoop : bodiesOfWorld) {
			//get bodies
			worldBodies.put(bodyLoop.getUserData().toString(), bodyLoop.getPosition());
			//get qica
			if(bodyLoop.getUserData().equals(Constants.QICE)) {
				//neizplupeno qice
				bodyLoop.setAwake(false);
				bodyLoop.setBullet(false);
				//get initial position of bodies
				positionsOfQica.add(new Vector2(bodyLoop.getPosition()));
				qice = new Qice(bodyLoop,timeOfIzlupvane);
				
				eggs.add(qice);
				allEggs.add(qice);
				timeOfIzlupvane += 30;
				addActor(qice);
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
		slingshot = (Slingshot)mapBodies.get("slingshot");
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
			System.out.println("i " + i);
			i++;
		}
		
		for (DynamicBall ball : sleeepingBalls) {
			ball.body.setAwake(false);
		}
		
		// reset
		numberOfEnemyKillings = 0;
		
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
		
		//label and dialog
		 label = new Label("text",skin);
		 dialog = new Dialog("please confirm", skin) {
			{
				button("replay","replay");
				button("return to menu", "menu");
			}

			@Override
			protected void result(Object object) {
				if(object.equals("replay")){
					resetStage();
					showGame = true;
				}		
				else if(object.equals("menu"))
				{
					getStage().dispose();
					world.dispose();
					game.setScreen(new LevelScreen(adsController,game));
				}
			}
		};		
		dialog.text(label);
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
			((Qice)enemy.body.getFixtureList().first().getUserData()).vzeto = true;
    		enemy.resetBody();            		
    	}
    	if(!BodyUtils.bodyInBounds(enemyOtherSide.body,camera)){
    		((Qice)enemyOtherSide.body.getFixtureList().first().getUserData()).vzeto = true;
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
	 * launch single enemy
	 */
	public void startEnemyOne(){
		enemy.enemyDraw = false;
		enemy.setSpeed(0);
		enemy.body.setAwake(false);
		enemy.resetBody();
		//reset the other enemy
		enemyOtherSide.enemyDraw = true;
		enemyOtherSide.setSpeed(Constants.ENEMYSPEED);
		enemyOtherSide.body.setAwake(true);
		enemyOtherSide.resetBody();
	}
	
	public void startEnemyTwo(){
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
		//reset enemy1
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
	@Override
	public void beginContact(Contact contact) {
        
		/**
		 * Enemy and ball
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
							//skin
							label.setText("Lost! For 2 broken eggs you must save min 3!");
							//dialog.text(label);
							dialog.setScale(.03333f);
							dialog.show(this);
							dialog.setOrigin( this.getWidth() * 0.25f , 
											  this.getHeight() / 2);
						}
						
						//max 2 razbiti qica
						if(uduljavane.size == 1 && vzeti.size ==1) {
							showGame = false;
							label.setText("Lost! You have 1 egg taken. You must save min 4!");
							//dialog.text(label);
							dialog.setScale(.03333f);
							dialog.show(this);
							dialog.setOrigin( this.getWidth() * 0.25f , 
											  this.getHeight() / 2);
						}
						
						//((Qice)body1.getFixtureList().first().getUserData()).vzeto = true;
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
							label.setText("Lost! For 2 broken eggs you must save min 3!");
							//dialog.text(label);
							dialog.setScale(.03333f);
							dialog.show(this);
							dialog.setOrigin( this.getWidth() * 0.25f , 
											  this.getHeight() / 2);
						}
						
						//max 1 razbito i vzeto (Trqbwa da spasish chetiri)
						if(uduljavane.size == 1 && vzeti.size ==1) {
							showGame = false;
							//skin
							label.setText("Lost! You have 1 egg taken. You must save min 4!");
							//dialog.text(label);
							dialog.setScale(.03333f);
							dialog.show(this);
							dialog.setOrigin( this.getWidth() * 0.25f , 
											  this.getHeight() / 2);
						}
						//((Qice)body2.getFixtureList().first().getUserData()).vzeto = true;
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
/*								if(!enemyOtherSide.enemyDraw){
									launchEnemyTwo();
									launchEnemyOne();
								}
								else
									launchEnemyOne();*/
								if(internetEnabled && timeIntervalAds >= timeAds){
									adsController.showInterstitialAd(new Runnable() {
										@Override
										public void run() {
											timeIntervalAds = 0;
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
									adsController.showInterstitialAd(new Runnable() {
										@Override
										public void run() {
											timeIntervalAds = 0;
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
/*							//launch both enemies
							if(!enemy.enemyDraw){
								launchEnemyTwo();
								launchEnemyOne();
							}
							else
								launchEnemyTwo();*/
							
							// launching both enemies
							if(numberOfEnemyKillings > launchBothEnemies){								
								//launch both enemies
/*								if(!enemyOtherSide.enemyDraw){
									launchEnemyTwo();
									launchEnemyOne();
								}
								else
									launchEnemyOne();*/
								if(internetEnabled && timeIntervalAds >= timeAds){
									adsController.showInterstitialAd(new Runnable() {
										@Override
										public void run() {
											timeIntervalAds = 0;
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
									adsController.showInterstitialAd(new Runnable() {
										@Override
										public void run() {
											timeIntervalAds = 0;
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

		//max 2 razbiti qica
		if(uduljavane.size == 3) {
			showGame = false;
			label.setText("Lost! You must save at least 3 eggs");
			//dialog.text(label);
			//skin
			dialog.setScale(.03333f);
			dialog.show(this);
			dialog.setOrigin( this.getWidth() * 0.25f , 
							  this.getHeight() / 2);
		}
		
		//fail dve s koito pticata e otletqla
		if(vzeti.size == 2) {
			showGame = false;
			label.setText("Lost! You have 1 egg taken. You must save min 4!");
			//dialog.text(label);
			//skin
			dialog.setScale(.03333f);
			dialog.show(this);
			dialog.setOrigin( this.getWidth() * 0.25f , 
							  this.getHeight() / 2);
		}
		
		//success 3 izlupeni
		if(izlupeni.size == 3){
			showGame = false;
			//skin
			label.setText("Congratulations! You won!");
			//dialog.text(label);
			dialog.setScale(.03333f);
			dialog.show(this);
			dialog.setOrigin( this.getWidth() * 0.4f , 
							  this.getHeight() / 2);
		}
	}
	
	
}

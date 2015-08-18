package com.saveggs.game;

import java.util.HashMap;
import java.util.Map;

import assets.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
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
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
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
import com.sageggs.actors.particles.ParticleEffectAn;
import com.sageggs.actors.particles.ParticleEffectBall;
import com.sageggs.actors.particles.ParticleEffectFlyingBird;
import com.sageggs.actors.particles.ParticleIzlupvane;
import com.sageggs.actors.particles.PruskaneQice;
import com.saveggs.utils.BodyUtils;
import com.saveggs.utils.Constants;
import com.saveggs.utils.ShaderSpec;
import com.saveggs.utils.WorldUtils;

public class GameStage extends Stage implements ContactListener{

	public Viewport viewport;
	public Box2DDebugRenderer debugRenderer;
	public World world;
	private FPSLogger logger;
	private Qice qice;
	private DynamicBall dynamicBall;
	private DynamicBall staticBall;
	private Ground ground;
	private Slingshot slingshot;
	private Enemy enemy;
	private EnemyOtherSide enemyOtherSide;
	private OrthographicCamera camera;
	private CreateMesh mesh;
	private CreateMesh2 mesh2;
	private WorldUtils worldUtils;
	private ShaderProgram shader;
	//Touch position
	private	Vector3 position = new Vector3();
	private Vector3 touchDown = new Vector3();
	private Vector2 getCoords = new Vector2();
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
	
	public GameStage(WorldUtils worldUtils){
		super(new ExtendViewport(Constants.SCENE_WIDTH / 35, Constants.SCENE_HEIGHT / 35, new OrthographicCamera()));
		setupCamera();
		getViewport().setCamera(camera);
		setUtils();
		setupWorld("data/maps/level4/map.tmx");
		Gdx.input.setInputProcessor(this);
		//Gdx.input.setCatchBackKey(true);
		this.worldUtils = worldUtils;	
		
	}
		
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		camera.unproject(touchDown.set(screenX, screenY, 0));
		//System.out.println("touchpos" + " " +touchDown.x + " " + touchDown.y);		
		return super.touchDown(screenX, screenY, pointer, button);
	}


	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		camera.unproject(position.set(screenX, screenY, 0));
		
		//Update mesh coordss
		mesh.calculateXYLastikOne(position.x,position.y);
		mesh.drawBody = true;
		//Update mesh2 coords
		mesh2.calculateXYLastikTwo(position.x,position.y);
		mesh2.drawBody = true;
		//Move the static ball on lastik - 0.4f
		staticBall.setBodyPosOnLastik(position.x, position.y);

		return super.touchDragged(screenX, screenY, pointer);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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
	    
		return super.touchUp(screenX, screenY, pointer, button);
	}

	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		super.act(delta);
				
		world.step(1 / 60f,6, 2);

		//destroy bodies if out of range
		destroyFlyingBirds();
		destroySleepingBalls();
		resetEnemyIfOutOfBounds();
		removeIzlupeniQica();
		//removeIzlupeniQica();
		//debugRenderer.render(world,camera.combined);
		logger.log();
		//System.out.println(GLProfiler.calls);
		
	}
	
	//Set up camera
	public void setupCamera(){
        camera = new OrthographicCamera();
	}
	
	//Set up world
	public void setupWorld(String mapPath){
		world = new World(new Vector2(0,-9.8f), true);
		world.setContactListener(this);
		
		System.out.println("gravity is " +world.getGravity());
		
		CurrentMap map = new CurrentMap(mapPath,world);
		addActor(map);


		bodiesOfWorld = new Array<Body>();
		world.getBodies(bodiesOfWorld);
		
		int timeOfIzlupvane = 10;
		//map of all bodies
		for (Body bodyLoop : bodiesOfWorld) {
			//get bodies
			worldBodies.put(bodyLoop.getUserData().toString(), bodyLoop.getPosition());
			//get qica
			if(bodyLoop.getUserData().equals(Constants.QICE)){	
				//neizplupeno qice
				bodyLoop.setAwake(false);
				
				qice = new Qice(bodyLoop,timeOfIzlupvane);
				eggs.add(qice);
				timeOfIzlupvane += 10;
				addActor(qice);
			}
		}
		

		
		//shader
		shader = new ShaderProgram(ShaderSpec.vertexShader, ShaderSpec.fragmentShader);		

		mesh = new CreateMesh(WorldUtils.createMesh(),shader);
		addActor(mesh);
		//static ball
		staticBall = new DynamicBall(WorldUtils.createDynamicBall(world));
		staticBall.body.setGravityScale(0f);
		staticBall.body.setTransform(Constants.middleX, Constants.middleY,0);
		addActor(staticBall);
		//draw 2nd mesh
		mesh2 = new CreateMesh2(WorldUtils.createMesh2(),shader);
		addActor(mesh2);
		//slingshot
		slingshot = new Slingshot(WorldUtils.createSlingshot(world));
		addActor(slingshot);

		//enemy
		enemyOtherSide = new EnemyOtherSide(WorldUtils.createEnemyOtherSide(world),eggs,worldBodies);
		addActor(enemyOtherSide);
		enemy = new Enemy(WorldUtils.createEnemy(world),eggs,worldBodies);
		addActor(enemy);
		startEnemyTwo();

		
		//bird1
		flyingBird = new FlyingBirds(WorldUtils.createFlyingBird(world));
		addActor(flyingBird);
		FlyingBirds.pointBodyToAngle(135f, flyingBird.body);
		destroyFlyingBirds.add(flyingBird);
		
		//bird2
		flyingBird2 = new FlyingBirds2(WorldUtils.createFlyingBird2(world));
		flyingBird2.animatedBox2DSprite.flipFrames(true, false);
		addActor(flyingBird2);
		FlyingBirds2.pointBodyToAngle(135f, flyingBird2.body);
		destroyFlyingBirds2.add(flyingBird2);
		
		//PArticle effects
		particleEffect = new ParticleEffectAn();
		addActor(particleEffect);
		particleBall = new ParticleEffectBall();
		addActor(particleBall);
		particleIzlupvane = new ParticleIzlupvane();
		addActor(particleIzlupvane);
		flyingBirdParticle = new ParticleEffectFlyingBird();
		addActor(flyingBirdParticle);
		pruskane = new PruskaneQice();
		addActor(pruskane);

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
    		enemy.resetBody();            		
    	}
    	if(!BodyUtils.bodyInBounds(enemyOtherSide.body,camera)){
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
					if(((Qice)body1.getFixtureList().first().getUserData()).runBatch == false){
						((Qice)body1.getFixtureList().first().getUserData()).vzetoQice = true;
					}
	        		enemy.pribirane = true;
				}
				//make sure qiceto ne se izlupva i qiceto e  tova za koeto se e zaputila pticata
				else if(body2 != null && !qice.isAwake() && (((Qice)body2.getFixtureList().first().getUserData()).body == qice)){
					enemyOtherSide.hvashtane = false;
					if(((Qice)body2.getFixtureList().first().getUserData()).runBatch == false)
						((Qice)body2.getFixtureList().first().getUserData()).vzetoQice = true;
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
							//launch the other enemy
							startEnemyOne();
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

							//puskane na qiceto
							if(enemyOtherSide.hvashtane){
								((Qice)toRemove2.getFixtureList().first().getUserData()).drawing = true;
								((Qice)toRemove2.getFixtureList().first().getUserData()).animationDraw = true;
							}
							startEnemyTwo();
						}
					});
				}
			}
        }
        
        /**
         * razmazano qice
         */
        if( (contact.getFixtureA().equals(Constants.QICE) && contact.getFixtureB().getBody().getUserData().equals(Constants.GROUND))
                || (contact.getFixtureA().getBody().getUserData().equals(Constants.GROUND) && contact.getFixtureB().getUserData().equals(Constants.QICE)) ){
        	
			final Body qice = contact.getFixtureA().getUserData().equals(Constants.QICE) ?
					  contact.getFixtureA().getBody() : contact.getFixtureB().getBody();
			if(qice.isSleepingAllowed()){				
				pruskane.effect.setPosition(qice.getPosition().x, qice.getPosition().y);
				pruskane.showEffect = true;
			}
			qice.setSleepingAllowed(false);
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
	public void endContact(Contact contact) {}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {}
	
}

package com.saveggs.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sageggs.actors.CreateMesh;
import com.sageggs.actors.CreateMesh2;
import com.sageggs.actors.CurrentMap;
import com.sageggs.actors.DynamicBall;
import com.sageggs.actors.DynamicBall.DynamicBallPool;
import com.sageggs.actors.FlyingBirds;
import com.sageggs.actors.particles.ParticleEffectAn;
import com.sageggs.actors.particles.ParticleEffectBall;
import com.sageggs.actors.particles.ParticleIzlupvane;
import com.sageggs.actors.Enemy;
import com.sageggs.actors.Ground;
import com.sageggs.actors.Qice;
import com.sageggs.actors.Slingshot;
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
	public Array<DynamicBall> sleeepingBalls;
	private ParticleEffectAn particleEffect;
	private ParticleEffectBall particleBall;
	private ParticleIzlupvane particleIzlupvane;
	private DynamicBallPool pool;
	private FlyingBirds flyingBird;
	
	public GameStage(){
		super(new ExtendViewport(Constants.SCENE_WIDTH / 35, Constants.SCENE_HEIGHT / 35, new OrthographicCamera()));
		setupCamera();
		getViewport().setCamera(camera);
		setUtils();
		setupWorld("data/maps/level1/map.tmx");
		Gdx.input.setInputProcessor(this);
		worldUtils = new WorldUtils();		
	}
	
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		camera.unproject(touchDown.set(screenX, screenY, 0));
		//System.out.println("touchpos" + " " +touchDown.x + " " + touchDown.y);
		//float angle = (float) Math.toDegrees(Math.atan2(target.y - (body.getPosition().y), target.x  - (body.getPosition().x)));
		//enemy.direction.x += 1f;
		//System.out.println(enemy.body.getWorldCenter());
		
		//EnemyUtils.pointBodyToAngle(enemy.currentAngle + 12f,enemy.body);
		//enemy.body.applyAngularImpulse(10, true);//(10, true);//10, true);

		//enemy.animatedBox2DSprite.setRotation(45f);
		
		//enemy.body.setTransform(enemy.body.getPosition().x, enemy.body.getPosition().y, enemy.body.getAngle());
	   
		
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
		//dynamicBall = null;
	   // dynamicBall = new DynamicBall(WorldUtils.createDynamicBall(world));
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
		//destroyBododies();
		destroySleepingBodies();
		
		debugRenderer.render(world,camera.combined);
		//logger.log();
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
		
		CurrentMap map = new CurrentMap(mapPath,world);
		addActor(map);
		//add the map
		//shader
		shader = new ShaderProgram(ShaderSpec.vertexShader, ShaderSpec.fragmentShader);		
		//Ground
		//ground = new Ground(WorldUtils.createGround(world));
		//addActor(ground);
		//draw mesh1
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
		//qice
		qice = new Qice(WorldUtils.createEgg(world));
		qice.body.setTransform(Constants.eggPositions[0].x, Constants.eggPositions[0].y, 0);
		//System.out.println(ba.body.getWorldCenter());
		addActor(qice);
		//enemy
		enemy = new Enemy(WorldUtils.createEnemy(world));
		addActor(enemy);
		
		flyingBird = new FlyingBirds(WorldUtils.createFlyingBird(world));
		//flyingBird.animatedBox2DSprite.flipFrames(true, false);
		addActor(flyingBird);
		FlyingBirds.pointBodyToAngle(90f + 45f, flyingBird.body);
		
		//PArticle effects
		particleEffect = new ParticleEffectAn();
		addActor(particleEffect);
		particleBall = new ParticleEffectBall();
		addActor(particleBall);
		particleIzlupvane = new ParticleIzlupvane();
		addActor(particleIzlupvane);
		
	}
	
	//utils
	public void setUtils(){
		debugRenderer = new Box2DDebugRenderer();
		bodies = new Array<Body>();
		sleeepingBalls = new Array<DynamicBall>();
		pool = new DynamicBallPool();
		logger = new FPSLogger();
	}
	
	
	//Destroy bodies if out of range
	public void destroyBododies(){
		world.getBodies(bodies);
        for (Body body : bodies) {
        	System.out.println(body.getUserData());
        	body.getFixtureList().first();
        	if(!BodyUtils.bodyInBounds(body,camera) && !body.getUserData().equals(Constants.DynamicBall))
        		System.out.println();//world.destroyBody(body);
        }
	}
	
	//Destroy sleeping bodies
	public void destroySleepingBodies(){
		
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

	/*
	 * Contact Listener
	 */
	@Override
	public void beginContact(Contact contact) {
        
		//Otvarqne na krakata na pticata
        if( (contact.getFixtureA().getBody().getUserData().equals(Constants.QICE) && contact.getFixtureB().getUserData().equals(Constants.DOKOSVANESQICE ))
            || (contact.getFixtureA().getUserData().equals(Constants.DOKOSVANESQICE) && contact.getFixtureB().getBody().getUserData().equals(Constants.QICE)) ){        	
        	enemy.naOtivaneDraw = false;
        	enemy.hvashtane = true;
        }
        //Hvashtane na qiceto i pribirane na krakata
        if( (contact.getFixtureA().getBody().getUserData().equals(Constants.QICE) && contact.getFixtureB().getUserData().equals( Constants.SENSORzaDOKOSVANE))
                || (contact.getFixtureA().getUserData().equals( Constants.SENSORzaDOKOSVANE) && contact.getFixtureB().getBody().getUserData().equals(Constants.QICE)) ){        	
        		enemy.hvashtane = false;
        		//qice.drawing = false;
        		enemy.pribirane = true;
                final Body toRemove = contact.getFixtureA().getBody().getUserData().equals(Constants.QICE) ?
						  contact.getFixtureA().getBody() : contact.getFixtureB().getBody();
						  
			  	Gdx.app.postRunnable(new Runnable() {
			  		@Override
			  		public void run () {
			  			//world.destroyBody(toRemove);
			  		}
			  	});
         }
        //unishtojavane na enemy i create a new one 
        if( (contact.getFixtureA().getBody().getUserData().equals(Constants.DynamicBall) && contact.getFixtureB().getUserData().equals( Constants.EnemyHitArea ))
                || (contact.getFixtureA().getUserData().equals( Constants.EnemyHitArea) && contact.getFixtureB().getBody().getUserData().equals(Constants.DynamicBall)) ){
        	particleEffect.effect.setPosition(enemy.body.getPosition().x, enemy.body.getPosition().y);
        	particleEffect.showEffect = true;
            final Body toRemove = contact.getFixtureA().getBody().getUserData().equals(Constants.Enemy) ?
						  contact.getFixtureA().getBody() : contact.getFixtureB().getBody();

			  	Gdx.app.postRunnable(new Runnable() {
			  		@Override
			  		public void run () {
			  			enemy.enemyDraw = false;
			  			world.destroyBody(toRemove);
			  			//enemy
			  			enemy = new Enemy(WorldUtils.createEnemy(world));
			  			addActor(enemy);
			  		}
			  	});
        }
        
        //FLYING BIRDS DIRECTION
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
			  			FlyingBirds.pointBodyToAngle(45 + 90f, myBody);
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
			  			FlyingBirds.pointBodyToAngle(20 - 90f, myBody);
			  		}
			  	});
        }
        
      }


	@Override
	public void endContact(Contact contact) {}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {}
	
}

package com.sageggs.actors.enemies;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import net.dermetfan.gdx.graphics.g2d.AnimatedBox2DSprite;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;
import assets.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.utils.Array;
import com.sageggs.actors.GameActor;
import com.sageggs.actors.Qice;
import com.sageggs.actors.particles.MaskaBurst;
import com.saveggs.utils.Constants;
import com.saveggs.utils.EnemyUtils;
import com.saveggs.utils.WorldUtils;

public class Enemy extends GameActor{

	private Vector2 target = new Vector2();
	public static boolean stopMoving = false; 
	Vector2 position = new Vector2();
	private AnimatedSprite animatedSprite;
	private AnimatedBox2DSprite animatedBox2DSprite;
	private Box2DSprite naOtivane, otvarqne, hvanatoQice;
	private Animation animation;
	private TextureRegion[] animationFrames;
	private Texture texture;
	public boolean naOtivaneDraw = true, hvashtane = false, pribirane = false,  enemyDraw = true;
	float[] positions = new float[]{1,2,3};
	float myPosition;
	private Array<Body> bodies;
	public Array<Vector2> pathPoints;
	private Vector2 resetPosition;
	private Map<String,Vector2> worldBodies;
	private Array<Qice> eggs;
	private int point = 0;
	private float angle;
	private  Vector2 direction = new Vector2(), velocity = new Vector2();
	private boolean continueUpdating = true;
	public Qice singleEgg;
	private Vector2 vec1,vec2, vec3, vec4;
	private boolean redirect = true;
	public boolean anyEggsLeft = true;
	private float speed = 0;
	private float speedBoost = 0;
	private TextureAtlas atlas;
	private float flySpeed = 0;
	private Box2DSprite maska;
	private TextureRegion brokenMask,normalMask;
	public boolean enemyHit,maskaBurst = false;
	public boolean mask = false;
	private MaskaBurst maskBurst;
	
	public Enemy(Body body,Array<Qice> eggs,Map<String,Vector2> worldBodies) {
		super(body);
		atlas = Assets.manager.get(Assets.gameAtlas, TextureAtlas.class);	
		normalMask = new TextureRegion(atlas.findRegion("maska1"));
		brokenMask = new TextureRegion(atlas.findRegion("maska2"));
		maska = new Box2DSprite(normalMask);		
		maska.flip(false, true);
		normalMask.flip(false, true);
		brokenMask.flip(false, true);
		vec1 = new Vector2();
		vec2 = new Vector2();
		vec3 = new Vector2();
		vec4 = new Vector2();
		resetPosition = new Vector2();
		pathPoints = new Array<Vector2>();
		
		if(Constants.gameDifficulty.getString("levels").equals(Constants.easyLevels)){
			flySpeed = 9;
		}
		else if(Constants.gameDifficulty.getString("levels").equals(Constants.mediumLevels)){
			flySpeed = 12;
		}
		else if(Constants.gameDifficulty.getString("levels").equals(Constants.difficultLevels)){
			flySpeed = 15;
		}
		
		this.eggs = eggs;
		this.worldBodies = worldBodies;
		//get random position
		
		//animation
		texture = Assets.manager.get(Assets.pileBezKraka, Texture.class);
		splitAnimation();
		animation = new Animation(1f/13f, animationFrames);
		animation.setPlayMode(Animation.PlayMode.LOOP);
		animatedSprite = new AnimatedSprite(animation);
		animatedBox2DSprite = new AnimatedBox2DSprite(animatedSprite);
		//kraka
		naOtivane = new Box2DSprite(atlas.findRegion("prisviti"));
		otvarqne = new Box2DSprite(atlas.findRegion("opunati"));
		hvanatoQice = new Box2DSprite(atlas.findRegion("hvanato_qice"));
		//nastroivane na ugula
		resetBody();
		
	}


    @Override
    public void act(float delta) {
        super.act(delta);
        
        if(anyEggsLeft){        	
        	if(redirect){
        		if(Math.abs(body.getPosition().sub(pathPoints.get(pathPoints.size - 1)).len()) <= EnemyUtils.comparVal ){
        			update(MathUtils.random(150f,90f),delta);
        			continueUpdating = false;
        			redirect = false;
        		}
        	}
        	//else update next point
        	if(continueUpdating)
        		update(getAngle(),delta);
        }
    }


	//set velocity of enemy to target
	public void update(float angle, float delta){
		
		//angle = (float) Math.toDegrees(Math.atan2(pathPoints.get(point).y - body.getPosition().y, pathPoints.get(point).x  - body.getPosition().x));
		//calculate velocity
		direction.x = (float) Math.cos((angle) * MathUtils.degreesToRadians);
		direction.y = (float) Math.sin((angle) * MathUtils.degreesToRadians);
		direction.nor();
		
		//accelerate speed of bird when grabbing the egg
		if(point == 1 || point == 2 ){
			speedBoost = 1.63f;
			animation.setFrameDuration(1/1f);
		}
		else if(point == 3 || point == 4){
			speedBoost = 1.30f;
			animation.setFrameDuration(1/(flySpeed+5));
		}
		else{
			speedBoost = 1f;
			animation.setFrameDuration(1/flySpeed);
		}
		velocity.x = (direction.x * speed * delta) * speedBoost;
		velocity.y = (direction.y * speed  * delta) * speedBoost;
		
		//get the angle
		float getAngle = (float) Math.atan2( -direction.x, direction.y );
		//position body at angle
		body.setTransform(body.getPosition(), getAngle);
		
		//set velocity
		body.setLinearVelocity(velocity);
		
		if(continueUpdating){			
			//increment to next point
			if(Math.abs(body.getPosition().sub(pathPoints.get(point)).len()) <= EnemyUtils.comparVal){
				point++;
			}
		}
	}

	public float getAngle(){
		return (float) Math.toDegrees(Math.atan2(pathPoints.get(point).y - body.getPosition().y, pathPoints.get(point).x  - body.getPosition().x));
	}
	
	 @Override
     public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if(enemyDraw){        	
        	if(naOtivaneDraw)
        		naOtivane.draw(batch, body.getFixtureList().get(2)); 
        	if(hvashtane)
        		otvarqne.draw(batch, body.getFixtureList().get(2));       
        	if(pribirane)
        		hvanatoQice.draw(batch, body.getFixtureList().get(3)); 
        	
    		naOtivane.setRotation(body.getAngle() - 90f);
    		otvarqne.setRotation(body.getAngle() - 90f);
    		hvanatoQice.setRotation(body.getAngle() - 70f);
    		animatedBox2DSprite.setRotation(body.getAngle() - 90f);
        	
    		
        	animatedBox2DSprite.draw(batch, body.getFixtureList().first()); 
        	if(mask){	
        		if(enemyHit){
        			setMask();
        		}
        		maska.setRotation(body.getAngle() + 70f);
        		maska.draw(batch, body.getFixtureList().get(6)); 
        	}
        	
        }
     }
	
	 
	 public void splitAnimation(){
		 TextureRegion[][] tmpFrames = TextureRegion.split(texture, 256, 256);
		 animationFrames = new TextureRegion[8];
		 int index = 0;		 
		 for (int i = 0; i < 2; ++i){
			 for (int j = 0; j < 4; ++j){
				 animationFrames[index++] = tmpFrames[i][j];
			 }
		 }
	 }
	 	

    //reset the body
	public void resetBody(){

		pathPoints.clear();
		//pick a random egg

	
		if(eggs.size - 1 != -1){	
			//position enemey
			myPosition = MathUtils.random(1, 3);

			resetPosition = worldBodies.get("position" + (int)myPosition);
			body.setTransform(resetPosition, 0);
			
			//get random egg
			singleEgg = eggs.get((int)MathUtils.random(0, eggs.size - 1));
			
			//used to make sure it's the right egg (see contact in gamestage)
			body.getFixtureList().first().setUserData(singleEgg);
			
			
			//fill in the path points for this egg
			pathPoints.add(singleEgg.body.getTransform().mul(vec1.set((((CircleShape) singleEgg.body.getFixtureList().get(1).getShape()).getPosition()))));
			pathPoints.add(singleEgg.body.getTransform().mul(vec2.set((((CircleShape) singleEgg.body.getFixtureList().get(2).getShape()).getPosition()))));
			pathPoints.add(singleEgg.body.getTransform().mul(vec3.set((((CircleShape) singleEgg.body.getFixtureList().get(3).getShape()).getPosition()))));
			pathPoints.add(singleEgg.body.getTransform().mul(vec4.set((((CircleShape) singleEgg.body.getFixtureList().get(5).getShape()).getPosition()))));
		}
		//no eggs left
		else{
			enemyDraw = false;
			speed = 0;
			anyEggsLeft = false;
		}
		
        point = 0;
        continueUpdating = true;
        hvashtane = false;
        pribirane = false;
        naOtivaneDraw = true;
        redirect = true;
	}


	public float getSpeed() {
		return speed;
	}


	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public void setMask(){
		maska.setRegion(brokenMask);
	}
	
	public void resetMask(){
		mask = false;
		maska.setRegion(normalMask);
	}
	
}

package com.sageggs.actors.enemies;

import java.text.DecimalFormat;
import java.util.Map;

import net.dermetfan.gdx.graphics.g2d.AnimatedBox2DSprite;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;
import assets.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.utils.Array;
import com.sageggs.actors.GameActor;
import com.sageggs.actors.Qice;
import com.saveggs.utils.Constants;
import com.saveggs.utils.EnemyUtils;
import com.saveggs.utils.WorldUtils;

public class EnemyOtherSide extends GameActor implements CommonEnemy{

	public static boolean stopMoving = false; 
	public static float angle = 0;
	private AnimatedSprite animatedSprite,animatedSprite2,animatedSprite3,animatedSprite4,animatedSprite5;
	public AnimatedBox2DSprite normalFlyingPile,zamaqnoPilence,actualAnimation,losh,zamaqnLosh,prediLosh;
	public Box2DSprite naOtivane,otvarqne,hvanatoQice,padashtoPile,padashtLosh;
	public Animation animation,animation2,animation3,animation4,animation5;
	private TextureRegion[] region1,region2,region3,region4,region5;
	public boolean naOtivaneDraw = true;
	public boolean hvashtane = false;
	public boolean pribirane = false;
	public boolean enemyDraw = false;
	float[] positions = new float[]{1,2,3};
	float myPosition;
	private  Vector2 direction = new Vector2(), velocity = new Vector2();
	public Qice singleEgg;
	private Vector2 vec1;
	private Vector2 vec2;
	private Vector2 vec3;
	private Vector2 vec4;
	private boolean redirect = true;
	public Array<Vector2> pathPoints;
	private Vector2 resetPosition;
	private Map<String,Vector2> worldBodies;
	private Array<Qice> eggs;
	int point = 0;
	private boolean continueUpdating = true;
	public boolean anyEggsLeft = true;
	private float speed = 0f;
	private float speedBoost = 0;
	private TextureAtlas atlas,atlas2;
	private float flySpeed = 0;
	public boolean padashto,padashtoLosh = false;
	public boolean pokajiPadashto,pokajiLossh = false;
	
	public EnemyOtherSide(Body body,Array<Qice> eggs,Map<String,Vector2> worldBodies) {
		super(body);
		atlas = Assets.manager.get(Assets.allEnemies, TextureAtlas.class);	
		atlas2 = Assets.manager.get(Assets.gameAtlas, TextureAtlas.class);	
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
		splitAnimation();
		animation = new Animation(1f/13f, region1);
		animation.setPlayMode(Animation.PlayMode.LOOP);
		//anim2
		animation2 = new Animation(1f/13f, region2);
		animation2.setPlayMode(Animation.PlayMode.LOOP);
		//anim3
		animation3 = new Animation(1f/13f, region3);
		animation3.setPlayMode(Animation.PlayMode.LOOP);
		//amnim4
		animation4 = new Animation(1f/13f, region4);
		animation4.setPlayMode(Animation.PlayMode.LOOP);
		
		//anim5
		animation5 = new Animation(1f/13f, region5);
		animation5.setPlayMode(Animation.PlayMode.LOOP);
		
		animatedSprite = new AnimatedSprite(animation);
		animatedSprite2 = new AnimatedSprite(animation2);
		animatedSprite3 = new AnimatedSprite(animation3);
		animatedSprite4 = new AnimatedSprite(animation4);
		animatedSprite5 = new AnimatedSprite(animation5);
		
		prediLosh= new AnimatedBox2DSprite(animatedSprite5);
		zamaqnLosh = new AnimatedBox2DSprite(animatedSprite4);
		losh = new AnimatedBox2DSprite(animatedSprite3);
		normalFlyingPile = new AnimatedBox2DSprite(animatedSprite2);
		zamaqnoPilence = new AnimatedBox2DSprite(animatedSprite);
		actualAnimation = normalFlyingPile;
		//box2d sprites
		naOtivane = new Box2DSprite(atlas2.findRegion("prisviti"));
		otvarqne = new Box2DSprite(atlas2.findRegion("opunati"));
		hvanatoQice = new Box2DSprite(atlas2.findRegion("hvanato_qice"));
		padashtoPile = new Box2DSprite(atlas.findRegion("padashtoRed"));
		padashtLosh = new Box2DSprite(atlas.findRegion("loshPadasht2"));

		//flip all sprites
		if(!normalFlyingPile.isFlipX()){			
			normalFlyingPile.flipFrames(true, true);
			zamaqnoPilence.flipFrames(true, true);
			zamaqnLosh.flipFrames(true, true);
			losh.flipFrames(true, true);
			prediLosh.flipFrames(true, true);
		}
		padashtoPile.flip(true, true);
		padashtLosh.flip(true, true);
		naOtivane.flip(false, true);
		otvarqne.flip(false, true);
		hvanatoQice.flip(false, true);
		
		resetBody();
	}
	
    @Override
    public void act(float delta) {
        super.act(delta);
        if(anyEggsLeft){ 
        	if(body.getGravityScale() == 0)
    		{	        		
        		if(redirect){        	
        			if(Math.abs(body.getPosition().sub(pathPoints.get(pathPoints.size - 1)).len()) <= EnemyUtils.comparVal ){
        				update(MathUtils.random(110f,50f),delta);
        				continueUpdating = false;
        				redirect = false;
        			}
        		}
        		//else update next point
        		if(continueUpdating)
        			update(getAngle(),delta);
    		}
        }
    }
    
    
	//set velocity of enemy to target
	public void update(float angle, float delta){
		
			//calculate velocity
			direction.x = (float) Math.cos((angle) * MathUtils.degreesToRadians);
			direction.y = (float) Math.sin((angle) * MathUtils.degreesToRadians);
			direction.nor();
			
			//accelerate speed of bird when grabbing the egg
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
    		hvanatoQice.setRotation(body.getAngle() - 120f);
    		actualAnimation.setRotation(body.getAngle() - 90f);
    		
        	
        	if(padashto){
        		padashtoPile.setRotation(body.getAngle() - 90f);
        		padashtoPile.draw(batch, body.getFixtureList().first());
        	}
        	else if(padashtoLosh){
        		padashtLosh.setRotation(body.getAngle() - 90f);
        		padashtLosh.draw(batch, body.getFixtureList().first());
        	}
        	else{
        		actualAnimation.draw(batch, body.getFixtureList().first()); 
        	}
        	
        }
     }
	
	 
	 public void splitAnimation(){
		 region1 = new TextureRegion[2];
		 region2 = new TextureRegion[8];
		 region3 = new TextureRegion[8];
		 region4 = new TextureRegion[2]; 
		 region5 = new TextureRegion[8];
		 
		 region1[0] = atlas.findRegion("zamaqnoRed1");
		 region1[1] = atlas.findRegion("zamaqnoRed2");
		 //losh
		 region4[0]= atlas.findRegion("zamaqnoLosh11");
		 region4[1]= atlas.findRegion("zamaqnoLosh21");
		 //
		 region2[0] = atlas.findRegion("normal1");
		 region2[1] = atlas.findRegion("normal2");
		 region2[2] = atlas.findRegion("normal3");
		 region2[3] = atlas.findRegion("normal4");
		 region2[4] = atlas.findRegion("normal5");
		 region2[5] = atlas.findRegion("normal6");
		 region2[6] = atlas.findRegion("normal7");
		 region2[7] = atlas.findRegion("normal8");
		 
		 region3[0] = atlas.findRegion("losh1");
		 region3[1] = atlas.findRegion("losh2");
		 region3[2] = atlas.findRegion("losh3");
		 region3[3] = atlas.findRegion("losh4");
		 region3[4] = atlas.findRegion("losh5");
		 region3[5] = atlas.findRegion("losh6");
		 region3[6] = atlas.findRegion("losh7");
		 region3[7] = atlas.findRegion("losh8");
		 
		 region5[0] = atlas.findRegion("normalRed1");
		 region5[1] = atlas.findRegion("normalRed2");
		 region5[2] = atlas.findRegion("normalRed3");
		 region5[3] = atlas.findRegion("normalRed4");
		 region5[4] = atlas.findRegion("normalRed5");
		 region5[5] = atlas.findRegion("normalRed6");
		 region5[6] = atlas.findRegion("normalRed7");
		 region5[7] = atlas.findRegion("normalRed8");
 
	 }
	
    //reset the body
	public void resetBody(){
		
		pathPoints.clear();
		
		if(eggs.size - 1 != -1) {
			//position enemy
			myPosition = MathUtils.random(1, 3);
			resetPosition = worldBodies.get("opposite" + (int)myPosition);
			body.setTransform(resetPosition, 0);
			//pick a random egg
			singleEgg = eggs.get((int)MathUtils.random(0, eggs.size - 1));
			//used to make sure it's the right egg (see contact in gamestage)
			body.getFixtureList().first().setUserData(singleEgg);
			
			//fill in the path points for this egg
			pathPoints.add(singleEgg.body.getTransform().mul(vec1.set((((CircleShape) singleEgg.body.getFixtureList().get(4).getShape()).getPosition()))));
			pathPoints.add(singleEgg.body.getTransform().mul(vec2.set((((CircleShape) singleEgg.body.getFixtureList().get(3).getShape()).getPosition()))));
			pathPoints.add(singleEgg.body.getTransform().mul(vec3.set((((CircleShape) singleEgg.body.getFixtureList().get(2).getShape()).getPosition()))));
			pathPoints.add(singleEgg.body.getTransform().mul(vec4.set((((CircleShape) singleEgg.body.getFixtureList().get(6).getShape()).getPosition()))));
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
	
	public AnimatedBox2DSprite getCurrentAnimation(){
		return actualAnimation;
	}
	
	public void setCurrentAnimation(AnimatedBox2DSprite animatedSprite){
		actualAnimation = animatedSprite;
	}
	
	public void resetBodyInitalStage(){
		body.setGravityScale(0);
		padashto = false;
		padashtoLosh = false;
		actualAnimation = normalFlyingPile;
		pokajiPadashto = false;
		pokajiLossh = false;
	}
	
	public void padaneNaDoly(){
		padashto = true;
		padashtoLosh = false;
		body.setGravityScale(0.5f);
	}
	
	public void padashtLosh(){
		padashtoLosh = true;
		padashto = false;
		body.setGravityScale(0.5f);
	}
	
	public void setLosh(){
		actualAnimation = losh;
	}
	
	public AnimatedBox2DSprite getLosh(){
		return losh;
	}
	
	@Override
	public EnemyOtherSide getEnemy(){
		return this;
	}
	
	public AnimatedBox2DSprite getZamqnoPilence(){
		return zamaqnoPilence;
	}
	
	public AnimatedBox2DSprite getZamaqnLosh(){
		return zamaqnLosh;
	}
	
	public boolean getEnemyDraw(){
		return enemyDraw;
	}

	public boolean getPokajiPadashto(){
		return pokajiPadashto;
	}
	
	public void setPokajiPadashto(boolean val){
		pokajiPadashto = val;
	}
	
	public boolean getPribirane(){
		return pribirane;
	}
	
	public Body getBody(){
		return body;
	}
	
	public boolean getPokajiLosh(){
		return pokajiLossh;
	}
	
	public void setPokajiLosh(boolean val){
		pokajiLossh = val;
	}
	
	public AnimatedBox2DSprite getPrediLosh() {
		return prediLosh;
	}

	public void setPrediLosh(AnimatedBox2DSprite prediLosh) {
		this.prediLosh = prediLosh;
	}
}

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
import com.saveggs.utils.Constants;
import com.saveggs.utils.EnemyUtils;
import com.saveggs.utils.WorldUtils;

public class Enemy extends GameActor implements CommonEnemy{

	private Vector2 target = new Vector2();
	public static boolean stopMoving = false; 
	Vector2 position = new Vector2();
	private AnimatedSprite animatedSprite,animatedSprite2,animatedSprite3,animatedSprite4,animatedSprite5;
	public AnimatedBox2DSprite normalFlyingPile,zamaqnoPilence,actualAnimation,losh,zamaqnLosh,prediLosh;
	public Box2DSprite naOtivane,otvarqne,hvanatoQice,padashtoPile,padashtLosh;
	public Animation animation,animation2,animation3,animation4,animation5;
	private TextureRegion[] region1,region2,region3,region4,region5;
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
	private TextureAtlas atlas,atlas2;
	private float flySpeed = 0;
	private Box2DSprite maska;
	public boolean padashto,padashtoLosh = false;
	public boolean pokajiPadashto,pokajiLossh = false;
	public static int loshCount = 0;
	
	public Enemy(Body body,Array<Qice> eggs,Map<String,Vector2> worldBodies) {
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
		//texture = Assets.manager.get(Assets.pileBezKraka, Texture.class);
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
		//animatedBox2DSprite.flipFrames(true,false );
		//kraka
		naOtivane = new Box2DSprite(atlas2.findRegion("prisviti"));
		otvarqne = new Box2DSprite(atlas2.findRegion("opunati"));
		hvanatoQice = new Box2DSprite(atlas2.findRegion("hvanato_qice"));
		padashtoPile = new Box2DSprite(atlas.findRegion("padashtoRed2"));
		padashtLosh = new Box2DSprite(atlas.findRegion("loshPadasht"));
		
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
		 //zamaqn predi losh
		 region1[0] = atlas.findRegion("zamaqnoRed12");
		 region1[1] = atlas.findRegion("zamaqnoRed22");
		 //losh
		 region4[0]= atlas.findRegion("zamaqnoLosh");
		 region4[1]= atlas.findRegion("zamaqnoLosh2");
		 //
		 region2[0] = atlas.findRegion("normal12");
		 region2[1] = atlas.findRegion("normal22");
		 region2[2] = atlas.findRegion("normal32");
		 region2[3] = atlas.findRegion("normal42");
		 region2[4] = atlas.findRegion("normal52");
		 region2[5] = atlas.findRegion("normal62");
		 region2[6] = atlas.findRegion("normal72");
		 region2[7] = atlas.findRegion("normal82");
		 
		 region3[0] = atlas.findRegion("losh12");
		 region3[1] = atlas.findRegion("losh22");
		 region3[2] = atlas.findRegion("losh32");
		 region3[3] = atlas.findRegion("losh42");
		 region3[4] = atlas.findRegion("losh52");
		 region3[5] = atlas.findRegion("losh62");
		 region3[6] = atlas.findRegion("losh72");
		 region3[7] = atlas.findRegion("losh82");
		 
		 region5[0] = atlas.findRegion("normalRed11");
		 region5[1] = atlas.findRegion("normalRed21");
		 region5[2] = atlas.findRegion("normalRed31");
		 region5[3] = atlas.findRegion("normalRed41");
		 region5[4] = atlas.findRegion("normalRed51");
		 region5[5] = atlas.findRegion("normalRed61");
		 region5[6] = atlas.findRegion("normalRed71");
		 region5[7] = atlas.findRegion("normalRed81");
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
	
    public AnimatedBox2DSprite getPrediLosh() {
		return prediLosh;
	}


	public void setPrediLosh(AnimatedBox2DSprite prediLosh) {
		this.prediLosh = prediLosh;
	}
	
	
	public void padaneNaDoly(){
		System.out.println("padashto idva");
		padashto = true;
		padashtoLosh = false;
		body.setGravityScale(0.5f);
		System.out.println(body.getGravityScale());
	}
	
	public void padashtLosh(){
		padashtoLosh = true;
		padashto = false;
		body.setGravityScale(0.5f);
	}
	
	public AnimatedBox2DSprite getLosh(){
		return losh;
	}
	
	public void setLosh(){
		actualAnimation = losh;
	}
	
	@Override
	public Enemy getEnemy(){
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
	
	public boolean getPokajiLosh(){
		return pokajiLossh;
	}
	
	public void setPokajiLosh(boolean val){
		pokajiLossh = val;
	}
	
	public boolean getPribirane(){
		return pribirane;
	}
	
	public Body getBody(){
		return body;
	}
	
}

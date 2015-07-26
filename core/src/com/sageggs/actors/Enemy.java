package com.sageggs.actors;

import java.text.DecimalFormat;

import net.dermetfan.gdx.graphics.g2d.AnimatedBox2DSprite;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;
import assets.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.saveggs.utils.Constants;
import com.saveggs.utils.EnemyUtils;
import com.saveggs.utils.WorldUtils;

public class Enemy extends GameActor{

	private Box2DSprite enemySkin;
	private Vector2 target = new Vector2();
	public static boolean stopMoving = false; 
	public static float angle = 0;
	Vector2 position = new Vector2();
	public Vector2 direction = new Vector2();
	private AnimatedSprite animatedSprite;
	private AnimatedBox2DSprite animatedBox2DSprite;
	public Box2DSprite naOtivane;
	public Box2DSprite otvarqne;
	private Animation animation;
	private TextureRegion[] animationFrames;
	private TextureRegion[] animationKraka;
	private Texture texture;
	private Vector2 ballPos;
	private Vector2 newPos;
	public Box2DSprite hvanatoQice;
	public boolean naOtivaneDraw = true;
	public boolean hvashtane = false;
	public boolean pribirane = false;
	public boolean enemyDraw = true;
	private Array<Body> bodies;
	
	public Enemy(Body body) {
		super(body);
		target = Constants.eggPositions[WorldUtils.randInt(0, Constants.eggPositions.length -1)];
        setAngleToTaget();
        
/*		bodies = new Array<Body>();
		body.getWorld().getBodies(bodies);
        for (Body bodyLoop : bodies) {
        	if(bodyLoop.getUserData().equals("position2"))
        		body.setTransform(bodyLoop.getPosition().x, bodyLoop.getPosition().y, 0);
        }
*/
    	
		
		//animation
		texture = Assets.manager.get(Assets.pileBezKraka, Texture.class);
		splitAnimation();
		animation = new Animation(1f/35f, animationFrames);
		animation.setPlayMode(Animation.PlayMode.LOOP);
		animatedSprite = new AnimatedSprite(animation);
		animatedBox2DSprite = new AnimatedBox2DSprite(animatedSprite);
		//kraka
		naOtivane = new Box2DSprite(Assets.manager.get(Assets.prisvivaneKraka, Texture.class));
		otvarqne = new Box2DSprite(Assets.manager.get(Assets.opuvaneKraka, Texture.class));
		naOtivane.setRotation(getAngleBodyEgg(target) + 30f);
		otvarqne.setRotation(getAngleBodyEgg(target) + 30f);
		//kraka s hvanato qice
		hvanatoQice = new Box2DSprite(Assets.manager.get(Assets.hvanatoQice, Texture.class));
		//nastroivane na ugula
		EnemyUtils.pointBodyToAngle(getAngleBodyEgg(target) + 3f, body);
		//set bird angle
		hvanatoQice.setRotation(getAngleBodyEgg(target) + 55f);
		animatedBox2DSprite.setRotation(getAngleBodyEgg(target) + 30f);
		//animatedBox2DSprite.flipFrames(true, false);
	}
	
    @Override
    public void act(float delta) {
        super.act(delta);
        EnemyUtils.updateEnemyAngle(target,body);
    }
    
	 @Override
	 
     public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if(enemyDraw){        	
        	/*if(naOtivaneDraw)
        		naOtivane.draw(batch, body.getFixtureList().get(2)); 
        	if(hvashtane)
        	{
        		naOtivane.draw(batch, body.getFixtureList().get(2)); 
        		naOtivane.set(otvarqne);
        	}       
        	if(pribirane)*/
        		hvanatoQice.draw(batch, body.getFixtureList().get(3)); 
        	
        	animatedBox2DSprite.draw(batch, body.getFixtureList().first()); 
        }
     }
	
	 
	 public void splitAnimation(){
		 TextureRegion[][] tmpFrames = TextureRegion.split(texture, 240, 314);
		 animationFrames = new TextureRegion[4 * 5];
		 int index = 0;		 
		 for (int i = 0; i < 4; ++i){
			 for (int j = 0; j < 5; ++j){
				 animationFrames[index++] = tmpFrames[i][j];
			 }
		 }
	 }
	 
	//get the angle between enemy and target
	public float getAngleBodyEgg(Vector2 target){
		 float angle = (float) Math.toDegrees(Math.atan2(target.y - (body.getPosition().y), target.x  - (body.getPosition().x)));
		 return angle;
	}
	
	//set enemy at specific position to the target
	public void setAngleToTaget(){
		System.out.println("distance is " + target.dst(new Vector2(Constants.SCENE_WIDTH / 35 * 0.5f,Constants.SCENE_HEIGHT / 35 * 0.5f) ));
		float angle = 0;
/*		if(target.x < 0){
			angle = 60;
		}
		else{
			angle = 60;
		}*/
		angle = 50f;
		float x = (float) ( (target.dst(new Vector2(Constants.SCENE_WIDTH / 35 * 0.6f,Constants.SCENE_HEIGHT / 35 * 0.6f)) - 3f ) * Math.cos(angle * MathUtils.degreesToRadians));
		float y = (float) ( (target.dst(new Vector2(Constants.SCENE_WIDTH / 35 * 0.6f,Constants.SCENE_HEIGHT / 35 * 0.6f)) - 3f ) * Math.sin(angle * MathUtils.degreesToRadians));
		ballPos = new Vector2(Constants.eggPositions[0].x, Constants.eggPositions[0].y);
	    newPos = ballPos.add(new Vector2(x,y));
  
        body.setTransform(newPos.x, newPos.y,0);
	}	
}

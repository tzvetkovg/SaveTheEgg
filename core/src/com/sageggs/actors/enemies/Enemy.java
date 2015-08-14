package com.sageggs.actors.enemies;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

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
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.utils.Array;
import com.sageggs.actors.GameActor;
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
	private Array<Body> eggs;
	int point = 0;
	float angle;
	private  Vector2 direction = new Vector2(), velocity = new Vector2();
	private boolean continueUpdating = true;
	
	public Enemy(Body body) {
		super(body);
		target = Constants.eggPositions[WorldUtils.randInt(0, Constants.eggPositions.length -1)];
        
		resetPosition = new Vector2();
		pathPoints = new Array<Vector2>();
		eggs = new Array<Body>();
		bodies = new Array<Body>();
		body.getWorld().getBodies(bodies);
		worldBodies = new HashMap<String,Vector2>();
		
		//map of all bodies
		for (Body bodyLoop : bodies) {
			worldBodies.put(bodyLoop.getUserData().toString(), bodyLoop.getPosition());
			if(bodyLoop.getUserData().equals(Constants.QICE)){	
				eggs.add(bodyLoop);
			}
		}


		/*pathPoints.add(worldBodies.get("egg1path11"));
		pathPoints.add(worldBodies.get("egg1path12"));
		pathPoints.add(worldBodies.get("egg1path13"));
		pathPoints.add(worldBodies.get("egg1path18"));
		pathPoints.add(worldBodies.get("egg1path19"));*/
		
		//get random position
		resetBody();
		
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
		hvanatoQice = new Box2DSprite(Assets.manager.get(Assets.hvanatoQice, Texture.class));
		//nastroivane na ugula

	}


    @Override
    public void act(float delta) {
        super.act(delta);
        
        //if last point reached continue with same velocity
		if(Math.abs(body.getPosition().sub(pathPoints.get(pathPoints.size - 1)).len()) <= EnemyUtils.comparVal ){
			body.setLinearVelocity(body.getLinearVelocity());
			continueUpdating = false;
		}
		//else update next point
		if(continueUpdating)
			update(delta);
    }


	//set velocity of enemy to target
	public void update(float delta){
		
		angle = (float) Math.toDegrees(Math.atan2(pathPoints.get(point).y - body.getPosition().y, pathPoints.get(point).x  - body.getPosition().x));
		//calculate velocity
		direction.x = (float) Math.cos((angle) * MathUtils.degreesToRadians);
		direction.y = (float) Math.sin((angle) * MathUtils.degreesToRadians);
		direction.nor();
		
		velocity.x = direction.x * Constants.ENEMYVELICOTYSPEED * delta;
		velocity.y = direction.y * Constants.ENEMYVELICOTYSPEED * delta;
		
		//get the angle
		float getAngle = (float) Math.atan2( -direction.x, direction.y );
		//position body at angle
		body.setTransform(body.getPosition(), getAngle);
		
		//set velocity
		body.setLinearVelocity(velocity);
		
		//increment to next point
		if(Math.abs(body.getPosition().sub(pathPoints.get(point)).len()) <= EnemyUtils.comparVal){
			point++;
		}
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
	 	
	 Body singleEgg;
    //reset the body
	public void resetBody(){
		//bird position
		myPosition = MathUtils.random(1, 3);
		System.out.println(myPosition);
		resetPosition = worldBodies.get("position" + (int)myPosition);
		body.setTransform(resetPosition, 0);

		singleEgg = eggs.get((int)MathUtils.random(0, eggs.size - 1));
		
		pathPoints.add(singleEgg.getTransform().mul(new Vector2((((CircleShape) singleEgg.getFixtureList().get(1).getShape()).getPosition()))));
		pathPoints.add(singleEgg.getTransform().mul(new Vector2((((CircleShape) singleEgg.getFixtureList().get(2).getShape()).getPosition()))));
		pathPoints.add(singleEgg.getTransform().mul(new Vector2((((CircleShape) singleEgg.getFixtureList().get(3).getShape()).getPosition()))));
		pathPoints.add(singleEgg.getTransform().mul(new Vector2((((CircleShape) singleEgg.getFixtureList().get(4).getShape()).getPosition()))));
		
        point = 0;
        continueUpdating = true;
        hvashtane = false;
        pribirane = false;
        naOtivaneDraw = true;
	}
	
}

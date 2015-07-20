package com.sageggs.actors;

import net.dermetfan.gdx.graphics.g2d.AnimatedBox2DSprite;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import assets.Assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.saveggs.utils.BodyUtils;
import com.saveggs.utils.Constants;
import com.saveggs.utils.EnemyUtils;

public class FlyingBirds extends GameActor{
	
	private Texture texture;
	private TextureRegion[] animationFrames;
	private AnimatedSprite animatedSprite;
	public AnimatedBox2DSprite animatedBox2DSprite;
	private Animation animation;
	private Vector2 direction = new Vector2();
	private Vector2 velocity = new Vector2();
	private Array<Body> bodies;
	
	public FlyingBirds(Body body){
		super(body);
		 bodies = new Array<Body>();
		 
		body.getWorld().getBodies(bodies);
        for (Body bodyLoop : bodies) {
        	if(bodyLoop.getUserData().equals(Constants.LINE1))
        		body.setTransform(bodyLoop.getPosition().x, bodyLoop.getPosition().y, 0);
        }

		texture = Assets.manager.get(Assets.pileBezKraka, Texture.class);	
		splitAnimation();
		animation = new Animation(1f/35f, animationFrames);
		animation.setPlayMode(Animation.PlayMode.LOOP);
		animatedSprite = new AnimatedSprite(animation);
		animatedBox2DSprite = new AnimatedBox2DSprite(animatedSprite);
		
	}
	
    @Override
    public void act(float delta) {
        super.act(delta);
        pointBodyToAngle(45 + 90f,body);
    }
    
	 @Override
	 
     public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        animatedBox2DSprite.draw(batch, body.getFixtureList().first()); 
        
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
	 
	 
	//set velocity of enemy to target
	public void pointBodyToAngle(float desiredAngle, Body body){
		//calculate velocity
		direction.x = (float) Math.cos((desiredAngle) * MathUtils.degreesToRadians);
		direction.y = (float) Math.sin((desiredAngle) * MathUtils.degreesToRadians);
		direction.nor();
		
		velocity.x = direction.x * Constants.FlYINGBIRDVELOCITY;
		velocity.y = direction.y * Constants.FlYINGBIRDVELOCITY;
		
		//get the angle
		float getAngle = (float) Math.atan2( -direction.x, direction.y );
		//position body at angle
		body.setTransform(body.getPosition(), getAngle);
		
		//set velocity
		body.setLinearVelocity(velocity);		
	}
	
}

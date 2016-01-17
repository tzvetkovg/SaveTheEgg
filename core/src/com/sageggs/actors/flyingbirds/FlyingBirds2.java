package com.sageggs.actors.flyingbirds;

import net.dermetfan.gdx.graphics.g2d.AnimatedBox2DSprite;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import assets.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.sageggs.actors.DynamicBall;
import com.sageggs.actors.GameActor;
import com.saveggs.utils.Constants;
import com.saveggs.utils.WorldUtils;

public class FlyingBirds2 extends GameActor{
	
	private Texture texture;
	private AnimatedSprite animatedSprite;
	public AnimatedBox2DSprite animatedBox2DSprite;
	private Vector2 direction = new Vector2();
	private Vector2 velocity = new Vector2();
	private Array<Body> bodies;
	public static World world;
	private Array<Animation> anim;
	public FlyingBirds2(Body body,Array<Animation> anim){
		super(body);
		world = body.getWorld();
		bodies = new Array<Body>();
		this.anim = anim;
		body.getWorld().getBodies(bodies);
        for (Body bodyLoop : bodies) {
        	if(bodyLoop.getUserData().equals(Constants.LINE2))
        		body.setTransform(bodyLoop.getPosition().x, bodyLoop.getPosition().y, 0);
        }

		animatedSprite = new AnimatedSprite(this.anim.get(1));
		animatedBox2DSprite = new AnimatedBox2DSprite(animatedSprite);

		
	}
	
    @Override
    public void act(float delta) {
        super.act(delta);
        
    }
    
	 @Override
     public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        animatedBox2DSprite.draw(batch, body.getFixtureList().first()); 
        
     }
		 
	//set velocity of enemy to target
	public void pointBodyToAngle(float desiredAngle, Body body){
		//calculate velocity
		direction.x = (float) Math.cos((desiredAngle) * MathUtils.degreesToRadians);
		direction.y = (float) Math.sin((desiredAngle) * MathUtils.degreesToRadians);
		direction.nor();
		
		velocity.x = -direction.x * Constants.FlYINGBIRDVELOCITY;
		velocity.y = -direction.y * Constants.FlYINGBIRDVELOCITY;
		
		//get the angle
		float getAngle = (float) Math.atan2( -direction.y, -direction.x );
		animatedBox2DSprite.getAnimation().setFrameDuration(1/MathUtils.random(10f,17f));
		//position body at angle
		body.setTransform(body.getPosition(), getAngle);
		
		//set velocity
		body.setLinearVelocity(velocity);		
	}
	
	
	public void resetBody(Body body){
		body.getWorld().getBodies(bodies);
        for (Body bodyLoop : bodies) {
        	if(bodyLoop.getUserData().equals(Constants.LINE2))
        		body.setTransform(bodyLoop.getPosition().x, bodyLoop.getPosition().y, 0);
        }
	}
	
	public void switchAnimations()
	{	
		int num = MathUtils.random(0, 7);

		if(num == 0)
			animatedSprite.setAnimation(anim.get(num));
		else if (num ==1)
			animatedSprite.setAnimation(anim.get(num));
		else if(num == 2)
			animatedSprite.setAnimation(anim.get(num));
		else if (num  == 3)
			animatedSprite.setAnimation(anim.get(num));
		else if (num  == 4)
			animatedSprite.setAnimation(anim.get(num));
		else if (num  == 5)	
			animatedSprite.setAnimation(anim.get(num));
		else if (num  == 6)	
			animatedSprite.setAnimation(anim.get(num));
		else if (num  == 7)	
			animatedSprite.setAnimation(anim.get(num));

	}
}

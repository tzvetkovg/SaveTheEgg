package com.sageggs.actors;

import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.saveggs.utils.BodyUtils;
import com.saveggs.utils.Constants;
import com.saveggs.utils.WorldUtils;

public class DynamicBall extends GameActor{
	
	private Box2DSprite topka; 
	private Vector2 applyForceCoords;
	private Vector2 coords;
	public boolean draw = true;
	public static World world;
	private Vector2 temp;
	private Vector2 temp2;
	private Vector2 temp3;
	private Vector2 temp4;
	
	public DynamicBall(Body body) {
		super(body);
		world = body.getWorld();
		topka = (Box2DSprite) body.getFixtureList().get(0).getUserData();
		applyForceCoords = new Vector2();
		coords = new Vector2();
		temp = new Vector2();
		temp2 = new Vector2();
		temp3 = new Vector2();
		temp4 = new Vector2();
	}
	
	 @Override
     public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if(draw)
        	topka.draw(batch, body); 
         
     }
	 
	
	//Ball position
	public void applyForceToNewBody() {
		//Get angle
	    float angle = (float) Math.toDegrees(Math.atan2((Constants.middleY) - body.getPosition().y,  (Constants.middleX) - body.getPosition().x));
	    //System.out.println("angle " + angle);
	    //convert to radians
		float radians =  (float) angle * MathUtils.degreesToRadians;
		//System.out.println("radians " + radians);
		// calculate distance
		float distance = temp.set(body.getPosition().x,body.getPosition().y).dst(temp2.set(Constants.middleX,Constants.middleY)) * 8f;
		//System.out.println("distance " + distance);
		//calculate x and y
		applyForceCoords.y = (float) (2 + distance * Math.sin(radians));
		applyForceCoords.x = (float) ( distance * Math.cos(radians));
		//System.out.println("applyForce " + applyForceCoords);

        body.applyLinearImpulse(applyForceCoords, body.getWorldCenter(), true);
	}
	
	 
	//Update coordinates for the body
	public void setBodyPosOnLastik(float x, float y) {
		//Middle point between gredite na slingshot
		//Get angle
	    float angle = (float) Math.toDegrees(Math.atan2(y - (Constants.middleY), x - (Constants.middleX)));
	    //convert to radians
		float radians =  (float) angle * MathUtils.degreesToRadians;
		// calculate distance
		float distance = temp4.set(x,y).dst(temp3.set(Constants.middleX,Constants.middleY)) - 0.4f;
		//calculate x and y
       coords.y = (float) (Constants.middleY + distance * Math.sin(radians));
       coords.x = (float) (Constants.middleX + distance * Math.cos(radians));
       //set body position na lastik - 0.4f
       body.setTransform(coords.x, coords.y,0);
	}
	
	
	public static class DynamicBallPool extends Pool<DynamicBall>{
		@Override
		protected DynamicBall newObject() {
			return new DynamicBall(WorldUtils.createDynamicBall(world));
		}
		
	}
	
}

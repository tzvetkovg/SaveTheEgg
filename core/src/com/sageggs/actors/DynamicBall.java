package com.sageggs.actors;

import net.dermetfan.gdx.graphics.g2d.Box2DSprite;
import assets.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
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
	private Body enemy;
	public boolean followBody = false;
	private TextureAtlas atlas;
	
	public DynamicBall(Body body) {
		super(body);
		world = body.getWorld();
		atlas = Assets.manager.get(Assets.gameAtlas, TextureAtlas.class);;
		topka = new Box2DSprite(atlas.findRegion("dynamicBall"));
		applyForceCoords = new Vector2();
		coords = new Vector2();
		temp = new Vector2();
		temp2 = new Vector2();
		temp3 = new Vector2();
		temp4 = new Vector2();
	}
	
	Vector2 direction = new Vector2();
	Vector2 velocity = new Vector2();
	float angle = 0;
	 @Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		super.act(delta);
		
		//if weapon is purchased
		if(!body.isSleepingAllowed()){
			angle = (float) Math.toDegrees(Math.atan2(enemy.getPosition().y - body.getPosition().y, enemy.getPosition().x  - body.getPosition().x));
			direction.x = (float) Math.cos((angle) * MathUtils.degreesToRadians);
			direction.y = (float) Math.sin((angle) * MathUtils.degreesToRadians);
			direction.nor();
			
			velocity.x = direction.x * 1500 * delta;
			velocity.y = direction.y * 1500 * delta;
			body.setLinearVelocity(velocity);
		}
	}



	@Override
     public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if(draw)
        	topka.draw(batch, body); 
     }
	 
	
	//Ball position
	public void applyForceToNewBody(Body enemy) {
		
		//Get angle
	    float angle = (float) Math.toDegrees(Math.atan2((Constants.middleY) - body.getPosition().y,  (Constants.middleX) - body.getPosition().x));
	    //convert to radians
		float radians =  (float) angle * MathUtils.degreesToRadians;
		// calculate distance
		float distance = temp.set(body.getPosition().x,body.getPosition().y).dst(temp2.set(Constants.middleX,Constants.middleY)) * Constants.ballSpeed;
		//calculate x and y
		applyForceCoords.y = (float) (0.4f + distance * Math.sin(radians));
		applyForceCoords.x = (float) ( distance * Math.cos(radians));
		//System.out.println("applyForce " + applyForceCoords);
		if(enemy != null){
			this.enemy = enemy;
			followBody = true;
		}
		else
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

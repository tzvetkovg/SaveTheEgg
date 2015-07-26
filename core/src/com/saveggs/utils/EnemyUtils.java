
package com.saveggs.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class EnemyUtils {
	
	private static float comparVal = 0.1f;
	private static Vector2 direction = new Vector2();
	private static Vector2 velocity = new Vector2();
	
	//set velocity of enemy to target
	public static void pointBodyToAngle(float desiredAngle, Body body){
		//calculate velocity
		direction.x = (float) Math.cos((desiredAngle) * MathUtils.degreesToRadians);
		direction.y = (float) Math.sin((desiredAngle) * MathUtils.degreesToRadians);
		direction.nor();
		
		velocity.x = direction.x * Constants.ENEMYVELICOTYSPEED;
		velocity.y = direction.y * Constants.ENEMYVELICOTYSPEED;
		
		//get the angle
		float getAngle = (float) Math.atan2( -direction.x, direction.y );
		//position body at angle
		body.setTransform(body.getPosition(), getAngle);
		
		//set velocity
		body.setLinearVelocity(velocity);		
	}
	
	
	//update enemy position when approaching target
	public static void updateEnemyAngle(Vector2 target, Body body){
		
		//birds fall
	    if( Math.abs(body.getPosition().sub(target).len() - 1.3) < comparVal  && body.getPosition().x > target.x ){
			pointBodyToAngle(210f, body);
	    }
	    else if( Math.abs(body.getPosition().sub(target).len() - 1.2)  < comparVal && body.getPosition().x > target.x ){
			pointBodyToAngle(205f, body);
	    }
	    else if( Math.abs(body.getPosition().sub(target).len() - 1.1) < comparVal && body.getPosition().x > target.x){
			pointBodyToAngle(200f, body);
	    }
	    else if( Math.abs(body.getPosition().sub(target).len() - 1.0) < comparVal && body.getPosition().x > target.x ){
			pointBodyToAngle(195f, body);
	    }
	    else if( Math.abs(body.getPosition().sub(target).len() - 0.9) < comparVal && body.getPosition().x > target.x  ){
			pointBodyToAngle(190f, body);
	    }
	    else if( Math.abs(body.getPosition().sub(target).len() - 0.8) < comparVal && body.getPosition().x > target.x  ){
			pointBodyToAngle(185f, body);
	    }
	    else if( Math.abs(body.getPosition().sub(target).len() - 0.7) < comparVal && body.getPosition().x > target.x ){
			pointBodyToAngle(180f, body);
			
	    }
    
	    //birds climb
	    if(Math.abs(body.getWorldCenter().x - (target.x - 0.4)) < comparVal ){
	    	pointBodyToAngle(170f, body);
	    }
	    else if(Math.abs(body.getWorldCenter().x - (target.x - 0.9)) < comparVal){
			pointBodyToAngle(160f, body);
	    }
	    else if(Math.abs(body.getWorldCenter().x - (target.x - 0.2)) < comparVal){
			pointBodyToAngle(150, body);
	    }
	    else if(Math.abs(body.getWorldCenter().x - (target.x - 0.7)) < comparVal){
			pointBodyToAngle(140, body);
	    }
	    else if(Math.abs(body.getWorldCenter().x - (target.x - 0.9)) < comparVal){
			pointBodyToAngle(130, body);
	    }
	}

}

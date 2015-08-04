
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

	//set velocity of enemy to target
	public static void pointBodyToAngleOtherSide(float desiredAngle, Body body){
		//calculate velocity
		direction.x = (float) Math.cos((desiredAngle) * MathUtils.degreesToRadians);
		direction.y = (float) Math.sin((desiredAngle) * MathUtils.degreesToRadians);
		direction.nor();
		
		velocity.x = direction.x * Constants.ENEMYVELICOTYSPEEDOTHERSIDE;
		velocity.y = direction.y * Constants.ENEMYVELICOTYSPEEDOTHERSIDE;
		
		//get the angle
		float getAngle = (float) Math.atan2( -direction.x, direction.y );
		//position body at angle
		body.setTransform(body.getPosition(), getAngle);
		
		//set velocity
		body.setLinearVelocity(velocity);		
	}
	

	
	static float[] positionPath = new float[]{1,2,3};
	static float[] climbAnglesEnemy1 = new float[]{130,140,150};
	static float[] climbAnglesEnemy2 = new float[]{120,130,140};
	static float[] climbAnglesEnemy3 = new float[]{110,120,130};
	static float[] climbAnglesEnemy4 = new float[]{100,110,120};
	public static float myAngleEnemy = 0;
	//update enemy position when approaching target
	public static void updateEnemyAngle(Vector2 target, Body body){
		
		//birds fall
	    if( Math.abs(body.getPosition().sub(target).len() - 2.3) < comparVal  && body.getPosition().x > target.x ){
			pointBodyToAngle(210f, body);
	    }
	    else if( Math.abs(body.getPosition().sub(target).len() - 2.2)  < comparVal && body.getPosition().x > target.x ){
			pointBodyToAngle(205f, body);
	    }
	    else if( Math.abs(body.getPosition().sub(target).len() - 2.1) < comparVal && body.getPosition().x > target.x){
			pointBodyToAngle(200f, body);
	    }
	    else if( Math.abs(body.getPosition().sub(target).len() - 2.0) < comparVal && body.getPosition().x > target.x ){
			pointBodyToAngle(195f, body);
	    }
	    else if( Math.abs(body.getPosition().sub(target).len() - 1.9) < comparVal && body.getPosition().x > target.x  ){
			pointBodyToAngle(190f, body);
	    }
	    else if( Math.abs(body.getPosition().sub(target).len() - 1.8) < comparVal && body.getPosition().x > target.x  ){
			pointBodyToAngle(185f, body);
	    }
	    else if( Math.abs(body.getPosition().sub(target).len() - 1.7) < comparVal && body.getPosition().x > target.x ){
			pointBodyToAngle(180f, body);
			
	    }
    
	    //birds climb
	    if(Math.abs(body.getWorldCenter().x - (target.x - 1.4)) < comparVal && body.getPosition().x < target.x ){
	    	if(myAngleEnemy == 0)
	    		myAngleEnemy = WorldUtils.getRandom(positionPath);
	    	pointBodyToAngle(climbAnglesEnemy1[(int)myAngleEnemy - 1], body);
	    	System.out.println(myAngleEnemy + " angle: " + climbAnglesEnemy1[(int)myAngleEnemy - 1]);
	    	//System.out.println(climbAnglesEnemy1[(int)myAngleEnemy]);
	    }
	    else if(Math.abs(body.getWorldCenter().x - (target.x - 1.9)) < comparVal && body.getPosition().x < target.x){
			pointBodyToAngle(climbAnglesEnemy2[(int)myAngleEnemy - 1], body);
			System.out.println(myAngleEnemy + " angle: " + climbAnglesEnemy2[(int)myAngleEnemy -1]);
			//System.out.println(climbAnglesEnemy2[(int)myAngleEnemy]);
	    }
	    else if(Math.abs(body.getWorldCenter().x - (target.x - 3.2)) < comparVal && body.getPosition().x < target.x){
			pointBodyToAngle(climbAnglesEnemy3[(int)myAngleEnemy - 1], body);
			System.out.println(myAngleEnemy + " angle: " + climbAnglesEnemy3[(int)myAngleEnemy - 1]);
			//System.out.println(climbAnglesEnemy3[(int)myAngleEnemy]);
	    }
	    else if(Math.abs(body.getWorldCenter().x - (target.x - 4.7)) < comparVal && body.getPosition().x < target.x){
			pointBodyToAngle(climbAnglesEnemy4[(int)myAngleEnemy - 1], body);
			System.out.println(myAngleEnemy + " angle: " + climbAnglesEnemy4[(int)myAngleEnemy - 1]);
			//System.out.println(climbAnglesEnemy4[(int)myAngleEnemy]);
	    }
	}
	
	/**
	 * other side fly
	 */
	static float[] climbAnglesOtherSide1 = new float[]{55,45,35};
	static float[] climbAnglesOtherSide2 = new float[]{65,55,40};
	static float[] climbAnglesOtherSide3 = new float[]{75,65,50};
	static float[] climbAnglesOtherSide4 = new float[]{80,75,60};

	//update enemy position when approaching target
	public static void updateEnemyAngleOtherSide(Vector2 target, Body body){
		
		//birds fall
	    if( Math.abs(body.getPosition().sub(target).len() - 1.3) < comparVal  && body.getPosition().x < target.x ){
	    	pointBodyToAngleOtherSide(-15f, body);
	    }
	    else if( Math.abs(body.getPosition().sub(target).len() - 1.2)  < comparVal && body.getPosition().x < target.x ){
	    	pointBodyToAngleOtherSide(-10f, body);
	    }
	    else if( Math.abs(body.getPosition().sub(target).len() - 1.1) < comparVal && body.getPosition().x < target.x){
	    	pointBodyToAngleOtherSide(-5f, body);
	    }
	    else if( Math.abs(body.getPosition().sub(target).len() - 1.0) < comparVal && body.getPosition().x < target.x ){
	    	pointBodyToAngleOtherSide(0f, body);
	    }
	    else if( Math.abs(body.getPosition().sub(target).len() - 0.9) < comparVal && body.getPosition().x < target.x  ){
	    	pointBodyToAngleOtherSide(5f, body);
	    }
    
	    //birds climb
	    if(Math.abs(body.getWorldCenter().x - (target.x + 0.5)) < comparVal && body.getPosition().x > target.x){
	    	if(myAngleEnemy == 0)
	    		myAngleEnemy = WorldUtils.getRandom(positionPath);
	    	pointBodyToAngleOtherSide(climbAnglesOtherSide1[(int)myAngleEnemy - 1], body);
	    }
	    else if(Math.abs(body.getWorldCenter().x - (target.x + 0.9)) < comparVal && body.getPosition().x > target.x){
	    	pointBodyToAngleOtherSide(climbAnglesOtherSide2[(int)myAngleEnemy - 1], body);
	    }
	    else if(Math.abs(body.getWorldCenter().x - (target.x + 1.3)) < comparVal && body.getPosition().x > target.x){
	    	pointBodyToAngleOtherSide(climbAnglesOtherSide3[(int)myAngleEnemy - 1], body);
	    }
	    else if(Math.abs(body.getWorldCenter().x - (target.x + 1.6)) < comparVal && body.getPosition().x > target.x){
	    	pointBodyToAngleOtherSide(climbAnglesOtherSide4[(int)myAngleEnemy - 1], body);
	    }
	}
	

}

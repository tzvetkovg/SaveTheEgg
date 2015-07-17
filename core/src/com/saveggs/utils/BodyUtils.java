package com.saveggs.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class BodyUtils {
	  
	  //destroy bodies out of screen
	  public static boolean bodyInBounds(Body body, OrthographicCamera camera) {
		  //if(!body.getUserData().equals(Constants.Enemy)){			  
			  if(  (body.getPosition().x >= -.5f
					  && body.getPosition().x <= camera.viewportWidth + .5f) 
					  			&& 
				   (body.getPosition().y >= -.5f &&
					   body.getPosition().y <= 	camera.viewportHeight + .5f)  ){
				  return true;
			  }
			  return false;

	  } 
}

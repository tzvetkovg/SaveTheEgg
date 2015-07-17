package com.sageggs.actors;

import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;

public class Slingshot extends GameActor {
	
	private static Box2DSprite slingshot;
	
	public Slingshot(Body body) {
		super(body);
		slingshot = (Box2DSprite) body.getFixtureList().get(0).getUserData();
	}
	
	 @Override
     public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);   
        slingshot.draw(batch, body);  
     }	

}

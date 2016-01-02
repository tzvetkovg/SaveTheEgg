package com.sageggs.actors;

import net.dermetfan.gdx.graphics.g2d.Box2DSprite;
import assets.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;

public class Slingshot extends GameActor {
	
	private Box2DSprite slingshot;
	private TextureAtlas atlas;
	private float initialX = 0;
	private float initialY=0;
	
	public Slingshot(Body body) {
		super(body);
		atlas = Assets.manager.get(Assets.gameAtlas, TextureAtlas.class);
		slingshot = new Box2DSprite(atlas.findRegion("slingshot"));
		initialX = body.getPosition().x;
		initialY = body.getPosition().y;
	}
	
	 @Override
     public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);   
        slingshot.draw(batch, body);  
     }
	 private float mydelta = 0;
	 private float timeSpan = 0;
	 private boolean toggle;
	 public boolean bounceEffect = false;
	 
	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		super.act(delta);
		
		if(bounceEffect){
			timeSpan+=delta;
			mydelta+=delta;
			if((float)mydelta < 0.09f){        	
				if(toggle)
				{ 
					body.setLinearVelocity(0.85f,0.0f);
				} 
				else 
				{ 
					body.setLinearVelocity(-0.85f,0.0f);
				} 
			}
			else{
				toggle = !toggle;
				mydelta = 0;
			}
			if((float)timeSpan >= 0.3f){
				bounceEffect = false;
				body.setLinearVelocity(0, 0);
				body.setTransform(initialX, initialY, 0);
				timeSpan=0;
			}
		}
	}	

}

package com.sageggs.actors.particles;

import assets.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.sageggs.actors.GameActor;

public class Fire extends GameActor{

	public ParticleEffect effect;
	public boolean showEffect = false;
	private float x,y;
	
	public Fire(float positionX, float positionY){
		effect = new ParticleEffect(Assets.manager.get(Assets.fire, ParticleEffect.class));
		x = positionX;
		y = positionY;
    	effect.setPosition(x, y);
    	effect.scaleEffect(0.0333f);
		effect.start();
	}
	
	 @Override
     public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if(showEffect){
        	effect.update(Gdx.graphics.getDeltaTime());
        	effect.draw(batch);
        	if(effect.isComplete()){
        		effect.start();
        	}
        }
     }
}

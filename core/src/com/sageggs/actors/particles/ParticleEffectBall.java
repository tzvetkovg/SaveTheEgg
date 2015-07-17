package com.sageggs.actors.particles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.sageggs.actors.GameActor;

public class ParticleEffectBall extends GameActor{
	public ParticleEffect effect;
	public boolean showEffect = false;
	
	public ParticleEffectBall(){
		effect = new ParticleEffect();
		effect.load(Gdx.files.internal("data/particles/explosionBall.p"), Gdx.files.internal("data/particles"));
		effect.setPosition(0, 0);
		effect.start();
	}
	
	 @Override
     public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if(showEffect){        	
        	effect.update(Gdx.graphics.getDeltaTime());
        	effect.draw(batch);
        	
        	if(effect.isComplete()){
        		showEffect = false;
        		effect.reset();
        	}
        }
        
     }
}

package com.sageggs.actors.particles;

import net.dermetfan.gdx.graphics.g2d.AnimatedBox2DSprite;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import assets.Assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.sageggs.actors.GameActor;
import com.saveggs.utils.Constants;

public class GroundExplosion extends GameActor{
	private AnimatedSprite animatedSprite;
	public AnimatedBox2DSprite animatedBox2DSprite;
	private TextureAtlas atlas;
	private TextureRegion[] tmpFrames;
	private Animation animation; 
	public boolean animate = false;
	
	public GroundExplosion(Body body){
		super(body);
		atlas = Assets.manager.get(Assets.explosion, TextureAtlas.class);
		tmpFrames = new TextureRegion[5];
		tmpFrames[0] = atlas.findRegion("groundExp1");
		tmpFrames[1] = atlas.findRegion("groundExp2");
		tmpFrames[2] = atlas.findRegion("groundExp3");
		tmpFrames[3] = atlas.findRegion("groundExp4");
		tmpFrames[4] = atlas.findRegion("groundExp5");
		animation = new Animation(1f/15f, tmpFrames);
		animation.setPlayMode(Animation.PlayMode.NORMAL);
		animatedSprite = new AnimatedSprite(animation);
		animatedBox2DSprite = new AnimatedBox2DSprite(animatedSprite);
	}
	
	  @Override
	    public void act(float delta) {
	        super.act(delta);
	        
	    }
	    
	 @Override
     public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        //animatedBox2DSprite.draw(batch, body);
        if(animate){
        	animatedBox2DSprite.draw(batch,body.getFixtureList().first());        	
        	if(animatedBox2DSprite.isAnimationFinished()){
        		animatedBox2DSprite.stop();
        		animate = false;
        	}
        } 
     }
	 
	 public void playAnimation(Body aBody){
		 this.body.setTransform(aBody.getPosition().x, aBody.getPosition().y,0);
		 animate = true;
		 animatedBox2DSprite.play();
	 }
	 
}

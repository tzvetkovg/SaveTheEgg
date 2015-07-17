package com.sageggs.actors;

import net.dermetfan.gdx.graphics.g2d.AnimatedBox2DSprite;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;
import assets.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.sageggs.actors.particles.ParticleIzlupvane;
import com.saveggs.utils.Constants;

public class Qice extends GameActor {
	
	private TextureRegion[] animationFrames;
	private Animation animation;
	private AnimatedSprite animatedSprite;
	private AnimatedBox2DSprite animatedBox2DSprite;
	public boolean drawing = true;
	public boolean animationDraw = false;
	private Box2DSprite sprite;
	public ParticleEffect effect;
	private boolean runBatch = false;
	
	public Qice(Body body) {
		super(body);
		
		effect = new ParticleEffect();
		effect.load(Gdx.files.internal("data/particles/izlupvane.p"), Gdx.files.internal("data/particles"));
		effect.setPosition(0, 0);
		effect.start();

		
		splitAnimation();
		sprite = new Box2DSprite(Assets.manager.get(Assets.qice, Texture.class));
		animation = new Animation(1f/1f, animationFrames);
		animation.setPlayMode(Animation.PlayMode.LOOP);
		animatedSprite = new AnimatedSprite(animation);
		animatedBox2DSprite = new AnimatedBox2DSprite(animatedSprite);
		
		Timer.schedule(new Task(){
            @Override
            public void run() {
            	runBatch = true;
            }
        },1f,1f,2);
	}
	
	 @Override
     public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if(drawing){
        	if(animationDraw) //should be !animationDraw 
        		sprite.draw(batch, body.getFixtureList().first());        
        	else{
        		animatedBox2DSprite.draw(batch, body.getFixtureList().first());
        		if(runBatch){
        			runEffect(batch);
        		}
        	}
        }
     }	
	 
	 public void runEffect(Batch batch){	//run the effect	 		
		 		effect.setPosition(body.getPosition().x, body.getPosition().y);
		 		effect.update(Gdx.graphics.getDeltaTime());
		 		effect.draw(batch);
		 		if(effect.isComplete()){
		 			effect.reset();
					runBatch = false;
		 		}
	 }
	 
	 public void splitAnimation(){
		 TextureRegion[][] tmpFrames = TextureRegion.split(Assets.manager.get(Assets.hodeneNaPile, Texture.class), 70, 48);
		 animationFrames = new TextureRegion[5];
		 int index = 0;		 
		 for (int i = 0; i < 1; ++i){
			 for (int j = 0; j < 5; ++j){
				 animationFrames[index++] = tmpFrames[i][j];
			 }
		 }
	 }
}

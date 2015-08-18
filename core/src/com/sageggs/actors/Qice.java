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
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
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
	public boolean runBatch = false;
	public boolean vzetoQice = false;
	public boolean razmazanoQice = false;
	
	public Qice(final Body body,int interval) {
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
            	animationDraw = true;
            	//izlupeno pile
            	body.setAwake(true);
            }
        },interval,1f,3);
	}
	
	 @Override
     public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if(drawing){
        	if(!animationDraw) //should be !animationDraw 
        		sprite.draw(batch, body.getFixtureList().first()); 
        	else{
        		//if animation finished draw pileto
        		if(animatedBox2DSprite.isAnimationFinished()){  
        			sprite.setRegion(animatedBox2DSprite.getAnimation().getKeyFrame(4));
        			animationDraw = false;
        			animatedBox2DSprite.stop();
        		}
        		else{
        			animatedBox2DSprite.draw(batch, body.getFixtureList().first());
        		}
        		//run effect
        		if(runBatch){
        			runEffect(batch);
        		}
        	}
        	
        	if(vzetoQice){
        		drawing = false;
        		animationDraw = false;
        		runBatch = false;
            	body.setAwake(true);
        	}
        	
        	if(razmazanoQice){
        		sprite.setRegion(Assets.manager.get(Assets.qice, Texture.class));
        		sprite.draw(batch, body.getFixtureList().first()); 
        		body.setType(BodyType.DynamicBody);
        	}
        	
        	/**
        	 * razmazvane na zemqta
        	 */
        	if(!body.isSleepingAllowed()){
        		razmazanoQice = false;
        		drawing = false;
        	}
        		
        }
     }	
	 
	 public void runEffect(Batch batch){	//run the effect	 		
	 		effect.update(Gdx.graphics.getDeltaTime());
	 		effect.setPosition(body.getPosition().x + 0.5f, body.getPosition().y + 0.5f);
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

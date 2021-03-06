package com.sageggs.actors;

import net.dermetfan.gdx.graphics.g2d.AnimatedBox2DSprite;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;
import assets.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.sageggs.actors.particles.ParticleIzlupvane;
import com.saveggs.utils.Constants;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
public class Qice extends GameActor {
	
	private TextureRegion[] animationFrames;
	private Animation animation;
	private AnimatedSprite animatedSprite;
	private AnimatedBox2DSprite animatedBox2DSprite;
	public boolean drawing = true;
	public boolean animationDraw = false;
	private Box2DSprite sprite;
	public ParticleEffect reward;
	public boolean runReward = false;
	public boolean vzetoQice = false;
	public boolean razmazanoQice = false;
	public boolean izlupenoQice = false, vzeto = false, uduljavaneIgra = false;
	private int interval;
	public Music pilence;
	public boolean playMusic, playHatchOut = false;
	public long pileId = 0;
	public boolean musicEnabled = true;
	public float timeSpend = 0;
	public boolean measureTime = true;
	private TextureAtlas atlas;
	
	public Qice(final Body body,int interval) {
		super(body);
		atlas = Assets.manager.get(Assets.gameAtlas, TextureAtlas.class);
		reward = new ParticleEffect(Assets.manager.get(Assets.reward, ParticleEffect.class));
		
		pilence = Assets.manager.get(Assets.pilence, Music.class);
		
		splitAnimation();
		sprite = new Box2DSprite(atlas.findRegion("qice"));
		animation = new Animation(1f/1f, animationFrames);
		animation.setPlayMode(Animation.PlayMode.LOOP);
		animatedSprite = new AnimatedSprite(animation);
		animatedBox2DSprite = new AnimatedBox2DSprite(animatedSprite);
		
		animatedBox2DSprite.update(timeSpend);
		
		this.interval = interval;

	}
	

	
	 @Override
     public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha); 
        if(drawing){
            if(runReward && !vzetoQice)
            	runRewardEffect(batch);
        	if(!animationDraw) //should be !animationDraw 
        		sprite.draw(batch, body.getFixtureList().first()); 
        	else if(animationDraw){
        		pilence.play();
        		if(animatedBox2DSprite.isAnimationFinished()){  
        			sprite.setRegion(animatedBox2DSprite.getAnimation().getKeyFrame(4));
        			animationDraw = false;
        			animatedBox2DSprite.stop();
        			izlupenoQice = true;
        		}
        		else{
        			animatedBox2DSprite.draw(batch, body.getFixtureList().first());
        		}
        	}
        	
        	//kogato e vzeto v krakata na pticata
        	if(vzetoQice){
        		//body.setFixedRotation(false);
        		drawing = false;
        		animationDraw = false;
        		runReward = false;
            	body.setAwake(true);
            	body.setBullet(true);
            	//body.getFixtureList().first().setSensor(false); 
        	}
        	
        	//kogato trugva da pada
        	if(razmazanoQice){
        		sprite.setRegion(atlas.findRegion("qice"));
        		animationDraw = false;
        		body.setType(BodyType.DynamicBody);
        	}
        	
        	/**
        	 * razmazvane na zemqta
        	 */
        	if(!body.isSleepingAllowed()){
        		//shchupeno qice
        		uduljavaneIgra = true;
        		razmazanoQice = false;
        		body.setType(BodyType.StaticBody);
        		drawing = false;
        	}
        }
     }	

	 @Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		super.act(delta);
		if(measureTime){
			if(interval == (int)timeSpend){
				startChickenAnimation();
			}
			timeSpend += delta;
		}
	}

	public void resetTimeSpend(){
		timeSpend = 0;
	}
	 
	public void startChickenAnimation(){
    	runReward = true;
    	animationDraw = true;
    	//izlupeno pile
    	body.setAwake(true);
	}
	
	public void pause(){
		measureTime = false;
		if(animatedBox2DSprite.isPlaying()){
			animatedBox2DSprite.pause();
		}
	}
	
	public void resume(){
		measureTime = true;
		if(!animatedBox2DSprite.isPlaying() && !animatedBox2DSprite.isAnimationFinished()){
			animatedBox2DSprite.play();
		}
	}
	 
	public void runRewardEffect(Batch batch){
 		reward.update(Gdx.graphics.getDeltaTime());
 		reward.setPosition(body.getPosition().x + 0.5f, body.getPosition().y + 0.5f);
 		reward.start();
 		reward.draw(batch);
	}
	
	 public void splitAnimation(){
		 TextureRegion[][] tmpFrames = TextureRegion.split(Assets.manager.get(Assets.izplupvane, Texture.class), 70, 48);
		 animationFrames = new TextureRegion[5];
		 int index = 0;		 
		 for (int i = 0; i < 1; ++i){
			 for (int j = 0; j < 5; ++j){
				 animationFrames[index++] = tmpFrames[i][j];
			 }
		 }
	 }
	 	 
	 //reset qice
	 public void resetInitialPositionQice(Vector2 position){
		
		 //reset animation
		 sprite = new Box2DSprite(atlas.findRegion("qice"));
		 animation = new Animation(1f/1f, animationFrames);
		 animation.setPlayMode(Animation.PlayMode.LOOP);
		 animatedSprite = new AnimatedSprite(animation);
		 animatedBox2DSprite = new AnimatedBox2DSprite(animatedSprite);
		 //reset egg
		 vzeto = false;
		 uduljavaneIgra = false;
		 izlupenoQice = false;
		 vzetoQice = false;
		 razmazanoQice = false;
		 runReward = false;
		 body.setAwake(false);
		 body.setSleepingAllowed(true);
		 body.setBullet(false);
		 animationDraw = false;
		 body.setTransform(position, 0);
		 sprite.setRegion(atlas.findRegion("qice"));
		 timeSpend = 0;
		 measureTime = true;
		 //reset animation
		 //reset izlupvane
/*		 if(task.isScheduled()){
			 task.cancel();
		 }
		 Timer.schedule(this.task,this.interval,1f,2);*/
		 
		 drawing = true;
	 }	 
}

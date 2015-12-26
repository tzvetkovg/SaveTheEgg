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
	public ParticleEffect effect;
	public boolean runBatch = false;
	public boolean vzetoQice = false;
	public boolean razmazanoQice = false;
	public boolean izlupenoQice = false, vzeto = false, uduljavaneIgra = false;
	private Task task;
	private int interval;
	public Music pilence;
	private long startTime;
	private long stopTime;
	public boolean playMusic, playHatchOut = false;
	public long pileId = 0;
	public boolean musicEnabled = true;
	public float timeSpend = 0;
	public boolean measureTime = true;
	private TextureAtlas atlas;
	
	public Qice(final Body body,int interval) {
		super(body);
		atlas = Assets.manager.get(Assets.gameAtlas, TextureAtlas.class);
		effect = new ParticleEffect(Assets.manager.get(Assets.izlupvaneQice, ParticleEffect.class));
		//effect.load(Gdx.files.internal("data/particles/izlupvane.p"), Gdx.files.internal("data/particles"));
		effect.setPosition(0, 0);
		effect.start();

		pilence = Assets.manager.get(Assets.pilence, Music.class);
		
		splitAnimation();
		sprite = new Box2DSprite(atlas.findRegion("qice"));
		animation = new Animation(1f/1f, animationFrames);
		animation.setPlayMode(Animation.PlayMode.LOOP);
		animatedSprite = new AnimatedSprite(animation);
		animatedBox2DSprite = new AnimatedBox2DSprite(animatedSprite);
		
		animatedBox2DSprite.update(timeSpend);
		
		this.interval = interval;
/*		task = new Task(){
            @Override
            public void run() {
            	runBatch = true;
            	animationDraw = true;
            	//izlupeno pile
            	body.setAwake(true);
            };
		};
		startTime = System.currentTimeMillis();
		Timer.schedule(task,this.interval,1f,2);*/

	}
	

	
	 @Override
     public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha); 
        if(drawing){
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
        		//run effect
        		if(runBatch){
        			runEffect(batch);
        		}
        	}
        	
        	//kogato e vzeto v krakata na pticata
        	if(vzetoQice){
        		//body.setFixedRotation(false);
        		drawing = false;
        		animationDraw = false;
        		runBatch = false;
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
	 
/*	 public void pauseTime(){
		 task.cancel();
	 }
	 
	 public void resumeTime(){
		stopTime = System.currentTimeMillis();
		if( ( (stopTime - startTime) / 1000.0) < this.interval + 3){
			 System.out.println("interval: " + (this.interval + 3));
			 System.out.println("time spent " +  ((stopTime - startTime) / 1000.0));
			 System.out.println("new time is " + ((this.interval + 3) - ( (stopTime - startTime) / 1000.0)));
			 Timer.schedule(this.task,(int)( (this.interval + 3) - ( (stopTime - startTime) / 1000.0) ),1f,2);
		}
		else{
			Timer.schedule(this.task,this.interval,1f,2);
		}
	 }*/
	 

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
    	runBatch = true;
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
		 runBatch = false;
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

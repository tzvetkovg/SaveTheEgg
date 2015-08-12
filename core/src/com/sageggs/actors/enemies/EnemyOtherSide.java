package com.sageggs.actors.enemies;

import java.text.DecimalFormat;

import net.dermetfan.gdx.graphics.g2d.AnimatedBox2DSprite;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;
import assets.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.sageggs.actors.GameActor;
import com.saveggs.utils.Constants;
import com.saveggs.utils.EnemyUtils;
import com.saveggs.utils.WorldUtils;

public class EnemyOtherSide extends GameActor{

	private Vector2 target = new Vector2();
	public static boolean stopMoving = false; 
	public static float angle = 0;
	Vector2 position = new Vector2();
	public Vector2 direction = new Vector2();
	private AnimatedSprite animatedSprite;
	public AnimatedBox2DSprite animatedBox2DSprite;
	public Box2DSprite naOtivane;
	public Box2DSprite otvarqne;
	private Animation animation;
	private TextureRegion[] animationFrames;
	private TextureRegion[] animationKraka;
	private Texture texture;
	private Vector2 ballPos;
	private Vector2 newPos;
	public Box2DSprite hvanatoQice;
	public boolean naOtivaneDraw = true;
	public boolean hvashtane = false;
	public boolean pribirane = false;
	public boolean enemyDraw = false;
	private Array<Body> bodies;
	float[] positions = new float[]{1,2,3};
	float myPosition;
	
	public EnemyOtherSide(Body body) {
		super(body);
		target = Constants.eggPositions[WorldUtils.randInt(0, Constants.eggPositions.length -1)];
 
		bodies = new Array<Body>();
		body.getWorld().getBodies(bodies);
		//get random position
		resetBody();

		//animation
		texture = Assets.manager.get(Assets.pileBezKraka, Texture.class);
		splitAnimation();
		animation = new Animation(1f/35f, animationFrames);
		animation.setPlayMode(Animation.PlayMode.LOOP);
		animatedSprite = new AnimatedSprite(animation);
		animatedBox2DSprite = new AnimatedBox2DSprite(animatedSprite);
		//kraka
		naOtivane = new Box2DSprite(Assets.manager.get(Assets.prisvivaneKraka, Texture.class));
		otvarqne = new Box2DSprite(Assets.manager.get(Assets.opuvaneKraka, Texture.class));

		//kraka s hvanato qice
		hvanatoQice = new Box2DSprite(Assets.manager.get(Assets.hvanatoQice, Texture.class));
		//nastroivane na ugula
		//EnemyUtils.pointBodyToAngleOtherSide(getAngleBodyEgg(target) + 3f, body);

		//flip all sprites
		animatedBox2DSprite.flipFrames(false, true);
		naOtivane.flip(false, true);
		otvarqne.flip(false, true);
		hvanatoQice.flip(false, true);
	
	}
	
    @Override
    public void act(float delta) {
        super.act(delta);
        EnemyUtils.updateEnemyAngleOtherSide(target,body);
    }
    
	 @Override
	 
     public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if(enemyDraw){        	
        	if(naOtivaneDraw)
        		naOtivane.draw(batch, body.getFixtureList().get(2)); 
        	if(hvashtane)
        		otvarqne.draw(batch, body.getFixtureList().get(2));      
        	if(pribirane)
        		hvanatoQice.draw(batch, body.getFixtureList().get(3)); 
        	
    		naOtivane.setRotation(body.getAngle() - 90f);
    		otvarqne.setRotation(body.getAngle() - 90f);
    		hvanatoQice.setRotation(body.getAngle() - 120f);
    		animatedBox2DSprite.setRotation(body.getAngle() - 90f);
    		
        	animatedBox2DSprite.draw(batch, body.getFixtureList().first()); 
        }
     }
	
	 
	 public void splitAnimation(){
		 TextureRegion[][] tmpFrames = TextureRegion.split(texture, 240, 314);
		 animationFrames = new TextureRegion[4 * 5];
		 int index = 0;		 
		 for (int i = 0; i < 4; ++i){
			 for (int j = 0; j < 5; ++j){
				 animationFrames[index++] = tmpFrames[i][j];
			 }
		 }
	 }
	 
	//get the angle between enemy and target
	public float getAngleBodyEgg(Vector2 target){
		 float angle = (float) Math.toDegrees(Math.atan2(target.y - (body.getPosition().y), target.x  - (body.getPosition().x)));
		 return angle;
	}
	
	public void resetBody(){
		myPosition = WorldUtils.getRandom(positions);
	
        for (Body bodyLoop : bodies) {
        	if(bodyLoop.getUserData().equals("opposite" + (int)myPosition)){
        		System.out.println("yes equals oppositte");
        		body.setTransform(bodyLoop.getPosition().x, bodyLoop.getPosition().y, 0);
        	}
        }
        EnemyUtils.pointBodyToAngleOtherSide(getAngleBodyEgg(target) + 5f, body);
        hvashtane = false;
        pribirane = false;
        naOtivaneDraw = true;
        EnemyUtils.myAngleEnemy = 0;
	}
	
}

package com.sageggs.actors.flyingbirds;

import assets.Assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class FlyingBirdAnimations 
{
	private TextureRegion[] animationFrames1,animationFrames2,animationFrames3,animationFrames4,animationFrames5,
	animationFrames6,animationFrames7,animationFrames8;
	private Texture texture;
	private Animation animation1,animation2,animation3,animation4,animation5,animation6,animation7,animation8;
	private Array<Animation> animations;
	public float flyingSpeed = 13f;
	
	public FlyingBirdAnimations(){
		texture = Assets.manager.get(Assets.flyingBirds, Texture.class);	
		animations = new Array<Animation>();
	}
	
	 public Array<Animation> splitAnimation(){
		 TextureRegion[][] tmpFrames = TextureRegion.split(texture, texture.getWidth()/8, texture.getHeight()/4);
		 animationFrames1 = new TextureRegion[4];
		 animationFrames2 = new TextureRegion[4];
		 animationFrames3 = new TextureRegion[4];
		 animationFrames4 = new TextureRegion[4];
		 animationFrames5 = new TextureRegion[4];
		 animationFrames6 = new TextureRegion[4];
		 animationFrames7 = new TextureRegion[4];
		 animationFrames8 = new TextureRegion[4];
		 int index = 0;		

		 for (int i = 0; i < 4; ++i){
			 for (int j = 0; j < 8; ++j){

				 if(index == 4){
					 index = 0;
				 }
				 if(j <= 3 && i == 0)
				 {
					animationFrames1[index++] = tmpFrames[i][j];					 
				 }
				 else if (j > 3  && j <= 7 && i == 0)
				 {
					 System.out.println("tvori");
					 animationFrames2[index++] = tmpFrames[i][j];
				 }
				 else if (j <= 3 && i == 1)
				 {
					 animationFrames3[index++] = tmpFrames[i][j];
				 }
				 else if(j > 3  && j <= 7 && i == 1)
				 {
					 System.out.println("treti");
					 animationFrames4[index++] = tmpFrames[i][j];
				 }
				
				 if(j <= 3 && i == 2)
				 {
					 System.out.println("4");
					animationFrames5[index++] = tmpFrames[i][j];					 
				 }
				 else if (j > 3  && j <= 7 && i == 2)
				 {
					 System.out.println("5");
					 animationFrames6[index++] = tmpFrames[i][j];
				 }
				
				 if(j <= 3 && i == 3)
				 {
					 System.out.println("6");
					animationFrames7[index++] = tmpFrames[i][j];					 
				 }
				 else if (j > 3  && j <= 7 && i == 3)
				 {
					 System.out.println("7");
					 animationFrames8[index++] = tmpFrames[i][j];
				 }
			 }

		 }
		 animation1 = new Animation(1f/flyingSpeed, animationFrames1);
		 animation1.setPlayMode(Animation.PlayMode.LOOP);
		 
		 animations.add(animation1);
		 animation2 = new Animation(1f/flyingSpeed, animationFrames2);
		 animation2.setPlayMode(Animation.PlayMode.LOOP);
		 animations.add(animation2);
		 
		 animation3 = new Animation(1f/flyingSpeed, animationFrames3);
		 animation3.setPlayMode(Animation.PlayMode.LOOP);
		 animations.add(animation3);
		 animation4 = new Animation(1f/flyingSpeed, animationFrames4);
		 animation4.setPlayMode(Animation.PlayMode.LOOP);
		 animations.add(animation4);
		 animation5 = new Animation(1f/flyingSpeed, animationFrames5);
		 animation5.setPlayMode(Animation.PlayMode.LOOP);
		 animations.add(animation5);
		 animation6 = new Animation(1f/flyingSpeed, animationFrames6);
		 animation6.setPlayMode(Animation.PlayMode.LOOP);
		 animations.add(animation6);
		 animation7 = new Animation(1f/flyingSpeed, animationFrames7);
		 animation7.setPlayMode(Animation.PlayMode.LOOP);
		 animations.add(animation7);
		 animation8 = new Animation(1f/flyingSpeed, animationFrames8);
		 animation8.setPlayMode(Animation.PlayMode.LOOP);
		 animations.add(animation8);
		 return animations;
	 }
}

package com.sageggs.actors.enemies;

import com.badlogic.gdx.physics.box2d.Body;

import net.dermetfan.gdx.graphics.g2d.AnimatedBox2DSprite;

public interface CommonEnemy {
	
	public CommonEnemy getEnemy();
	public AnimatedBox2DSprite getCurrentAnimation();
	public void padaneNaDoly();
	public void padashtLosh();
	public AnimatedBox2DSprite getZamqnoPilence();
	public AnimatedBox2DSprite getZamaqnLosh();
	public boolean getEnemyDraw();
	public boolean getPokajiPadashto();
	public boolean getPribirane();
	public Body getBody();
	public boolean getPokajiLosh();
	public void setCurrentAnimation(AnimatedBox2DSprite animatedSprite);
	public void setPokajiPadashto(boolean val);
	public void setLosh();
	public void setPokajiLosh(boolean val);
	public AnimatedBox2DSprite getLosh();
	public AnimatedBox2DSprite getPrediLosh();
	public void setPrediLosh(AnimatedBox2DSprite prediLosh);
	
}

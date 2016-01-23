package com.sageggs.actors.runnables;

import com.admob.AdsController;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.sageggs.actors.Qice;
import com.sageggs.actors.enemies.Enemy;
import com.sageggs.actors.enemies.EnemyOtherSide;
import com.sageggs.actors.particles.Explosion;
import com.saveggs.utils.Constants;

public class RunnableEnemy1 implements Runnable{
	private Body ball;
	private Enemy enemy;
	private EnemyOtherSide enemyOtherSide;
	private Explosion explosion;
	private Body toRemove;
	private int numberOfEnemyKillings,launchBothEnemies,timeIntervalAds,timeAds;
	private Array<Qice> allEggs;
	private boolean internetEnabled;
	private AdsController adsController;
	
	public RunnableEnemy1(Body ball,Enemy enemy,EnemyOtherSide enemyOtherSide,Explosion explosion,Body toRemove
						  ,int numberOfEnemyKillings,int launchBothEnemies,int timeIntervalAds,int timeAds,
						  Array<Qice> allEggs,boolean internetEnabled,AdsController adsController){
		this.ball = ball;
		this.enemy = enemy;
		this.explosion = explosion;
		this.toRemove = toRemove;
		this.numberOfEnemyKillings = numberOfEnemyKillings;
		this.launchBothEnemies = launchBothEnemies;
		this.timeIntervalAds = timeIntervalAds;
		this.timeAds = timeAds;
		this.allEggs = allEggs;
		this.internetEnabled = internetEnabled;
		this.adsController = adsController;
		this.enemyOtherSide = enemyOtherSide;
	}
	
	@Override
	public void run() {
		ball.setAwake(false);
		//launch particle effect
		if(enemy.enemyDraw){
			enemy.animatedBox2DSprite.setAnimation(enemy.animation2);
			enemy.animatedBox2DSprite.flipFrames(true,false );
			if(!enemy.animatedBox2DSprite.isFlipX()){									
				enemy.animatedBox2DSprite.flipFrames(true,false );
			}
			explosion.playAnimation(enemy.body);
			//particleEffect.effect.setPosition(enemy.body.getPosition().x, enemy.body.getPosition().y);
			//particleEffect.showEffect = true;
		}

		//puskane na qiceto
		if(enemy.pribirane){
			((Qice)toRemove.getFixtureList().first().getUserData()).drawing = true;
			((Qice)toRemove.getFixtureList().first().getUserData()).body.setTransform(toRemove.getPosition(), 0);
			((Qice)toRemove.getFixtureList().first().getUserData()).vzetoQice = false;
			((Qice)toRemove.getFixtureList().first().getUserData()).razmazanoQice = true;
			
		}
		
		//start launching both enemies
		if(numberOfEnemyKillings > launchBothEnemies){								
			//launch both enemies
			if(internetEnabled && timeIntervalAds >= timeAds){
				for(Qice qice: allEggs){
					qice.pause();
				}
				adsController.showInterstitialAd(new Runnable() {
					@Override
					public void run() {
						timeIntervalAds = 0;
						for(Qice qice: allEggs){
							qice.resume();
						}
					}
				});
			}						
			timeIntervalAds++;
			if(!enemyOtherSide.enemyDraw){
				launchEnemyTwo();
				launchEnemyOne();
			}
			else
				launchEnemyOne();
			//reset
			if(timeIntervalAds > timeAds)
				timeIntervalAds = 0;
		}
		//launch only one enemy
		else
		{								
			//if mobile internet and limit reached
			if(internetEnabled && timeIntervalAds >= timeAds){
				for(Qice qice: allEggs){
					qice.pause();
				}
				adsController.showInterstitialAd(new Runnable() {
					@Override
					public void run() {
						timeIntervalAds = 0;
						for(Qice qice: allEggs){
							qice.resume();
						}
					}
				});
			}
			timeIntervalAds++;
			launchEnemy();
			//reset
			if(timeIntervalAds > timeAds)
				timeIntervalAds = 0;
		}
	}	

	
	
	public void startEnemyOne(){
		
		enemy.enemyDraw = false;
		enemy.setSpeed(0);
		enemy.body.setAwake(false);

		enemy.resetBody();
		//reset the other enemy
		enemyOtherSide.enemyDraw = true;
		enemyOtherSide.setSpeed(Constants.ENEMYSPEED);
		enemyOtherSide.body.setAwake(true);
		enemyOtherSide.resetBody();
		//launch bird scream
	}
	
	public void startEnemyTwo(){

		//launch enemy2
		enemyOtherSide.enemyDraw = false;
		enemyOtherSide.setSpeed(0);
		enemyOtherSide.body.setAwake(false);
		enemyOtherSide.resetBody();
		//reset enemy1
		enemy.enemyDraw = true;
		/*if(numberOfEnemyKillings == maskaAppearingKillings){
			enemy.switchToMask1();
		}
		else if(numberOfEnemyKillings == maskaAppearingKillings2){
			enemy.switchToMask2();
		}*/
		enemy.setSpeed(Constants.ENEMYSPEED);
		enemy.body.setAwake(true);
		enemy.resetBody();
		//launch music
	}
	
	public void launchEnemy(){
    	if(MathUtils.random(1, 2) == 1)
    		startEnemyOne();
    	else
    		startEnemyOne();
	}
		
	/**
	 * launch both enemies independently
	 */
	//launch both enemies
	public void launchEnemyOne(){

		//reset enemy1
/*		if(numberOfEnemyKillings == maskaAppearingKillings){
			enemy.mask = true;
		}*/
		enemy.enemyDraw = true;
		enemy.setSpeed(Constants.ENEMYSPEED);
		enemy.body.setAwake(true);
		enemy.resetBody();
	}
	
	public void launchEnemyTwo(){
		//reset the other enemy
		enemyOtherSide.enemyDraw = true;
		enemyOtherSide.setSpeed(Constants.ENEMYSPEED);
		enemyOtherSide.body.setAwake(true);
		enemyOtherSide.resetBody();
	}
	
}

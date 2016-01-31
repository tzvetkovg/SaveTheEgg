package com.sageggs.actors.runnables;

import com.admob.AdsController;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.sageggs.actors.Qice;
import com.sageggs.actors.enemies.CommonEnemy;
import com.sageggs.actors.enemies.Enemy;
import com.sageggs.actors.enemies.EnemyOtherSide;
import com.sageggs.actors.particles.Explosion;
import com.sageggs.actors.particles.PartExplosion;
import com.saveggs.game.GameStage;
import com.saveggs.utils.Constants;

public class RunnableEnemy1 implements Runnable{
	private Body ball;
	private CommonEnemy enemy;
	private Explosion explosion;
	private Body toRemove;
	private int timeIntervalAds,timeAds;
	private Array<Qice> allEggs;
	private boolean internetEnabled,volume;
	private AdsController adsController;
	private GameStage stage;
	private PartExplosion partExplpsion;
	private Sound sound;
	
	public RunnableEnemy1(Body ball,CommonEnemy enemy,Explosion explosion,PartExplosion partExplpsion,
						Body toRemove
						  ,int timeIntervalAds,int timeAds,Array<Qice> allEggs,
						  boolean internetEnabled,AdsController adsController,GameStage stage,Sound sound,boolean volume){
		this.ball = ball;
		this.enemy = enemy;
		this.explosion = explosion;
		this.toRemove = toRemove;
		this.timeIntervalAds = timeIntervalAds;
		this.timeAds = timeAds;
		this.allEggs = allEggs;
		this.internetEnabled = internetEnabled;
		this.adsController = adsController;
		this.stage = stage;
		this.partExplpsion = partExplpsion;
		this.sound = sound;
		this.volume = volume;
	}
	
	@Override
	public void run() {
		ball.setAwake(false);
		if(volume)
			sound.play();
		//mask handling
		if(enemy.getCurrentAnimation() == enemy.getZamqnoPilence()){
			partExplpsion.playAnimation(enemy.getBody());
			enemy.padaneNaDoly();
			return;
		}
		//losh
		if(enemy.getCurrentAnimation() == enemy.getZamaqnLosh()){
			Enemy.loshCount+=1;
			if(Enemy.loshCount ==2)
			{									
				enemy.padashtLosh();
				Enemy.loshCount = 0;								
			}
			partExplpsion.playAnimation(enemy.getBody());
			return;
		}
		
		if(enemy.getEnemyDraw()){
			//normal
			if(enemy.getPokajiPadashto())
			{
				partExplpsion.playAnimation(enemy.getBody());
				enemy.setCurrentAnimation(enemy.getZamqnoPilence());
				//enemy.getCurrentAnimation()= enemy.zamaqnoPilence;
				return;
			}
			
			//losh
			if(enemy.getPokajiLosh())
			{
				partExplpsion.playAnimation(enemy.getBody());
				enemy.setCurrentAnimation(enemy.getZamaqnLosh());
				//enemy.actualAnimation = enemy.zamaqnLosh;
				return;
			}
			explosion.playAnimation(enemy.getBody());
		}
		
		//puskane na qiceto
		if(enemy.getPribirane()){
			((Qice)toRemove.getFixtureList().first().getUserData()).drawing = true;
			((Qice)toRemove.getFixtureList().first().getUserData()).body.setTransform(toRemove.getPosition(), 0);
			((Qice)toRemove.getFixtureList().first().getUserData()).vzetoQice = false;
			((Qice)toRemove.getFixtureList().first().getUserData()).razmazanoQice = true;
		}							
		//if mobile enabled
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
		stage.launchEnemy();
		//reset
		if(timeIntervalAds > timeAds)
			timeIntervalAds = 0;
	}
}

package com.saveggs.game;

import java.util.Map;

import assets.Assets;

import com.admob.AdsController;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.physics.box2d.World;
import com.saveggs.game.screens.LevelScreen;
import com.saveggs.game.screens.MainMenuScreen;
import com.saveggs.game.screens.SplashScreen;
import com.saveggs.game.screens.StageScreen;
import com.saveggs.utils.Constants;


public class GameClass extends Game {
	
	private AdsController adsController;
	private Map<String,Object> worldBodies;
	private World world;
	
	public SplashScreen splash;
	public MainMenuScreen mainMenu;
	public LevelScreen levelScreen;
	public StageScreen stageScreen;
	
	public GameClass(AdsController adsController){
	    this.adsController = adsController; 

	}
	
	@Override
	public void create() {
		//splash = new SplashScreen(adsController,this);
		//this.setScreen(splash);
		
		Assets asset = new Assets();
		Constants constant = new Constants();
		Assets.manager.load(Assets.class);		
		
		while(!Assets.manager.update()){
			System.out.println(Assets.manager.getProgress());
		}
		Assets.manager.finishLoading();
		this.setScreen(new MainMenuScreen(adsController,this));
		//this.setScreen(new LevelScreen(null,this));
	}
	
	@Override
	public void render() {
		super.render();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		Assets.manager.dispose();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		
	}
	
}

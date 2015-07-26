package com.saveggs.game;

import assets.Assets;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.saveggs.game.screens.LevelScreen;
import com.saveggs.game.screens.MainMenuScreen;
import com.saveggs.game.screens.SplashScreen;
import com.saveggs.game.screens.StageScreen;

public class GameClass extends Game {
	
	public StageScreen stage;
	public MainMenuScreen mainMenu;
	
	@Override
	public void create() {
		Assets.manager.load(Assets.class);
		while(!Assets.manager.update())
			System.out.println(Assets.manager.getProgress() * 100 + " %");
		Assets.manager.finishLoading();
		//this.setScreen(new SplashScreen(this));
		//this.setScreen(new LevelScreen(this));
		this.setScreen(new StageScreen(this));
	}
	
	@Override
	public void render() {
		super.render();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		this.getScreen().dispose();
		Assets.manager.dispose();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		
	}
	
}

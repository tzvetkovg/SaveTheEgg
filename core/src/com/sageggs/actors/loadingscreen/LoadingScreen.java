package com.sageggs.actors.loadingscreen;

import assets.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.saveggs.utils.Constants;

public class LoadingScreen extends Actor {
	
	private Image image;
	public boolean draw = true;
	
	public LoadingScreen(){
		Sprite splash = new Sprite(Assets.manager.get(Assets.loadingScreen, Texture.class));
		image = new Image(splash);
		image.setSize(Constants.SCENE_WIDTH / 30f, Constants.SCENE_HEIGHT / 30f);
		image.setOrigin(Constants.SCENE_WIDTH / 30f * 0.5f, Constants.SCENE_HEIGHT / 30f * 0.5f);
	}
	
    @Override
    public void act(float delta) {
        super.act(delta);
    }
    
	 @Override
     public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if(draw)
        	image.draw(batch, parentAlpha);
     }
	
}

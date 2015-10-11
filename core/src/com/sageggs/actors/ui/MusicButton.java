package com.sageggs.actors.ui;

import assets.Assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.saveggs.game.screens.MainMenuScreen;
import com.saveggs.utils.Constants;

public class MusicButton extends Actor{
	private TextButton button;
	
	public MusicButton(){
		 //back button
		TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle();
		tbs.font =  Assets.manager.get(Assets.bitmapfont, BitmapFont.class);
		tbs.font.getData().setScale(0.01f);
		tbs.up = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.music, Texture.class)));
		//tbs.over = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.buttonOverTex, Texture.class)));
		//tbs.down = new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.buttonDownTex, Texture.class)));

		button = new TextButton("back", tbs);
		button.setSize(1.5f, 1.5f);
		button.setOrigin(Constants.SCENE_WIDTH / 30f * 0.5f, Constants.SCENE_HEIGHT / 30f * 0.5f);
		button.setPosition(button.getX(), button.getY() + 14.7f);
		
		button.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
			  System.out.println(button.getStyle().up);
			  System.out.println("hello guys");
			}
		});
		
	}
	
    @Override
    public void act(float delta) {
        super.act(delta);
    }
    
	 @Override
     public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        button.draw(batch, parentAlpha);
     }
	
}

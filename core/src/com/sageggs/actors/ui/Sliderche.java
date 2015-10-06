package com.sageggs.actors.ui;

import assets.Assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.sageggs.actors.loadingscreen.LoadingScreen;
import com.saveggs.utils.*;

public class Sliderche extends Actor {
	private Slider slider;
	private Table container;
	private Skin skin;
	
	public Sliderche(){
		
		//Instantiate the Skin
        skin = new Skin();
        //skin.add("knob", new Texture(Gdx.files.internal("imagens/Slider/knob.png")));
        skin.add("bgs", Assets.manager.get(Assets.slider, Texture.class));

        Slider.SliderStyle style = new Slider.SliderStyle();
        style.background = skin.getDrawable("bgs");
       
        slider = new Slider(0, 1, 1, false, style);		

		slider.setOrigin(Constants.SCENE_WIDTH / 30f * 0.5f, Constants.SCENE_HEIGHT / 30f * 0.5f);
		slider.setScale(0.000001f);

		
		//container.setFillParent(true);
		
	}
	
    @Override
    public void act(float delta) {
        super.act(delta);
    }
    
	 @Override
     public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        slider.draw(batch, parentAlpha);
        
     }
}

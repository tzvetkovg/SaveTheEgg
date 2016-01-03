package com.sageggs.actors;

import assets.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.saveggs.utils.Constants;

public class Text extends GameActor{
	public BitmapFont font;
	public int remainingBalls;
	
    public Text(int maxBalls){
    		FreeTypeFontGenerator fontGenerator = Assets.manager.get(Assets.myFont, FreeTypeFontGenerator.class);
			FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter =
			      new FreeTypeFontGenerator.FreeTypeFontParameter();
			freeTypeFontParameter.size = 15;
			freeTypeFontParameter.borderColor = Color.BLACK;
			freeTypeFontParameter.borderWidth = 3;
			fontGenerator.generateData(freeTypeFontParameter);
			font = fontGenerator.generateFont(freeTypeFontParameter);
			font.getData().setScale(0.04f, 0.04f);
			font.setColor(Color.ORANGE);
			remainingBalls = maxBalls;
    }

	@Override
    public void draw(Batch batch, float parentAlpha) {
		font.draw(batch, remainingBalls + "", 3f, Constants.SCENE_HEIGHT / 30f * 0.97f );
	}
	
	
	
}

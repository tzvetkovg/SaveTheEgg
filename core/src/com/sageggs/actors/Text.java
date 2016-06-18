package com.sageggs.actors;

import assets.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.saveggs.utils.Constants;

public class Text extends GameActor{
	public BitmapFont font;
	public int remainingBalls;
	public float position;
	GlyphLayout layout;
	
    public Text(int maxBalls){
    		FreeTypeFontGenerator fontGenerator = Assets.manager.get(Assets.myFont, FreeTypeFontGenerator.class);
			FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter =
			      new FreeTypeFontGenerator.FreeTypeFontParameter();
			freeTypeFontParameter.size = 20;
			freeTypeFontParameter.borderColor = Color.BLACK;
			freeTypeFontParameter.borderWidth = 2;
			fontGenerator.generateData(freeTypeFontParameter);
			font = fontGenerator.generateFont(freeTypeFontParameter);
			font.getData().setScale(0.08f, 0.03f);
			font.setColor(Color.ORANGE);
			remainingBalls = maxBalls;
			position = Constants.SCENE_HEIGHT / 30f * 0.97f;
    }

	@Override
    public void draw(Batch batch, float parentAlpha) {
		font.draw(batch, remainingBalls + "", 3f, position );
	}
	
	
	
}

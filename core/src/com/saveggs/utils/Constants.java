package com.saveggs.utils;

import assets.Assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;


public class Constants {
	
	public Constants(){};
	
	public static final float SCENE_WIDTH = 800f;
	public static final float SCENE_HEIGHT = 480f;

	/**
	 * Slingshot
	 *///3.5 7.75
	//slingshot position
	public static final float slingshotPosX = 2.8f;
	public static final float slingshotPosY = 2.5f;
	//middle between lasticite5.34375 10.25
	public static float middleY = 3.4333334f;
	public static float middleX = 2.85f;
	/**
	 * Mesh One
	 */
	//Middle point of one greda
	public static float mesh1MiddleY = 3.5f;
	public static float mesh1MiddleX = 2.333333f;
	/**
	 * Mesh Two
	 */
	//Middle point of one greda
	public static float mesh2MiddleY = 3.5333333f;
	public static float mesh2MiddleX = 3.2666664f;
	
	/**
	 * Enemy
	 */
	public static final float ENEMYSPEED =150f;
	public static final float FlYINGBIRDVELOCITY = 3f;
	/**
	 * Actors contants
	 */
	public static final String GROUND = "ground";
	public static final String StaticBall = "staticBall";
	public static final String DynamicBall = "dynamicBall";
	public static final String Slingshot = "slingshot";
	public static final String QICE = "qice";
	public static final String Enemy = "enemey";
	public static final String Enemy2 = "enemey2";
	public static final String ENEMYBOUNDARIES = "boundaries";
	public static final String EnemyHitArea = "hitArea";
	public static final String KRAKA = "kraka";
	public static final String HVANATOQICE = "hvanatoQice";
	public static final String DOKOSVANESQICE = "dokosvane";
	public static final String SENSORzaDOKOSVANE = "sensor";
	//Flying Bird
	public static final String FLYINGBIRD2 = "flyingbird2";
	public static final String FLYINGBIRDENEMYBOUNDARIES2 = "boundariesFB2";
	public static final String FLYINGBIRDHITAREA2 = "hitAreaFB2";
	public static final String FLYINGBIRD = "flyingbird";
	public static final String FLYINGBIRDENEMYBOUNDARIES = "boundariesFB";
	public static final String FLYINGBIRDHITAREA = "hitAreaFB";
	//flying area
	public static final String LINE1 = "line1";
	public static final String LINE2 = "line2";
	
	public static NinePatch getNinePatch() {
	    
		// Get the image
		final Texture t = Assets.manager.get(Assets.levelBackGround, Texture.class);
	    
		// create a new texture region, otherwise black pixels will show up too, we are simply cropping the image
		// last 4 numbers respresent the length of how much each corner can draw,
		// for example if your image is 50px and you set the numbers 50, your whole image will be drawn in each corner
		// so what number should be good?, well a little less than half would be nice
	    return new NinePatch(new TextureRegion(t, 1, 1 , t.getWidth(), t.getHeight()), 10, 10, 10, 10);
	}
	
}

package com.saveggs.utils;

import assets.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.saveggs.game.GameStage;


public class Constants {
	public static Preferences preferences;
	public static Sound sound;
	
	public Constants(){
		preferences = Gdx.app.getPreferences(GameStage.class.getName());
		preferences.putBoolean("Level1", true);
		preferences.flush();
		sound = Assets.manager.get(Assets.musicTest, Sound.class);
	};
	
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
	public static float ENEMYSPEED = 160;
	public static float FlYINGBIRDVELOCITY = 4;
	public static float ballSpeed = 14f;
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
	
	
}

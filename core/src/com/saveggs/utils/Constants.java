package com.saveggs.utils;

import com.badlogic.gdx.math.Vector2;


public class Constants {
	public static final float SCENE_WIDTH = 800f;
	public static final float SCENE_HEIGHT = 480f;
	
	/**
	 * Slingshot
	 *///3.5 7.75
	//slingshot position
	public static float slingshotPosX = 2.8f;
	public static float slingshotPosY = 2.5f;
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
	//loshi
	//y= -2.2285714f x = -8.542857f
	//dobri
	//y = 0.8714285f; x = 0.028572083f
	/**
	 * Ground
	 */
	public static float groundX = 0;
	public static float groundY = -6f;
	
	/**
	 * Enemy
	 */
	public static float ENEMYVELICOTYSPEED = 0f;
	/**
	 * Actors contants
	 */
	public static final String GROUND = "ground";
	public static final String StaticBall = "staticBall";
	public static final String DynamicBall = "dynamicBall";
	public static final String Slingshot = "slingshot";
	public static final String QICE = "dokosvane";
	public static final String Enemy = "enemey";
	public static final String ENEMYBOUNDARIES = "boundaries";
	public static final String EnemyHitArea = "hitArea";
	public static final String KRAKA = "kraka";
	public static final String HVANATOQICE = "qice";
	public static final String DOKOSVANESQICE = "dokosvane";
	public static final String SENSORzaDOKOSVANE = "sensor";

	/*
	 * Eggs
	 */
	public static final Vector2[] eggPositions = new Vector2[]{new Vector2(13.40625f, 3.28125f)};
/*	new Vector2(7.0571423f, -4.2857146f),
	   new Vector2(1.9999995f, -4.257143f)
*/}

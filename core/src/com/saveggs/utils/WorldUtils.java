package com.saveggs.utils;

import java.util.Random;

import net.dermetfan.gdx.graphics.g2d.Box2DSprite;
import assets.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class WorldUtils {
	
	public static float[] verts = new float[] { -0.2571426f,0.7714283f, 0, 0.2f, 0.3f, 0.4f, 1f, 0, 1,
												-0.28571403f , 1.0000004f, 0, 0.1f, 0.2f, 0.1f, 1f, 1, 1,
												0f, 0f, 0, 0, 0.4f, 0.5f, 0.5f, 1f, 0 };
	
	public static float[] verts2 = new float[] { -0.2571426f,0.7714283f, 0, 0.2f, 0.3f, 0.4f, 1f, 0, 1,
												-0.28571403f , 1.0000004f, 0, 0.1f, 0.2f, 0.1f, 1f, 1, 1,
												0f, 0f, 0, 0, 0.4f, 0.5f, 0.5f, 1f, 0 };
	
	//static ball
    public static Body createEgg(World world) {
	       //Ball one
	       BodyDef def2 = new BodyDef();
	       def2.position.x = Constants.middleX;
	       def2.position.y = Constants.middleY;
	       def2.type = BodyDef.BodyType.DynamicBody;
	       
	       PolygonShape shape2 = new PolygonShape();
	       shape2.setAsBox(0.5f, 0.4f);
	       
	       FixtureDef fixDef2 = new FixtureDef();
	       fixDef2.shape = shape2;
	       fixDef2.filter.groupIndex = -1;
		   Body body = world.createBody(def2);
		   body.createFixture(fixDef2).setUserData(Constants.QICE);
		   body.setGravityScale(0);
		   body.setUserData(Constants.QICE);
		   shape2.dispose();
		   return body;
    }
    
    //dynamic ball
    public static Body createDynamicBall(World world) {
	       //Ball one
	       BodyDef def2 = new BodyDef();
	       def2.position.x =  0;
	       def2.position.y = 0;
	       def2.type = BodyDef.BodyType.DynamicBody;
	       CircleShape shape2 = new CircleShape();
	       shape2.setRadius(0.22f);
	       
	       FixtureDef fixDef2 = new FixtureDef();
	       fixDef2.shape = shape2;
	       fixDef2.filter.groupIndex = -1;
	       fixDef2.restitution = 0.3f;
		   Body body = world.createBody(def2);
		   body.createFixture(fixDef2).setUserData(Constants.DynamicBall);

		   body.setUserData(Constants.DynamicBall);
		   shape2.dispose();
		   return body;
    }
    
  
    //slingshot
    public static Body createSlingshot(World world){
	       //Slingshot
	       BodyDef def = new BodyDef();
	       def.position.x = Constants.slingshotPosX;
	       def.position.y = Constants.slingshotPosY;
	       def.type = BodyDef.BodyType.StaticBody;
	       PolygonShape shape = new PolygonShape();
	       shape.setAsBox(0.8f, 1.6f);
	       FixtureDef fixDef = new FixtureDef();
	       fixDef.shape = shape;
	       fixDef.filter.groupIndex = -1;
	       Body body = world.createBody(def);
	       body.createFixture(fixDef).setUserData(Constants.Slingshot);
	       body.setUserData(Constants.Slingshot);
	       shape.dispose();
	       return body;
    }
    
    
    //enemy
    public static Body createEnemy(World world){
	
	       BodyDef def = new BodyDef();
	       def.type = BodyDef.BodyType.DynamicBody;
	       
	       //na otivane
	       PolygonShape kraka = new PolygonShape(); 
	       kraka.setAsBox(0.7f, 0.7f,new Vector2(-0.7f,0f),0f);
	       FixtureDef krakaFD = new FixtureDef(); 
	       krakaFD.isSensor = true; 
	       krakaFD.shape = kraka;  
	       
	       //na pribirane
	       PolygonShape krakata = new PolygonShape(); 
	       //kraka.setRadius(1f);
	       krakata.setAsBox(0.7f, 0.7f,new Vector2(-0.7f,-0.3f),0);
	       FixtureDef krakataFD = new FixtureDef(); 
	       krakataFD.isSensor = true; 
	       krakataFD.shape = krakata;  
	      
	       //dokosvane za otvarqne na kraka
	       PolygonShape dokosvaneQice = new PolygonShape(); 
	       dokosvaneQice.setAsBox(1.2f, 1f,new Vector2(-0f,2.1f),0);
	       FixtureDef dokosvane = new FixtureDef(); 
	       dokosvane.isSensor = true; 
	       dokosvane.shape = dokosvaneQice;
	       
	       //dokosvane s qiceto
	       PolygonShape sensorZaDokosvane = new PolygonShape(); 
	       //kraka.setRadius(1f);
	       sensorZaDokosvane.setAsBox(0.7f, 0.7f,new Vector2(-0.67f,-0.9f),0);
	       FixtureDef dokosvaneFd = new FixtureDef(); 
	       dokosvaneFd.isSensor = true; 
	       dokosvaneFd.shape = sensorZaDokosvane; 
	       
	       //2nd fixture
	       PolygonShape shape = new PolygonShape();
	       shape.setAsBox(1.4f, 2.0f);
	       PolygonShape circle = new PolygonShape(); 
	       circle.setAsBox(0.3f, 0.7f);
	       FixtureDef sensorFD = new FixtureDef(); 
	       sensorFD.isSensor = true; 
	       sensorFD.shape = circle;  
	       //1st fixture
	       FixtureDef fixDef = new FixtureDef();
	       fixDef.shape = shape;
	       fixDef.isSensor = true;
	       Body body = world.createBody(def);
	       
	       //Add the fixtures
	       body.createFixture(fixDef).setUserData(Constants.ENEMYBOUNDARIES);
	       body.createFixture(sensorFD).setUserData(Constants.EnemyHitArea);
	       body.createFixture(krakaFD).setUserData(Constants.KRAKA);
	       body.createFixture(krakataFD).setUserData(Constants.HVANATOQICE);
	       body.createFixture(dokosvane).setUserData(Constants.DOKOSVANESQICE);
	       body.createFixture(dokosvaneFd).setUserData(Constants.SENSORzaDOKOSVANE);
	       body.setUserData(Constants.Enemy);
	      // circle.dispose();
	       shape.dispose();
	       body.setGravityScale(0);
	       return body;
    }
    
    //Flipped enemy flying from the other side
    public static Body createEnemyOtherSide(World world){
    	
	       BodyDef def = new BodyDef();
	       def.type = BodyDef.BodyType.DynamicBody;
	       
	       //na otivane
	       PolygonShape kraka = new PolygonShape(); 
	       kraka.setAsBox(0.7f, 0.7f,new Vector2(0.7f,-0.1f),0f);
	       FixtureDef krakaFD = new FixtureDef(); 
	       krakaFD.isSensor = true; 
	       krakaFD.shape = kraka;  
	       
	       //na pribirane
	       PolygonShape krakata = new PolygonShape(); 
	       //kraka.setRadius(1f);
	       krakata.setAsBox(0.7f, 0.7f,new Vector2(0.7f,-0.6f),0);
	       FixtureDef krakataFD = new FixtureDef(); 
	       krakataFD.isSensor = true; 
	       krakataFD.shape = krakata;  
	      
	       //dokosvane za otvarqne na kraka
	       PolygonShape dokosvaneQice = new PolygonShape(); 
	       dokosvaneQice.setAsBox(1.2f, 1f,new Vector2(0f,2.1f),0);
	       FixtureDef dokosvane = new FixtureDef(); 
	       dokosvane.isSensor = true; 
	       dokosvane.shape = dokosvaneQice;
	       
	       //dokosvane s qiceto
	       PolygonShape sensorZaDokosvane = new PolygonShape(); 
	       //kraka.setRadius(1f);
	       sensorZaDokosvane.setAsBox(0.7f, 0.7f,new Vector2(0.67f,-0.9f),0);
	       FixtureDef dokosvaneFd = new FixtureDef(); 
	       dokosvaneFd.isSensor = true; 
	       dokosvaneFd.shape = sensorZaDokosvane; 
	       
	       //2nd fixture
	       PolygonShape shape = new PolygonShape();
	       shape.setAsBox(1.4f, 2.0f);
	       PolygonShape circle = new PolygonShape(); 
	       circle.setAsBox(0.3f, 0.7f);
	       FixtureDef sensorFD = new FixtureDef(); 
	       sensorFD.isSensor = true; 
	       sensorFD.shape = circle;  
	       //1st fixture
	       FixtureDef fixDef = new FixtureDef();
	       fixDef.shape = shape;
	       fixDef.isSensor = true;
	       Body body = world.createBody(def);
	       
	       //Add the fixtures
	       body.createFixture(fixDef).setUserData(Constants.ENEMYBOUNDARIES);
	       body.createFixture(sensorFD).setUserData(Constants.EnemyHitArea);
	       body.createFixture(krakaFD).setUserData(Constants.KRAKA);
	       body.createFixture(krakataFD).setUserData(Constants.HVANATOQICE);
	       body.createFixture(dokosvane).setUserData(Constants.DOKOSVANESQICE);
	       body.createFixture(dokosvaneFd).setUserData(Constants.SENSORzaDOKOSVANE);
	       body.setUserData(Constants.Enemy2);
	      // circle.dispose();
	       shape.dispose();
	       body.setGravityScale(0);
	       return body;
 }
    
    
    //Flying Bird
    public static Body createFlyingBird(World world){
	
	       BodyDef def = new BodyDef();
	       def.type = BodyDef.BodyType.DynamicBody;	       
	       //2nd fixture
	       PolygonShape shape = new PolygonShape();
	       shape.setAsBox(1.6f, 1.2f); 
	       //1st fixture
	       FixtureDef fixDef = new FixtureDef();
	       fixDef.shape = shape;
	       fixDef.isSensor = true;
	       Body body = world.createBody(def);
	
	       //2nd fixture
	       PolygonShape hitArea = new PolygonShape(); 
	       hitArea.setAsBox(1.2f, 0.8f);
	       FixtureDef sensorFD = new FixtureDef(); 
	       sensorFD.isSensor = true; 
	       sensorFD.shape = hitArea;
	       
	       //Add the fixtures
	       body.createFixture(fixDef).setUserData(Constants.FLYINGBIRDENEMYBOUNDARIES);
	       body.createFixture(sensorFD).setUserData(Constants.FLYINGBIRDHITAREA);
	       body.setUserData(Constants.FLYINGBIRD);
	       body.setGravityScale(0);
	       
	       return body;
    }
    
    //slingshot
    public static Body createFlyingBird2(World world){
	
	       BodyDef def = new BodyDef();
	       def.type = BodyDef.BodyType.DynamicBody;	       
	       //2nd fixture
	       PolygonShape shape = new PolygonShape();
	       shape.setAsBox(1.2f, 0.9f); 
	       //1st fixture
	       FixtureDef fixDef = new FixtureDef();
	       fixDef.shape = shape;
	       fixDef.isSensor = true;
	       Body body = world.createBody(def);
	
	       //2nd fixture
	       PolygonShape hitArea = new PolygonShape(); 
	       hitArea.setAsBox(0.9f, 0.5f);
	       FixtureDef sensorFD = new FixtureDef(); 
	       sensorFD.isSensor = true; 
	       sensorFD.shape = hitArea;
	       
	       //Add the fixtures
	       body.createFixture(fixDef).setUserData(Constants.FLYINGBIRDENEMYBOUNDARIES2);
	       body.createFixture(sensorFD).setUserData(Constants.FLYINGBIRDHITAREA2);
	       body.setUserData(Constants.FLYINGBIRD2);
	       body.setGravityScale(0);
	       
	       return body;
    }
    
    
    public static Mesh createMesh(){
		Mesh mesh = new Mesh(true, 3, 3, 
				VertexAttribute.Position(), VertexAttribute.ColorUnpacked(), VertexAttribute.TexCoords(0));

		mesh.setVertices(verts);
		mesh.setIndices(new short[] { 0, 1, 2 });

		return mesh;
    }
    
    public static Mesh createMesh2(){
		Mesh mesh = new Mesh(true, 3, 3, 
				VertexAttribute.Position(), VertexAttribute.ColorUnpacked(), VertexAttribute.TexCoords(0));

		mesh.setVertices(verts2);
		mesh.setIndices(new short[] { 0, 1, 2 });

		return mesh;
    }
    
    public static Mesh newMesh() {
	   Mesh mesh = new Mesh(true, 3, 3, VertexAttribute.Position(), VertexAttribute.ColorUnpacked(), VertexAttribute.TexCoords(0));
	   return mesh;
    }
   
}

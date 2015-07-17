package com.saveggs.game;

import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Slingshot extends ApplicationAdapter implements InputProcessor {

	
	private static final float SCENE_WIDTH = 800f;
	private static final float SCENE_HEIGHT = 480f;
	Sprite sprite;
	Body body;
	Body body2;
	Body body3;
	Viewport viewport;
	Box2DDebugRenderer debugRenderer;
	//World world;
	CircleShape circle;
	private Box2DSprite slingshot;
	private Box2DSprite topka;
	private Box2DSprite topche;
	SpriteBatch batch;
	private OrthographicCamera camera;
	private Mesh mesh;
	private Mesh mesh2;
	private World world;
	private Box2DDebugRenderer  degubRenderer;
	private Array<Body> bodies; 
	FixtureDef fixDef2;
	FixtureDef fixDef3;
	BodyDef def2 ;
	BodyDef def3 ;
	Body ground;
	Body newBody;
	private float[] verts = new float[] { -0.2571426f,0.7714283f, 0, 0.2f, 0.3f, 0.4f, 1f, 0, 1,
										  -0.28571403f , 1.0000004f, 0, 0.1f, 0.2f, 0.1f, 1f, 1, 1,
										  0f, 0f, 0, 0, 0.4f, 0.5f, 0.5f, 1f, 0 };
	private float[] verts2 = new float[] { 0.28571403f,0.74285734f, 0, 0.2f, 0.3f, 0.4f, 1f, 0, 1,
										   0.28571403f,0.9714285f, 0, 0.1f, 0.2f, 0.1f, 1f, 1, 1,
										   0f, 0f, 0, 0, 0.4f, 0.5f, 0.5f, 1f, 0 };

	private int idx = 0;
	Texture texture;
	
	ShaderProgram shader;
	@Override
	public void create () {
		
		Gdx.input.setInputProcessor(this);
		
		world = new World(new Vector2(0,-9.8f), true);
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(SCENE_WIDTH / 35, SCENE_HEIGHT / 35, camera);
		batch = new SpriteBatch();
		//Texture text = new Texture(Gdx.files.internal("data/caveman.png"));
		sprite = new Sprite( new Texture(Gdx.files.internal("data/caveman.png")));
		
		Gdx.app.log("GDX", "create...");
		 
		String vertexShader = 
				"attribute vec4 a_position;\n" + 
                "attribute vec4 a_color;\n" +
                "attribute vec2 a_texCoord;\n" + 
                "uniform mat4 u_worldView;\n" + 
                "varying vec4 v_color;\n" + 
                "varying vec2 v_texCoords;\n" +  
                "void main()\n" + 
                "{\n" + 
                "   v_color =  vec4(1, 1, 1, 1);\n" + 
                "   v_texCoords = a_texCoord;\n" + 
                "   gl_Position =  u_worldView * a_position;\n" + 
                "}\n" ;
		
				String fragmentShader = 
				  "#ifdef GL_ES\n" +
                  "precision mediump float;\n" + 
                  "#endif\n" + 
                  "varying vec4 v_color;\n" + 
                  "varying vec2 v_texCoords;\n" + 
                  "uniform sampler2D u_texture;\n" + 
                  "void main()\n" + 
                  "{\n" + 
                  	 "vec4 texColor = texture2D(u_texture, v_texCoords);\n" +
                   	" gl_FragColor = texColor;\n" +
                  "}";
				
		
		  shader = new ShaderProgram(vertexShader, fragmentShader);
		  
		 if (shader.isCompiled() == false) {
	         Gdx.app.log("ShaderError", shader.getLog());
	         System.exit(0);
	      }
		 	 
	       mesh = new Mesh(true, 3, 3, VertexAttribute.Position(), VertexAttribute.ColorUnpacked(), VertexAttribute.TexCoords(0));

	       mesh.setVertices(verts);
	       mesh.setIndices(new short[] { 0, 1, 2 });
     
	       mesh2 = new Mesh(true, 3, 3, VertexAttribute.Position(), VertexAttribute.ColorUnpacked(), VertexAttribute.TexCoords(0));

	       mesh2.setVertices(verts2);
	       mesh2.setIndices(new short[] { 0, 1, 2 });

	       texture = new Texture(Gdx.files.internal("data/lastik.png"));
	       
	       degubRenderer = new Box2DDebugRenderer();
	       
	       slingshot = new Box2DSprite(new Texture(Gdx.files.internal("data/slingshot2.png")));
	       topka = new Box2DSprite(new Texture(Gdx.files.internal("data/ball_small.png")));
	 
	       //Slingshot
	       BodyDef def = new BodyDef();
	       def.position.x = 0;
	       def.position.y = 0;
	       def.type = BodyDef.BodyType.StaticBody;
	       PolygonShape shape = new PolygonShape();
	       shape.setAsBox(0.5f, 1.5f);
	       FixtureDef fixDef = new FixtureDef();
	       fixDef.shape = shape;
	       fixDef.filter.groupIndex = -1;
	       body = world.createBody(def);
	       body.createFixture(fixDef);
	       body.setUserData(slingshot);
	       
	       //Ball one
	       def2 = new BodyDef();
	       def2.position.x = 0.028572083f;
	       def2.position.y = 0.9714285f;
	       def2.type = BodyDef.BodyType.DynamicBody;
	       CircleShape shape2 = new CircleShape();
	       shape2.setRadius(0.2f);
	       
	       fixDef2 = new FixtureDef();
	       fixDef2.shape = shape2;
	       fixDef2.filter.groupIndex = -1;
		   body2 = world.createBody(def2);
		   body2.createFixture(fixDef2);
		   body2.setUserData(topka);
		   body2.setGravityScale(0);
	       
	       
	       //2nd ball
	       def3  = new BodyDef();
	       def3.position.x = 0;
	       def3.position.y = 0;
	       def3.type = BodyDef.BodyType.DynamicBody;
	       fixDef3 = new FixtureDef();
	       fixDef3.shape = shape2;
	       fixDef3.filter.groupIndex = -1;
	       
	       //Ground
	       BodyDef groundDef = new BodyDef();
	       groundDef.position.x	= 0 ;
	       groundDef.position.y	= -5f;
	       groundDef.type = BodyType.StaticBody;
	       PolygonShape s = new PolygonShape();
	       s.setAsBox(SCENE_WIDTH, 0.5f);
	       FixtureDef groundFix = new FixtureDef();
	       groundFix.shape = s;
	       ground = world.createBody(groundDef);
	       ground.createFixture(groundFix);
	       
	       bodies = new Array<Body>(); 
	}
	
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, false);
	}
	
	//Udpade ball position
	Vector2 coords = new Vector2();
	Vector2 getCoords = new Vector2();
	
	//Update lastici position
	Vector2 lastikOneX = new Vector2();
	Vector2 lastikOneY = new Vector2();
	Vector2 lastikTwoX = new Vector2();
	Vector2 lastikTwoY = new Vector2();
	
	//Touch position
	Vector3 position = new Vector3();
	Vector3 touchDown = new Vector3();
	
	boolean createBody = false;
	boolean newBodyCreated = false;
	Vector2 newBodyCoords = new Vector2();
	boolean startDrawing = false;
	Vector2 applyForceCoords = new Vector2();
	float trY =  0.9714285f;
	float trX =  0.028572083f;

	
	//Ball position
	public Vector2 getCoords(float x, float y) {
		//Middle point between gredite na slingshot
		//Get angle
	    float angle = (float) Math.toDegrees(Math.atan2(y - (trY), x - (trX)));
	    //convert to radians
		float radians =  (float) angle * MathUtils.degreesToRadians;
		// calculate distance
		float distance = new Vector2(x,y).dst(new Vector2(trX,trY)) - 0.4f;
		//calculate x and y
        coords.y = (float) (trY + distance * Math.sin(radians));
        coords.x = (float) (trX + distance * Math.cos(radians));
        
	    return coords;
	}
	
	
	//Ball position
	public void applyForceToNewBody(float x, float y, Body body) {
		//Get angle
	    float angle = (float) Math.toDegrees(Math.atan2((trY) - y,  (trX) - x));
	    //convert to radians
		float radians =  (float) angle * MathUtils.degreesToRadians;
		// calculate distance
		float distance = new Vector2(x,y).dst(new Vector2(trX,trY)) * 5f;
		//calculate x and y
		applyForceCoords.y = (float) (trY + distance * Math.sin(radians));
		applyForceCoords.x = (float) (trX + distance * Math.cos(radians));
        body.applyLinearImpulse(applyForceCoords, body.getWorldCenter(), true);

	}
	
	//Lastik 1 position
	public void calculateXYLastikOne(float x, float y) {
		//Middle point of one greda
		float middleY =  0.94285756f;
		float middleX =  0.3142861f;
		//Get angle
		float angle = (float) Math.toDegrees(Math.atan2(y - (middleY), x - (middleX)));
		float radians =  (float) (angle + 45) * MathUtils.degreesToRadians;
		//Update coordinate
		lastikOneX.y = (float) (middleY + 0.15f * Math.sin(radians));
		lastikOneX.x = (float) (middleX + 0.15f * Math.cos(radians));
        //Update coordinate 2
        float radians2 =  (float) (angle - 45) * MathUtils.degreesToRadians;
        lastikOneY.y = (float) (middleY + 0.15f * Math.sin(radians2));
        lastikOneY.x = (float) (middleX + 0.15f * Math.cos(radians2));
	}
	
	//Lastik 2 position
	public void calculateXYLastikTwo(float x, float y) {
		//Middle point of one greda
		float middleY =  0.97714263f;
		float middleX =  -0.28285697f;
		//Get angle
		float angle = (float) Math.toDegrees(Math.atan2(y - (middleY), x - (middleX)));
		float radians =  (float) (angle + 45) * MathUtils.degreesToRadians;
		//Update coordinate
		lastikTwoX.y = (float) (middleY + 0.15f * Math.sin(radians));
		lastikTwoX.x = (float) (middleX + 0.15f * Math.cos(radians));
        //Update coordinate 2
        float radians2 =  (float) (angle - 45) * MathUtils.degreesToRadians;
        lastikTwoY.y = (float) (middleY + 0.15f * Math.sin(radians2));
        lastikTwoY.x = (float) (middleX + 0.15f * Math.cos(radians2));
	}

	@Override
	public void render () {
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(viewport.getCamera().combined);
						
		//Update ball position
		
	    batch.begin();
			topka.draw(batch, body2);
		batch.end();
		
		if(createBody){	
			calculateXYLastikOne(position.x,position.y);
			calculateXYLastikTwo(position.x,position.y);

			drawTriangle(lastikOneX.x,lastikOneX.y, lastikOneY.x , lastikOneY.y,position.x,position.y,
						 lastikTwoX.x,lastikTwoX.y, lastikTwoY.x,lastikTwoY.y,position.x,position.y);
		}

		world.step(1 / 60f,6, 2);
		
		batch.begin();
    	slingshot.draw(batch, body);
    		
		if(bodies.size > 0){    			
			for (Body body : bodies) {
				topka.draw(batch, body);           
			}
		}
    	batch.end();
    	
		if(Gdx.input.isKeyJustPressed(Keys.UP)){
			camera.zoom -= 0.1f;
			camera.update();
		}
		
		//degubRenderer.render(world,viewport.getCamera().combined);
		
		if(Gdx.input.isKeyJustPressed(Keys.DOWN)){
			camera.zoom += 0.1f;
			camera.update();
		}
		
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		camera.unproject(touchDown.set(screenX, screenY, 0));	
		System.out.println(touchDown.x + " " + touchDown.y);
		return false;
	}
	

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {	
		//Create a new body
	    Body newBody = world.createBody(def3);
	    newBody.createFixture(fixDef3);
	    newBody.setUserData(topka);
		newBody.setTransform(body2.getPosition(), 0);
		//Apply force
		applyForceToNewBody(newBody.getPosition().x, newBody.getPosition().y, newBody);
		//add to the list for rendering
		bodies.add(newBody);
		
		//stop rendering old body
		createBody = false;
		//destroy it
		world.destroyBody(body2);
		
		//Create new body and place it at default position
		body2 = world.createBody(def2);
		body2.createFixture(fixDef2);
		body2.setUserData(topka);
		body2.setGravityScale(0);
		
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		camera.unproject(position.set(screenX, screenY, 0));
		//Update coordinates for the body
		getCoords = getCoords(position.x,position.y);
		//Start drawing/positioning of new body(see render)
		createBody = true;
		body2.setTransform(getCoords.x, getCoords.y,0);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	void drawTriangle(float x, float y,float x2, float y2,float x3, float y3,
				      float x4, float y4,float x5, float y5,float x6, float y6) {		
	
		//Update coordinate for lastik one
		verts[0] = x; 
		verts[1] = y;
		
		verts[9] = x2; 			 
		verts[10] = y2;	
		
		verts[18] = x3; 			 
		verts[19] = y3;

		//Update coordinate for lastik two		
		verts2[0] = x4; 	
		verts2[1] = y4;
		
		verts2[9] = x5; 			 
		verts2[10] = y5; 	
		
		verts2[18] = x6; 			 
		verts2[19] = y6;
		
		
		mesh.setVertices(verts);
		mesh2.setVertices(verts2);
		
	    Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);
	    //Draw mesh 1
	    texture.bind();
		shader.begin();
		shader.setUniformMatrix("u_worldView", viewport.getCamera().combined);
		shader.setUniformi("u_texture", 0);
		mesh.render(shader, GL20.GL_TRIANGLES);
		shader.end();	
		
	    //Draw ball
	    batch.begin();
	    	topka.draw(batch, body2);
	    batch.end();

	    //Draw mesh 2
	    texture.bind();
		shader.begin();
		shader.setUniformMatrix("u_worldView", viewport.getCamera().combined);
		shader.setUniformi("u_texture", 0);
		mesh2.render(shader, GL20.GL_TRIANGLES);
		shader.end();	
		
	    //Draw slinshot
	    batch.begin();
	    	slingshot.draw(batch, body);
	    batch.end();
	    	    
	}

}
package com.saveggs.game;

import net.dermetfan.gdx.graphics.g2d.Box2DSprite;
import net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser;
import assets.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.sageggs.actors.*;
import com.saveggs.utils.Constants;

public class Test extends Stage implements Screen {

	GameClass game;
	private Stage stage;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private World world;
	private Box2DDebugRenderer debugRenderer ;
	private OrthographicCamera camera;
	
	public Test(GameClass game){
		super(new ExtendViewport(Constants.SCENE_WIDTH / 35, Constants.SCENE_HEIGHT / 35, new OrthographicCamera()));

		camera = new OrthographicCamera();
		getViewport().setCamera(camera);

		world = new World(new Vector2(0,-9.8f), true);
		
		debugRenderer = new Box2DDebugRenderer();
		
		map = new TmxMapLoader().load("data/map.tmx");
		Box2DMapObjectParser parser = new Box2DMapObjectParser(.03125f);
		parser.load(world, map);
		
		
		renderer = new OrthogonalTiledMapRenderer(map,parser.getUnitScale()); 
		
		this.game = game;
		Gdx.input.setInputProcessor(this);
		
	       BodyDef def2 = new BodyDef();
	       def2.position.x =  11f;
	       def2.position.y = 8.6f;
	       def2.type = BodyDef.BodyType.DynamicBody;
	       CircleShape shape2 = new CircleShape();
	       shape2.setRadius(0.2f);
	       
	       FixtureDef fixDef2 = new FixtureDef();
	       fixDef2.shape = shape2;
	       fixDef2.filter.groupIndex = -1;
		   Body body = world.createBody(def2);
		   body.createFixture(fixDef2).setUserData(new Box2DSprite(Assets.manager.get(Assets.dynamicBall, Texture.class)));

		   body.setUserData(Constants.DynamicBall);
		   shape2.dispose();

	}
	
	@Override
	public void show() {
		
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.act(delta);
		world.step(1 / 60f,6, 2);
        renderer.setView(camera);
        renderer.render();
        debugRenderer.render(world,camera.combined);
        
	}

	@Override
	public void resize(int width, int height) {
		getViewport().update(width, height,false);
		camera.viewportWidth = Constants.SCENE_WIDTH / 32f;
		camera.viewportHeight = Constants.SCENE_HEIGHT / 32f;
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0); 
		camera.update();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
	}
	
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		 Vector3 touchDown = new Vector3();
		camera.unproject( touchDown.set(screenX, screenY, 0));
		System.out.println(touchDown.x + " " + touchDown.y);
		
		
		return super.touchDown(screenX, screenY, pointer, button);
	}

}

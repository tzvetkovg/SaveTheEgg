package com.saveggs.game.screens;

import java.util.Map;

import assets.AssetTest;
import assets.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.saveggs.game.GameClass;
import com.saveggs.game.GameStage;
import com.saveggs.utils.Constants;
import com.saveggs.utils.WorldUtils;

public class StageScreen implements Screen {

	GameClass eggSaver;
	private Stage stage;
	
	
	public StageScreen(GameClass stage2,Map<String,Object> mapBodies,World world){
		
		this.eggSaver = stage2;
		this.stage = new GameStage(mapBodies,world);
	}
	
	@Override
	public void show() {}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// TODO Auto-generated method stub	
		this.stage.act(delta);
		this.stage.draw();
		
		if(Gdx.input.isKeyJustPressed(Keys.UP)){
			((OrthographicCamera) this.stage.getCamera() ).zoom -= 0.1f;
			((OrthographicCamera) this.stage.getCamera() ).update();
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.DOWN)){
			((OrthographicCamera) this.stage.getCamera() ).zoom += 0.1f;
			((OrthographicCamera) this.stage.getCamera() ).update();
		}
	}

	@Override
	public void resize(int width, int height) {
		this.stage.getViewport().update(width, height,false);
		this.stage.getCamera().viewportWidth = Constants.SCENE_WIDTH / 30f;
		this.stage.getCamera().viewportHeight = Constants.SCENE_HEIGHT / 30f;
		this.stage.getCamera().position.set(this.stage.getCamera().viewportWidth / 2f, this.stage.getCamera().viewportHeight / 2f, 0); 
		this.stage.getCamera().update();
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose() {
		this.dispose();
		Assets.manager.dispose();
	}

}

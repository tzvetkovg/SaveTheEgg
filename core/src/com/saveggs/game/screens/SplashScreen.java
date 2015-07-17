package com.saveggs.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.saveggs.game.GameClass;
import com.saveggs.utils.Constants;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class SplashScreen implements Screen {
	
	private GameClass game;
	private Stage stage;
	private Image image;
	private OrthographicCamera camera;
	
	public SplashScreen(GameClass game){
		this.game = game;
		this.stage = new Stage(new ExtendViewport(Constants.SCENE_WIDTH / 35, Constants.SCENE_HEIGHT / 35));
		camera = new OrthographicCamera();
		stage.getViewport().setCamera(camera);
		
		Gdx.input.setInputProcessor(stage);
		Sprite splash = new Sprite(new Texture(Gdx.files.internal("data/caveman.png")));
		
		image = new Image(splash);
		image.setSize(splash.getWidth() / 100, splash.getHeight() / 100);
		image.setOrigin(splash.getWidth()/ 35 / 2, splash.getHeight()/ 35 / 2);
		stage.addActor(image);
	}
	
	@Override
	public void show() 
	{
		image.addAction(sequence(alpha(0),scaleTo(.1f,.1f),
						parallel(fadeIn(2f, Interpolation.pow2),
								scaleTo(2f,2f,2.5f, Interpolation.pow5),
								moveTo(1f,1f, 2f ,Interpolation.swing)),
						delay(1.5f), fadeOut(1.25f)));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
		
		if(image.getActions().size == 0)
			game.setScreen(new MainMenuScreen(game));
		
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height,false);
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose() {
		stage.dispose();
		
	}

}

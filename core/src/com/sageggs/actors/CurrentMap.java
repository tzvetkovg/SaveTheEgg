package com.sageggs.actors;

import net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser;
import assets.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;

public class CurrentMap extends GameActor{
	
	public OrthogonalTiledMapRenderer renderer;
	String vertexShader = " attribute vec3 a_position;\n " +
							"attribute vec4 a_color;\n" +
							"attribute vec2 a_texCoord0;\n"+
						
							" uniform mat4 u_projTrans;\n"
							+ "uniform vec3 u_distort;\n"+
						
							"varying vec4 v_color;\n"+
							"varying vec2 v_texCoords;\n"+
						
							"void main() {\n"+
							    "v_color = a_color;\n"+
							    "v_texCoords = a_texCoord0;\n"+
							    "gl_Position =  u_projTrans * vec4(a_position + u_distort, 1.);\n"+
							"}";
		String fragmentShader = "#ifdef GL_ES\n" +
					              "precision mediump float;\n" + 
					              "#endif\n" + 
					              "varying vec4 v_color;\n" + 
					              "varying vec2 v_texCoords;\n" + 
					              "uniform sampler2D u_texture;\n" + 
					              "void main()\n" + 
					              "{\n" + 
					              "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n" +
					              "}";
	private ShaderProgram shader; 
	public boolean earthQuake = false;
	
	public CurrentMap(TiledMap map, World world){
		Box2DMapObjectParser parser = new Box2DMapObjectParser(.03333f);
		//Box2DMapObjectParser parser = new Box2DMapObjectParser(1f);
		parser.load(world, map);
		renderer = new OrthogonalTiledMapRenderer(map,parser.getUnitScale());
		shader = new ShaderProgram(vertexShader,fragmentShader);
		//Gdx.app.log("Problem loading shader:", shader.getLog());
		ShaderProgram.pedantic = false;
		renderer.getBatch().setShader(shader);
	}
	
	private float mydelta = 0;
	
    @Override
    public void act(float delta) {
        super.act(delta);
        Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);
        if(earthQuake){ 
        	mydelta+=delta;
        	if((float)mydelta < 0.3f){        		
        		shader.begin();
        		shader.setUniformf("u_distort",MathUtils.random(0.09f),MathUtils.random(0.2f),0);
        		shader.end();
        	}
        	else{
        		shader.begin();
        		shader.setUniformf("u_distort",0,0,0);
        		shader.end();
        		mydelta = 0;
        		earthQuake = false;
        	}
        }
        renderer.setView(((OrthographicCamera)getStage().getCamera()));
        renderer.render();
    }
	
    
}

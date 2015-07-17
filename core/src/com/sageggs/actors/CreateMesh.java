package com.sageggs.actors;

import assets.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.saveggs.utils.Constants;

public class CreateMesh extends GameActor {
	Texture texture;
	public Mesh mesh;
	public  Vector2 lastikOneX = new Vector2();
	public  Vector2 lastikOneY = new Vector2();
	public  boolean drawBody = false;
	
	public float[] verts = new float[] { -9.314286f, -2.2285714f, 0, 0.2f, 0.3f, 0.4f, 1f, 0, 1,
										-8.828571f , -2.2285714f, 0, 0.1f, 0.2f, 0.1f, 1f, 1, 1,
										0f, 0f, 0, 0, 0.4f, 0.5f, 0.5f, 1f, 0 };
	
	ShaderProgram shader;
	
	public CreateMesh(Mesh mesh, ShaderProgram shader){
		this.mesh = mesh;
		texture = Assets.manager.get(Assets.meshLastik, Texture.class);
		this.shader = shader;
	}
	
	 @Override
     public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        
        if(drawBody){        
        	getStage().getBatch().end();
        	Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);
        	//Draw mesh 1
        	texture.bind();
        	shader.begin();
        	shader.setUniformMatrix("u_worldView", getStage().getViewport().getCamera().combined);
        	shader.setUniformi("u_texture", 0);
        	mesh.render(shader, GL20.GL_TRIANGLES);
        	shader.end();	
        	getStage().getBatch().begin();
        }
        
     }
	 
	 public void drawMesh(float x, float y,float x2, float y2,float x3, float y3){
			verts[0] = x; 
			verts[1] = y;
			
			verts[9] = x2; 			 
			verts[10] = y2;	
			
			verts[18] = x3; 			 
			verts[19] = y3;
			this.mesh.setVertices(verts);
	 }
	 
	//Lastik 1 position
	public void calculateXYLastikOne(float x, float y) {
		//Get angle
		float angle = (float) Math.toDegrees(Math.atan2(y - (Constants.mesh1MiddleY), x - (Constants.mesh1MiddleX)));
		float radians =  (float) (angle + 45) * MathUtils.degreesToRadians;
		//Update coordinate
		lastikOneX.y = (float) (Constants.mesh1MiddleY + 0.15f * Math.sin(radians));
		lastikOneX.x = (float) (Constants.mesh1MiddleX + 0.15f * Math.cos(radians));
        //Update coordinate 2
        float radians2 =  (float) (angle - 45) * MathUtils.degreesToRadians;
        lastikOneY.y = (float) (Constants.mesh1MiddleY + 0.15f * Math.sin(radians2));
        lastikOneY.x = (float) (Constants.mesh1MiddleX + 0.15f * Math.cos(radians2));
        drawMesh(lastikOneX.x,lastikOneX.y, lastikOneY.x , lastikOneY.y, x , y);
	}	
}

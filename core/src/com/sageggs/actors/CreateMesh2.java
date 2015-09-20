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
import com.saveggs.utils.Constants;

public class CreateMesh2 extends GameActor{
	Texture texture;
	Mesh mesh;
	protected float[] verts = new float[] { 0.28571403f,0.74285734f, 0, 0.2f, 0.3f, 0.4f, 1f, 0, 1,
											0.28571403f,0.9714285f, 0, 0.1f, 0.2f, 0.1f, 1f, 1, 1,
											0f, 0f, 0, 0, 0.4f, 0.5f, 0.5f, 1f, 0 };
	
	ShaderProgram shader;
	
	public Vector2 lastikTwoX = new Vector2();
	public Vector2 lastikTwoY = new Vector2();

	public boolean drawBody = false;
	
	public CreateMesh2(Mesh mesh, ShaderProgram shader){
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
	 
	//Lastik 2 position
	public void calculateXYLastikTwo(float x, float y) {
		//Get angle
		float angle = (float) Math.toDegrees(Math.atan2(y - (Constants.mesh2MiddleY), x - (Constants.mesh2MiddleX)));
		float radians =  (float) (angle + 45) * MathUtils.degreesToRadians;
		//Update coordinate
		lastikTwoX.y = (float) (Constants.mesh2MiddleY + 0.15f * Math.sin(radians));
		lastikTwoX.x = (float) (Constants.mesh2MiddleX + 0.15f * Math.cos(radians));
        //Update coordinate 2
        float radians2 =  (float) (angle - 45) * MathUtils.degreesToRadians;
        lastikTwoY.y = (float) (Constants.mesh2MiddleY + 0.15f * Math.sin(radians2));
        lastikTwoY.x = (float) (Constants.mesh2MiddleX + 0.15f * Math.cos(radians2));
        
/*        float angleFromMiddlePoint = (float) Math.toDegrees(Math.atan2(y - Constants.middleY, x - Constants.middleX));
        float rad = (float) angleFromMiddlePoint * MathUtils.degreesToRadians;
        Vector2 myVec = new Vector2(Constants.middleX,Constants.middleY);
        float distance = myVec.dst(myVec.cpy().scl(1.20f));
        System.out.println(distance);
		float xNew = (float) (Constants.middleX + distance * (Math.cos(rad)));
		float yNew = (float) (Constants.middleY + distance * (Math.sin(rad)));*/
        
        drawMesh(lastikTwoX.x,lastikTwoX.y, lastikTwoY.x , lastikTwoY.y, x , y);
	}
}

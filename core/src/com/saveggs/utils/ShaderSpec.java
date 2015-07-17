package com.saveggs.utils;

public class ShaderSpec {

	public static final String vertexShader = 
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
	
	public static final	String fragmentShader = 
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

}

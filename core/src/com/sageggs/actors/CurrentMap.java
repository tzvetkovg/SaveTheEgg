package com.sageggs.actors;

import net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser;
import assets.AssetTest;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class CurrentMap extends GameActor{
	
	private TiledMap map;
	public OrthogonalTiledMapRenderer renderer;
	
	public CurrentMap(String mapPath, World world){
		
		map = AssetTest.manager.get("data/maps/level4/map.tmx");
		//map = new TmxMapLoader().load(mapPath);
		Box2DMapObjectParser parser = new Box2DMapObjectParser(.03333f);
		parser.load(world, map);
		renderer = new OrthogonalTiledMapRenderer(map,parser.getUnitScale()); 
	}
	
    @Override
    public void act(float delta) {
        super.act(delta);
        renderer.setView(((OrthographicCamera)getStage().getCamera()));
        renderer.render();
    }
	
}

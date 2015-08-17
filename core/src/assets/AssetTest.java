package assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class AssetTest {
	public static AssetManager manager;
	
	public AssetTest(){
		
		manager = new AssetManager();
		manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		manager.load("data/maps/level4/map.tmx", TiledMap.class);
		manager.finishLoading();
	
	}
	
}

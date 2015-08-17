package assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.maps.tiled.TideMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;

import net.dermetfan.gdx.assets.AnnotationAssetManager;
import net.dermetfan.gdx.assets.AnnotationAssetManager.Asset;

public class Assets {
	
	public static AnnotationAssetManager manager;
	
	public Assets(){
		manager = new AnnotationAssetManager(); 	

		//Assets.manager.setLoader(TiledMap.class, new TideMapLoader(new InternalFileHandleResolver()));
		//Assets.manager.load("data/maps/level4/map.tmx", TiledMap.class);
	}
	
	@Asset(BitmapFont.class)
	public static final String bitmapfont = "data/ui/font.fnt";
	
	@Asset(Texture.class)
	/**
	 * MenuScreen
	 */
	public static final String 
	gameTitle = "data/gameTitle.png",
	buttonUpTex = "data/ui/normalButton.png",
	buttonOverTex = "data/ui/normalButton.png",
	buttonDownTex = "data/ui/clickedButton.png",
	/**
	 * Level Menu
	 */
	gameTitleL = "data/gameTitle.png",
	/**
	 * Stage
	 */
	qice = "data/qice.png",
	dynamicBall = "data/ball_small.png",
	slingshot = "data/slingshot2.png",
	meshLastik = "data/lastik.png",
	//ptica
	pileBezKraka = "data/pilebezkraka.png",
	prisvivaneKraka = "data/prisviti.png",
	opuvaneKraka = "data/opunati.png",
	hvanatoQice = "data/hvanato Qice.png",
	hodeneNaPile = "data/izlupvane.png",
	gulubi = "data/gulubitest.png",
	razmazanoQice = "data/razplokanoQice.png";
	/**
	 * ParticleEffect
	 */
	@Asset(ParticleEffect.class)
	public static final String
	particle = "data/particles/explosion.p",
	particleBall = "data/particles/explosionBall.p",
	particleFlyingBird = "data/particles/flyingbird.p",
	particleIzlupvane = "data/particles/izlupvane.p";
	
/*	@Asset(TiledMap.class)
	public static final String
	map = "data/maps/level4/map.tmx";*/
	
	public static void dispose(){
		manager.dispose();
	}
	
}

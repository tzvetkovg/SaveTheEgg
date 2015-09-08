package assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TideMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import net.dermetfan.gdx.assets.AnnotationAssetManager;
import net.dermetfan.gdx.assets.AnnotationAssetManager.Asset;

public class Assets {
	
	public static AnnotationAssetManager manager;
	
	public Assets(){
		manager = new AnnotationAssetManager(); 	
		manager.setLoader(TiledMap.class, new TmxMapLoader());
	}
	
	@Asset(BitmapFont.class)
	public static final String bitmapfont = "data/ui/font.fnt";
	
	@Asset(Texture.class)
	/**
	 * MenuScreen
	 */
	public static final String 
	gameTitle = "data/ui/gameTitle.png",
	buttonUpTex = "data/ui/normalButton.png",
	buttonOverTex = "data/ui/normalButton.png",
	buttonDownTex = "data/ui/clickedButton.png",
	/**
	 * Level Menu
	 */
	levels = "data/ui/levels.png",
	levelBackGround = "data/ui/bah.png",
	arrow = "data//ui/arrow.png",
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
	razmazanoQice = "data/razplokanoQice.png",
	/**
	 * Loading Screen
	 */
	loadingScreen = "data/loadingScreen.png";
	/**
	 * ParticleEffect
	 */
	@Asset(ParticleEffect.class)
	public static final String
	particle = "data/particles/explosion.p",
	particleBall = "data/particles/explosionBall.p",
	particleFlyingBird = "data/particles/flyingbird.p",
	particleIzlupvane = "data/particles/izlupvane.p",
	pruskaneQice = "data/particles/pruskane.p";

	/**
	 * Maps
	 */
	@Asset(TiledMap.class)
	public static final String
	map2 = "data/maps/level2/map.tmx",
	map3 = "data/maps/level3/map.tmx",
	map4 = "data/maps/level4/map.tmx";
	
	
	@Asset(Sound.class)
	public static final String
	flyingbird = "data/sound/flyingbird.wav";
	
	
	public static void dispose(){
		manager.dispose();
	}
	
}

package assets;

import net.dermetfan.gdx.assets.AnnotationAssetManager;
import net.dermetfan.gdx.assets.AnnotationAssetManager.Asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
	
	public static AnnotationAssetManager manager;
	
	public Assets(){
		manager = new AnnotationAssetManager(); 	
		manager.setLoader(TiledMap.class, new TmxMapLoader());
		manager.setLoader(Skin.class, new SkinLoader(new FileHandleResolver() {
			@Override
			public FileHandle resolve(String fileName) {
				return Gdx.files.internal("data/uiskin.json");
			}
		}));
		manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(new FileHandleResolver() {
			@Override
			public FileHandle resolve(String fileName) {
				return Gdx.files.internal("data/arial.ttf");
			}
		}));

	}
	
	@Asset(FreeTypeFontGenerator.class)
	public static final String myFont = "data/arial.ttf";
	
	@Asset(BitmapFont.class)
	public static final String bitmapfont = "data/ui/font.fnt";
	
	@Asset(Texture.class)
	/**
	 * MenuScreen
	 */
	public static final String 
	credits = "data/credits.png",
	tutorial = "data/tutorialtest.png",
	levels = "data/ui/background.png",
	/**
	 * Stage
	 */
	meshLastik = "data/lastik.png",
	//ptica
	pileBezKraka = "enemy/myEnemy.png",
	izplupvane = "data/izlupvane.png",
	pticheta = "flyingbird1/pticheta1.png",
	pticheta2 = "flyingbird2/pticheta2.png",
	/**
	 * Loading Screen
	 */
	loadingScreen = "data/loadingScreen.png";
	/**
	 * ParticleEffect
	 */
	@Asset(TextureAtlas.class)
	public static final String gameAtlas = "data/ui/all_assets.pack";
	
	@Asset(ParticleEffect.class)
	public static final String
	particle = "data/particles/explosion.p",
	particleBall = "data/particles/explosionBall.p",
	particleFlyingBird = "data/particles/flyingbird.p",
	particleIzlupvane = "data/particles/izlupvane.p",
	pruskaneQice = "data/particles/pruskane.p",
	izlupvaneQice = "data/particles/izlupvane.p",
	reward = "data/particles/reward.p",
	maskaBurst = "data/particles/maskaBurst.p",
	maskaBurst2 = "data/particles/maskaBurst2.p",
	smoke = "data/particles/smoke.p",
	fire = "data/particles/fire.p";

	/**
	 * Maps
	 */
	public static final String
	map1 = "data/maps/level1/map.tmx",
	map2 = "data/maps/level2/map.tmx",
	map3 = "data/maps/level3/map.tmx",
	map4 = "data/maps/level4/map.tmx",
	map5 = "data/maps/level5/map.tmx",
	map6 = "data/maps/level6/map.tmx",
	map7 = "data/maps/level7/map.tmx",
	map8 = "data/maps/level8/map.tmx",
	map9 = "data/maps/level9/map.tmx",
	map10 = "data/maps/level10/map.tmx",
	map11 = "data/maps/level11/map.tmx",
	map12 = "data/maps/level12/map.tmx",
	map13 = "data/maps/level13/map.tmx",
	map14 = "data/maps/level14/map.tmx",
	map15 = "data/maps/level15/map.tmx",
	map16 = "data/maps/level16/map.tmx",
	map17 = "data/maps/level17/map.tmx",
	map18 = "data/maps/level18/map.tmx",	
	map19 = "data/maps/level19/map.tmx",	
	map20 = "data/maps/level20/map.tmx",
	map21 = "data/maps/level21/map.tmx",
	map22 = "data/maps/level22/map.tmx",
	map23 = "data/maps/level23/map.tmx",
	map24 = "data/maps/level24/map.tmx",
	map25 = "data/maps/level25/map.tmx",
	map26 = "data/maps/level26/map.tmx",
	map27 = "data/maps/level27/map.tmx";
	
	@Asset(Skin.class)
	public static final String
	skin = "data/uiskin.json";
	
	/**
	 * Music
	 */
	@Asset(Sound.class)
	public static final String
	dyingBird = "data/sound/dyingbird.wav",
	breakingEgg = "data/sound/breakingEgg.wav",
	musicTest = "data/sound/menuMusic.wav",
	rubber = "data/sound/rubber.wav",
	shot = "data/sound/shot.wav";

	@Asset(Music.class)
	public static final String
	birdScream = "data/sound/trycopy.wav",
	pilence = "data/sound/pilence.wav";
	

	public static void dispose(){
		manager.dispose();
	}
	
}

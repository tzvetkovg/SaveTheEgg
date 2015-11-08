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
		

	}
	
	@Asset(BitmapFont.class)
	public static final String bitmapfont = "data/ui/font.fnt";
	
	@Asset(Texture.class)
	/**
	 * MenuScreen
	 */
	public static final String 
	gameTitle = "data/ui/gameTitle.png",
	levelButton = "data/ui/closedBlack.png",
	levelButtonClicked = "data/ui/closedClick.png",
	readyToBeSolved = "data/ui/readyToBeSolved.png",
	solvedLevel = "data/ui/solved.png",
	solvedLevelClicked = "data/ui/solvedClick.png",
	normalButton = "data/ui/normalButton.png",
	buttonOverTex = "data/ui/normalButton.png",
	buttonDownTex = "data/ui/clickedButton.png",
	slider = "data/ui/PowerBar.bmp",
	sliderKnob = "data/ui/ArrowDown.bmp",
	backbutton = "data/ui/backbutton.png",
	backbuttonClicked = "data/ui/00135white.png",
	/**
	 * Level Menu
	 */
	levels = "data/ui/background.png",
	levelBackGround = "data/ui/bah.png",
	music = "data/ui/music.png",
	noMusic = "data/ui/noMusic.png",
	replay = "data/ui/00074.png",
	myMenu = "data/ui/00161.png",
	replayDown = "data/ui/00074white.png",
	myMenuDown = "data/ui/00161white.png",
	continueButton = "data/ui/00120.png",
	continueButtonDown = "data/ui/00120white.png",
	pause = "data/ui/00127.png",
	pauseDown = "data/ui/00127white.png",
	/**
	 * Stage
	 */
	qice = "data/qice.png",
	dynamicBall = "data/ball_small.png",
	slingshot = "data/slingshot2.png",
	meshLastik = "data/lastik.png",
	//ptica
	pileBezKraka = "data/newEnemy.png",
	prisvivaneKraka = "data/prisviti.png",
	opuvaneKraka = "data/opunati.png",
	hvanatoQice = "data/hvanato Qice.png",
	hodeneNaPile = "data/izlupvane.png",
	gulubi = "data/newEnemy.png",
	pticheta = "data/pticheta1.png",
	pticheta2 = "data/pticheta2.png",
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
	pruskaneQice = "data/particles/pruskane.p",
	izlupvaneQice = "data/particles/izlupvane.p";

	/**
	 * Maps
	 */
	@Asset(TiledMap.class)
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
	map20 = "data/maps/level20/map.tmx";	
	
	@Asset(Skin.class)
	public static final String
	skin = "data/uiskin.json";
	
	/**
	 * Music
	 */
	@Asset(Music.class)
	public static final String
	birdScream = "data/sound/trycopy.wav",
	birdScream2 = "data/sound/trycopy.wav",
	dyingBird = "data/sound/dyingbird.wav",
	pilence = "data/sound/pilence.wav",
	breakingEgg = "data/sound/breakingEgg.wav";
	
	public static void dispose(){
		manager.dispose();
	}
	
}

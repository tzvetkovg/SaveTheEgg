package assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import net.dermetfan.gdx.assets.AnnotationAssetManager;
import net.dermetfan.gdx.assets.AnnotationAssetManager.Asset;

public class Assets {
	
	public static final AnnotationAssetManager manager = new AnnotationAssetManager(); 
	
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
	gulubi = "data/gulubitest.png";
	/**
	 * ParticleEffect
	 */
	
	public static void dispose(){
		manager.dispose();
	}
	
}

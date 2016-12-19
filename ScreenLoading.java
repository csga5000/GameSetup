package com.hobogames.GameSetup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class ScreenLoading implements Screen{	
	public static final float WIDTH = GameMain.properties.viewsize.x, HEIGHT = GameMain.properties.viewsize.y;
	BitmapFont font;
	SpriteBatch batch;
	OrthographicCamera cam;
	Texture bg;
	boolean initiatedLoading = false;
	String msg;
	boolean done = false;
	protected AssetManager man;
	int numAud, numAsset;
	String fontName = null;
	protected float scale = .03f;

	GlyphLayout layout = new GlyphLayout();

	public ScreenLoading(){
		this("");
	}
	public ScreenLoading(String fname){
		fontName = fname;
		cam = new OrthographicCamera(WIDTH,HEIGHT);
		cam.position.set(WIDTH/2,HEIGHT/2,0);
		cam.update();
	}
	public void start(){
		man = new AssetManager();
		numAud = AudioManager.getInstance(GameMain.properties.audioManagerConstructorArgs).que(man);
		numAsset = GameMain.instance.que(man);
		loadCustomItems();
	}
	/**
	 * Called in start() after default items are loaded
	 * @see #start()
	 */
	public abstract void loadCustomItems();
	public void setMessage(String s){
		msg = s;
		//render(0);
	}
	@Override
	public void render(float delta) {
		draw();
		
		man.update();
		if(man.getProgress() == 1 && !done){
			onLoad();
			complete();
			done = true;
		}else
			msg = "Loading: " + (int) Math.round(man.getProgress()*1000)/10;
		if(!initiatedLoading)
			initiatedLoading = true;
			
	}
	public void onLoad(){
		AudioManager.getInstance().init(man);
		AudioManager.getInstance().start();
		GameMain.instance.init(man);
	}
	/**
	 * Called when everything is loaded. 
	 */
	public abstract void complete();
	
	public void draw(){
		Gdx.gl.glClearColor(.5f, .5f, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
			if(bg != null)
				batch.draw(bg, 0,0,cam.viewportWidth,cam.viewportHeight);
			if(msg != null && font != null){
				layout.setText(font, msg);
				font.draw(batch, msg, WIDTH/2-layout.width/2, HEIGHT/2+layout.height/2);
			}
		batch.end();
	}
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		font = new BitmapFont();
		if(fontName != null)
			if(!fontName.equals(""))
					font = new BitmapFont(Gdx.files.internal("fonts/" + fontName));
		String bgFile = GameMain.properties.loadingScreenBg;
		if(bgFile != null)
			if(!bgFile.equals(""))
				bg = new Texture(Gdx.files.internal(bgFile));
		batch = new SpriteBatch();
		font.getData().setScale(scale);
		font.setColor(1,1,1,1);
		font.setUseIntegerPositions(false);
		start();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		if(font != null)font.dispose();
		if(bg!=null)bg.dispose();
		batch.dispose();
	}

}
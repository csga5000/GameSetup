package com.hobogames.GameSetup;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;

public abstract class GameMain extends Game {
	public static GameProperties properties = new GameProperties();
	public static Random random = new Random();
	public static TextureAtlas atlas;
	public static InputMultiplexer multiplexer;
	public static InputManager input;
	public static GestureDetector inputDetect;
	public static boolean keys[] = new boolean[512];
	public static Screen screen;
	public static boolean exists = false;
	public static GameMain instance;
	private static Texture screenLoadingBg;
	public GameMain(){
		if(exists)
			System.out.println("WARNING: Game Instance already exists! Game will likely misbehave");
		exists = true;
		instance = this;
		multiplexer = new InputMultiplexer();
		input = new InputManager(1,1);
		inputDetect = new GestureDetector(input);
	}
	public static Texture getScreenLoadingBg(){
		if(screenLoadingBg == null)
			screenLoadingBg = new Texture(Gdx.files.internal(GameMain.properties.loadingScreenBg));
		return screenLoadingBg;
	}
	public void init(AssetManager man){
		DataManager.getInstance(properties.dataManagerConstructorArgs);
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setInputProcessor(multiplexer);
		multiplexer.addProcessor(input);
		multiplexer.addProcessor(inputDetect);
		if(man != null){
			if(!properties.atlas.equals(""))
				atlas = (TextureAtlas) man.get(properties.atlas);
		}else
			if(!properties.atlas.equals(""))
				atlas = new TextureAtlas(properties.atlas);
	}
	public int que(AssetManager man){
		if(!properties.atlas.equals(""))
			man.load(properties.atlas, TextureAtlas.class);
		return 1;
	}
	@Override
	public abstract void create();
	public void resize(int width, int height){
		InputManager.ppux = width/properties.viewsize.x;
		InputManager.ppuy = height/properties.viewsize.y;
	}
	public abstract void gotoScreen(int screen);
	public void setScreen(Screen screen){
		while(multiplexer.size() != 0)
			multiplexer.removeProcessor(0);
		multiplexer.addProcessor(input);
		if(GameMain.screen != null)
			GameMain.screen.dispose();
		GameMain.screen = screen;
		super.setScreen(screen);
	}
	
	public void dispose(){
		screen.dispose();
		if(atlas!=null)
			atlas.dispose();
		if(screenLoadingBg != null)
			screenLoadingBg.dispose();
		Gdx.input.setInputProcessor(null);
	}
}

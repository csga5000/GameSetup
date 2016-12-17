 package com.hobogames.GameSetup;

import com.badlogic.gdx.math.Vector2;

public class GameProperties {
	/**
	 * Title of application.  On PC appears on title bar.
	 */
	public String title = "Hobogame";
	/**
	 * The directory to the texture atlas .atlas file
	 */
	public String atlas = "data/graphics/images.atlas";
	/**
	 * Directory of audio files.  
	 */
	public String audiodir = "audio/";
	/**
	 * Must be called if you use the com.hobogames.GameSetup.ScreenLoading.  This texture belongs directly in the assets directory, and loaded first as Texture GameMain.loadingScreenBg.
	 */
	public String loadingScreenBg = "";
	/**
	 * List of all audio files to be loaded as music objects
	 */
	public String musicFiles[] = {};
	/**
	 * The default viewport size for the camera used for all com.hobogames.GameSetup Screens
	 */
	public Vector2 viewsize = new Vector2(100,66);
	/**
	 * The gravity applied to all GameObjects that have the boolean falls set to true.
	 */
	public Vector2 gravity = new Vector2(0,0);
	/**
	 * The value for frictions slows all objects down.
	 */
	public float airfriction = 0;
	/**
	 * The default value for the friciton that occurs as two items slide against eachother.
	 */
	public float objfriction = 0;
	/**
	 * If true a grid, and all object colmasks are drawn.
	 */
	public boolean debug = false;
	/**
	 * The class used by the app to manage data.  DataManager.getInstance() will return an object of this class.
	 * MUST a child of DataMnaager
	 */
	public Class<?> dataManagerClass = null;
	/**
	 * The class used by the app to manage the games Audio.  AudioManager.getInstance() will return an object of this class
	 * MUST a child of AudioMnaager
	 */
	public Class<?> audioManagerClass = null;
	/**
	 * The Arguments used to construct a DataManager. 
	 * Used ONLY if you defined dataManagerClass.
	 * The arguments should be of the types used in a constructor for the class defined as dataManager.
	 */
	public Object[] dataManagerConstructorArgs = new Object[0];
	/**
	 * The Arguments used to construct a AudioManager. 
	 * Used ONLY if you defined audioManagerClass.
	 * The arguments should be of the types used in a constructor for the class defined as audioManager.
	 */
	public Object[] audioManagerConstructorArgs = new Object[0];
}

package com.hobogames.gamesetup;

import java.lang.reflect.InvocationTargetException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class DataManager {
	private static DataManager instance;
	/**
	 * Only one instance of AudioManager(or one of it's subclasses) is allowed to exist
	 * @return boolean for it DataManager is instantiated
	 */
	public static boolean isInstantiated(){
		return instance!=null;
	}
	/**
	 * 
	 * @return The instance of DataManager
	 */
	public static DataManager getInstance(){
		return getInstance(GameMain.properties.dataManagerConstructorArgs);
	}
	/**
	 * Only one instance of DataManager is allowed to exist per instance of the game, that instance is retrieved via the getInstance() method, which creates an instance of the type retrieved from
	 * NOTE: Parameters are only neccessary if:
	 *     - an instance of DataManager does not already exist.
	 *     - you defined GameProperties.dataManagerClass
	 *     
	 * @param args The arguments to be used to construct an Object of the type defined in GameProperties   
	 * @return The current instance of DataManager
	 * @see #getInstance()
	 * @see GameProperties#dataManagerClass
	 */
	public static DataManager getInstance(Object args[]){
		if(instance == null){
			Class<?> type = GameMain.properties.dataManagerClass;
			if(type != null){
				try {
					Class<?> types[] = new Class<?>[args.length];
					for(int i = 0; i < args.length; i++)
						types[i] = args[i].getClass();
					Object obj = type.getConstructor(types).newInstance(args);
					if(!DataManager.class.isInstance(obj))
						throw new Exception("Invalid class");
					else
						instance = (DataManager)obj;
				}catch(NoSuchMethodException e){
					String msg = "Warning!  The class " + type + " does not have an accessable constructor that takes the parameters ";
					if(args.length > 0){
						msg += args[0] + " in DataManager.getInstance(Object args[])";
						for(Object a : args)
							msg += ", " + a;
					}else
						msg +=" given(none)";
					msg += ".  Instance of DataManager will be created instead.";
					System.out.println(msg);
				}catch(SecurityException e){
					String msg = "Warning! A security exception occured created class " + type + " in DataManager.getinstance(Object args[])";
					System.out.println(msg);
				}catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					String msg = "Warning! Invalid arguments " + args[0];
					for(Object a : args)
						msg += ", " + a;
					msg += " to construct " + type + " in DataManager.getInstance(Ojbect args[]).  A instance of DataManager will be created instead.";
					System.out.println(msg);
				}catch(Exception e){
					if(e.getMessage() == null)
						e.printStackTrace();
					else
						System.out.println("Warning!  The class " + type + " is not a child of DataManager!  A instance of DataManager will be created instead.");
						
				}
			}
			if(instance == null)
				instance = new DataManager();
		}
		return instance;
	}
	public static final String KEY_PLAYED = "played";
	public static final String KEY_GEMS = "gems";
	public static final String KEY_CONTROL = "ctrl";
	public static final String KEY_FNT = "fnt";
	public static final String KEY_VOLUME = "vol";
	public static final String KEY_VERSION = "version";
	
	public static final int BUTTONS = 0;
	public static final int TOUCH = 1;
	
	public boolean FIRST_TIME = false;
	public float VERSION = 1.2f;
	public float LAST_VERSION = VERSION;
	public int CONTROL_METHOD = BUTTONS;
	public String FONT = "Christy";
	public float AUDIO_VOLUME = 1;
	protected Preferences prefs;
	public void init(){
		prefs = Gdx.app.getPreferences("GreenCubePrefs");
		FIRST_TIME = !prefs.getBoolean("played");
		if(FIRST_TIME){
			reset();
		}else{
			retrievePreferences();
		}
		put();
		prefs.flush();
	}
	/**
	 * If overridden call super.reset() at end of method to flush properly
	 */
	public void reset(){
		FIRST_TIME = false;
		VERSION = 1.2f;
		LAST_VERSION = VERSION;
		CONTROL_METHOD = BUTTONS;
		FONT = "Christy";
		AUDIO_VOLUME = 1;
		prefs.flush();
	}
	/**
	 * Stores that values from prefs to DataManager feilds
	 */
	public void retrievePreferences(){
		LAST_VERSION = prefs.getFloat(KEY_VERSION);
		CONTROL_METHOD = prefs.getInteger(KEY_CONTROL);
		FONT = prefs.getString(KEY_FNT);
		AUDIO_VOLUME = prefs.getFloat(KEY_VOLUME);
	}
	/**
	 * When called saves the current preference values to Prefs.  If overriden calling super.put() is advised  
	 */
	public void put(){
		prefs.putBoolean(KEY_PLAYED, true);
		prefs.putFloat(KEY_VERSION, VERSION);
		prefs.putInteger(KEY_CONTROL,CONTROL_METHOD);
		prefs.putString(KEY_FNT,FONT);
		prefs.putFloat(KEY_VOLUME, AUDIO_VOLUME);
	}

	public void flush(){
		prefs.flush();
	}
	protected DataManager(){
	}
}

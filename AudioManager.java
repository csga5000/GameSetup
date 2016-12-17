package com.hobogames.gamesetup;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;

/**
 * Access AudioManager by the getInstance() method
 * @author Chase Anderson
 */
public class AudioManager{
	private static AudioManager instance;
	/**
	 * Override this method if you wish to use your own AudioManager
	 * @return The child of AudioManager to be instantiated
	 */
	protected static Class<?> getAudioManagerClass(){
		return null;
	}
	/**
	 * Only one instance of AudioManager(or one of it's subclasses) is allowed to exist
	 * @return boolean for if AudioManager has been instantiated
	 */
	public static boolean isInstantiated(){
		return instance!=null;
	}
	/**
	 * Only 
	 * @return The instance of AudioManager
	 */
	public static AudioManager getInstance(){
		return getInstance(GameMain.properties.audioManagerConstructorArgs);
	}
	/**
	 * Only one instance of AudioManager is allowed to exist per instance of the game, that instance is retrieved via the getInstance() method, which creates an instance of the type retrieved from
	 * NOTE: Parameters are only neccessary if:
	 *     - an instance of AudioManager does not already exist.
	 *     - or if there are no parameters(default).   
	 *     
	 * @param args The arguments to be used to construct an Object of the type returned from getAudioManagerClass().   
	 * @return The current instance of AudioManager
	 * @see #getInstance()
	 * @see #getAudioManagerClass()
	 */
	public static AudioManager getInstance(Object args[]){
		if(instance == null){
			Class<?> type = getAudioManagerClass();
			if(type != null){
				try {
					Class<?> types[] = new Class<?>[args.length];
					for(int i = 0; i < args.length; i++)
						types[i] = args[i].getClass();
					type.getConstructor(types).newInstance(args);
					
				}catch(NoSuchMethodException e){
					String msg = "Warning!  The class " + type + " does not have a constructor that takes the parameters " + args[0] + " in AudioManager.getInstance(Object args[])";
					for(Object a : args)
						msg += ", " + a;
					msg += ".  Instance of AudioManager will be created instead.";
					System.out.println(msg);
				}catch(SecurityException e){
					String msg = "Warning! A security exception occured created class " + type + " in AudioManager.getinstance(Object args[])";
					System.out.println(msg);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException| InvocationTargetException e) {
					String msg = "Warning! Invalid arguments " + args[0];
					for(Object a : args)
						msg += ", " + a;
					msg += " to construct " + type + " in AudioManager.getInstance(Ojbect args[]).  A instance of AudioManager will be created instead.";
					System.out.println(msg);
				}catch (Exception e){
					System.out.println("Warning!  The class " + type + " is not a child of DataManager!  A instance of DataManager will be created instead");
				}
			}
			if(instance == null)
				instance = new AudioManager();
		}
		return instance;
	}
	private Music songs[];
	private int current = -1;
	private ArrayList<Integer> prevSongs;
	private final String names[] = GameMain.properties.musicFiles;
	
	public int que(AssetManager man){
		for(int i = 0; i < names.length; i++){
			man.load(GameMain.properties.audiodir + names[i], Music.class);
		}
		return names.length;
	}
	
	public void init(AssetManager man){
		songs = new Music[names.length];
		for(int i = 0; i < songs.length; i++){
			if(man != null){
				songs[i] = (Music) man.get("audio/" + names[i]);
			}else{
				songs[i] = Gdx.audio.newMusic(Gdx.files.internal("audio/" + names[i]));
			}
		}
		prevSongs = new ArrayList<Integer>();
	}
	public void start(){
		if(current != -1)
			resume();
		else
			playNextSong();
	}
	public void updateSettings(){
		songs[current].setVolume(DataManager.getInstance().AUDIO_VOLUME);
	}
	public void playNextSong(){
		int oc = current;
		if(songs.length == 0)
			return;
		if(current == -1)
			current = GameMain.random.nextInt(songs.length);
		else{
			prevSongs.add(oc);
			if(prevSongs.size() == songs.length)
				prevSongs.clear();
			current = GameMain.random.nextInt(songs.length-prevSongs.size());
			for(int i : prevSongs)
				if(current >= i)
					current++;
		}
		songs[current].setVolume(DataManager.getInstance().AUDIO_VOLUME);
		songs[current].setLooping(false);
		songs[current].setOnCompletionListener(new OnCompletionListener(){
			@Override
			public void onCompletion(Music music) {
				playNextSong();
			}
		});
		songs[current].play();
	}
	public void stop(){
		songs[current].stop();
	}
	public void resume(){
		songs[current].play();
	}
	public void dispose(){
		for(Music a : songs)
			a.dispose();
	}
	
	/**
	 * Subclasses must have public constructors!  For the subclass to be used please override getAudioManagerClass()
	 * @see #getAudioManagerClass()
	 */
	protected AudioManager(){}
}
package com.hobogames.GameSetup.game3D;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;


public class ModelObject {
	public static String modelsFolder = "/data/models";
	public static Map<String, ModelType> types = new HashMap<String,ModelType>();
	
	ModelInstance model;
	int ID;
	Vector3 velocity;
	
	BoundingBox box;
	boolean solid = true, real = true; 
	ScreenGame3D screen;
	public static void que(AssetManager man){
		FileHandle file = Gdx.files.internal(modelsFolder);
		FileHandle files[] = file.list();
		for(FileHandle a : files){
			man.load(a.path(), Model.class);
			types.put(a.path(),null);
		}
	}
	public static void load(AssetManager man){
		for(String a : types.keySet()){
			types.put(a,new ModelType(man.get(a,Model.class)));
		}
	}
	public static void dispose(){
		for(String a : types.keySet()){
			types.get(a).model.dispose();
		}
	}
	
	/**
	 * Calls ModelObject(String name, pos.x, pos.y, pos.z)
	 * @param modelName The name of the model.  (Do not include extension or path)
	 * @param pos Position of object
	 * @see #ModelObject(String, float, float, float)
	 */
	public ModelObject(ScreenGame3D screen, String modelName, Vector3 pos){
		this(screen,modelName,pos.x,pos.y,pos.z);
	}
	/**
	 * Calls ModelObject(String name, pos.x, pos.y, pos.z)
	 * @param modelName The name of the model.  (Do not include extension or path)
	 * @param pos Position of object
	 * @see #ModelObject(String, float, float, float)
	 */
	public ModelObject(ScreenGame3D screen, String modelName, float x, float y, float z){
		this.screen = screen;
		modelName = modelsFolder + "/" + modelName + ".g3db";
		model = new ModelInstance(types.get(modelName).model);
		box = types.get(modelName).size;
		model.transform.setTranslation(x,y,z);
		//TODO:Handle rotation
	}
	public ModelObject setScale(float scale){
		return setScale(scale,scale,scale);
	}
	public ModelObject setScale(float x, float y, float z){
		model.transform.scale(x, y, z);
		return this;
	}
	public void update(float delta){
		move(delta);
	}
	@SuppressWarnings("unused")
	public void move(float delta){
		for(ModelObject a : screen.objects3D){
			//TODO: COllisions
		}
		model.transform.translate(velocity.x*delta, velocity.y*delta, velocity.z*delta);
	}
}

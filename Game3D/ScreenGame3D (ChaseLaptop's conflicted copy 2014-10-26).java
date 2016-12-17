package com.hobogames.gamesetup.game3D;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.hobogames.gamesetup.GameMain;
import com.hobogames.gamesetup.GameObject;
import com.hobogames.gamesetup.ScreenGame;
import com.hobogames.gamesetup.TodoAction;


public abstract class ScreenGame3D extends ScreenGame{
	public ArrayList<ModelObject> objects3D = new ArrayList<ModelObject>();
	
	public Environment environment;
	public PerspectiveCamera camera3D;
	public CameraInputController camControl;
	private ModelBatch modelBatch;
	
	public ScreenGame3D(){
		modelBatch = new ModelBatch();
		
		//EnvironmentSetup//
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight,.4f,.4f,.4f,1f));
		environment.add(new DirectionalLight().set(.8f,.8f,.8f,1f,-.8f,-.2f));
		
		camera3D = new PerspectiveCamera(67,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		camera3D.position.set(10,0,20);
		camera3D.lookAt(0,0,0);
		camera3D.near = 1f;
		camera3D.far = 300f;
		camera3D.update();
		
		clear = false;
		backgrounds = null;
		camControl = new CameraInputController(camera3D);
		GameMain.multiplexer.addProcessor(camControl);
	}
	public void render(float delta){
		//TODO: New actions
		render3D(delta);
		super.render(delta);
	}
	public void render3D(float delta){
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		float xtrans = 0;
		float ztrans = 0;
		double angle = Math.atan2(camera3D.direction.z, camera3D.direction.x);
		angle += Math.PI/2;
		float strafe = 0;
		
		///TODO:Adjust values
		
		xtrans+=Math.cos(angle)*strafe;
		ztrans+=Math.sin(angle)*strafe;
		
		///TODO: Update those who request it.
		
		float dback = 20;
		//cam.position.sub(dback*cam.direction.x,dback*cam.direction.y-5f,dback*cam.direction.z);
		camera3D.update();
		
		modelBatch.begin(camera3D);
			for(ModelObject a : objects3D){
				modelBatch.render(a.model,environment);
			}
			modelBatch.end();
	}
	public void handleAction(TodoAction act){
		if(act.obj instanceof GameObject){
			super.handleAction(act);
		}else if(act.obj instanceof ModelObject){
			switch(act.type){
			case TodoAction.ADD_OBJECT:
				objects3D.add((ModelObject)act.obj);
				break;
			case TodoAction.REMOVE_OBJECT:
				objects3D.add((ModelObject)act.obj);
				break;
			}
		}
	}
}

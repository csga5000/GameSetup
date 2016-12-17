package com.hobogames.GameSetup.game3D;

public class TodoAction3D {
	public static final int ADD_OBJECT = 0;
	public static final int REMOVE_OBJECT = 1;
	public int type;
	public ModelObject obj;
	public TodoAction3D(int type, ModelObject obj){
		this.type = type;
		this.obj = obj;
	}
}

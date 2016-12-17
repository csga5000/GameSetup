package com.hobogames.GameSetup;

public class TodoAction{
	public static final int ADD_OBJECT = 0;
	public static final int REMOVE_OBJECT = 1;
	public static final int UPDATE_DEPTH = 2;
	//public static final int LOAD_LEVEL = 3;
	public int type;
	public Object obj;
	//public int pack, level;
	public TodoAction(int type, Object obj){
		this.type = type;
		this.obj = obj;
	}
	/*public TodoAction(int pack, int level){
		type = LOAD_LEVEL;
		this.pack = pack;
		this.level = level;
	}*/
}
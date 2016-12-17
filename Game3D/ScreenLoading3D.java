package com.hobogames.gamesetup.game3D;

import com.hobogames.gamesetup.ScreenLoading;

public abstract class ScreenLoading3D extends ScreenLoading{
	public void start(){
		super.start();
		ModelObject.que(man);
	}
	public void onLoad(){
		super.onLoad();
		ModelObject.load(man);
	}
}

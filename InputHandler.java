package com.hobogames.gamesetup;

public interface InputHandler {
	public void positionUpdate(float mx, float my);
	public void press(float mx, float my, int pointer);
	public void release(float mx, float my, int pointer);
	public void tap(float mx, float my, int count);
	public void type(int key);
}

package com.hobogames.gamesetup;

import com.badlogic.gdx.math.Rectangle;



public interface RecievesClick {
	public boolean trackingPointer(int pointer);
	/**
	 * Called by InputManager when taps occur within the Rectangle given by getClickBounds().
	 * @param mx The screen x coordinate in game units
	 * @param my The screen y coordinate in game units
	 * @param times The number of taps
	 */
	public void press(float mx, float my, int pointer);
	public void release(float mx, float my, int pointer);
	public void tap(float mx, float my, int times);
	public void longPress(float mx, float my);
	/**
	 * Used by InputManager when testing if this listener should receive a touch.
	 * @return The rectangle on the screen in which this object receives touch
	 */
	public abstract Rectangle getClickBounds();
}

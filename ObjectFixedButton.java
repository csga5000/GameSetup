package com.hobogames.GameSetup;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;

public class ObjectFixedButton extends ObjectFixed implements RecievesClick{
	public static final int DEFAULT_IMAGE_INDEX = 0;
	public static final int PRESSED_IMAGE_INDEX = 1;
	private ArrayList<Integer> presses = new ArrayList<Integer>();
	public ObjectFixedButton(ScreenGame game, String[] texs, float x, float y, float width, float height) {
		super(game, texs, x, y, width, height);
		GameMain.input.addClickable(this);
	}
	public boolean isPressed(){
		return presses.size() > 0;
			
	}
	@Override
	public Rectangle getClickBounds() {
		return new Rectangle(getCLeft(),getCBottom(),colmask.width,colmask.height);
	}
	@Override
	public boolean trackingPointer(int pointer){
		return presses.contains((Integer)pointer);
	}
	@Override
	public void press(float mx, float my, int pointer) {
		presses.add(pointer);
		if(regions.length > 1)texindex = PRESSED_IMAGE_INDEX;
	}
	@Override
	public void release(float mx, float my, int pointer) {
		if(presses.contains(pointer))
			presses.remove((Integer)pointer);
		if(!isPressed())
			texindex = DEFAULT_IMAGE_INDEX;
	}
	@Override public void tap(float mx, float my, int times){}
	@Override public void longPress(float mx, float my){}
}

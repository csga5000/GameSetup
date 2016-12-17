package com.hobogames.GameSetup;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class InputManager implements InputProcessor, GestureListener{
	public ArrayList<RecievesClick> clickchecks;
	public ArrayList<InputHandler> handlers;
	public static float keytimes[] = new float[512];
	public static float mousex = -1, mousey = -1;
	
	public static float ppux, ppuy;
	
	public static Vector2 pointers[] = new Vector2[8];
	public InputManager(float ppux, float ppuy){
		InputManager.ppux = ppux;
		InputManager.ppuy = ppuy;
		clickchecks = new ArrayList<RecievesClick>();
		handlers = new ArrayList<InputHandler>();
	}
	public void clearListeners(){
		handlers.clear();
		clickchecks.clear();
	}
	public void addHandler(InputHandler handler){
		handlers.add(handler);
		handler.positionUpdate(mousex, mousey);
	}
	public void addClickable(RecievesClick handler){
		clickchecks.add(handler);
	}
	/*public void notifyListeners(){
		if(input[0]){
			for(InputHandler a : handlers)
				a.isPressed(mousex,mousey);
		}
	}*/
	@Override
	public boolean tap(float x, float y, int count, int button) {
		try{
			for(InputHandler handler : handlers)
				handler.tap(convertMouseX(x),convertMouseY(y),count);
			for(RecievesClick click : clickchecks){
				Rectangle a = click.getClickBounds();
				if(a.contains(x, y)){
					click.tap(x-a.x, y-a.x, count);
				}
			}
		}catch(ConcurrentModificationException e){}
		return false;
	}
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button){
		mousex = convertMouseX(screenX);
		mousey = convertMouseY(screenY);
		if(pointer < pointers.length)
			pointers[pointer] = new Vector2(mousex,mousey);
		try{
			for(InputHandler a : handlers)
				a.press(mousex, mousey, pointer);
			for(RecievesClick click : clickchecks){
				Rectangle a = click.getClickBounds();
				if(a.contains(mousex, mousey)){
					click.press(mousex-a.x, mousey-a.x,pointer);
				}
			}
		}catch(ConcurrentModificationException e){}
		return false;
	}
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		mousex = convertMouseX(screenX);
		mousey = convertMouseY(screenY);
		if(pointer < pointers.length)
			pointers[pointer] = null;
		try{
			for(InputHandler a : handlers)
				a.release(mousex, mousey, pointer);
			for(RecievesClick click : clickchecks){
				Rectangle a = click.getClickBounds();
				if(a.contains(mousex, mousey)){
					click.release(mousex-a.x, mousey-a.x, pointer);
				}
			}
		}catch(ConcurrentModificationException e){}
		return false;
	}
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		mousex = convertMouseX(screenX);
		mousey = convertMouseY(screenY);
		try{
			for(InputHandler handler : handlers)
				handler.positionUpdate(mousex,mousey);
			for(RecievesClick click : clickchecks){
				Rectangle a = click.getClickBounds();
				if(click.trackingPointer(pointer)){
					if(!a.contains(mousex, mousey)){
						click.release(mousex, mousey, pointer);
					}
				}else{
					if(a.contains(mousex, mousey)){
						click.press(mousex, mousey, pointer);
					}
				}
			}
		}catch(ConcurrentModificationException e){}
		if(pointer < pointers.length)
			pointers[pointer] = new Vector2(mousex,mousey);
		return false;
	}
	@Override
	public boolean longPress(float x, float y) {
		try{
			for(RecievesClick click : clickchecks){
				Rectangle a = click.getClickBounds();
				if(a.contains(x, y))
					click.longPress(x-a.x, y-a.x);
			}
		}catch(ConcurrentModificationException e){}
		return false;
	}
	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}
	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		return false;
	}
	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}
	@Override
	public boolean zoom(float initialDistance, float distance) {
		return false;
	}
	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		return false;
	}
	
	////Key Input Methods////
	@Override
	public boolean keyDown(int keycode) {
		if(keycode < GameMain.keys.length){
			GameMain.keys[keycode] = true;
			keytimes[keycode] = ScreenGame.time;
		}
		return false;
	}
	@Override
	public boolean keyUp(int keycode) {
		try{
			if(keycode < GameMain.keys.length){
				GameMain.keys[keycode] = false;
				if(ScreenGame.time-keytimes[keycode] <= 1){
					for(InputHandler handler : handlers)
						handler.type(keycode);
				}
				keytimes[keycode] = 0;
			}
		}catch(ConcurrentModificationException e){}
		return false;
	}
	@Override
	public boolean keyTyped(char character) {
		return false;
	}
	
	////Mouse Exclusive Methods////
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		mousex = convertMouseX(screenX);
		mousey = convertMouseY(screenY);
		try{
			for(InputHandler handler : handlers)
				handler.positionUpdate(mousex,mousey);
		}catch(ConcurrentModificationException e){}
		return false;
	}
	
	////Converter Methods////
	public float convertMouseX(float screenX){
		return screenX/ppux;
	}
	public float convertMouseY(float screenY){
		return ((Gdx.graphics.getHeight()-screenY)*1f)/ppuy;
	}
	
	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {//Not used because redundant
		return false;
	}
}

package com.hobogames.GameSetup;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Creates an object that has a fixed position on the screen.  This means that it ignores the camera's position, and draws at it's location relative to the bottom left corner of the screen 
 * @author Chase Anderson
 */
public class ObjectFixed extends GameObject{
	
	public ObjectFixed(ScreenGame game, float x, float y, float width, float height){
		super(game,x,y,width,height);
		init(x,y);
	}
	/**
	 * Create
	 * @param game The screen this object belongs to
	 * @param texs The array of images that graphically represent object
	 * @param x the xcoord
	 * @param y the ycoord
	 * @param width the width
	 * @param height the height
	 */
	public ObjectFixed(ScreenGame game, String texs[], float x, float y, float width, float height) {
		super(game, texs, x, y, width, height);
		init(x,y);
	}
	
	private void init(float x, float y){
		falls = mobile = solid = tangible = false;
		depth = 100;
		this.setCentered(x, y);
	}
	
	public void drawTex(SpriteBatch batch){
		batch.draw(regions[texindex], 
				bounds.getX()+game.camera.position.x-game.getViewWidth()/2,
				bounds.getY()+game.camera.position.y-game.getViewHeight()/2,
				center.x, center.y, 		//Origin x, y
				bounds.width, bounds.height, 
				1, 1, 						//Scale x, y
				angle);					//Rotation angle, clockwise
	}
}

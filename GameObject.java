package com.hobogames.GameSetup;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class GameObject {
	public static Vector2 gravity = GameMain.properties.gravity;
	public float airfriction = GameMain.properties.airfriction;
	
	private Float alarms[] = new Float[10];
	
	public static final int NO_CHECK = 0;
	public static final int CHECK_TANGIBLE_AND_NOT_SOLID = 1;
	public static final int CHECK_TANGIBLE = 2;
	public static final int CHECK_SOLID = 3;
	public static final int CHECK_ALL = 4;
	
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int UP = 2;
	public static final int DOWN = 3;
	public int checking = CHECK_SOLID;
	public boolean mobile = true, solid, tangible, falls, autorotate;
	public float friction, gravmult = 1;
	public Vector2 speeds;
	public Vector2 center;
	public float angle;
	public int depth = 0;//any number, lower numbers drawn below higher numbers, if reorganize is called in ScreenGame.
	
	protected boolean drawColmask = false;
	protected boolean applyGroundFric;
	@Deprecated
	protected boolean autoGuessRotatedRectCol = false;//For angled objects
	protected TextureRegion regions[];
	protected int texindex = 0;
	protected float texspf = 0;//Seconds per frame
	protected float textime = 0;
	protected ScreenGame game;
	protected float reboundfactor = .0001f;
	protected Rectangle bounds;
	protected Rectangle colmask;
	
	/**
	 * Creates a Game Object
	 * @param game The screen this object belongs to
	 * @param texs The array of images that graphically represent object
	 * @param x the xcoord
	 * @param y the ycoord
	 * @param width the width
	 * @param height the height
	 */
	public GameObject(ScreenGame game, TextureRegion texs[], float x, float y, float width, float height){
		this.game = game;
		regions = texs;
		for(int i = 0; i < alarms.length;i++)
			alarms[i] = -9f;
		bounds = new Rectangle(0,0,width,height);
		colmask = new Rectangle(0,0,width,height);
		tangible = falls = solid = true;
		friction = GameMain.properties.objfriction;
		speeds = new Vector2(0,0);
		center = new Vector2(width/2f,height/2f);
		setCentered(x,y);
		if(gravity.len2()>0)
			applyGroundFric = true;

		angle = 0;
	}
	/**
	 * Converts the String[] texs to a TextureRegion[], then calls GameObject(ScreenGame game, TextureRegion texs[], float x, float y, float width, float height)
	 * @param game The screen this object belongs to
	 * @param texs The array of String filenames for Textures in the atlas 
	 * @param x the xcoord
	 * @param y the ycoord
	 * @param width the width
	 * @param height the height
	 * @see #GameObject(ScreenGame, TextureRegion, float, float, float, float)
	 */
	public GameObject(ScreenGame game, String texs[], float x, float y, float width, float height){
		this(game,getTexsFromStrings(texs),x,y,width,height);
	}
	/**
	 * Creates new TextureRegion of length 0, then calls constructor GameObject(ScreenGame game, TextureRegion texs[], float x, float y, float width, float height){
	 * @param game The screen this object belongs to
	 * @param x the xcoord
	 * @param y the ycoord
	 * @param width the width
	 * @param height the height
	 * @see #GameObject(ScreenGame, TextureRegion, float, float, float, float)
	 */
	public GameObject(ScreenGame game, float x, float y, float width, float height){
		this(game,new TextureRegion[0],x,y,width,height);
	}
	public static TextureRegion[] getTexsFromStrings(String texs[]){
		TextureRegion converted[] = new TextureRegion[texs.length];
		for(int i = 0; i <  texs.length; i++)
			converted[i] = GameMain.atlas.findRegion(texs[i]);
		return converted;
	}
	
	////Colmask locations////
	public float getCLeft(){
		return bounds.getX()+colmask.getX();
	}
	public float getCRight(){
		return bounds.getX()+colmask.getX()+colmask.getWidth();
	}
	public float getCTop(){
		return bounds.getY()+colmask.getY()+colmask.getHeight();
	}
	public float getCBottom(){
		return bounds.getY()+colmask.getY();
	}
	////Graphics bounds locations////
	public float getBLeft(){
		return bounds.getX();
	}
	public float getBRight(){
		return bounds.getX()+bounds.getWidth();
	}
	public float getBTop(){
		return bounds.getY()+bounds.getHeight();
	}
	public float getBBottom(){
		return bounds.getY();
	}
	////Center/Origin////
	public float getCenterX(){
		return getBLeft()+center.x;
	}
	public float getCenterY(){
		return getBBottom()+center.y;
	}
	public void setCentered(float x, float y){
		bounds.x = x-center.x;
		bounds.y = y-center.y;
	}
	public void adjustCenter(float cx, float cy, boolean shift){
		bounds.x += cx-center.x;
		bounds.y += cy-center.y;
		center.x = cx;
		center.y = cy;
	}
	////Colmask setters////
	public void setSquareCol(float r){
		colmask.x = center.x-r;
		colmask.y = center.y-r;
		colmask.width = 2*r;
		colmask.height = 2*r;
	}
	
	////Trig setters/getters////
	public void setSpeedAndAngle(float speed, boolean sRelative, float angle, boolean aRelative){
		setSpeed(speed,sRelative);
		setAngle(angle,aRelative);
	}
	public void setSpeed(float speed, boolean relative){
		float angle = getAngle();
		if(relative)
			speed+=getSpeed();
		speeds.x = (float) (Math.cos(Math.toRadians(angle))*speed);
		speeds.y = (float) (Math.sin(Math.toRadians(angle))*speed);
	}
	public void setAngle(float angle, boolean relative){
		float speed = getSpeed();
		if(relative)
			angle+=getAngle();
		speeds.x = (float) (Math.cos(Math.toRadians(angle))*speed);
		speeds.y = (float) (Math.sin(Math.toRadians(angle))*speed);
	}
	public float getAngle(){
		return (float) Math.atan2(speeds.y, speeds.x);
	}
	public float getSpeed(){
		return (float) Math.sqrt(Math.pow(speeds.x,2)+Math.pow(speeds.y,2));
	}
	////Move Methods////
	public void moveTowardsPoint(float x, float y, float speed){
		float angle = angleTo(x,y);
		setSpeedAndAngle(speed,false,angle,false);
	}
	public float angleTo(float x, float y){
		return (float) Math.toDegrees(Math.atan2(y-getCenterY(), x-getCenterX()));
	}
	public float angleTo(GameObject a){
		return angleTo(a.getCenterX(),a.getCenterY());
	}
	public float getDistance(GameObject object){
		return (float) (Math.sqrt(Math.pow(getCenterX()-object.getCenterX(),2)+Math.pow(getCenterY()-object.getCenterY(),2)));
	}
	/**
	 * Called from ScreenGame on all objects.  Calls update(float), applyGravity(float), move(float), and applies friction, and updates the animation
	 * @param delta The amount of time passed since last call
	 * @see #update(float)
	 * @see #applyGravity(float)
	 * @see #move(float)
	 * @see #applyFriction(float, float)
	 * @see #animate(float)
	 */
	public void tic(float delta){
		for(int i = 0; i < alarms.length; i++){
			if(alarms[i] != -9){
				alarms[i] -= delta;
				if(alarms[i] <= 0){
					alarms[i] = -9f;
					alarm(i);
				}
			}
		}
		update(delta);
		if(mobile){
			//if(autoGuessRotatedRectCol)
				//setGuessedCol(.8f);
			if(falls)
				applyGravity(delta);
			move(delta);
			applyFriction(delta,airfriction);
			if(applyGroundFric){
				float groundfric = 0;
				int num = 0;
				for(GameObject a : cols(game,this,0,-reboundfactor*100,CHECK_TANGIBLE)){
					num++;
					groundfric += a.friction;
				}
				if(num > 0 && groundfric != 0)
					applyFriction(delta,groundfric/num);
			}
		}
		animate(delta);
	}
	/**
	 * Just sets the float in the alarms array at the given index, where it will automatically count down to 0 or less, at which times alarm(int alarm) will be called
	 * @param alarm The index of the alarm.  Cannot be greater than 9, or less than 0, or arrayIndexOutOfBounds exception will be thrown.
	 * @param time The time until the alarm() is called.
	 */
	public void setAlarm(int alarm, float time){
		alarms[alarm] = time;
	}
	/**
	 * Called when an alarm's time reaches 0
	 * @param alarm The alarm id that was fired
	 */
	public void alarm(int alarm){
		
	}
	@Deprecated
	@SuppressWarnings("unused")
	public void setGuessedCol(float bufferPortion){
		//TODO:This
		//Distances from size of colmask to center
		float leftWidth = getCenterX()-getCLeft();
		float leftHeight = getCenterY()-getCBottom();
		float rightWidth = getCRight()-getCenterX();
		float rightHeight = getCTop()-getCenterX();
		
	}
	public void applyFriction(float delta, float friction){
		if(speeds.x > 0)
			speeds.x = Math.max(0,speeds.x-friction*delta);
		else if(speeds.x < 0){
			speeds.x = Math.min(0, speeds.x+friction*delta);
		}
		if(speeds.y > 0)
			speeds.y = Math.max(0,speeds.y-friction*delta);
		else if(speeds.y < 0){
			speeds.y = Math.min(0, speeds.y+friction*delta);
		}
	}
	public void applyGravity(float delta){
		speeds.add(gravity.x*delta*gravmult,gravity.y*delta*gravmult);
	}
	ArrayList<GameObject> cols;
	protected void moveVertically(float sp){
		cols = cols(game,this,0,sp,checking);
		if(cols.size() == 0){
			bounds.y+=sp;
		}else{
			speeds.y = 0;
			boolean down = sp <= 0;
			if (Math.abs(sp) < 0.1f) {//At these slow speeds, it's more reliable to go off of relative position to object, since it's unlikely we "passed through" it
				down = cols.get(0).getCenterY() < getCenterY();
			}
			for(GameObject obj : cols){
				if(down){
					bounds.y = Math.max(bounds.y, obj.getCTop()-colmask.getY()+reboundfactor);
				}
				else{
					bounds.y = Math.min(bounds.y, obj.getCBottom()-colmask.getY()-colmask.getHeight()-reboundfactor);
				}
			}
		}
	}
	protected void moveHorizontally(float sp){
		cols = cols(game,this,sp,0,checking);
		if(cols.size() == 0){
			bounds.x+=sp;
		}else{
			speeds.x = 0;
			if(sp < 0){
				for(GameObject obj : cols){
					bounds.x = Math.max(bounds.x, obj.getCRight()-colmask.getX()+reboundfactor);
				}
			}else{
				for(GameObject obj : cols){
					bounds.x = Math.min(bounds.x, obj.getCLeft()-colmask.getX()-colmask.getWidth()-reboundfactor);
				}
			}
		}
	}
	public void move(float delta){
		float mspy = speeds.y*delta;
		float mspx = speeds.x*delta;
		if(Math.abs(mspx) > Math.abs(mspy)){
			moveHorizontally(mspx);
			moveVertically(mspy);
		}else{
			moveVertically(mspy);
			moveHorizontally(mspx);
		}
	}
	public void animate(float delta){
		if(texspf <= 0)
			return;
		if(regions.length > 1){
			textime+=delta;
			texindex = (int) Math.floor(textime/texspf);
			if(texindex > regions.length-1){
				texindex = 0;
				textime-=texspf*regions.length;
			}
		}
	}
	/**
	 * Does nothing.  Called when object updates.  Meant to be over-ridden by subclasses
	 * @param delta Amount of Time pass
	 */
	public void update(float delta){
		
	}
	/**
	 * The default draw method for GameObjects
	 * @param batch The ScreenGame's SpriteBatch that is used to draw objects onto the screen. By default batch.being() has be called.
	 * @param renderer The ScreenGame's ShapeRender that can be used to draw shapes.  Not started.
	 */
	public void draw(SpriteBatch batch, ShapeRenderer renderer){
		drawTex(batch);
		if(drawColmask)
			drawColmask(batch,renderer);
	}
	/**
	 * Draws a Line Rectangle that shows the bounds of colmask
	 * @param batch The ScreenGame's SpriteBatch that is used to draw objects onto the screen. By default batch.being() has be called.
	 * @param renderer The ScreenGame's ShapeRender that can be used to draw shapes.  Not started.
	 * @see #colmask
	 */
	public void drawColmask(SpriteBatch batch, ShapeRenderer renderer){
		batch.end();
		renderer.begin(ShapeType.Line);
			renderer.setColor(1,0,0,1);
			renderer.rect(getCLeft(),getCBottom(), colmask.width,colmask.height);
		renderer.end();
		batch.begin();
	}
	public void drawTex(SpriteBatch batch){
		batch.draw(regions[texindex], 
				bounds.getX(), bounds.getY(), 
				center.x, center.y, 		//Origin x, y
				bounds.width, bounds.height, 
				1, 1, 						//Scale x, y
				angle);					//Rotation angle, clockwise
	}
	/*public ArrayList<GameObject> getTouching(){
		ArrayList<GameObject> objs = new ArrayList<GameObject>();
		for(GameObject a : game.objects){
			if(col(a,this,0,0))
				objs.add(a);
		}
		return objs;
	}*/
	public static ArrayList<GameObject> touching(ScreenGame game, GameObject a){
		return touching(game, a, NO_CHECK);
	}
	public static ArrayList<GameObject> touching(ScreenGame game, GameObject a,int check){
		ArrayList<GameObject> touches = cols(game,a,0,0,check);
		/*for(int i = 0; i < touches.size();){
			if(touches.get(i).solid)
				touches.remove(i);
			else
				i++;
		}*/
		return touches;
	}
	public static ArrayList<GameObject> cols(ScreenGame game, GameObject a, float mx, float my, int check){
		ArrayList<GameObject> cols = new ArrayList<GameObject>();
		for(GameObject b : game.objects){
			if(a!=b)
				if(col(a,b,mx,my)){
					if((check == CHECK_ALL && b.solid && b.tangible) ||
							(check == CHECK_SOLID && b.solid) ||
							(check == CHECK_TANGIBLE && b.tangible) ||
							(check == CHECK_TANGIBLE_AND_NOT_SOLID && b.tangible && !b.solid)||
							(check == NO_CHECK))
						cols.add(b);
				}
		}
		return cols;
	}
	public static boolean col(GameObject a, GameObject b, float mx, float my){
		if(overlap(a.getCLeft()+mx,a.getCRight()+mx,b.getCLeft(),b.getCRight())){
			if(overlap(a.getCBottom()+my,a.getCTop()+my,b.getCBottom(),b.getCTop()))
				return true;
		}
		return false;
	}
	public static boolean overlap(double a1, double a2, double b1, double b2){
		if(b2 < a1 || b1 > a2){
			return false;
		}
		return true;
	}
}

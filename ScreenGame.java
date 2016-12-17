package com.hobogames.gamesetup;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public abstract class ScreenGame implements Screen{
	public static float time = 0;
	protected ArrayList<GameObject> objects;
	protected ArrayList<TodoAction> actions;
	
	public OrthographicCamera camera;
	protected SpriteBatch batch;
	protected ShapeRenderer renderer;
	protected Stage stage;
	
	protected TextureRegion backgrounds[];
	protected float bgcolor[] = {0,0,0,1};
	protected Vector2 bgsizeproportion = new Vector2(1,1);
	private int bgindexs[][];
	public static final int NOT_AUTO = 0;
	public static final int AUTO_WIDTH = 1;
	public static final int AUTO_HEIGHT = 2;
	public int autosize = NOT_AUTO;
	protected boolean drawGrid = false;
	public boolean clear = true;
	
	protected float bgmovepercent = 0;
	private float vwidth, vheight;
	public float ppux = 1, ppuy = 1;//Pixles per unit x, and y
	public abstract void preconfig();

	public boolean updateObjects = true;
	public ScreenGame(){
		preconfig();
		objects = new ArrayList<GameObject>();
		actions = new ArrayList<TodoAction>();
		vwidth = GameMain.properties.viewsize.x;
		vheight = GameMain.properties.viewsize.y;
		if(GameMain.properties.debug)
			drawGrid = true;
	}
	public abstract void initialize();
	public void determineBgIndexes(){
		if(backgrounds == null)
			return;
		bgindexs = new int[(int) Math.ceil(1/bgsizeproportion.x)][(int) Math.ceil(1/bgsizeproportion.y)];
		for(int x = 0; x < bgindexs.length; x++){
			for(int y = 0; y < bgindexs[x].length; y++){
				bgindexs[x][y] = GameMain.random.nextInt(backgrounds.length);
			}
		}
	}
	@Override
	public void render(float delta) {
		time+=delta;
		if(delta > .1f)
			delta = .1f;
		if(updateObjects){
			updateObjects(delta);
		}
		while(actions.size() > 0){
			handleAction(actions.get(0));
			actions.remove(0);
		}
		onUpdate(delta);
		drawEverything();
	}
	protected void updateObjects(float delta){
		for(GameObject obj : objects)
			obj.tic(delta);
		stage.act(delta);
	}
	protected void drawEverything(){
		if(clear){
			Gdx.gl.glClearColor(bgcolor[0],bgcolor[1],bgcolor[2],bgcolor[3]);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		}
		batch.setProjectionMatrix(camera.combined);
		renderer.setProjectionMatrix(camera.combined);
		batch.begin();
			if(backgrounds != null){
				float lx = camera.position.x-vwidth/2, ty = camera.position.y-vheight/2;
				float w = bgsizeproportion.x*vwidth, h = bgsizeproportion.y*vheight;
				float startx = lx-(lx%w)-w;//+((bgmovepercent*lx)%w);
				float starty = ty-(ty%h)-h;
				for(float x = startx; x-w <= camera.position.x+vwidth/2; x+=w){ 
					for(float y = starty; y-h <= camera.position.y+vheight/2; y+=h){
						batch.draw(backgrounds[GameMain.random.nextInt(backgrounds.length)],x,y,w,h);
					}
				}
			}
		if(drawGrid){
			batch.end();
			renderer.begin(ShapeType.Line);
			renderer.setColor(1f, 1f, 1f, .5f);
			for(float dx = (float)Math.floor(getViewX()); dx <= getViewX()+vwidth; dx++){
				renderer.line(dx, getViewY(), dx, getViewY()+vheight);
				for(float dy = (float) Math.floor(getViewY()); dy <= getViewY()+vheight; dy++)
					renderer.line(getViewX(), dy, getViewX()+vwidth, dy);
			}
			renderer.end();
			batch.begin();
		}
		for(GameObject obj : objects){
			obj.draw(batch, renderer);
		}
		batch.end();
		stage.draw();
	}
	public float getViewX(){
		return camera.position.x-vwidth/2;
	}
	public float getViewY(){
		return camera.position.y-vheight/2;
	}
	public float getViewWidth(){
		return camera.viewportWidth;
	}
	public float getViewHeight(){
		return camera.viewportHeight;
	}
	protected void handleAction(TodoAction act){
		GameObject obj  = null;
		if(act.obj != null)
			obj = (GameObject)act.obj;
		else
			return;
		switch(act.type){
		case TodoAction.ADD_OBJECT:
			int i = 0;
			for(;i < objects.size(); i++)
				if(objects.get(i).depth > obj.depth)
					break;
			objects.add(i,obj);
			break;
		case TodoAction.REMOVE_OBJECT:
			objects.remove(obj);
			break;
		case TodoAction.UPDATE_DEPTH:
			objects.remove(obj);
			actions.add(new TodoAction(TodoAction.ADD_OBJECT,obj));
			break;
		}
	}
	public void addObject(Object object){
		actions.add(new TodoAction(TodoAction.ADD_OBJECT,object));
	}
	public void removeObject(Object object){
		actions.add(new TodoAction(TodoAction.REMOVE_OBJECT,object));
	}
	public void updateObjectDepth(Object object){
		actions.add(new TodoAction(TodoAction.UPDATE_DEPTH,object));
	}
	public abstract void onUpdate(float delta);
	@Override
	public void resize(int width, int height) {
		if(autosize != NOT_AUTO){
			if(autosize == AUTO_HEIGHT){
				camera.viewportWidth = vwidth = GameMain.properties.viewsize.x;
				float oldh = vheight;
				camera.viewportHeight = vheight = vwidth*(height*1f/width);
				camera.position.y += (oldh-vheight)/2;
			}else if(autosize == AUTO_WIDTH){
				camera.viewportHeight = vheight = GameMain.properties.viewsize.y;
				float oldw = vwidth;
				camera.viewportWidth= vwidth = vheight*(width*1f/height);
				camera.position.x += (oldw-vwidth)/2;
			}
			camera.update();
			((StretchViewport)stage.getViewport()).setWorldSize(vwidth, vheight);
			stage.getViewport().update(width, height);
			determineBgIndexes();
		}
		ppux = width/camera.viewportWidth;
		ppuy = height/camera.viewportHeight;
		InputManager.ppux = ppux;
		InputManager.ppuy = ppuy;
	}
	@Override
	public void show() {
		camera = new OrthographicCamera(vwidth,vheight);
		camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
		camera.update();
		
		stage = new Stage(new StretchViewport(vwidth,vheight,camera));
		GameMain.multiplexer.addProcessor(stage);
		
		batch = new SpriteBatch();
		renderer = new ShapeRenderer();
		determineBgIndexes();
		initialize();
	}
	@Override
	public void hide() {
		GameMain.multiplexer.removeProcessor(stage);
	}
	@Override
	public void pause() {
		
	}
	@Override
	public void resume() {
		
	}
	@Override
	public void dispose() {
		batch.dispose();
		stage.dispose();
		renderer.dispose();
	}
}
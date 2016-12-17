package com.hobogames.gamesetup;

import com.badlogic.gdx.math.Vector2;

@SuppressWarnings("unused")
public class CollisionPolygon {
	private static Vector2[] toArray(float x, float y, float width, float height){
		Vector2 array[] = {new Vector2(x,y),new Vector2(x+width,x+height)};
		return array;
	}
	private static Vector2[] toArray(float array[]){
		Vector2 newArray[] = new Vector2[array.length/2];
		for(int i = 0; i < newArray.length; i++)
			newArray[i] = new Vector2(array[i/2],array[i/2+1]);
		return newArray;
	}
	
	public CollisionLine lines[];
	
	public CollisionPolygon(Vector2 points[]){
		lines = new CollisionLine[points.length];
		for(int i = 0; i < points.length; i++){
			if(i != points.length-1)
				lines[i] = new CollisionLine(points[i],points[i+1]);
			else
				lines[i] = new CollisionLine(points[i],points[0]);
		}
	}
	public CollisionPolygon(float points[]){
		this(toArray(points));
	}
	public CollisionPolygon(float x, float y, float width, float height){
		this(toArray(x,y,width,height));
	}
	public void determineExtremeLines(float axisangle){
		float maxleft, maxright, maxtop,maxbottom;
		for(CollisionLine a : lines){
			a.determineAxisPoints(axisangle);
		}
	}
	private class CollisionLine {
		public static final int NOT = 0;
		public static final int LEFT = 0;
		public static final int RIGHT = 0;
		public static final int UP = 0;
		public static final int DOWN = 0;
		
		public Vector2 p1, p2;
		public Vector2 axisp1, axisp2;
		private float distance, angle;
		public int extreme = NOT;
		public CollisionLine(Vector2 p1, Vector2 p2){
			this.p1 = p1;
			this.p2 = p2;
			distance = p1.dst(p2);
			angle = p2.sub(p1).angle();
		}
		public void determineAxisPoints(float axisangle){
			float tangle = p1.angle()-axisangle;
			distance = (float) p1.dst(0,0);
			axisp1 = new Vector2((float) (distance*Math.cos(tangle)),(float) (distance*Math.sin(tangle)));
			tangle = p2.angle()-axisangle;
			distance = p2.dst(0, 0);
			axisp2 = new Vector2((float) (distance*Math.cos(tangle)),(float) (distance*Math.sin(tangle)));
		}
	}
}

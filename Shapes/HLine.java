package com.hobogames.gamesetup.Shapes;

import com.badlogic.gdx.math.Vector2;

public class HLine{
	private float x1, y1, x2, y2;
	Vector2 tmp = new Vector2();
	public HLine(float x1, float y1, float x2, float y2){
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	public HLine(Vector2 p1, Vector2 p2){
		this(p1.x,p1.y,p2.x,p2.y);
	}
	public Vector2 getP1(){
		return tmp.set(x1, y1).cpy();
	}
	public Vector2 getP2(){
		return tmp.set(x2, y2).cpy();
	}
	public float angle(){
		return (float) Math.toDegrees(Math.atan2(y2-y1, x2-x1));
		//return HMath.angle(getP1(), getP2());
	}
	public float length(){
		float x = x2-x1;
		float y = y2-y1;
		return (float) Math.sqrt(y*y + x*x);
	}
	public String toString(){
		return "{Line("+x1 + "," + y1 + ")(" + x2 + "," + y2 +")}"; 
	}
}
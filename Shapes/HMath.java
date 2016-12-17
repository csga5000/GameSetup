package com.hobogames.GameSetup.Shapes;

import com.badlogic.gdx.math.Vector2;

public class HMath {
	private HMath(){}
	public static float angle(Vector2 a, Vector2 b){
		float angle = (float)Math.toDegrees(Math.atan2(b.y-a.y, b.x-a.x));
		while(angle < 0)
			angle+=360;
		return angle;
		//return b.cpy().sub(a).angle();
	}
}

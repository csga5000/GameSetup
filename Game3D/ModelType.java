package com.hobogames.gamesetup.game3D;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.collision.BoundingBox;

public class ModelType {
	BoundingBox size;
	Model model;
	public ModelType(Model a){
		//size = a.extendBoundingBox(size);
		model = a;
	}
}

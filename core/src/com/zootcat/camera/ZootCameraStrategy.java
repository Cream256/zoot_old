package com.zootcat.camera;

import com.badlogic.gdx.math.Vector3;
import com.zootcat.scene.ZootActor;

public interface ZootCameraStrategy
{
	void calculateCameraPosition(ZootActor actor, Vector3 outPosition, float delta);
}

package com.zootcat.camera;

import com.badlogic.gdx.math.Vector3;
import com.zootcat.scene.ZootActor;

//TODO add test
public class ZootCameraCenterOnStrategy implements ZootCameraStrategy
{
	@Override
	public void calculateCameraPosition(ZootActor actor, Vector3 outPosition, float delta)
	{
		if(actor == null)
		{
			return;
		}
		
		outPosition.x = actor.getX();
		outPosition.y = actor.getY();
	}
}

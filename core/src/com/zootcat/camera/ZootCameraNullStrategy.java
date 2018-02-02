package com.zootcat.camera;

import com.badlogic.gdx.math.Vector3;
import com.zootcat.scene.ZootActor;

//TODO add test
public class ZootCameraNullStrategy implements ZootCameraStrategy
{
	public static final ZootCameraNullStrategy Instance = new ZootCameraNullStrategy();
	
	private ZootCameraNullStrategy()
	{
		//use instance
	}
	
	@Override
	public void calculateCameraPosition(ZootActor actor, Vector3 outPosition, float delta)
	{
		//noop
	}
}
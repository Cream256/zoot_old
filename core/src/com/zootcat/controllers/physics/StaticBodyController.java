package com.zootcat.controllers.physics;

import com.zootcat.physics.ZootPhysicsBodyShape;
import com.zootcat.physics.ZootPhysicsBodyType;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class StaticBodyController extends PhysicsBodyController 
{
	public StaticBodyController(ZootActor actor, ZootScene scene) 
	{
		this(ZootPhysicsBodyShape.RECTANGLE.toString(), actor, scene);
	}
	
	public StaticBodyController(String shape, ZootActor actor, ZootScene scene) 
	{
		super(1.0f, shape, ZootPhysicsBodyType.STATIC.toString(), actor, scene);
	}
}

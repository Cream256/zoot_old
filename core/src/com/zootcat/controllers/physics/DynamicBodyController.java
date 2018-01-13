package com.zootcat.controllers.physics;

import com.zootcat.physics.ZootPhysicsBodyShape;
import com.zootcat.physics.ZootPhysicsBodyType;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class DynamicBodyController extends PhysicsBodyController 
{
	public DynamicBodyController(ZootActor actor, ZootScene scene)
	{
		this(1.0f, actor, scene);
	}
	
	public DynamicBodyController(float density, ZootActor actor, ZootScene scene) 
	{
		this(density, ZootPhysicsBodyShape.RECTANGLE.toString(), actor, scene);
	}
	
	public DynamicBodyController(float density, String shape, ZootActor actor, ZootScene scene) 
	{
		super(density, shape, ZootPhysicsBodyType.DYNAMIC.toString(), actor, scene);
	}
}

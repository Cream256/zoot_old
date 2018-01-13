package com.zootcat.controllers.physics;

import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class CharacterController extends DynamicBodyController
{
	public CharacterController(float density, ZootActor actor, ZootScene scene) 
	{
		super(density, actor, scene);
	}
	
	@Override
	public void onAdd(ZootActor actor) 
	{
		super.onAdd(actor);
		getPhysicsBody().setCanRotate(false);
	}
	
}

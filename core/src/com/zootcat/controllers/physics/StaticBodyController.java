package com.zootcat.controllers.physics;

import com.zootcat.physics.ZootPhysicsBodyType;
import com.zootcat.physics.ZootPhysicsFixture;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class StaticBodyController extends PhysicsBodyController 
{		
	public StaticBodyController(ZootScene scene)
	{
		super.scene = scene;
	}
	
	@Override
	protected ZootPhysicsBodyType getBodyType() 
	{
		return ZootPhysicsBodyType.STATIC;
	}

	@Override
	protected ZootPhysicsFixture[] createFixtures(ZootActor actor)
	{
		ZootPhysicsFixture[] fixtures = new ZootPhysicsFixture[1];
		fixtures[0] = ZootPhysicsFixture.createBox(0, 0, actor.getWidth(), actor.getHeight());
		return fixtures;
	}
}

package com.zootcat.controllers.physics;

import com.zootcat.physics.ZootPhysicsBodyType;
import com.zootcat.physics.ZootPhysicsFixture;
import com.zootcat.physics.ZootPhysicsFixtureType;
import com.zootcat.scene.ZootActor;

public class DynamicBodyController extends PhysicsBodyController 
{
	private ZootPhysicsFixtureType fixtureType; 
		
	@Override
	protected ZootPhysicsBodyType getBodyType() 
	{
		return ZootPhysicsBodyType.DYNAMIC;
	}

	@Override
	protected ZootPhysicsFixture[] createFixtures(ZootActor actor)
	{
		ZootPhysicsFixture[] shapes = new ZootPhysicsFixture[1];		
		if(fixtureType == ZootPhysicsFixtureType.BOX)
		{
			shapes[0] = ZootPhysicsFixture.createBox(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());	
		}
		else
		{
			shapes[0] = ZootPhysicsFixture.createCircle(actor.getX(), actor.getY(), actor.getWidth() / 2);
		}
		return shapes;
	}
}

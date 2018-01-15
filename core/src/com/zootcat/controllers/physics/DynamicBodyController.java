package com.zootcat.controllers.physics;

import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.physics.ZootPhysicsBodyType;
import com.zootcat.physics.ZootPhysicsFixture;
import com.zootcat.physics.ZootPhysicsFixtureType;
import com.zootcat.scene.ZootActor;

public class DynamicBodyController extends PhysicsBodyController 
{
	@CtrlParam private ZootPhysicsFixtureType shape = ZootPhysicsFixtureType.BOX; 
		
	@Override
	protected ZootPhysicsBodyType getBodyType() 
	{
		return ZootPhysicsBodyType.DYNAMIC;
	}

	@Override
	protected ZootPhysicsFixture[] createFixtures(ZootActor actor)
	{
		ZootPhysicsFixture[] fixtures = new ZootPhysicsFixture[1];		
		if(shape == ZootPhysicsFixtureType.BOX)
		{
			fixtures[0] = ZootPhysicsFixture.createBox(0, 0, actor.getWidth(), actor.getHeight());	
		}
		else if (shape == ZootPhysicsFixtureType.CIRCLE)
		{
			fixtures[0] = ZootPhysicsFixture.createCircle(0, 0, actor.getWidth() / 2);
		}
		else
		{
			throw new RuntimeZootException("Unknown fixture type for for actor: " + actor);
		}
		return fixtures;
	}
}

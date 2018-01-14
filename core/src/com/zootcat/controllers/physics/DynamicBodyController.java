package com.zootcat.controllers.physics;

import com.zootcat.physics.ZootPhysicsFixtureType;
import com.zootcat.physics.ZootPhysicsBodyType;
import com.zootcat.physics.ZootPhysicsFixture;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;
import com.zootcat.utils.ZootUtils;

public class DynamicBodyController extends PhysicsBodyController 
{
	private ZootPhysicsFixtureType fixtureType; 
	
	public DynamicBodyController(ZootActor actor, ZootScene scene)
	{
		this(ZootPhysicsFixtureType.BOX.toString(), 1.0f, 0.0f, 0.0f, actor, scene);
	}
	
	public DynamicBodyController(float density, ZootActor actor, ZootScene scene)
	{
		this(ZootPhysicsFixtureType.BOX.toString(), density, 0.0f, 0.0f, actor, scene);
	}
	
	public DynamicBodyController(String fixtureType, float density, ZootActor actor, ZootScene scene)
	{
		this(ZootPhysicsFixtureType.BOX.toString(), density, 0.0f, 0.0f, actor, scene);
	}
	
	public DynamicBodyController(String fixtureType, float density, float friction, float restitution, ZootActor actor, ZootScene scene) 
	{
		super(density, friction, restitution, actor, scene);
		this.fixtureType = ZootUtils.searchEnum(ZootPhysicsFixtureType.class, fixtureType);
	}
	
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

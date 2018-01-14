package com.zootcat.controllers.physics;

import com.zootcat.physics.ZootPhysicsBodyType;
import com.zootcat.physics.ZootPhysicsFixture;
import com.zootcat.scene.ZootActor;

public class CharacterBodyController extends PhysicsBodyController
{	
	@Override
	public void onAdd(ZootActor actor) 
	{
		super.onAdd(actor);
		getPhysicsBody().setCanRotate(false);
	}
	
	@Override
	protected ZootPhysicsFixture[] createFixtures(ZootActor actor)
	{
		ZootPhysicsFixture[] shapes = new ZootPhysicsFixture[2];

		//feet shape
		shapes[0] = ZootPhysicsFixture.createCircle(actor.getX(), actor.getY(), actor.getWidth() / 2.0f);
		
		//body shape
		shapes[1] = ZootPhysicsFixture.createBox(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
		
		return shapes;
	}

	@Override
	protected ZootPhysicsBodyType getBodyType() 
	{
		return ZootPhysicsBodyType.DYNAMIC;
	}
	
}

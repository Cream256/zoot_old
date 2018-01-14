package com.zootcat.controllers.physics;

import com.zootcat.physics.ZootPhysicsBodyType;
import com.zootcat.physics.ZootPhysicsFixture;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class CharacterBodyController extends PhysicsBodyController
{
	public CharacterBodyController(ZootActor actor, ZootScene scene)
	{
		this(1.0f, actor, scene);
	}
	
	public CharacterBodyController(float density, ZootActor actor, ZootScene scene)
	{
		this(density, 0.0f, 0.0f, actor, scene);
	}
	
	public CharacterBodyController(float density, float friction, float restitution, ZootActor actor, ZootScene scene) 
	{
		super(density, friction, restitution, actor, scene);
	}
	
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

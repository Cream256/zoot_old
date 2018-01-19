package com.zootcat.controllers.physics;

import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.controllers.Controller;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.physics.ZootDefaultContactFilter;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class OneWayPlatformController implements Controller, ContactFilter
{
	@CtrlParam(global = true) private ZootScene scene;
	
	@Override
	public void init(ZootActor actor)
	{
		//noop
	}

	@Override
	public void onAdd(ZootActor actor)
	{
		PhysicsBodyController ctrl = actor.getController(PhysicsBodyController.class);
		ctrl.getFixtures().forEach((fixture) -> scene.getPhysics().addFixtureContactFilter(fixture, this));		
	}

	@Override
	public void onRemove(ZootActor actor)
	{
		PhysicsBodyController ctrl = actor.getController(PhysicsBodyController.class);
		ctrl.getFixtures().forEach((fixture) -> scene.getPhysics().removeFixtureContactFilter(fixture, this));
	}

	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		//noop
	}

	@Override
	public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB)
	{
		//check category / mask / groupid first
		if(!ZootDefaultContactFilter.shouldCollide(fixtureA, fixtureB))
		{
			return false;
		}
		
		float actorBottom = fixtureA.getBody().getPosition().y + fixtureA.getShape().getRadius();
		float platformTop = fixtureB.getBody().getPosition().y + fixtureB.getShape().getRadius();				
		boolean actorOnTop = actorBottom > platformTop;
		
		float actorVelocityY = fixtureA.getBody().getLinearVelocity().y;
		boolean actorFallingDown = actorVelocityY < 0.0f;
		
		return actorOnTop && actorFallingDown;
	}
}

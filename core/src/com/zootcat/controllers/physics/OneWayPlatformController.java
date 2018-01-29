package com.zootcat.controllers.physics;

import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.controllers.Controller;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.physics.ZootDefaultContactFilter;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

//TODO move this to logic package
public class OneWayPlatformController implements Controller, ContactFilter
{
	@CtrlParam(global = true) private ZootScene scene;
	
	private ZootActor platform;
	
	@Override
	public void init(ZootActor actor)
	{
		platform = actor;
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
		
		Fixture platformFixture = fixtureA.getBody().getUserData() == platform ? fixtureA : fixtureB;
		Fixture otherFixture = fixtureA.getBody().getUserData() == platform ? fixtureB : fixtureA;
				
		float actorBottom = otherFixture.getBody().getPosition().y + otherFixture.getShape().getRadius();
		float platformTop = platformFixture.getBody().getPosition().y + platformFixture.getShape().getRadius();				
		boolean actorOnTop = actorBottom > platformTop;
		
		float actorVelocityY = otherFixture.getBody().getLinearVelocity().y;
		boolean actorFallingDown = actorVelocityY < 0.0f;
		
		return actorOnTop && actorFallingDown;
	}
}

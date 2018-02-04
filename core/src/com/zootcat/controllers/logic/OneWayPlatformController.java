package com.zootcat.controllers.logic;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.physics.PhysicsCollisionController;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class OneWayPlatformController extends PhysicsCollisionController
{
	@CtrlParam(global = true) private ZootScene scene;

	private ZootActor platform;
	private boolean shouldCollide = true;
	
	@Override
	public void init(ZootActor actor)
	{
		platform = actor;
	}
	
	@Override
	public void beginContact(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		Fixture platformFixture = contact.getFixtureA().getBody().getUserData() == platform ? contact.getFixtureA() : contact.getFixtureB();
		Fixture otherFixture = contact.getFixtureA().getBody().getUserData() == platform ? contact.getFixtureB() : contact.getFixtureA();
				
		float actorBottom = otherFixture.getBody().getPosition().y + otherFixture.getShape().getRadius();
		float platformTop = platformFixture.getBody().getPosition().y + platformFixture.getShape().getRadius();				
		boolean actorOnTop = actorBottom > platformTop;
		
		float actorVelocityY = otherFixture.getBody().getLinearVelocity().y;
		boolean actorFallingDown = actorVelocityY < 0.0f;
		
		shouldCollide = actorOnTop && actorFallingDown;
	}

	@Override
	public void endContact(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		//noop
	}

	@Override
	public void preSolve(ZootActor actorA, ZootActor actorB, Contact contact, Manifold manifold)
	{
		ZootActor actor = actorA == platform ? actorB : actorA;		
		boolean ignorePlatforms = actor.controllerCondition(IgnorePlatformsController.class, ctrl -> ctrl.isActive());		
		contact.setEnabled(shouldCollide && !ignorePlatforms);
	}

	@Override
	public void postSolve(ZootActor actorA, ZootActor actorB, ContactImpulse contactImpulse)
	{
		//noop
	}
}

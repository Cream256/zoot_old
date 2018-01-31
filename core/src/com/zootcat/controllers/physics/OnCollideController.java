package com.zootcat.controllers.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;
import com.zootcat.utils.BitMaskConverter;

public abstract class OnCollideController extends PhysicsCollisionController
{
	@CtrlParam private String category = null;
	@CtrlDebug private int categoryBits = -1;
	
	private ZootActor controllerActor;
	
	@Override
	public void init(ZootActor actor)
	{
		this.controllerActor = actor;
		if(category != null && !category.isEmpty())
		{
			categoryBits = BitMaskConverter.Instance.fromString(category);
		}
	}
	
	@Override
	public void beginContact(ZootActor actorA, ZootActor actorB, Contact contact)
	{				
		if(collides(actorA, actorB, contact))
		{
			onEnter(actorA, actorB, contact);
		}
	}
	
	@Override
	public void endContact(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		if(collides(actorA, actorB, contact))
		{
			onLeave(actorA, actorB, contact);
		}
	}
	
	@Override
	public void preSolve(ZootActor actorA, ZootActor actorB, Contact contact, Manifold manifold)
	{
		//noop
	}
	
	@Override
	public void postSolve(ZootActor actorA, ZootActor actorB, ContactImpulse contactImpulse)
	{
		//noop
	}
		
	public ZootActor getControllerActor()
	{
		return controllerActor;
	}
	
	public abstract void onEnter(ZootActor actorA, ZootActor actorB, Contact contact);
	
	public abstract void onLeave(ZootActor actorA, ZootActor actorB, Contact contact);
	
	protected ZootActor getOtherActor(ZootActor actorA, ZootActor actorB)
	{
		return actorA == controllerActor ? actorB : actorA;
	}
	
	protected Fixture getOtherFixture(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		return (actorA == controllerActor) ? contact.getFixtureB() : contact.getFixtureA();
	}
	
	protected Fixture getControllerActorFixture(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		return (actorA == controllerActor) ? contact.getFixtureA() : contact.getFixtureB();
	}
	
	private boolean collides(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		Fixture otherFixture = getOtherFixture(actorA, actorB, contact);	
		return categoryBits == -1 || otherFixture.getFilterData().categoryBits == categoryBits;
	}
}

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
	
	protected ZootActor controllerActor;
	
	@Override
	public void init(ZootActor actor)
	{
		this.controllerActor = actor;
		if(category != null && !category.isEmpty())
		{
			categoryBits = BitMaskConverter.Instance.fromString(category);
		}
	}
	
	public void beginContact(ZootActor actorA, ZootActor actorB, Contact contact)
	{				
		if(collides(actorA, actorB, contact))
		{
			onEnter(actorA, actorB);
		}
	}
	
	public void endContact(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		if(collides(actorA, actorB, contact))
		{
			onLeave(actorA, actorB);
		}
	}
	
	public void preSolve(ZootActor actorA, ZootActor actorB, Contact contact, Manifold manifold)
	{
		//noop
	}
	
	public void postSolve(ZootActor actorA, ZootActor actorB, ContactImpulse contactImpulse)
	{
		//noop
	}
	
	private boolean collides(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		Fixture otherFixture = (actorA == controllerActor) ? contact.getFixtureB() : contact.getFixtureA();		
		return categoryBits == -1 || otherFixture.getFilterData().categoryBits == categoryBits;
	}
	
	public abstract void onEnter(ZootActor actorA, ZootActor actorB);
	public abstract void onLeave(ZootActor actorA, ZootActor actorB);
}

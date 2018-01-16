package com.zootcat.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.zootcat.physics.ZootCollisionEvent.Type;
import com.zootcat.scene.ZootActor;

public class ZootPhysicsContactListener implements ContactListener 
{
	private ZootCollisionEvent event = new ZootCollisionEvent();
	
	@Override
	public void beginContact(Contact contact) 
	{
		ZootActor actorA = (ZootActor) contact.getFixtureA().getBody().getUserData();
		ZootActor actorB = (ZootActor) contact.getFixtureB().getBody().getUserData();		
	
		event.reset();
		event.setActorA(actorA);
		event.setActorB(actorB);
		event.setType(Type.BeginContact);
		event.setContact(contact);		
		actorA.fire(event);
		actorB.fire(event);
	}

	@Override
	public void endContact(Contact contact) 
	{
		ZootActor actorA = (ZootActor) contact.getFixtureA().getBody().getUserData();
		ZootActor actorB = (ZootActor) contact.getFixtureB().getBody().getUserData();		
	
		event.reset();
		event.setActorA(actorA);
		event.setActorB(actorB);
		event.setType(Type.EndContact);
		event.setContact(contact);		
		actorA.fire(event);
		actorB.fire(event);
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) 
	{
		ZootActor actorA = (ZootActor) contact.getFixtureA().getBody().getUserData();
		ZootActor actorB = (ZootActor) contact.getFixtureB().getBody().getUserData();	
	
		event.reset();
		event.setActorA(actorA);
		event.setActorB(actorB);
		event.setType(Type.PreSolve);
		event.setContact(contact);
		event.setManifold(oldManifold);
		actorA.fire(event);
		actorB.fire(event);
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) 
	{
		ZootActor actorA = (ZootActor) contact.getFixtureA().getBody().getUserData();
		ZootActor actorB = (ZootActor) contact.getFixtureB().getBody().getUserData();
		
		event.reset();
		event.setActorA(actorA);
		event.setActorB(actorB);
		event.setType(Type.PostSolve);
		event.setContact(contact);
		event.setContactImpulse(impulse);
		actorA.fire(event);
		actorB.fire(event);		
	}
}

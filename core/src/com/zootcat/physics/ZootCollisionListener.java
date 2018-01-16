package com.zootcat.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.scene.ZootActor;

public class ZootCollisionListener implements EventListener 
{
	@Override
	public boolean handle (Event e) 
	{
		if (!(e instanceof ZootCollisionEvent)) return false;
		ZootCollisionEvent event = (ZootCollisionEvent)e;
		
		switch(event.getType())
		{
		case BeginContact:
			return beginContact(event.getActorA(), event.getActorB(), event.getContact());			
		
		case EndContact:
			return endContact(event.getActorA(), event.getActorB(), event.getContact());
			
		case PreSolve:
			return preSolve(event.getActorA(), event.getActorB(), event.getContact(), event.getManifold());
		
		case PostSolve:
			return postSolve(event.getActorA(), event.getActorB(), event.getContactImpulse());
			
		default:
			throw new RuntimeZootException("Unsupported collision event type: " + event.getType());
		}
	}

	public boolean beginContact(ZootActor actorA, ZootActor actorB, Contact contact) 
	{
		return false;
	}
	
	public boolean endContact(ZootActor actorA, ZootActor actorB, Contact contact) 
	{
		return false;
	}
	
	public boolean preSolve(ZootActor actorA, ZootActor actorB, Contact contact, Manifold manifold) 
	{
		return false;
	}
	
	public boolean postSolve(ZootActor actorA, ZootActor actorB, ContactImpulse contactImpulse) 
	{
		return false;
	}
}

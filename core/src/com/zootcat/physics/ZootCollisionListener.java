package com.zootcat.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.scene.ZootActor;

public abstract class ZootCollisionListener implements EventListener 
{
	@Override
	public boolean handle (Event e) 
	{
		if (!(e instanceof ZootCollisionEvent)) return false;
		ZootCollisionEvent event = (ZootCollisionEvent)e;
				
		ZootActor eventActor = getEventActor(event);
		ZootActor otherActor = getOtherActor(event);
		
		switch(event.getType())
		{
		case BeginContact:
			return beginContact(eventActor, otherActor, event.getContact());			
		
		case EndContact:
			return endContact(eventActor, otherActor, event.getContact());
			
		case PreSolve:
			return preSolve(eventActor, otherActor, event.getContact(), event.getManifold());
		
		case PostSolve:
			return postSolve(eventActor, otherActor, event.getContactImpulse());
			
		default:
			throw new RuntimeZootException("Unsupported collision event type: " + event.getType());
		}
	}

	public abstract boolean beginContact(ZootActor actorA, ZootActor actorB, Contact contact); 	
	
	public abstract boolean endContact(ZootActor actorA, ZootActor actorB, Contact contact);	
	
	public abstract boolean preSolve(ZootActor actorA, ZootActor actorB, Contact contact, Manifold manifold);
	
	public abstract boolean postSolve(ZootActor actorA, ZootActor actorB, ContactImpulse contactImpulse);
	
	private ZootActor getEventActor(ZootCollisionEvent event)
	{
		return event.getTarget() == event.getActorA() ? event.getActorA() : event.getActorB();
	}
	
	private ZootActor getOtherActor(ZootCollisionEvent event)
	{
		return event.getTarget() == event.getActorA() ? event.getActorB() : event.getActorA();
	}
}

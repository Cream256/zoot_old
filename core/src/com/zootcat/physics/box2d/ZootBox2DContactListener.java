package com.zootcat.physics.box2d;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.zootcat.scene.ZootActor;

public class ZootBox2DContactListener implements ContactListener 
{
	@Override
	public void beginContact(Contact contact) 
	{
		ZootActor actorA = (ZootActor) contact.getFixtureA().getBody().getUserData();
		ZootActor actorB = (ZootActor) contact.getFixtureA().getBody().getUserData();
	}

	@Override
	public void endContact(Contact contact) 
	{
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) 
	{
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) 
	{
		
	}
}

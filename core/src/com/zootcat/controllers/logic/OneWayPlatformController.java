package com.zootcat.controllers.logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.controllers.physics.PhysicsCollisionController;
import com.zootcat.math.ZootBoundingBoxFactory;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

//TODO add tests for several bodies on platform at once: one is droping from platform,
//so only one should drop not the rest of them, the problem is that the controller
//is holding state which should only affect body it is connected with!

//Implementation based on: https://www.iforce2d.net/b2dtut/one-way-walls
public class OneWayPlatformController extends PhysicsCollisionController
{
	@CtrlParam(global = true) private ZootScene scene;
	@CtrlDebug private boolean shouldCollide = true;
	@CtrlDebug private boolean ignorePlatforms = false;
	
	private Vector2 tmp;
	private ZootActor platform;
	
	@Override
	public void init(ZootActor actor)
	{
		tmp = new Vector2();
		platform = actor;
	}
	
	@Override
	public void beginContact(ZootActor actorA, ZootActor actorB, Contact contact)
	{		
		shouldCollide = true;
		
		//get bodies
		ZootActor platformActor = actorA == platform ? actorA : actorB; 
		ZootActor otherActor = actorA == platform ? actorB : actorA;
		Body platformBody = platformActor.getController(PhysicsBodyController.class).getBody();
		Body otherBody = otherActor.getController(PhysicsBodyController.class).getBody();
		
		//get platform front face height
		float platformFaceY = getPlatformFixtureFrontFace(actorA, actorB, contact);
		
	    //check if contact points are moving downward
		final Vector2[] points = contact.getWorldManifold().getPoints();	    
		for (int i = 0; i < points.length; ++i) 
		{				
			Vector2 pointVelOther = otherBody.getLinearVelocityFromWorldPoint(points[i]);
			Vector2 pointVelPlatform = platformBody.getLinearVelocityFromWorldPoint(points[i]);
						
			tmp.x = pointVelOther.x - pointVelPlatform.x;
			tmp.y = pointVelOther.y - pointVelPlatform.y;			
			Vector2 relativeVel = platformBody.getLocalVector(tmp);			
			if (relativeVel.y < -1.0f)	//if moving down faster than 1 m/s, handle as before
			{
		          return;	//point is moving into platform, leave contact solid and exit
			}
		    else if (relativeVel.y < 1.0f)  //if moving slower than 1 m/s 
		    {
		    	//borderline case, moving only slightly out of platform
		    	Vector2 relativePoint = platformBody.getLocalPoint(points[i]);
	        	if (relativePoint.y > platformFaceY - 0.05)
	        	{
	        		return;	//contact point is less than 5cm inside front face of platfrom
	        	}
		    }	//else moving up faster than 1 m/s, do nothing
		}
		
		//no points are moving downward, contact should not be solid
		shouldCollide = false;
	}

	private float getPlatformFixtureFrontFace(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		Fixture platformFixture = contact.getFixtureA().getBody().getUserData() == platform ? contact.getFixtureA() : contact.getFixtureB(); 
		BoundingBox bbox = ZootBoundingBoxFactory.create(platformFixture);
		return bbox.getHeight() / 2.0f;
	}

	@Override
	public void endContact(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		//This is necessary because contacts exists as long as the AABBs of two fixtures continue to overlap, 
		//even if the fixtures themselves do not overlap. If the player jumps just high enough to clear the top of the platform,
		//but not high enough for the AABBs to separate, the contact will remain disabled and he will fall back down again.
		//https://www.iforce2d.net/b2dtut/one-way-walls		
		contact.setEnabled(true);
		ignorePlatforms = false;
	}

	@Override
	public void preSolve(ZootActor actorA, ZootActor actorB, Contact contact, Manifold manifold)
	{
		ZootActor otherActor = actorA == platform ? actorB : actorA;		
		if(otherActor.controllerCondition(IgnorePlatformsController.class, ctrl -> ctrl.isActive()))
		{
			ignorePlatforms = true;	//we should ignore this platform until end contact
		}
		contact.setEnabled(shouldCollide && !ignorePlatforms);
	}
		
	@Override
	public void postSolve(ZootActor actorA, ZootActor actorB, ContactImpulse contactImpulse)
	{
		//noop
	}
}

package com.zootcat.controllers.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class JumpableController extends PhysicsCollisionController
{
	@CtrlParam private int feetWidth = 0;		
	@CtrlParam(global = true) private ZootScene scene;	
	@CtrlDebug private int feetContactCount = 0;
	@CtrlDebug private ZootActor actorWithFeet;
	private Fixture feet;
	
	@Override
	public void init(ZootActor actor)
	{
		//noop
	}

	@Override
	public void onAdd(ZootActor actor)
	{
		super.onAdd(actor);
		actorWithFeet = actor;
		
		//create feet shape
		PhysicsBodyController ctrl = actor.getController(PhysicsBodyController.class);		
		Shape feetShape = createFeetShape(actor);
		
		//create fixture
		FixtureDef feetDef = createFeetSensorFixtureDef(ctrl.getBody(), actor, feetShape);		
		feet = ctrl.addFixture(feetDef);
		
		//cleanup
		feetShape.dispose();
	}

	@Override
	public void onRemove(ZootActor actor)
	{
		PhysicsBodyController ctrl = actor.getController(PhysicsBodyController.class);
		ctrl.removeFixture(feet);
		
		super.onRemove(actor);
	}

	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		//noop		
	}
	
	@Override
	public boolean beginContact(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		if(actorA == actorWithFeet || actorB == actorWithFeet)
		{
			++feetContactCount;
			return true;
		}
		return false;
	}

	@Override
	public boolean endContact(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		if(actorA == actorWithFeet || actorB == actorWithFeet)
		{
			--feetContactCount;
			return true;
		}
		return false;
	}

	@Override
	public boolean preSolve(ZootActor actorA, ZootActor actorB, Contact contact, Manifold manifold)
	{
		return false;
	}

	@Override
	public boolean postSolve(ZootActor actorA, ZootActor actorB, ContactImpulse contactImpulse)
	{
		return false;
	}
	
	public boolean canJump()
	{
		return feetContactCount > 0;
	}

	private float calculateWidth(ZootActor actor)
	{
		if(feetWidth == 0)
		{
			return actor.getWidth() / 2.0f;
		}
		return feetWidth * scene.getUnitScale();
	}
	
	private Shape createFeetShape(ZootActor actor)
	{
		Vector2 center = new Vector2(0.0f, -actor.getHeight() / 2.0f);		
		
		float feetWidth = calculateWidth(actor);
		float feetHeight = actor.getHeight() * 0.1f;	//10% of actor height
		
		PolygonShape feetShape = new PolygonShape();
		feetShape.setAsBox(feetWidth, feetHeight, center, 0.0f);				
		return feetShape;
	}
	
	private FixtureDef createFeetSensorFixtureDef(Body body, ZootActor actor, Shape feetShape)
	{
		FixtureDef def = new FixtureDef();
		def.isSensor = true;
		def.friction = 0.0f;		
		def.shape = feetShape;		
		return def;
	}
}

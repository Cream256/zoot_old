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
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;
import com.zootcat.utils.ZootUtils;

public class JumpableController extends PhysicsCollisionController
{
	@CtrlParam private int feetWidth = 0;		
	@CtrlParam private int jumpTimeout = 0;
	@CtrlParam(debug = true) private float jumpForce = 1.0f;	
	@CtrlParam(global = true) private ZootScene scene;	
	@CtrlDebug private int feetContactCount = 0;
	@CtrlDebug private int currentJumpTimeout = 0;
	@CtrlDebug private ZootActor actorWithFeet;
	private Fixture feet;
	private PhysicsBodyController physicsCtrl;
	private ZootEvent landEvent;
	
	@Override
	public void init(ZootActor actor)
	{
		landEvent = new ZootEvent(ZootEventType.Land);
	}

	@Override
	public void onAdd(ZootActor actor)
	{
		super.onAdd(actor);
		actorWithFeet = actor;
		physicsCtrl = actor.getController(PhysicsBodyController.class);
				
		//create feet shape		
		Shape feetShape = createFeetShape(actor);
		
		//create fixture
		FixtureDef feetDef = createFeetSensorFixtureDef(physicsCtrl.getBody(), actor, feetShape);		
		feet = physicsCtrl.addFixture(feetDef);
		
		//cleanup
		feetShape.dispose();
	}

	@Override
	public void onRemove(ZootActor actor)
	{
		super.onRemove(actor);
		physicsCtrl.removeFixture(feet);
		physicsCtrl = null;		
	}

	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		currentJumpTimeout = Math.max(0, currentJumpTimeout - ZootUtils.trunc(delta*1000.0f));
	}
	
	@Override
	public boolean beginContact(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		if(contactWithFeetSensor(actorA, actorB, contact))
		{
			if(++feetContactCount == 1)
			{
				landEvent.reset();
				landEvent.setType(ZootEventType.Land);
				actorWithFeet.fire(landEvent);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean endContact(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		if(contactWithFeetSensor(actorA, actorB, contact))
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
		return feetContactCount > 0 && currentJumpTimeout == 0;
	}
	
	public void jump()
	{
		if(!canJump()) return;
		
		physicsCtrl.setVelocity(0.0f, jumpForce, false, true);
		currentJumpTimeout = jumpTimeout;
	}
	
	private boolean contactWithFeetSensor(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		boolean contactWithFeet = contact.getFixtureA() == feet || contact.getFixtureB() == feet;
		boolean contactWithActor = actorA == actorWithFeet || actorB == actorWithFeet;
		boolean notSensors = !contact.getFixtureA().isSensor() || !contact.getFixtureB().isSensor();
		return contactWithFeet && contactWithActor && notSensors;
	}

	private float calculateWidth(ZootActor actor)
	{
		if(feetWidth == 0)
		{
			return actor.getWidth() / 2.0f;
		}
		return (feetWidth / 2.0f) * scene.getUnitScale();
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

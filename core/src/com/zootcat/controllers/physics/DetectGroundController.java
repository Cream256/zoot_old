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
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class DetectGroundController extends PhysicsCollisionController
{
	public static final float SENSOR_HEIGHT_PERCENT = 0.2f;
	
	@CtrlParam private int sensorWidth = 0;		
	@CtrlParam(global = true) private ZootScene scene;		
	@CtrlDebug private int contactCount = 0;
	@CtrlDebug private boolean isOnGround = false;
		
	private Fixture feet;
	private ZootEvent groundEvent;
	private ZootActor actorWithSensor;
	private PhysicsBodyController physicsCtrl;
		
	@Override
	public void init(ZootActor actor)
	{
		groundEvent = new ZootEvent(ZootEventType.Ground);
	}

	@Override
	public void onAdd(ZootActor actor)
	{
		super.onAdd(actor);
		actorWithSensor = actor;
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
		boolean nowOnGround = contactCount > 0;
		
		if(nowOnGround && !isOnGround)
		{
			groundEvent.reset();
			groundEvent.setType(ZootEventType.Ground);
			actorWithSensor.fire(groundEvent);			
		}
		isOnGround = nowOnGround;
	}
	
	@Override
	public void beginContact(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		if(isContactWithGroundSensor(actorA, actorB, contact)) ++contactCount;
	}

	@Override
	public void endContact(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		if(isContactWithGroundSensor(actorA, actorB, contact)) --contactCount;
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
	
	public boolean isOnGround()
	{
		return isOnGround;
	}

	private boolean isContactWithGroundSensor(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		boolean contactWithFeet = contact.getFixtureA() == feet || contact.getFixtureB() == feet;
		boolean contactWithActor = actorA == actorWithSensor || actorB == actorWithSensor;
		boolean notSensors = !contact.getFixtureA().isSensor() || !contact.getFixtureB().isSensor();
		return contactWithFeet && contactWithActor && notSensors;
	}

	private float calculateWidth(ZootActor actor)
	{
		if(sensorWidth == 0)
		{
			return actor.getWidth() / 2.0f;
		}
		return (sensorWidth / 2.0f) * scene.getUnitScale();
	}
	
	private Shape createFeetShape(ZootActor actor)
	{
		Vector2 center = new Vector2(0.0f, -actor.getHeight() / 2.0f);		
		
		float feetWidth = calculateWidth(actor);
		float feetHeight = (actor.getHeight() * SENSOR_HEIGHT_PERCENT) / 2.0f;	//10% of actor height
		
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

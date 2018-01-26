package com.zootcat.controllers.physics;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.physics.ZootPhysics;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;
import com.zootcat.testing.ActorEventCounterListener;

public class DetectGroundControllerTest
{		
	private static final float ACTOR_WIDTH = 20.0f;
	private static final float ACTOR_HEIGHT = 10.0f;
	private static final float SCENE_UNIT_SCALE = 1.0f;
	
	private ZootActor actor;
	private ZootActor otherActor;
	private PhysicsBodyController otherActorPhysicsCtrl;
	
	private ZootScene sceneMock;
	private ZootPhysics physics;
	private PhysicsBodyController physicsCtrl;
	private DetectGroundController groundCtrl;
	private ActorEventCounterListener eventCounter;
	
	@Before
	public void setup()
	{
		//create physics
		physics = new ZootPhysics();
		
		//create scene
		sceneMock = mock(ZootScene.class);
		when(sceneMock.getPhysics()).thenReturn(physics);
		when(sceneMock.getUnitScale()).thenReturn(SCENE_UNIT_SCALE);		
		
		//create main actor
		actor = new ZootActor();
		actor.setSize(ACTOR_WIDTH, ACTOR_HEIGHT);
		
		eventCounter = new ActorEventCounterListener();
		actor.addListener(eventCounter);
		
		physicsCtrl = new PhysicsBodyController();
		ControllerAnnotations.setControllerParameter(physicsCtrl, "scene", sceneMock);
		
		physicsCtrl.init(actor);		
		actor.addController(physicsCtrl);
		
		//create other actor
		otherActor = new ZootActor();
		otherActor.setSize(ACTOR_WIDTH, ACTOR_HEIGHT);
		
		otherActorPhysicsCtrl = new PhysicsBodyController();
		ControllerAnnotations.setControllerParameter(otherActorPhysicsCtrl, "scene", sceneMock);
		
		otherActorPhysicsCtrl.init(actor);
		otherActor.addController(otherActorPhysicsCtrl);
		
		//create ground detector controller
		groundCtrl = new DetectGroundController();
		ControllerAnnotations.setControllerParameter(groundCtrl, "scene", sceneMock);	
	}
	
	@After
	public void tearDown()
	{
		physics.dispose();
		physics = null;
	}
	
	@Test
	public void onAddTest()
	{
		//given
		assertEquals("Should have only body fixture", 1, physicsCtrl.getFixtures().size());
		
		//when
		groundCtrl.init(actor);
		groundCtrl.onAdd(actor);

		//then
		assertEquals("Sensor fixture should be present", 2, physicsCtrl.getFixtures().size());
		
		Fixture sensorFixture = physicsCtrl.getFixtures().get(1);
		assertTrue("Fixture should be a sensor", sensorFixture.isSensor());		
		assertEquals("Fixture should be assigned to proper body", physicsCtrl.getBody(), sensorFixture.getBody());
		assertEquals("Fixture shape should be polygon", Type.Polygon, sensorFixture.getShape().getType());
		
		assertTrue("Actor should have registered ground detector listener", actor.getListeners().contains(groundCtrl, true));
	}
	
	@Test
	public void onAddWithCustomWidthShouldCreateFixtureUsingCustomWidthTest()
	{
		//when
		final int customWidth = 128;
		ControllerAnnotations.setControllerParameter(groundCtrl, "sensorWidth", customWidth);
		
		groundCtrl.init(actor);
		groundCtrl.onAdd(actor);
		PolygonShape fixtureShape = (PolygonShape)physicsCtrl.getFixtures().get(1).getShape();
		
		//then
		Vector2 vertex1 = new Vector2();
		Vector2 vertex2 = new Vector2();
		Vector2 vertex3 = new Vector2();
		fixtureShape.getVertex(0, vertex1);
		fixtureShape.getVertex(1, vertex2);
		fixtureShape.getVertex(2, vertex3);
		assertEquals("Sensor should have actor width", customWidth, Math.abs(vertex1.x - vertex2.x), 0.0f);
		assertEquals("Sensor should have 10% of actor height", ACTOR_HEIGHT * DetectGroundController.SENSOR_HEIGHT_PERCENT, 
					Math.abs(vertex1.y - vertex3.y), 0.0f);
	}
	
	@Test
	public void onAddWithZeroWidthShouldCreateFixtureUsingActorWidthTest()
	{
		//when
		groundCtrl.init(actor);
		groundCtrl.onAdd(actor);
		PolygonShape fixtureShape = (PolygonShape)physicsCtrl.getFixtures().get(1).getShape();
		
		//then
		Vector2 vertex1 = new Vector2();
		Vector2 vertex2 = new Vector2();
		Vector2 vertex3 = new Vector2();
		fixtureShape.getVertex(0, vertex1);
		fixtureShape.getVertex(1, vertex2);
		fixtureShape.getVertex(2, vertex3);
		assertEquals("Sensor should have actor width", ACTOR_WIDTH, Math.abs(vertex1.x - vertex2.x), 0.0f);
		assertEquals("Sensor should have 10% of actor height", ACTOR_HEIGHT * DetectGroundController.SENSOR_HEIGHT_PERCENT, 
					Math.abs(vertex1.y - vertex3.y), 0.0f);
	}
	
	@Test
	public void onRemoveTest()
	{
		//when
		groundCtrl.init(actor);
		groundCtrl.onAdd(actor);
		groundCtrl.onRemove(actor);
		
		//then
		assertEquals("Sensor fixture should be removed", 1, physicsCtrl.getFixtures().size());
		assertFalse("Actor should have deregistered ground detector listener", actor.getListeners().contains(groundCtrl, true));
	}
	
	@Test
	public void beginContactTest()
	{
		//given
		Fixture fixtureB = mock(Fixture.class);
		Contact contactMock = mock(Contact.class);
		when(contactMock.getFixtureB()).thenReturn(fixtureB);
		
		//when
		groundCtrl.init(actor);
		groundCtrl.onAdd(actor);
		
		Fixture groundSensorFixture = physicsCtrl.getFixtures().get(1);		
		when(contactMock.getFixtureA()).thenReturn(groundSensorFixture);
		
		//then
		assertTrue("Should detect contact", groundCtrl.beginContact(actor, otherActor, contactMock));
		assertFalse("Should not detect contact between invalid actors", groundCtrl.beginContact(otherActor, otherActor, contactMock));
				
		//when
		when(fixtureB.isSensor()).thenReturn(true);
		
		//then
		assertFalse("Should not detect contact between two sensors", groundCtrl.beginContact(actor, otherActor, contactMock));
	}
	
	@Test
	public void endContactTest()
	{
		//given
		Fixture fixtureB = mock(Fixture.class);
		Contact contactMock = mock(Contact.class);
		when(contactMock.getFixtureB()).thenReturn(fixtureB);
		
		//when
		groundCtrl.init(actor);
		groundCtrl.onAdd(actor);
		
		Fixture groundSensorFixture = physicsCtrl.getFixtures().get(1);		
		when(contactMock.getFixtureA()).thenReturn(groundSensorFixture);
		
		//then
		assertTrue("Should detect contact", groundCtrl.endContact(actor, otherActor, contactMock));
		assertFalse("Should not detect contact between invalid actors", groundCtrl.endContact(otherActor, otherActor, contactMock));
				
		//when
		when(fixtureB.isSensor()).thenReturn(true);
		
		//then
		assertFalse("Should not detect contact between two sensors", groundCtrl.endContact(actor, otherActor, contactMock));
	}
	
	@Test
	public void isOnGroundTest()
	{
		//given
		groundCtrl.init(actor);
		groundCtrl.onAdd(actor);
		
		//then
		assertFalse("Should not be on ground by default", groundCtrl.isOnGround());
		
		//given
		Fixture fixtureB = mock(Fixture.class);
		Contact contactMock = mock(Contact.class);
		when(contactMock.getFixtureB()).thenReturn(fixtureB);
		Fixture groundSensorFixture = physicsCtrl.getFixtures().get(1);		
		when(contactMock.getFixtureA()).thenReturn(groundSensorFixture);
		
		//when
		groundCtrl.beginContact(actor, otherActor, contactMock);
		groundCtrl.onUpdate(1.0f, actor);
		
		//then
		assertTrue("Ground should be detected", groundCtrl.isOnGround());
		assertEquals("Event should be sent to actor", 1, eventCounter.getCount());
		
		//when
		groundCtrl.endContact(actor, otherActor, contactMock);
	    groundCtrl.beginContact(actor, otherActor, contactMock);
		groundCtrl.onUpdate(1.0f, actor);
		
		//then
		assertTrue("Ground still should be detected", groundCtrl.isOnGround());
		assertEquals("No event should be send", 1, eventCounter.getCount());
		
		//when
		groundCtrl.endContact(actor, otherActor, contactMock);
		groundCtrl.onUpdate(1.0f, actor);
		
		//then
		assertFalse("Ground should not be detected", groundCtrl.isOnGround());
		assertEquals("No event should be send", 1, eventCounter.getCount());
		
		//when
		groundCtrl.beginContact(actor, otherActor, contactMock);
		groundCtrl.onUpdate(1.0f, actor);
		
		//then
		assertTrue("Ground should be detected", groundCtrl.isOnGround());
		assertEquals("Event should be sent to actor", 2, eventCounter.getCount());
	}
	
	@Test
	public void preSolveTest()
	{
		assertFalse("Should return false", groundCtrl.preSolve(actor, otherActor, mock(Contact.class), mock(Manifold.class)));
	}
	
	@Test
	public void postSolveTest()
	{
		assertFalse("Should return false", groundCtrl.postSolve(actor, otherActor, mock(ContactImpulse.class)));
	}

}

package com.zootcat.fsm.states;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zootcat.controllers.gfx.AnimatedSpriteController;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.scene.ZootActor;

public class IdleStateTest
{
	private ZootActor actor;
	private IdleState idleState;	
	@Mock private AnimatedSpriteController animatedSpriteCtrlMock;
	@Mock private PhysicsBodyController physicsBodyCtrlMock;
		
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		
		idleState = new IdleState();
		actor = new ZootActor();
		actor.addController(animatedSpriteCtrlMock);
		actor.addController(physicsBodyCtrlMock);		
		actor.getStateMachine().addState(new WalkState());
		actor.getStateMachine().addState(new JumpState());
	}
	
	@Test
	public void getIdTest()
	{
		assertEquals(IdleState.ID, idleState.getId());
	}
	
	@Test
	public void onEnterShouldSetIdleAnimationTest()
	{
		idleState.onEnter(actor, null);
		verify(animatedSpriteCtrlMock).setAnimation(idleState.getName());
	}
	
	@Test
	public void onEnterShouldZeroHoritontalVelocityActorTest()
	{
		idleState.onEnter(actor, null);
		verify(physicsBodyCtrlMock).setVelocity(0.0f, 0.0f, true, false);
	}
	
	@Test
	public void handleWalkEventTest()
	{
		assertTrue(idleState.handle(createEvent(ZootEventType.WalkRight)));
		assertEquals(WalkState.ID, actor.getStateMachine().getCurrentState().getId());
		assertTrue(idleState.handle(createEvent(ZootEventType.WalkLeft)));
		assertEquals(WalkState.ID, actor.getStateMachine().getCurrentState().getId());
	}
		
	@Test
	public void handleJumpEventTest()
	{
		assertTrue(idleState.handle(createEvent(ZootEventType.Jump)));
		assertEquals(JumpState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	private ZootEvent createEvent(ZootEventType type)
	{
		ZootEvent event = new ZootEvent(type);
		event.setTarget(actor);
		return event;
	}
	
}

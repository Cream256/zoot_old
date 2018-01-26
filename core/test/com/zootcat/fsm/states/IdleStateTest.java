package com.zootcat.fsm.states;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.events.ZootEventType;
import com.zootcat.testing.ZootStateTestCase;

public class IdleStateTest extends ZootStateTestCase
{
	private IdleState idleState;	
		
	@Before
	public void setup()
	{		
		super.setup();
		idleState = new IdleState();
	}
	
	@Test
	public void getIdTest()
	{
		assertEquals(IdleState.ID, idleState.getId());
	}
	
	@Test
	public void onEnterShouldSetIdleAnimationTest()
	{
		//first invocation is done by DefaultStateMachineController
		verify(animatedSpriteCtrlMock, times(1)).setAnimation(idleState.getName());
		idleState.onEnter(actor, null);
		verify(animatedSpriteCtrlMock, times(2)).setAnimation(idleState.getName());
	}
	
	@Test
	public void onEnterShouldZeroHoritontalVelocityActorTest()
	{
		//first invocation is done by DefaultStateMachineController
		verify(animatedSpriteCtrlMock, times(1)).setAnimation(idleState.getName());
		idleState.onEnter(actor, null);
		verify(physicsBodyCtrlMock, times(2)).setVelocity(0.0f, 0.0f, true, false);
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
}

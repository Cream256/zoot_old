package com.zootcat.fsm.states;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zootcat.controllers.gfx.AnimatedSpriteController;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.scene.ZootActor;

public class IdleStateTest
{
	private ZootActor actor;
	private IdleState idleState;	
	@Mock private AnimatedSpriteController animatedSpriteCtrlMock;
		
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		
		idleState = new IdleState();
		actor = new ZootActor();
		actor.addController(animatedSpriteCtrlMock);
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
	public void handleWalkEventTest()
	{
		assertTrue(idleState.handle(createEvent(ZootEventType.WalkRight)));
		assertEquals(WalkState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleJumpEventTest()
	{
		assertTrue(idleState.handle(createEvent(ZootEventType.Jump)));
		assertEquals(JumpState.ID, actor.getStateMachine().getCurrentState().getId());
		assertEquals(0, 1);
	}
	
	private ZootEvent createEvent(ZootEventType type)
	{
		ZootEvent event = new ZootEvent(type);
		event.setTarget(actor);
		return event;
	}
	
}

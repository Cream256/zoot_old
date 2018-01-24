package com.zootcat.fsm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.scene.ZootActor;

public class StateMachineTest
{
	private StateMachine sm;
	private ZootActor owner;
	
	@Before
	public void setup()
	{
		sm = new StateMachine();
		owner = new ZootActor();
	}
	
	@Test
	public void addStateTest()
	{
		State state1 = new NamedState("state1");
		State state2 = new NamedState("state2");
		
		sm.addState(state1);
		assertEquals(1, sm.getStates().size());
		assertTrue(sm.getStates().contains(state1));
		
		sm.addState(state2);
		assertEquals(2, sm.getStates().size());
		assertTrue(sm.getStates().contains(state1));
		assertTrue(sm.getStates().contains(state2));
		
		sm.addState(state1);
		assertEquals("Should not add twice the same state", 2, sm.getStates().size());
		assertTrue(sm.getStates().contains(state1));
		assertTrue(sm.getStates().contains(state2));
	}
	
	@Test
	public void getStateByIdTest()
	{
		//given
		State state1 = mock(State.class);
		State state2 = mock(State.class);
		when(state1.getId()).thenReturn(100);
		when(state2.getId()).thenReturn(200);
		
		//when
		sm.addState(state1);
		sm.addState(state2);
		
		//then
		assertEquals(state1, sm.getStateById(100));
		assertEquals(state2, sm.getStateById(200));		
	}
	
	@Test(expected = RuntimeZootException.class)
	public void getStateByIdShouldThrowOnNotExistingIdTest()
	{
		sm.getStateById(256);
	}
	
	@Test
	public void initTest()
	{
		//given
		State initialState = mock(State.class);
		
		//when
		sm.setOwner(owner);
		sm.init(initialState);
		
		//then
		verify(initialState, times(1)).onEnter(owner);
		assertEquals("Initial state should be current", initialState, sm.getCurrentState());		
		assertEquals("Previous state should reset", null, sm.getPreviousState());
		assertEquals("Initial state should be added to state machine", 1, sm.getStates().size());
		assertTrue("Initial state should be added to state machine", sm.getStates().contains(initialState));
	}
	
	@Test
	public void setOwnerTest()
	{
		sm.setOwner(owner);
		assertEquals(owner, sm.getOwner());
		sm.setOwner(null);
		assertEquals(null, sm.getOwner());
	}
	
	@Test(expected = RuntimeZootException.class)
	public void changeStateShouldThrowOnNullStateTest()
	{
		sm.changeState(null);
	}
	
	@Test
	public void changeStateTest()
	{
		//given
		State firstState = mock(State.class);
		State secondState = mock(State.class);
		when(firstState.getId()).thenReturn(1);				
		when(secondState.getId()).thenReturn(2);
		
		sm.setOwner(owner);
		sm.init(firstState);
		sm.addState(secondState);
		
		//when
		sm.changeState(secondState);
		
		//then
		verify(firstState, times(1)).onLeave(owner);
		verify(secondState, times(1)).onEnter(owner);		
		assertEquals(secondState, sm.getCurrentState());
		assertEquals(firstState, sm.getPreviousState());
	}
	
	@Test
	public void handleTest()
	{
		//given
		State state = mock(State.class);
		sm.init(state);	
		
		//when
		when(state.handle(anyObject())).thenReturn(true);		

		//then
		assertTrue(sm.handle(new Event()));
		verify(state, times(1)).handle(anyObject());
		
		//when
		when(state.handle(anyObject())).thenReturn(false);		

		//then
		assertFalse(sm.handle(new Event()));
		verify(state, times(2)).handle(anyObject());
	}
	
	@Test
	public void updateTest()
	{
		//given
		State state = mock(State.class);
		sm.init(state);
		sm.setOwner(owner);
		
		//when
		sm.update(0.0f);
		
		//then
		verify(state, times(1)).update(owner, 0.0f);
		
		//when
		sm.update(0.33f);

		//then
		verify(state, times(1)).update(owner, 0.33f);
	}
}

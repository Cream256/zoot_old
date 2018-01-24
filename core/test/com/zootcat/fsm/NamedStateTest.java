package com.zootcat.fsm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Test;

import com.zootcat.events.ZootEvent;
import com.zootcat.fsm.states.NamedState;
import com.zootcat.scene.ZootActor;

public class NamedStateTest
{
	@Test
	public void hashCodeTest()
	{
		NamedState state1 = new NamedState("test");
		NamedState state2 = new NamedState("test");
		NamedState state3 = new NamedState("TEST");
		
		assertEquals(state1.hashCode(), state2.hashCode());
		assertFalse(state2.hashCode() == state3.hashCode());
	}
		
	@Test
	public void equalsTest()
	{
		NamedState state1 = new NamedState("test");
		NamedState state2 = new NamedState("test");
		NamedState state3 = new NamedState("TEST");
		
		assertTrue(state1.equals(state2));
		assertTrue(state1.equals(state1));
		assertTrue(state2.equals(state1));
		assertTrue(state2.equals(state2));
		assertFalse(state1.equals(state3));
		assertFalse(state2.equals(state3));
		assertFalse(state3.equals(state1));
		assertFalse(state3.equals(state2));
		
		assertFalse(state1.equals(1));
		assertFalse(state1.equals(null));
		assertFalse(state1.equals("this is a string"));		
	}
	
	@Test
	public void toStringTest()
	{
		assertEquals("ABC", new NamedState("ABC").toString());
		assertEquals("test", new NamedState("test").toString());
		assertEquals("", new NamedState("").toString());		
	}
	
	@Test
	public void handleTest()
	{
		assertFalse("NamedState should always return false", new NamedState("").handle(new ZootEvent()));
	}
	
	@Test
	public void actionsShouldNotCauseAnySideEffectsTest()
	{
		//given
		NamedState state = new NamedState("test");
		ZootActor actor = mock(ZootActor.class);
		
		//when
		state.onEnter(actor);
		state.onUpdate(actor, 0.0f);
		state.onLeave(actor);
		
		//then
		verifyZeroInteractions(actor);		
	}
}

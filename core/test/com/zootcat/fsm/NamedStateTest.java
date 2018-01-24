package com.zootcat.fsm;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.badlogic.gdx.scenes.scene2d.Event;
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
	public void getIdTest()
	{
		NamedState state1 = new NamedState("abc");
		NamedState state2 = new NamedState("def");
		
		assertEquals("abc".hashCode(), state1.getId());
		assertEquals("def".hashCode(), state2.getId());
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
		assertFalse("NamedState should always return false", new NamedState("").handle(new Event()));
	}
	
	@Test
	public void actionsShouldNotCauseAnySideEffectsTest()
	{
		//given
		NamedState state = new NamedState("test");
		ZootActor actor = mock(ZootActor.class);
		
		//when
		state.onEnter(actor);
		state.update(actor, 0.0f);
		state.onLeave(actor);
		
		//then
		verifyZeroInteractions(actor);		
	}
}

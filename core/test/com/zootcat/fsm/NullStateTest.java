package com.zootcat.fsm;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.zootcat.scene.ZootActor;

public class NullStateTest
{
	private NullState state;
	
	@Before
	public void setup()
	{
		state = NullState.INSTANCE;
	}
	
	@Test
	public void getIdTest()
	{
		assertEquals(0, state.getId());		
	}
	
	@Test
	public void handleTest()
	{
		assertFalse(state.handle(new Event()));
		assertFalse(state.handle(null));
	}
	
	@Test
	public void actionsShouldNotCauseAnySideEffectsTest()
	{
		//given
		ZootActor actor = mock(ZootActor.class);
		
		//when
		state.onEnter(actor);
		state.update(actor, 0.0f);
		state.onLeave(actor);
		
		//then
		verifyZeroInteractions(actor);		
	}
}

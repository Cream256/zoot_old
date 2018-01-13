package com.zootcat.controllers.input;

import static org.junit.Assert.*;

import org.junit.Test;

import com.zootcat.scene.ZootActor;

public class InputControllerTest 
{	
	@Test
	public void onAddTest()
	{
		//given
		ZootActor actor = new ZootActor();
		InputController ctrl = new InputController();
		
		//when
		ctrl.onAdd(actor);
		
		//then
		assertEquals(1, actor.getListeners().size);
		assertEquals(ctrl, actor.getListeners().get(0));
	}
	
	@Test
	public void onRemoveTest()
	{
		//given
		ZootActor actor = new ZootActor();
		InputController ctrl = new InputController();
		
		
		//when
		actor.addListener(ctrl);

		//then
		assertEquals(1, actor.getListeners().size);
		
		//when
		ctrl.onRemove(actor);
		
		//then
		assertEquals(0, actor.getListeners().size);
	}
	
}

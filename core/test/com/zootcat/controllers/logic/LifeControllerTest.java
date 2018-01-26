package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.scene.ZootActor;
import com.zootcat.testing.ActorEventCounterListener;

public class LifeControllerTest
{
	private LifeController ctrl;	
	
	@Before
	public void setup()
	{
		ctrl = new LifeController();	
	}
	
	@Test
	public void defaultsTest()
	{
		assertEquals(LifeController.DEFAULT_LIFE, ctrl.getLife());
		assertEquals(LifeController.DEFAULT_LIFE, ctrl.getMaxLife());		
	}
	
	@Test
	public void isAliveTest()
	{
		ctrl.setLife(3);
		assertTrue(ctrl.isAlive());
		
		ctrl.setLife(1);
		assertTrue(ctrl.isAlive());
		
		ctrl.setLife(0);
		assertFalse(ctrl.isAlive());
		
		ctrl.setLife(-1);
		assertFalse(ctrl.isAlive());
	}
	
	@Test
	public void addLifeTest()
	{
		ctrl.addLife(-1);
		assertEquals(2, ctrl.getLife());
		
		ctrl.addLife(1);
		assertEquals(3, ctrl.getLife());
		
		ctrl.addLife(ctrl.getMaxLife());
		assertEquals(ctrl.getMaxLife(), ctrl.getLife());
		
		ctrl.addLife(-Integer.MAX_VALUE);
		assertEquals(0, ctrl.getLife());
	}
	
	@Test
	public void addMaxLifeTest()
	{
		ctrl.addMaxLife(1);
		assertEquals(4, ctrl.getMaxLife());
		
		ctrl.addMaxLife(-1);
		assertEquals(3, ctrl.getMaxLife());
		
		ctrl.addMaxLife(0);
		assertEquals(3, ctrl.getMaxLife());
	}
	
	@Test
	public void setLifeTest()
	{		
		ctrl.setLife(0);
		assertEquals(0, ctrl.getLife());
		
		ctrl.setLife(1);
		assertEquals(1, ctrl.getLife());
		
		ctrl.setLife(ctrl.getMaxLife());
		assertEquals(ctrl.getMaxLife(), ctrl.getLife());
		
		ctrl.setLife(ctrl.getMaxLife() + 1);
		assertEquals("Should not be able to set more than max life", ctrl.getMaxLife(), ctrl.getLife());
		
		ctrl.setLife(-1);
		assertEquals("Should not be able to set negative value", 0, ctrl.getLife());
	}
	
	@Test
	public void setMaxLifeTest()
	{		
		ctrl.setMaxLife(10);
		assertEquals(10, ctrl.getMaxLife());
		
		ctrl.setMaxLife(0);
		assertEquals("Should not be able to set max life below 1", 1, ctrl.getMaxLife());
		
		ctrl.setMaxLife(-1);
		assertEquals("Should not be able to set max life below 1", 1, ctrl.getMaxLife());
				
		ctrl.setMaxLife(10);
		ctrl.setLife(10);
		ctrl.setMaxLife(5);
		assertEquals(5, ctrl.getMaxLife());
		assertEquals("Life should be clamped to max life vaule", 5, ctrl.getLife());
		
		ctrl.setLife(5);
		ctrl.setMaxLife(0);
		assertEquals(1, ctrl.getMaxLife());
		assertEquals("Life should be clamped to max life vaule", 1, ctrl.getLife());
	}
	
	@Test
	public void updateTest()
	{	
		//given
		ZootActor actor = new ZootActor();		
		ActorEventCounterListener counter = new ActorEventCounterListener();
		actor.addListener(counter);
		ctrl.init(actor);
		ctrl.onAdd(actor);
		
		//when
		ctrl.onUpdate(1.0f, actor);
		
		//then
		assertEquals(0, counter.getCount());
		
		//when
		ctrl.setLife(0);
		ctrl.onUpdate(1.0f, actor);
		
		//then
		assertEquals("Should send dead event", 1, counter.getCount());
		
		//when
		ctrl.onUpdate(1.0f, actor);
		
		//then
		assertEquals("Should not send any event", 1, counter.getCount());
		
		//when
		ctrl.setLife(1);
		ctrl.setLife(0);
		ctrl.onUpdate(1.0f, actor);
		
		//then
		assertEquals("Should send dead event again", 2, counter.getCount());
	}
}

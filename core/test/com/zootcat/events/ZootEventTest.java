package com.zootcat.events;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.zootcat.scene.ZootActor;

public class ZootEventTest
{
	@Test
	public void defaultCtorTest()
	{
		assertEquals(ZootEventType.None, new ZootEvent().getType());		
	}
	
	@Test
	public void typeCtorTest()
	{
		assertEquals(ZootEventType.Attack, new ZootEvent(ZootEventType.Attack).getType());
		assertEquals(ZootEventType.Dead, new ZootEvent(ZootEventType.Dead).getType());
		assertEquals(ZootEventType.Update, new ZootEvent(ZootEventType.Update).getType());
	}
	
	@Test
	public void setTypeTest()
	{
		ZootEvent event = new ZootEvent();
		
		event.setType(ZootEventType.WalkLeft);
		assertEquals(ZootEventType.WalkLeft, event.getType());
		
		event.setType(ZootEventType.RunRight);
		assertEquals(ZootEventType.RunRight, event.getType());
	}
	
	@Test
	public void toStringTest()
	{
		assertEquals(ZootEventType.Attack.toString(), new ZootEvent(ZootEventType.Attack).toString());
		assertEquals(ZootEventType.Dead.toString(), new ZootEvent(ZootEventType.Dead).toString());
		assertEquals(ZootEventType.Update.toString(), new ZootEvent(ZootEventType.Update).toString());
	}
	
	@Test
	public void resetTest()
	{
		ZootEvent event = new ZootEvent(ZootEventType.Attack);
		
		event.reset();
		assertEquals(ZootEventType.None, event.getType());		
	}
	
	@Test
	public void getTargetZootActorTest()
	{
		ZootEvent event = new ZootEvent();		
		assertEquals(null, event.getTargetZootActor());
		
		event.setTarget(new Actor());
		assertEquals("Should return null on Scene2D actor", null, event.getTargetZootActor());
		
		ZootActor zootActor = new ZootActor();
		event.setTarget(zootActor);
		assertEquals(zootActor, event.getTargetZootActor());
	}	
}

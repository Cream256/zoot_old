package com.zootcat.testing;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.events.ZootEvent;

public class ActorEventCounterListener implements EventListener
{
	private int count = 0;
	private Event lastEvent = null;
	
	@Override
	public boolean handle(Event event)
	{
		++count;
		
		boolean zootEvent = ClassReflection.isInstance(ZootEvent.class, event);
		lastEvent = zootEvent ? new ZootEvent((ZootEvent)event) : event;
		
		return true;
	}
	
	public int getCount()
	{
		return count;
	}
	
	public Event getLastEvent()
	{
		return lastEvent;
	}
}

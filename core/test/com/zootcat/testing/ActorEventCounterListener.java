package com.zootcat.testing;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public class ActorEventCounterListener implements EventListener
{
	private int count = 0;
	
	@Override
	public boolean handle(Event event)
	{
		++count;
		return true;
	}
	
	public int getCount()
	{
		return count;
	}
}

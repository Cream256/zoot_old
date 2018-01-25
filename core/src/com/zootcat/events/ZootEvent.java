package com.zootcat.events;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.scene.ZootActor;

public class ZootEvent extends Event
{
	private ZootEventType type;
	
	public ZootEvent()
	{
		this(ZootEventType.None);
	}
	
	public ZootEvent(ZootEventType type)
	{
		this.type = type;
	}
		
	public ZootEventType getType()
	{
		return type;
	}
	
	public void setType(ZootEventType type)
	{
		this.type = type;
	}
	
	public ZootActor getTargetZootActor()
	{
		Actor target = getTarget();
		if(ClassReflection.isInstance(ZootActor.class, target))
		{
			return (ZootActor)target;
		}
		return null;
	}
	
	@Override
	public void reset() 
	{
		super.reset();
		type = ZootEventType.None;
	}
	
	@Override
	public String toString() 
	{
		return type.toString();
	}
}

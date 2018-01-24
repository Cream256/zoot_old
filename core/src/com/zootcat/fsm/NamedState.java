package com.zootcat.fsm;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.scene.ZootActor;

public class NamedState implements State
{
	private String name;
	
	public NamedState(String name)
	{
		this.name = name;
	}
	
	@Override
	public int getId()
	{
		return name.hashCode();
	}

	@Override
	public void onEnter(ZootActor actor)
	{
		//noop
	}

	@Override
	public void onLeave(ZootActor actor)
	{
		//noop
	}

	@Override
	public void update(ZootActor actor, float delta)
	{
		//noop
	}

	@Override
	public boolean handle(Event event)
	{
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return getId();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj) return true;
		if(!ClassReflection.isInstance(State.class, obj)) return false;
		return getId() == ((State)obj).getId();
	}
	
	@Override
	public String toString()
	{
		return name;
	}
}

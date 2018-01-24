package com.zootcat.fsm.states;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.events.ZootEvent;
import com.zootcat.fsm.ZootState;
import com.zootcat.fsm.ZootStateMachine;
import com.zootcat.scene.ZootActor;

public class NamedState implements ZootState
{
	private String name;
	
	public NamedState(String name)
	{
		this.name = name;
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
	public void onUpdate(ZootActor actor, float delta)
	{
		//noop
	}

	@Override
	public boolean handle(ZootEvent event)
	{
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj) return true;
		if(!ClassReflection.isInstance(ZootState.class, obj)) return false;
		return hashCode() == obj.hashCode();
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
	protected void changeState(ZootEvent event, Class<? extends ZootState> newState)
	{
		ZootStateMachine sm = event.getTargetZootActor().getStateMachine();
		sm.changeState(sm.getStateByClass(newState));
	}
}

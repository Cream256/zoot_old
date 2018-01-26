package com.zootcat.fsm.states;

import com.zootcat.events.ZootEvent;

public class DeadState extends AnimationBasedState
{
	public static final int ID = DeadState.class.hashCode();
			
	public DeadState()
	{
		super("Dead");
	}
	
	@Override
	public boolean handle(ZootEvent event)
	{	
		return true;
	}
	
	@Override
	public int getId()
	{
		return ID;
	}
}

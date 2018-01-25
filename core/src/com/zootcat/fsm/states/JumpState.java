package com.zootcat.fsm.states;

import com.zootcat.events.ZootEvent;
import com.zootcat.scene.ZootActor;

//TODO add test
public class JumpState extends BasicState
{
	public static final int ID = JumpState.class.hashCode();
	
	public JumpState()
	{
		super("Jump");
	}
	
	@Override
	public void onEnter(ZootActor actor)
	{
		setAnimationBasedOnStateName(actor);
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

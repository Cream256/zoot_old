package com.zootcat.fsm.states;

import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.scene.ZootActor;

//TODO add test
public class WalkState extends BasicState
{	
	public static final int ID = WalkState.class.hashCode();
	
	public WalkState()
	{
		super("Walk");
	}

	@Override
	public void onEnter(ZootActor actor)
	{
		setAnimationBasedOnStateName(actor);
	}
	
	@Override
	public boolean handle(ZootEvent event)
	{
		if(event.getType() == ZootEventType.Stop)
		{
			changeState(event, IdleState.ID);
		}
		return true;
	}
	
	@Override
	public int getId()
	{
		return ID;
	}
	
}

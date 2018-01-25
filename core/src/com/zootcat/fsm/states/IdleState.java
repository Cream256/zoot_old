package com.zootcat.fsm.states;

import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.scene.ZootActor;

public class IdleState extends BasicState
{	
	public static final int ID = IdleState.class.hashCode();
	
	public IdleState()
	{
		super("Idle");
	}
	
	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{
		setAnimationBasedOnStateName(actor);
		controllerAction(actor, PhysicsBodyController.class, (ctrl) -> ctrl.setVelocity(0.0f, 0.0f, true, false));
	}
	
	@Override
	public boolean handle(ZootEvent event)
	{		
		if(event.getType() == ZootEventType.WalkRight || event.getType() == ZootEventType.WalkLeft)
		{		
			changeState(event, WalkState.ID);			
		}
		else if(event.getType() == ZootEventType.Jump)
		{
			changeState(event, JumpState.ID);
		}
		
		return true;
	}
	
	@Override
	public int getId()
	{
		return ID;
	}
}

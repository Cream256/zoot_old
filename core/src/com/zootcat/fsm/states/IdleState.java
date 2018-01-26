package com.zootcat.fsm.states;

import com.zootcat.controllers.gfx.DirectionController;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

public class IdleState extends BasicState
{	
	public static final int ID = IdleState.class.hashCode();
	
	private ZootDirection actorDirection;
	
	public IdleState()
	{
		super("Idle");
	}
	
	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{
		setAnimationBasedOnStateName(actor);
		actor.controllerAction(PhysicsBodyController.class, (ctrl) -> ctrl.setVelocity(0.0f, 0.0f, true, false));
		
		actorDirection = ZootDirection.None;
		actor.controllerAction(DirectionController.class, c -> actorDirection = c.getDirection());
	}
	
	@Override
	public boolean handle(ZootEvent event)
	{		
		if(event.getType() == ZootEventType.WalkRight || event.getType() == ZootEventType.WalkLeft)
		{		
			ZootDirection eventDirection = ZootStateUtils.getDirectionFromEvent(event);
			int nextStateId = eventDirection == actorDirection || actorDirection == ZootDirection.None ? WalkState.ID : TurnState.ID;
			changeState(event, nextStateId);	
		}
		else if(event.getType() == ZootEventType.Jump)
		{		
			changeState(event, JumpState.ID);
		}
		else if(event.getType() == ZootEventType.Fall)
		{
			changeState(event, FallState.ID);
		}
		return true;
	}
	
	@Override
	public int getId()
	{
		return ID;
	}
}

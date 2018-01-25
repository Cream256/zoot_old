package com.zootcat.fsm.states;

import com.zootcat.controllers.gfx.DirectionController;
import com.zootcat.controllers.physics.MoveableController;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

//TODO add test
public class WalkState extends BasicState
{	
	public static final int ID = WalkState.class.hashCode();
	
	private ZootDirection moveDirection = ZootDirection.None;
	
	public WalkState()
	{
		super("Walk");
	}

	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{
		setAnimationBasedOnStateName(actor);
		moveDirection = ZootStateUtils.getDirectionFromEvent(event);
		actor.controllerAction(DirectionController.class, (ctrl) -> ctrl.setDirection(moveDirection));
	}
	
	@Override
	public void onUpdate(ZootActor actor, float delta)
	{
		actor.controllerAction(MoveableController.class, (mvCtrl) -> mvCtrl.move(moveDirection));		
	}
	
	@Override
	public boolean handle(ZootEvent event)
	{
		if(event.getType() == ZootEventType.Stop)
		{
			changeState(event, IdleState.ID);
		}
		if(event.getType() == ZootEventType.Jump)
		{
			changeState(event, JumpState.ID);
		}
		if(event.getType() == ZootEventType.Fall)
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

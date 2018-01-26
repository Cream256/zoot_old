package com.zootcat.fsm.states;

import com.zootcat.controllers.gfx.DirectionController;
import com.zootcat.controllers.physics.MoveableController;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

public class JumpState extends BasicState
{
	public static final int ID = JumpState.class.hashCode();
		
	public JumpState()
	{
		super("Jump");
	}
	
	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{
		setAnimationBasedOnStateName(actor);
		actor.controllerAction(MoveableController.class, (ctrl) -> ctrl.jump());
	}
	
	@Override
	public void onUpdate(ZootActor actor, float delta)
	{
		//noop
	}

	@Override
	public boolean handle(ZootEvent event)
	{
		if(event.getType() == ZootEventType.Fall)
		{
			changeState(event, FallState.ID);
		}		
		else if(event.getType() == ZootEventType.Ground)
		{
			changeState(event, IdleState.ID);
		}
		else if(ZootStateUtils.isMoveEvent(event))
		{
			ZootDirection dir = ZootStateUtils.getDirectionFromEvent(event);
			event.getTargetZootActor().controllerAction(MoveableController.class, (ctrl) -> ctrl.move(dir));
			event.getTargetZootActor().controllerAction(DirectionController.class, (ctrl) -> ctrl.setDirection(dir));
		}
		return true;
	}
	
	@Override
	public int getId()
	{
		return ID;
	}
}

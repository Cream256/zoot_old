package com.zootcat.fsm.states;

import com.zootcat.controllers.gfx.DirectionController;
import com.zootcat.controllers.physics.JumpableController;
import com.zootcat.controllers.physics.MoveableController;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

//TODO add test
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
		JumpableController jumpableCtrl = actor.tryGetController(JumpableController.class);
		if(jumpableCtrl != null)
		{
			jumpableCtrl.jump();
		}
	}
	
	@Override
	public boolean handle(ZootEvent event)
	{
		if(event.getType() == ZootEventType.Land)
		{
			changeState(event, IdleState.ID);
		}
		else if(isMoveEvent(event))
		{
			ZootDirection dir = getDirectionFromEvent(event);
			controllerAction(event.getTargetZootActor(), MoveableController.class, (ctrl) -> ctrl.move(dir));
			controllerAction(event.getTargetZootActor(), DirectionController.class, (ctrl) -> ctrl.setDirection(dir));
		}
		
		return true;
	}
	
	@Override
	public int getId()
	{
		return ID;
	}
}

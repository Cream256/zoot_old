package com.zootcat.fsm.states;

import com.zootcat.controllers.gfx.AnimatedSpriteController;
import com.zootcat.controllers.gfx.DirectionController;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.gfx.ZootAnimation;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

public class TurnState extends BasicState
{
	public static final int ID = TurnState.class.hashCode();
	
	private ZootDirection direction;
	private ZootAnimation turnAnimation;
	
	public TurnState()
	{
		super("Turn");
	}
	
	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{		
		//get turn animation
		turnAnimation = null;
		actor.controllerAction(AnimatedSpriteController.class, ctrl -> 
		{
			turnAnimation = ctrl.getAnimation(getName());
			if(turnAnimation != null) ctrl.setAnimation(getName());
		});
		
		//get actor direction
		direction = ZootDirection.None;
		actor.controllerAction(DirectionController.class, ctrl -> direction = ctrl.getDirection());
	}
	
	@Override
	public void onLeave(ZootActor actor, ZootEvent event)
	{
		actor.controllerAction(DirectionController.class, ctrl -> ctrl.setDirection(direction.invert()));
	}
	
	@Override
	public void onUpdate(ZootActor actor, float delta)
	{
		if(turnAnimation == null || turnAnimation.isFinished())
		{
			changeState(actor, IdleState.ID);
		}
	}
	
	@Override
	public boolean handle(ZootEvent event)
	{
		if(event.getType() == ZootEventType.Jump)
		{
			changeState(event.getTargetZootActor(), JumpState.ID);
		}		
		return true;
	}
	
	@Override
	public int getId()
	{
		return ID;
	}
}

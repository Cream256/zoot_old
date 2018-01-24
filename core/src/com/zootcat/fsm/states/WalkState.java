package com.zootcat.fsm.states;

import com.zootcat.controllers.gfx.AnimatedSpriteController;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.scene.ZootActor;

public class WalkState extends NamedState
{	
	public WalkState()
	{
		super("Walk");
	}

	@Override
	public void onEnter(ZootActor actor)
	{
		AnimatedSpriteController ctrl = actor.tryGetController(AnimatedSpriteController.class);
		if(ctrl != null)
		{
			ctrl.setAnimation("WALK");
		}
	}
	
	@Override
	public boolean handle(ZootEvent event)
	{
		if(event.getType() == ZootEventType.Stop)
		{
			changeState(event, IdleState.class);
		}		
		return false;
	}
	
}

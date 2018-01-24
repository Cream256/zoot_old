package com.zootcat.fsm.states;

import com.zootcat.controllers.gfx.AnimatedSpriteController;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.fsm.ZootStateMachine;
import com.zootcat.scene.ZootActor;

public class IdleState extends NamedState
{
	public static final int ID = IdleState.class.hashCode();
	
	public IdleState()
	{
		super("Idle");
	}
	
	@Override
	public void onEnter(ZootActor actor)
	{
		AnimatedSpriteController ctrl = actor.tryGetController(AnimatedSpriteController.class);
		if(ctrl != null)
		{
			ctrl.setAnimation("IDLE");
		}
	}
	
	@Override
	public boolean handle(ZootEvent event)
	{
		if(event.getType() == ZootEventType.Walk)
		{			
			ZootStateMachine sm = event.getTargetZootActor().getStateMachine();
			sm.changeState(sm.getStateByClass(WalkState.class));			
			return true;
		}		
		return false;
	}
	
}

package com.zootcat.fsm.states;

import com.zootcat.controllers.gfx.AnimatedSpriteController;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.events.ZootEvent;
import com.zootcat.gfx.ZootAnimation;
import com.zootcat.scene.ZootActor;

public class AttackState extends BasicState
{
	public static final int ID = AttackState.class.hashCode();
		
	private ZootAnimation attackAnimation;
	
	public AttackState()
	{
		super("Attack");
	}
	
	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{		
		//get attack animation
		attackAnimation = null;
		actor.controllerAction(AnimatedSpriteController.class, ctrl -> 
		{
			attackAnimation = ctrl.getAnimation(getName());
			if(attackAnimation != null) ctrl.setAnimation(getName());
		});
		
		//stop actor
		if(attackAnimation != null)
		{
			actor.controllerAction(PhysicsBodyController.class, (ctrl) -> ctrl.setVelocity(0.0f, 0.0f, true, false));
		}
	}
		
	@Override
	public void onUpdate(ZootActor actor, float delta)
	{
		if(attackAnimation == null || attackAnimation.isFinished())
		{
			changeState(actor, IdleState.ID);
		}
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

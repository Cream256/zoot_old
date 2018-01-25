package com.zootcat.fsm.states;

import java.util.function.Consumer;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.controllers.Controller;
import com.zootcat.controllers.gfx.AnimatedSpriteController;
import com.zootcat.events.ZootEvent;
import com.zootcat.fsm.ZootState;
import com.zootcat.fsm.ZootStateMachine;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

public class BasicState implements ZootState
{
	private final String name;
	
	public BasicState(String name)
	{
		this.name = name;
	}
		
	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{
		//noop
	}

	@Override
	public void onLeave(ZootActor actor, ZootEvent event)
	{
		//noop
	}

	@Override
	public void onUpdate(ZootActor actor, float delta)
	{
		//noop
	}

	@Override
	public boolean handle(ZootEvent event)
	{
		return false;
	}
	
	@Override
	public int getId()
	{
		return name.hashCode();
	}
	
	@Override
	public int hashCode()
	{
		return getId();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj) return true;
		if(!ClassReflection.isInstance(ZootState.class, obj)) return false;
		return getId() == ((ZootState)obj).getId();
	}
		
	@Override
	public String toString()
	{
		return name;
	}
		
	public String getName()
	{
		return name;
	}
	
	//TODO extract to EventUtils
	protected boolean isMoveEvent(ZootEvent event)
	{
		switch(event.getType())
		{
		case WalkRight:
		case WalkLeft:
		case RunRight:
		case RunLeft:
			return true;
		
		default:
			return false;
		}
	}
	
	protected ZootDirection getDirectionFromEvent(ZootEvent event)
	{
		switch(event.getType())
		{
		case RunRight:
		case WalkRight:		
			return ZootDirection.Right;
			
		case RunLeft:
		case WalkLeft:
			return ZootDirection.Left;
				
		default:
			return ZootDirection.None;
		}
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends Controller> void controllerAction(ZootActor actor, Class<T> clazz, Consumer<T> action)
	{
		if(actor == null) return;		
		
		Controller ctrl = actor.tryGetController(clazz);
		if(ctrl != null)
		{
			action.accept((T) ctrl);
		}
	}
	
	protected void changeState(ZootEvent event, int stateId)
	{
		ZootStateMachine sm = event.getTargetZootActor().getStateMachine();
		sm.changeState(sm.getStateById(stateId), event);
	}
	
	protected void setAnimationBasedOnStateName(ZootActor actor)
	{
		controllerAction(actor, AnimatedSpriteController.class, (ctrl) -> ctrl.setAnimation(name));
	}
}

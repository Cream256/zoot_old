package com.zootcat.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.input.ZootBindableInputProcessor;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

public class GameCharacterInputProcessor extends ZootBindableInputProcessor
{
	private ZootActor player;
	private Pool<ZootEvent> eventPool = Pools.get(ZootEvent.class);	
		
	public GameCharacterInputProcessor(ZootActor player)
	{
		setPlayer(player);
		bindUp(Input.Keys.RIGHT, () -> stop());
		bindDown(Input.Keys.RIGHT, () -> run(ZootDirection.Right));
		
		bindUp(Input.Keys.LEFT, () -> stop());
		bindDown(Input.Keys.LEFT, () -> run(ZootDirection.Left));
		
		bindDown(Input.Keys.SPACE, () -> jump());
		
		bindDown(Input.Keys.CONTROL_LEFT, () -> attack());
		bindDown(Input.Keys.CONTROL_RIGHT, () -> attack());
		
		bindDown(Input.Keys.F8, () -> hurt());
	}
	
	public void setPlayer(ZootActor actor)
	{
		player = actor;
	}
	
	private boolean hurt()
	{
		sendEventToActor(player, ZootEventType.Hurt, -1);
		return true;
	}
	
	private void sendEventToActor(ZootActor actor, ZootEventType eventType)
	{
		sendEventToActor(actor, eventType, null);
	}
	
	private void sendEventToActor(ZootActor actor, ZootEventType eventType, Object userObj)
	{
		ZootEvent event = eventPool.obtain();
		event.setType(eventType);				
		event.setUserObject(userObj);
		actor.fire(event);		
		eventPool.free(event);
	}
	
	private boolean attack()
	{
		sendEventToActor(player, ZootEventType.Attack);
		return true;
	}
	
	private boolean jump()
	{		
		sendEventToActor(player, ZootEventType.Jump);
		return true;
	}
	
	private boolean stop()
	{
		sendEventToActor(player, ZootEventType.Stop);
		return true;
	}
	
	private boolean run(ZootDirection direction)
	{		
		sendEventToActor(player, direction == ZootDirection.Right ? ZootEventType.RunRight : ZootEventType.RunLeft);	
		return true;
	}
	
	private boolean walk(ZootDirection direction)
	{		
		sendEventToActor(player, direction == ZootDirection.Right ? ZootEventType.WalkRight : ZootEventType.WalkLeft);	
		return true;
	}
}

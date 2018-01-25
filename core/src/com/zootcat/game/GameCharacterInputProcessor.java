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
		bindDown(Input.Keys.RIGHT, () -> walk(ZootDirection.Right));
		
		bindUp(Input.Keys.LEFT, () -> stop());
		bindDown(Input.Keys.LEFT, () -> walk(ZootDirection.Left));
		
		bindUp(Input.Keys.SPACE, () -> jump());
	}
	
	public void setPlayer(ZootActor actor)
	{
		player = actor;
	}
				
	private void sendEventToActor(ZootActor actor, ZootEventType eventType)
	{
		ZootEvent event = eventPool.obtain();
		event.setType(eventType);				
		actor.fire(event);
		eventPool.free(event);
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
	
	private boolean walk(ZootDirection direction)
	{		
		sendEventToActor(player, direction == ZootDirection.Right ? ZootEventType.WalkRight : ZootEventType.WalkLeft);	
		return true;
	}
}

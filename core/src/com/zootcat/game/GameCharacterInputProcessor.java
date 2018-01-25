package com.zootcat.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.zootcat.controllers.gfx.DirectionController;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.input.ZootBindableInputProcessor;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

public class GameCharacterInputProcessor extends ZootBindableInputProcessor
{
	private ZootActor player;
	private Pool<ZootEvent> eventPool = Pools.get(ZootEvent.class);
	private float jumpVelocity = 0.0f;
	private float movementVelocity = 0.0f;	
	private PhysicsBodyController bodyController;
	
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
		bodyController = null;
	}
	
	public void setMovementVelocity(float velocity)
	{
		movementVelocity = velocity;
	}
	
	public void setJumpVelocity(float velocity)
	{
		jumpVelocity = velocity;
	}
	
	private PhysicsBodyController getBodyController()
	{
		if(bodyController == null)
		{
			bodyController = player.getController(PhysicsBodyController.class);	
		}		
		return bodyController;
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
		getBodyController().setVelocity(0.0f, jumpVelocity, false, true);		
		sendEventToActor(player, ZootEventType.Jump);
		return true;
	}
	
	private boolean stop()
	{
		getBodyController().setVelocity(0.0f, 0.0f, true, false);
		sendEventToActor(player, ZootEventType.Stop);
		return true;
	}
	
	private boolean walk(ZootDirection direction)
	{
		float vx = direction == ZootDirection.Right ? movementVelocity : -movementVelocity;
		getBodyController().setVelocity(vx, 0.0f, true, false);		
		getDirectionController().setDirection(direction);	
		sendEventToActor(player, ZootEventType.Walk);	
		return true;
	}

	private DirectionController getDirectionController()
	{
		return player.getController(DirectionController.class);		
	}
}

package com.zootcat.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.zootcat.camera.ZootCamera;
import com.zootcat.camera.ZootWindowScrollingStrategy;
import com.zootcat.controllers.physics.PhysicsBodyScale;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.fsm.states.CrouchState;
import com.zootcat.fsm.states.DownState;
import com.zootcat.input.ZootBindableInputProcessor;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

public class GameCharacterInputProcessor extends ZootBindableInputProcessor
{
	private static final float SCROLLING_SPEED = 6.0f;
	private static final float CAMERA_OFFSET_X = 2.0f;
	private static final float CAMERA_OFFSET_Y = 0.0f;
	
	private ZootActor player;
	private ZootWindowScrollingStrategy scrolling;
	
	private Pool<ZootEvent> eventPool = Pools.get(ZootEvent.class);	
		
	public GameCharacterInputProcessor(ZootActor player, ZootCamera camera)
	{
		setPlayer(player);
		bindUp(Input.Keys.RIGHT, () -> stop());
		bindDown(Input.Keys.RIGHT, () -> run(ZootDirection.Right));
		
		bindUp(Input.Keys.LEFT, () -> stop());
		bindDown(Input.Keys.LEFT, () -> run(ZootDirection.Left));
		
		bindDown(Input.Keys.DOWN, () -> down());
		bindUp(Input.Keys.DOWN, () -> up());
		
		bindDown(Input.Keys.SPACE, () -> jump());
		
		bindDown(Input.Keys.CONTROL_LEFT, () -> attack());
		bindDown(Input.Keys.CONTROL_RIGHT, () -> attack());
		
		bindDown(Input.Keys.F8, () -> hurt());
		
		PhysicsBodyScale crouchingScale = new PhysicsBodyScale(1.0f, 0.4f, 0.75f, false);
		
		DownState downState = (DownState)player.getStateMachine().getStateById(DownState.ID);
		downState.setBodyScaling(crouchingScale);
		
		CrouchState crouchState = (CrouchState)player.getStateMachine().getStateById(CrouchState.ID);
		crouchState.setBodyScaling(crouchingScale);
		
		//camera		
		scrolling = new ZootWindowScrollingStrategy(1.0f, 1.0f, 1.0f, 1.0f);		
		scrolling.setOffset(CAMERA_OFFSET_X, CAMERA_OFFSET_Y);		
		scrolling.setScrollingSpeed(SCROLLING_SPEED);		
		camera.setEdgeSnapping(true);
    	camera.setScrollingStrategy(scrolling);		
		camera.setTarget(player);
	}
	
	private boolean up()
	{
		sendEventToActor(player, ZootEventType.Up);
		return true;
	}

	private boolean down()
	{
		sendEventToActor(player, ZootEventType.Down);
		return true;
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
		//scrolling.setOffset(direction.getHorizontalValue() * CAMERA_OFFSET_X, CAMERA_OFFSET_Y);
		sendEventToActor(player, direction == ZootDirection.Right ? ZootEventType.RunRight : ZootEventType.RunLeft);	
		return true;
	}
}

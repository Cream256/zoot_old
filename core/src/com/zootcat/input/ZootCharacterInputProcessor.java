package com.zootcat.input;

import com.badlogic.gdx.Input;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.scene.ZootActor;

public class ZootCharacterInputProcessor extends ZootBindableInputProcessor
{
	private ZootActor player;
	private float movementVelocity = 0.0f;
	private PhysicsBodyController bodyController;
	
	public ZootCharacterInputProcessor(ZootActor player)
	{
		this.player = player;
		bindUp(Input.Keys.RIGHT, () -> stopCharacter());
		bindDown(Input.Keys.RIGHT, () -> moveHorizontally(true));
		
		bindUp(Input.Keys.LEFT, () -> stopCharacter());
		bindDown(Input.Keys.LEFT, () -> moveHorizontally(false));
	}
	
	public void setPlayer(ZootActor actor)
	{
		player = actor;
	}
	
	public void setMovementVelocity(float velocity)
	{
		movementVelocity = velocity;
	}
	
	private boolean stopCharacter()
	{
		bodyController.setVelocity(0.0f, 0.0f, 0.0f);
		return true;
	}
	
	private boolean moveHorizontally(boolean right)
	{
		float vx = right ? movementVelocity : -movementVelocity;
		
		bodyController = player.getController(PhysicsBodyController.class);
		bodyController.setVelocity(vx, 0.0f, 0.0f);
		
		return true;
	}
	
}

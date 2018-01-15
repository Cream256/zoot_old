package com.zootcat.input;

import com.badlogic.gdx.Input;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.scene.ZootActor;

public class GameCharacterInputProcessor extends ZootBindableInputProcessor
{
	private ZootActor player;
	private float jumpVelocity = 0.0f;
	private float movementVelocity = 0.0f;	
	private PhysicsBodyController bodyController;
	
	public GameCharacterInputProcessor(ZootActor player)
	{
		setPlayer(player);
		bindUp(Input.Keys.RIGHT, () -> stop());
		bindDown(Input.Keys.RIGHT, () -> walk(true));
		
		bindUp(Input.Keys.LEFT, () -> stop());
		bindDown(Input.Keys.LEFT, () -> walk(false));
		
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
	
	private PhysicsBodyController getController()
	{
		if(bodyController == null)
		{
			bodyController = player.getController(PhysicsBodyController.class);	
		}		
		return bodyController;
	}
	
	private boolean jump()
	{
		getController().getPhysicsBody().setVelocityY(jumpVelocity);
		return true;
	}
	
	private boolean stop()
	{
		getController().getPhysicsBody().setVelocityX(0.0f);
		return true;
	}
	
	private boolean walk(boolean right)
	{
		float vx = right ? movementVelocity : -movementVelocity;
		getController().getPhysicsBody().setVelocityX(vx);		
		return true;
	}
	
}

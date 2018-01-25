package com.zootcat.controllers.physics;

import com.zootcat.controllers.Controller;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

public class MoveableController implements Controller
{
	@CtrlParam(debug = true) private float moveForce = 1.0f;
	
	private PhysicsBodyController physicsCtrl;
	
	@Override
	public void init(ZootActor actor)
	{
		//noop
	}

	@Override
	public void onAdd(ZootActor actor)
	{
		physicsCtrl = actor.getController(PhysicsBodyController.class);
	}

	@Override
	public void onRemove(ZootActor actor)
	{
		physicsCtrl = null;
	}

	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		//noop		
	}
	
	public void move(ZootDirection direction)
	{
		float vx = direction == ZootDirection.Right ? moveForce : -moveForce;
		physicsCtrl.setVelocity(vx, 0.0f, true, false);
	}
}

package com.zootcat.controllers.physics;

import com.zootcat.controllers.Controller;
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;
import com.zootcat.utils.ZootUtils;

public class MoveableController implements Controller
{
	@CtrlParam(debug = true) private float moveForce = 1.0f;
	@CtrlParam(debug = true) private float jumpForce = 1.0f;
	@CtrlParam private int jumpTimeout = 0;
	@CtrlDebug private int timeout = 0;
	
	private PhysicsBodyController physicsCtrl;
	private DetectGroundController groundCtrl;
	
	@Override
	public void init(ZootActor actor)
	{
		//noop
	}

	@Override
	public void onAdd(ZootActor actor)
	{
		physicsCtrl = actor.getController(PhysicsBodyController.class);
		groundCtrl = actor.getController(DetectGroundController.class);
	}

	@Override
	public void onRemove(ZootActor actor)
	{
		physicsCtrl = null;
		groundCtrl = null;
	}

	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		jumpTimeout = Math.max(0, jumpTimeout - ZootUtils.trunc(delta * 1000));
	}
	
	public void jump()
	{
		if(jumpTimeout > 0) return;
		if(!groundCtrl.isOnGround()) return;
		
		physicsCtrl.setVelocity(0.0f, jumpForce, false, true);
	}
	
	public void move(ZootDirection direction)
	{
		float vx = direction == ZootDirection.Right ? moveForce : -moveForce;		
		physicsCtrl.setVelocity(vx, 0.0f, true, false);
	}
}

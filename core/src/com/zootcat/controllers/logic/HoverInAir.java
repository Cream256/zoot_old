package com.zootcat.controllers.logic;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.scene.ZootActor;

//TODO add test
public class HoverInAir extends ControllerAdapter
{
	@CtrlParam(debug = true) private float vy = 1.0f;
	@CtrlParam(debug = true) private float timeScale = 1.0f;
	
	private float time = 0.0f;
	
	@Override
	public void init(ZootActor actor) 
	{
		time = 0.0f;
	}
	
	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		time += delta * timeScale;
		actor.controllerAction(PhysicsBodyController.class, ctrl ->
		{
			float signum = (float) Math.signum(Math.sin(time));
	        float velocityY = signum * vy;	
	        ctrl.setVelocity(0, velocityY, false, true);
		});
	}
}

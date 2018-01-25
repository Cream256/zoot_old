package com.zootcat.controllers.physics;

import com.zootcat.controllers.Controller;
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.scene.ZootActor;

//TODO add tests
public class DetectFallController implements Controller
{
	@CtrlDebug private boolean falling;
	@CtrlParam(debug = true) private float threshold = -0.5f;
	
	private ZootEvent fallEvent;
	private DetectGroundController jumpCtrl;	
		
	@Override
	public void init(ZootActor actor)
	{
		falling = false;
		fallEvent = new ZootEvent();	
	}

	@Override
	public void onAdd(ZootActor actor)
	{
		jumpCtrl = actor.getController(DetectGroundController.class);
	}

	@Override
	public void onRemove(ZootActor actor)
	{
		//noop
	}

	@Override
	public void onUpdate(float delta, ZootActor actor)
	{		
		boolean fallingNow = actor.controllerCondition(PhysicsBodyController.class,	(c) -> c.getBody().getLinearVelocity().y < threshold);		
		if(fallingNow && !falling && !jumpCtrl.isOnGround())
		{
			fallEvent.reset();
			fallEvent.setType(ZootEventType.Fall);
			actor.fire(fallEvent);
		}
		falling = fallingNow;
	}
	
	public boolean isFalling()
	{
		return falling;
	}
}

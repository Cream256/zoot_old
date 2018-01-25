package com.zootcat.controllers.gfx;

import com.zootcat.controllers.Controller;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

public class DirectionController implements Controller
{
	@CtrlParam(debug = true) private ZootDirection direction = ZootDirection.Right;
		
	@Override
	public void init(ZootActor actor)
	{
		//noop
	}

	@Override
	public void onAdd(ZootActor actor)
	{
		//noop		
	}

	@Override
	public void onRemove(ZootActor actor)
	{
		//noop
	}

	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		//noop		
	}

	public ZootDirection getDirection()
	{
		return direction;
	}
	
	public void setDirection(ZootDirection direction)
	{
		this.direction = direction;		
	}	
}

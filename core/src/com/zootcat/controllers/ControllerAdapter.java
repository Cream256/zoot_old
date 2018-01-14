package com.zootcat.controllers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.zootcat.controllers.gfx.RenderController;
import com.zootcat.scene.ZootActor;

public class ControllerAdapter implements Controller, ChangeListenerController, RenderController 
{
	@Override
	public void init() 
	{
		//noop
	}

	@Override
	public void onRender(Batch batch, float parentAlpha, ZootActor actor, float delta) 
	{
		//noop
	}

	@Override
	public void onSizeChange(ZootActor actor) 
	{
		//noop
	}

	@Override
	public void onPositionChange(ZootActor actor) 
	{
		//noop
	}

	@Override
	public void onRotationChange(ZootActor actor) 
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
}

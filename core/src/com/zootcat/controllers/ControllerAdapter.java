package com.zootcat.controllers;

import com.badlogic.gdx.assets.AssetManager;
import com.zootcat.scene.ZootActor;

public class ControllerAdapter implements Controller 
{
	@Override
	public void init(ZootActor actor, AssetManager assetManager) 
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

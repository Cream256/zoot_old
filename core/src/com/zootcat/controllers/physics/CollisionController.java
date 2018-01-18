package com.zootcat.controllers.physics;

import com.badlogic.gdx.assets.AssetManager;
import com.zootcat.controllers.Controller;
import com.zootcat.physics.ZootCollisionListener;
import com.zootcat.scene.ZootActor;

public class CollisionController extends ZootCollisionListener implements Controller 
{
	@Override
	public void init(ZootActor actor, AssetManager assetManager)
	{
		//noop
	}

	@Override
	public void onAdd(ZootActor actor) 
	{
		actor.addListener(this);
	}

	@Override
	public void onRemove(ZootActor actor) 
	{
		actor.removeListener(this);
	}

	@Override
	public void onUpdate(float delta, ZootActor actor) 
	{
		//noop
	}
}

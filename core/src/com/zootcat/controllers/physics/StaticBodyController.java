package com.zootcat.controllers.physics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class StaticBodyController extends PhysicsBodyController 
{	
	public StaticBodyController()
	{
		//noop
	}
	
	public StaticBodyController(ZootScene scene)
	{
		super.scene = scene;
	}
	
	@Override
	public void init(ZootActor actor, AssetManager assetManager)
	{
		super.type = BodyType.StaticBody;		
		super.init(actor, assetManager);
	}
}

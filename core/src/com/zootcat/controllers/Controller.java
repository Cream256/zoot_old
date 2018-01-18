package com.zootcat.controllers;

import com.badlogic.gdx.assets.AssetManager;
import com.zootcat.scene.ZootActor;

public interface Controller 
{
	void init(ZootActor actor, AssetManager assetManager);
	void onAdd(ZootActor actor);
	void onRemove(ZootActor actor);	
	void onUpdate(float delta, ZootActor actor);
}

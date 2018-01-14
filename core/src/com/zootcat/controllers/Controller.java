package com.zootcat.controllers;

import com.zootcat.scene.ZootActor;

public interface Controller 
{
	void init();
	void onAdd(ZootActor actor);
	void onRemove(ZootActor actor);	
	void onUpdate(float delta, ZootActor actor);
}

package com.zootcat.scene;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Disposable;
import com.zootcat.physics.ZootPhysics;
import com.zootcat.render.ZootRender;

public interface ZootScene extends Disposable
{
	void update(float delta);
	void render(float delta);
	
	void addActor(ZootActor actor);
	void removeActor(ZootActor actor);
	
	Camera getCamera();
	ZootPhysics getPhysics();
	ZootRender getRender();
	
	boolean isDebugMode();	
}

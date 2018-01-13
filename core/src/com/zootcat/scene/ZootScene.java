package com.zootcat.scene;

import java.util.List;
import java.util.function.Predicate;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.zootcat.physics.ZootPhysics;
import com.zootcat.render.ZootRender;

public interface ZootScene extends Disposable
{
	void update(float delta);
	void render(float delta);
	
	void addActor(ZootActor actor);
	void removeActor(ZootActor actor);	
	List<ZootActor> getActors();
	List<ZootActor> getActors(Predicate<Actor> filter);
	
	Camera getCamera();
	ZootPhysics getPhysics();
	ZootRender getRender();
	
	boolean isDebugMode();
	void setDebugMode(boolean debug);
	
	void setFocusedActor(ZootActor actor);
}

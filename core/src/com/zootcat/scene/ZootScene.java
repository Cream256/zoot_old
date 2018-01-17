package com.zootcat.scene;

import java.util.List;
import java.util.function.Predicate;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.utils.Disposable;
import com.zootcat.gfx.ZootRender;
import com.zootcat.physics.ZootPhysics;

public interface ZootScene extends Disposable
{
	void update(float delta);
	void render(float delta);
	void resize(int width, int height);
	
	void addActor(ZootActor actor);
	void removeActor(ZootActor actor);	
	List<ZootActor> getActors();
	List<ZootActor> getActors(Predicate<Actor> filter);
	
	OrthographicCamera getCamera();
	ZootPhysics getPhysics();
	ZootRender getRender();
	InputProcessor getInputProcessor();
	
	boolean isDebugMode();
	void setDebugMode(boolean debug);
	
	void setFocusedActor(ZootActor actor);
	
	void addListener(EventListener listener);
	void removeListener(EventListener listener);
	
	float getUnitScale();
}

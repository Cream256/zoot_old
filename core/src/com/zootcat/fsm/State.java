package com.zootcat.fsm;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.zootcat.scene.ZootActor;

public interface State
{
	int getId();
	void onEnter(ZootActor actor);
    void onLeave(ZootActor actor);
    void update(ZootActor actor, float delta);    
    boolean handle(Event event);
}
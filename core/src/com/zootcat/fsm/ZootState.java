package com.zootcat.fsm;

import com.zootcat.events.ZootEvent;
import com.zootcat.scene.ZootActor;

public interface ZootState
{
	int getId();
	void onEnter(ZootActor actor, ZootEvent event);
    void onLeave(ZootActor actor, ZootEvent event);
    void onUpdate(ZootActor actor, float delta);    
    boolean handle(ZootEvent event);
}
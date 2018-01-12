package com.zootcat.classcontainer.mocks;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.scene.ZootActor;

public class MixedConstructorMock extends ControllerAdapter
{
    public String name;
    public ZootActor actor;
    public float value;
    public int points;
    
    public MixedConstructorMock(String name, ZootActor actor, Float value, Integer points)
    {
        this.name = name;
        this.actor = actor;
        this.value = value;
        this.points = points;
    }
}

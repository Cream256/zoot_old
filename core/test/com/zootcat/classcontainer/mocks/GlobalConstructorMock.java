package com.zootcat.classcontainer.mocks;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.scene.ZootActor;

public class GlobalConstructorMock extends ControllerAdapter
{
    public int value;
    
    public GlobalConstructorMock()
    {
        value = 0;
    }
    
    public GlobalConstructorMock(int i)
    {
        value = 1;
    }
    
    public GlobalConstructorMock(int i, ZootActor actor)
    {
        value = 2;
    }
}
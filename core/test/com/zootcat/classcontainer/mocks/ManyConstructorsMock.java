package com.zootcat.classcontainer.mocks;

import com.zootcat.controllers.ControllerAdapter;

public class ManyConstructorsMock extends ControllerAdapter	
{
    public int value;
    
    public ManyConstructorsMock()
    {
        value = 1;
    }
    
    public ManyConstructorsMock(Integer newValue)
    {
        value = newValue;
    }
    
    public ManyConstructorsMock(Integer x, Integer y)
    {
        value = x + y;
    }
}

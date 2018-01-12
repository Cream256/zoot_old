package com.zootcat.classcontainer.mocks;

import com.zootcat.controllers.ControllerAdapter;

public class PrimitiveConstructorMock extends ControllerAdapter
{
    public double value;
    
    public PrimitiveConstructorMock(int a, float b, double c)
    {
        value = a + b + c;
    }
}

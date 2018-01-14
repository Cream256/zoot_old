package com.zootcat.controllers.factory;

public class ControllerFactoryException extends Exception 
{
    private static final long serialVersionUID = 546176829252670713L;

    public ControllerFactoryException(String message)
    {
       super(message);
    }
   
    public ControllerFactoryException(String message, Exception innerException)
    {
        super(message, innerException);
    }
	
}

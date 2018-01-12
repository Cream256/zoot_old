package com.zootcat.classcontainer;

public class ClassContainerException extends Exception 
{
    private static final long serialVersionUID = 546176829252670713L;

    public ClassContainerException(String message)
    {
       super(message);
    }
   
    public ClassContainerException(String message, Exception innerException)
    {
        super(message, innerException);
    }
	
}

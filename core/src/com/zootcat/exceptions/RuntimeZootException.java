package com.zootcat.exceptions;

public class RuntimeZootException extends RuntimeException 
{
	private static final long serialVersionUID = 6080990978903677467L;
	
	public RuntimeZootException(String msg)
	{
		super(msg);
	}
	
	public RuntimeZootException(String msg, Exception inner)
	{
		super(msg, inner);
	}
	
}

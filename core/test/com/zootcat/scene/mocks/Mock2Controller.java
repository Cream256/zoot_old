package com.zootcat.scene.mocks;

import com.zootcat.controllers.ControllerAdapter;

public class Mock2Controller extends ControllerAdapter 
{
	public int a;
	public float b;
	public String c;
	
	public Mock2Controller(int a, float b, String c)
	{
		this.a = a;
		this.b = b;
		this.c = c;
	}
}

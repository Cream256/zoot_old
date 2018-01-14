package com.zootcat.controllers.factory.mocks;

import com.zootcat.controllers.factory.CtrlParam;

public class DerivedControllerMock extends BaseControllerMock 
{
	@CtrlParam private int derivedParam = 0;
	
	public int getDerivedParam()
	{
		return derivedParam;
	}
	
}

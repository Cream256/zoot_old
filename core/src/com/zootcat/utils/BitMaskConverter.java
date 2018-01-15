package com.zootcat.utils;

import java.util.HashMap;
import java.util.Map;

public class BitMaskConverter
{
	private Map<String, Short> values = new HashMap<String, Short>();
		
	public short fromString(String str)
	{
		if(!values.containsKey(str))
		{
			values.put(str, (short) ZootUtils.trunc(Math.pow(2.0f, values.size())));				
		}		
		return values.get(str);
	}
}

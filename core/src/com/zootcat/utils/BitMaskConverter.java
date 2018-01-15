package com.zootcat.utils;

import java.util.HashMap;
import java.util.Map;

import com.zootcat.exceptions.RuntimeZootException;

public class BitMaskConverter
{	
	private Map<String, Short> values = new HashMap<String, Short>();
		
	public short fromString(String str)
	{
		if(!values.containsKey(str))
		{
			double value = Math.pow(2.0f, values.size());
			if(value > Short.MAX_VALUE)
			{
				throw new RuntimeZootException("BitMaskConverter has run out of values on: " + str);
			}
			values.put(str, (short) ZootUtils.trunc(value));				
		}		
		return values.get(str);
	}
}

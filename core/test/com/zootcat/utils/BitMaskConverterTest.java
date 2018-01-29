package com.zootcat.utils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zootcat.exceptions.RuntimeZootException;

public class BitMaskConverterTest 
{
	private BitMaskConverter converter;
	
	@Before
	public void setup()
	{
		converter = BitMaskConverter.Instance;
		converter.clear();
	}
	
	@After
	public void tearDown()
	{
		converter.clear();
	}
	
	@Test
	public void fromStringShouldProduceValidValuesTest()
	{
		assertEquals(0x0001, converter.fromString("first"));
		assertEquals(0x0002, converter.fromString("second"));
		assertEquals(0x0004, converter.fromString("third"));
		assertEquals(0x0008, converter.fromString("fourth"));
	}
	
	@Test
	public void fromStringShouldGiveTheSameResultForTheSamStringTest()
	{
		assertEquals(0x0001, converter.fromString("first"));
		assertEquals(0x0002, converter.fromString("second"));
		assertEquals(0x0001, converter.fromString("first"));
		assertEquals(0x0002, converter.fromString("second"));
		assertEquals(0x0004, converter.fromString("third"));
		assertEquals(0x0004, converter.fromString("third"));
	}
	
	@Test
	public void fromStringShouldBeCaseSensitiveTest()
	{
		assertEquals(0x0001, converter.fromString("text"));
		assertEquals(0x0002, converter.fromString("TEXT"));
		assertEquals(0x0004, converter.fromString("TexT"));
	}
	
	@Test(expected = RuntimeZootException.class)
	public void fromStringShouldThrowWhenCalculatedValueIsGreaterThanShortMaxValueTest()
	{
		for(int i = 0; i < 15; ++i)
		{
			assertTrue(Short.MAX_VALUE > converter.fromString(String.valueOf(i)));
		}
		converter.fromString("this should throw");
	}
}

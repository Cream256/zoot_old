package com.zootcat.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class BitMaskConverterTest 
{
	@Test
	public void fromStringShouldProduceValidValuesTest()
	{
		BitMaskConverter converter = new BitMaskConverter();
		assertEquals(0x0001, converter.fromString("first"));
		assertEquals(0x0002, converter.fromString("second"));
		assertEquals(0x0004, converter.fromString("third"));
		assertEquals(0x0008, converter.fromString("fourth"));
	}
	
	@Test
	public void fromStringShouldGiveTheSameResultForTheSamStringTest()
	{
		BitMaskConverter converter = new BitMaskConverter();
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
		BitMaskConverter converter = new BitMaskConverter();
		assertEquals(0x0001, converter.fromString("text"));
		assertEquals(0x0002, converter.fromString("TEXT"));
		assertEquals(0x0004, converter.fromString("TexT"));
	}
	
}

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
	public void shouldProduceValidValuesTest()
	{
		assertEquals(0x0001, converter.convertMask("first"));
		assertEquals(0x0002, converter.convertMask("second"));
		assertEquals(0x0004, converter.convertMask("third"));
		assertEquals(0x0008, converter.convertMask("fourth"));
	}
	
	@Test
	public void shouldGiveTheSameResultForTheSamStringTest()
	{
		assertEquals(0x0001, converter.convertMask("first"));
		assertEquals(0x0002, converter.convertMask("second"));
		assertEquals(0x0001, converter.convertMask("first"));
		assertEquals(0x0002, converter.convertMask("second"));
		assertEquals(0x0004, converter.convertMask("third"));
		assertEquals(0x0004, converter.convertMask("third"));
	}
	
	@Test
	public void shouldBeCaseSensitiveTest()
	{
		assertEquals(0x0001, converter.convertMask("text"));
		assertEquals(0x0002, converter.convertMask("TEXT"));
		assertEquals(0x0004, converter.convertMask("TexT"));
	}
	
	@Test(expected = RuntimeZootException.class)
	public void shouldThrowWhenCalculatedValueIsGreaterThanShortMaxValueTest()
	{
		for(int i = 0; i < 15; ++i)
		{
			assertTrue(Short.MAX_VALUE > converter.convertMask(String.valueOf(i)));
		}
		converter.convertMask("this should throw");
	}
	
	@Test
	public void shouldConvertToMaskContainingMultiplyValuesTest()
	{
		//given
		short mask1 = converter.convertMask("A");		
		short mask2 = converter.convertMask("B");
		short mask3 = converter.convertMask("C");
		
		//when
		short mask4 = converter.convertMask("A | B | C");
		
		//then
		assertEquals(mask1 | mask2 | mask3, mask4);
	}
	
	@Test
	public void shouldReturnMaskThatCollidesWithAllWhenNoParamsAreGivenTest()
	{
		assertEquals(BitMaskConverter.MASK_COLLIDE_WITH_ALL, converter.convertMask(null));
		assertEquals(BitMaskConverter.MASK_COLLIDE_WITH_ALL, converter.convertMask(""));
	}
}

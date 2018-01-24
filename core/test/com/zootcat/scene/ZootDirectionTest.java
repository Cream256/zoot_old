package com.zootcat.scene;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ZootDirectionTest
{	
    @Test
    public void isOppositeTest()
    {                
        assertTrue(ZootDirection.UP.isOpposite(ZootDirection.DOWN));
        assertFalse(ZootDirection.UP.isOpposite(ZootDirection.UP));
        assertFalse(ZootDirection.UP.isOpposite(ZootDirection.LEFT));
        assertFalse(ZootDirection.UP.isOpposite(ZootDirection.RIGHT));
        
        assertTrue(ZootDirection.DOWN.isOpposite(ZootDirection.UP));
        assertFalse(ZootDirection.DOWN.isOpposite(ZootDirection.DOWN));
        assertFalse(ZootDirection.DOWN.isOpposite(ZootDirection.LEFT));
        assertFalse(ZootDirection.DOWN.isOpposite(ZootDirection.RIGHT));
        
        assertTrue(ZootDirection.LEFT.isOpposite(ZootDirection.RIGHT));
        assertFalse(ZootDirection.LEFT.isOpposite(ZootDirection.DOWN));
        assertFalse(ZootDirection.LEFT.isOpposite(ZootDirection.UP));
        assertFalse(ZootDirection.LEFT.isOpposite(ZootDirection.LEFT));
        
        assertTrue(ZootDirection.RIGHT.isOpposite(ZootDirection.LEFT));
        assertFalse(ZootDirection.RIGHT.isOpposite(ZootDirection.DOWN));
        assertFalse(ZootDirection.RIGHT.isOpposite(ZootDirection.UP));
        assertFalse(ZootDirection.RIGHT.isOpposite(ZootDirection.RIGHT));
        
        assertTrue(ZootDirection.NONE.isOpposite(ZootDirection.NONE));
    }
    
    @Test
    public void invertTest()
    {
        assertEquals(ZootDirection.UP, ZootDirection.DOWN.invert());
        assertEquals(ZootDirection.DOWN, ZootDirection.UP.invert());        
        assertEquals(ZootDirection.LEFT, ZootDirection.RIGHT.invert());
        assertEquals(ZootDirection.RIGHT, ZootDirection.LEFT.invert());
        assertEquals(ZootDirection.NONE, ZootDirection.NONE.invert());
    }
    
    @Test
    public void fromStringTest()
    {
        assertEquals(ZootDirection.UP, ZootDirection.fromString("up"));
        assertEquals(ZootDirection.UP, ZootDirection.fromString("UP"));
        assertEquals(ZootDirection.UP, ZootDirection.fromString("uP"));
        assertEquals(ZootDirection.UP, ZootDirection.fromString(" up "));
        
        assertEquals(ZootDirection.DOWN, ZootDirection.fromString("down"));
        assertEquals(ZootDirection.DOWN, ZootDirection.fromString("DOWN"));
        assertEquals(ZootDirection.DOWN, ZootDirection.fromString("DowN"));
        assertEquals(ZootDirection.DOWN, ZootDirection.fromString(" down "));
        
        assertEquals(ZootDirection.LEFT, ZootDirection.fromString("left"));
        assertEquals(ZootDirection.LEFT, ZootDirection.fromString("LEFT"));
        assertEquals(ZootDirection.LEFT, ZootDirection.fromString("leFT"));
        assertEquals(ZootDirection.LEFT, ZootDirection.fromString(" LEFT "));
        
        assertEquals(ZootDirection.RIGHT, ZootDirection.fromString("right"));
        assertEquals(ZootDirection.RIGHT, ZootDirection.fromString("RIGHT"));
        assertEquals(ZootDirection.RIGHT, ZootDirection.fromString("RighT"));
        assertEquals(ZootDirection.RIGHT, ZootDirection.fromString(" right "));
        
        assertEquals(ZootDirection.NONE, ZootDirection.fromString(""));
        assertEquals(ZootDirection.NONE, ZootDirection.fromString(" "));
        assertEquals(ZootDirection.NONE, ZootDirection.fromString("1"));
        assertEquals(ZootDirection.NONE, ZootDirection.fromString("!@#4"));
    }
    
	@Test
	public void fromStringRandomTest()
	{
		int lefts = 0;
		int numberOfRandomizations = 10000;
		for(int i = 0; i < numberOfRandomizations; ++i)
		{
			ZootDirection randomZootDirection = ZootDirection.fromString("random");
			lefts += randomZootDirection == ZootDirection.LEFT ? 1 : 0;
		}
		
		float percentLeft = lefts / (float)numberOfRandomizations;		
		assertEquals(0.5f, percentLeft, 0.1f);
	}
    
}

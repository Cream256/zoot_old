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
        assertTrue(ZootDirection.Up.isOpposite(ZootDirection.Down));
        assertFalse(ZootDirection.Up.isOpposite(ZootDirection.Up));
        assertFalse(ZootDirection.Up.isOpposite(ZootDirection.Left));
        assertFalse(ZootDirection.Up.isOpposite(ZootDirection.Right));
        
        assertTrue(ZootDirection.Down.isOpposite(ZootDirection.Up));
        assertFalse(ZootDirection.Down.isOpposite(ZootDirection.Down));
        assertFalse(ZootDirection.Down.isOpposite(ZootDirection.Left));
        assertFalse(ZootDirection.Down.isOpposite(ZootDirection.Right));
        
        assertTrue(ZootDirection.Left.isOpposite(ZootDirection.Right));
        assertFalse(ZootDirection.Left.isOpposite(ZootDirection.Down));
        assertFalse(ZootDirection.Left.isOpposite(ZootDirection.Up));
        assertFalse(ZootDirection.Left.isOpposite(ZootDirection.Left));
        
        assertTrue(ZootDirection.Right.isOpposite(ZootDirection.Left));
        assertFalse(ZootDirection.Right.isOpposite(ZootDirection.Down));
        assertFalse(ZootDirection.Right.isOpposite(ZootDirection.Up));
        assertFalse(ZootDirection.Right.isOpposite(ZootDirection.Right));
        
        assertTrue(ZootDirection.None.isOpposite(ZootDirection.None));
    }
    
    @Test
    public void invertTest()
    {
        assertEquals(ZootDirection.Up, ZootDirection.Down.invert());
        assertEquals(ZootDirection.Down, ZootDirection.Up.invert());        
        assertEquals(ZootDirection.Left, ZootDirection.Right.invert());
        assertEquals(ZootDirection.Right, ZootDirection.Left.invert());
        assertEquals(ZootDirection.None, ZootDirection.None.invert());
    }
    
    @Test
    public void fromStringTest()
    {
        assertEquals(ZootDirection.Up, ZootDirection.fromString("up"));
        assertEquals(ZootDirection.Up, ZootDirection.fromString("UP"));
        assertEquals(ZootDirection.Up, ZootDirection.fromString("uP"));
        assertEquals(ZootDirection.Up, ZootDirection.fromString(" up "));
        
        assertEquals(ZootDirection.Down, ZootDirection.fromString("down"));
        assertEquals(ZootDirection.Down, ZootDirection.fromString("DOWN"));
        assertEquals(ZootDirection.Down, ZootDirection.fromString("DowN"));
        assertEquals(ZootDirection.Down, ZootDirection.fromString(" down "));
        
        assertEquals(ZootDirection.Left, ZootDirection.fromString("left"));
        assertEquals(ZootDirection.Left, ZootDirection.fromString("LEFT"));
        assertEquals(ZootDirection.Left, ZootDirection.fromString("leFT"));
        assertEquals(ZootDirection.Left, ZootDirection.fromString(" LEFT "));
        
        assertEquals(ZootDirection.Right, ZootDirection.fromString("right"));
        assertEquals(ZootDirection.Right, ZootDirection.fromString("RIGHT"));
        assertEquals(ZootDirection.Right, ZootDirection.fromString("RighT"));
        assertEquals(ZootDirection.Right, ZootDirection.fromString(" right "));
        
        assertEquals(ZootDirection.None, ZootDirection.fromString(""));
        assertEquals(ZootDirection.None, ZootDirection.fromString(" "));
        assertEquals(ZootDirection.None, ZootDirection.fromString("1"));
        assertEquals(ZootDirection.None, ZootDirection.fromString("!@#4"));
    }
    
	@Test
	public void fromStringRandomTest()
	{
		int lefts = 0;
		int numberOfRandomizations = 10000;
		for(int i = 0; i < numberOfRandomizations; ++i)
		{
			ZootDirection randomZootDirection = ZootDirection.fromString("random");
			lefts += randomZootDirection == ZootDirection.Left ? 1 : 0;
		}
		
		float percentLeft = lefts / (float)numberOfRandomizations;		
		assertEquals(0.5f, percentLeft, 0.1f);
	}
    
}

package com.zootcat.scene;

import com.badlogic.gdx.math.MathUtils;

public enum ZootDirection
{
    None, Up, Down, Left, Right;

    public ZootDirection invert()
    {
        switch(this) 
        {
        case Up:
            return Down;
            
        case Down:
            return Up;
            
        case Left:
            return Right;
            
        case Right:
            return Left;
            
        default:
            return None;
        }
    }
    
    public static ZootDirection fromString(String value)
    {
        String trimmedValue = value.trim();
        
        if(value.equalsIgnoreCase("random"))
        {
        	return getRandomLeftRight();
        }
        
        for(ZootDirection dir : ZootDirection.values())
        {
            if(dir.name().equalsIgnoreCase(trimmedValue))
            {
                return dir;
            }
        }
        
        return None;
    }
    
    public boolean isOpposite(ZootDirection other)
    {
        return this.invert().equals(other);
    }
    
    private static ZootDirection getRandomLeftRight()
    {
    	int randomNumber = MathUtils.random(1, 100);
    	if(randomNumber <= 50) return ZootDirection.Left;
    	else return ZootDirection.Right;
    }
}

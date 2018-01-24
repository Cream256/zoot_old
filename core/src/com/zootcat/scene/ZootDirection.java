package com.zootcat.scene;

import com.badlogic.gdx.math.MathUtils;

public enum ZootDirection
{
    NONE, UP, DOWN, LEFT, RIGHT;

    public ZootDirection invert()
    {
        switch(this) 
        {
        case UP:
            return DOWN;
            
        case DOWN:
            return UP;
            
        case LEFT:
            return RIGHT;
            
        case RIGHT:
            return LEFT;
            
        default:
            return NONE;
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
        
        return NONE;
    }
    
    public boolean isOpposite(ZootDirection other)
    {
        return this.invert().equals(other);
    }
    
    private static ZootDirection getRandomLeftRight()
    {
    	int randomNumber = MathUtils.random(1, 100);
    	if(randomNumber <= 50) return ZootDirection.LEFT;
    	else return ZootDirection.RIGHT;
    }
}

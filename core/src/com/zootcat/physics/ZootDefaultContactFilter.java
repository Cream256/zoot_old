package com.zootcat.physics;

import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;

public class ZootDefaultContactFilter
{		
	public static boolean shouldCollide(Fixture fixtureA, Fixture fixtureB)
	{
		Filter filterA = fixtureA.getFilterData();
		Filter filterB = fixtureB.getFilterData();
	
		if (filterA.groupIndex == filterB.groupIndex && filterA.groupIndex != 0)
		{
			return filterA.groupIndex > 0;
		}
	
		boolean collide = (filterA.maskBits & filterB.categoryBits) != 0 && (filterA.categoryBits & filterB.maskBits) != 0;
		return collide;
	}
}
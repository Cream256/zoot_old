package com.zootcat.physics;

import com.badlogic.gdx.utils.Disposable;

public interface ZootPhysicsBody extends Disposable
{
	float getX();
	float getY();
	float getAngle();
	float getWidth();
	float getHeight();
	
	boolean canRotate();
	void setCanRotate(boolean value);	
}

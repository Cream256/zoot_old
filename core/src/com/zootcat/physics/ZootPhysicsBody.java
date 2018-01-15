package com.zootcat.physics;

import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.utils.Disposable;

public interface ZootPhysicsBody extends Disposable
{
	float getX();
	float getY();
	float getWidth();
	float getHeight();
	
	float getAngle();
	void setAngle(float angle);
	
	boolean canRotate();
	void setCanRotate(boolean canRotate);
	
	void applyImpulse(float fx, float fy, float fz);
	void setVelocity(float vx, float vy, float vz);
	void setVelocityX(float vx);
	void setVelocityY(float vy);
	void setVelocityZ(float vz);
	
	void setCollisionFilter(Filter collisionFilter);
	void setActive(boolean active);		
}

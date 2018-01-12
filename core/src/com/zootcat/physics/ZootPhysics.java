package com.zootcat.physics;

import com.badlogic.gdx.utils.Disposable;

public interface ZootPhysics extends Disposable
{
	void step(float delta);
	void setGravity(float x, float y, float z);
	
	ZootPhysicsBody createBody(ZootPhysicsBodyDef bodyDef);
	void removeBody(ZootPhysicsBody body);
}

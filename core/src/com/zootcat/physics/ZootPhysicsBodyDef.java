package com.zootcat.physics;

public class ZootPhysicsBodyDef 
{
	public float x = 0.0f;
	public float y = 0.0f;
	public float z = 0.0f;
	public float width = 1.0f;
	public float height = 1.0f;
	public float density = 1.0f;
	public float friction = 0.0f;
	public float restitution = 0.0f;	
	public float rotation = 0.0f;
	public ZootPhysicsFixture[] fixtures = null;
	public ZootPhysicsBodyType type = ZootPhysicsBodyType.DYNAMIC;
	public boolean sensor = false;
}

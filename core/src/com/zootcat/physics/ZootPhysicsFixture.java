package com.zootcat.physics;

public class ZootPhysicsFixture
{
	public float x;
	public float y;
	public float width;
	public float height;
	public ZootPhysicsFixtureType type;
	
	public static ZootPhysicsFixture createBox(float x, float y, float width, float height)
	{
		ZootPhysicsFixture shape = new ZootPhysicsFixture();
		shape.x = x;
		shape.y = y;
		shape.width = width;
		shape.height = height;
		shape.type = ZootPhysicsFixtureType.BOX;
		return shape;
	}

	public static ZootPhysicsFixture createCircle(float x, float y, float radius)
	{
		ZootPhysicsFixture shape = new ZootPhysicsFixture();
		shape.x = x;
		shape.y = y;
		shape.width = radius;
		shape.height = 0.0f;
		shape.type = ZootPhysicsFixtureType.CIRCLE;
		return shape;
	}
}

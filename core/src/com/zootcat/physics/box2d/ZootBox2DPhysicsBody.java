package com.zootcat.physics.box2d;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.physics.ZootPhysicsBody;

public class ZootBox2DPhysicsBody implements ZootPhysicsBody 
{	
	private Body body;
	private Fixture fixture;
	private float height;
	private float width;
	
	public ZootBox2DPhysicsBody(Body body, Fixture fixture, float width, float height)
	{
		this.body = body;
		this.fixture = fixture;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void dispose() 
	{
		//noop
	}
	
	public Body getBody()
	{
		return body;
	}
	
	public Fixture getFixture()
	{
		return fixture;
	}

	@Override
	public float getX() 
	{		
		//body position is centered, we need lower left corner
		return body.getPosition().x - width * 0.5f;
	}

	@Override
	public float getY() 
	{
		//body position is centered, we need lower left corner
		return body.getPosition().y - height * 0.5f;
	}

	@Override
	public float getWidth()
	{
		return width;
	}
	
	@Override
	public float getHeight()
	{
		return height;
	}
	
	@Override
	public float getAngle() 
	{
		return body.getAngle();
	}

	@Override
	public void setCanRotate(boolean value)
	{
		body.setFixedRotation(value);
	}

	@Override
	public boolean canRotate() 
	{
		return body.isFixedRotation();
	}

	@Override
	public void applyImpulse(float fx, float fy, float fz) 
	{
		body.applyLinearImpulse(fx, fy, body.getPosition().x, body.getPosition().y, true);
	}
}

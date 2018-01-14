package com.zootcat.physics.box2d;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.physics.ZootPhysicsBody;

public class ZootBox2DPhysicsBody implements ZootPhysicsBody 
{	
	private Body body;
	private List<Fixture> fixtures;
	private float height;
	private float width;
	
	public ZootBox2DPhysicsBody(Body body, List<Fixture> fixtures, float width, float height)
	{
		this.body = body;
		this.fixtures = fixtures;
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
	
	public List<Fixture> getFixtures()
	{
		return fixtures;
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
	public void setCanRotate(boolean canRotate)
	{
		body.setFixedRotation(!canRotate);
	}

	@Override
	public boolean canRotate() 
	{
		return body.isFixedRotation();
	}

	@Override
	public void applyImpulse(float fx, float fy, float fz) 
	{
		Vector2 bodyCenter = body.getPosition();
		body.applyLinearImpulse(fx, fy, bodyCenter.x, bodyCenter.y, true);
	}
}

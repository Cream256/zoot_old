package com.zootcat.physics.box2d;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.zootcat.physics.ZootPhysics;

public class ZootBox2DPhysics implements ZootPhysics
{
	private static final int POSITION_ITERATIONS = 2;
	private static final int VELOCITY_ITERATIONS = 6;
	private static final Vector2 DEFAULT_GRAVITY = new Vector2(0.0f, -9.80f);
	
	private World world;
	private ContactListener contactListener;
	
	public ZootBox2DPhysics()
	{
		Box2D.init();
		world = new World(DEFAULT_GRAVITY, true);
	
		contactListener = new ZootBox2DContactListener();
		world.setContactListener(contactListener);
	}
	
	@Override
	public void setGravity(float x, float y, float z)
	{
		world.setGravity(new Vector2(x, y));
	}
	
	@Override
	public Body createBody(BodyDef bodyDef)
	{
		return world.createBody(bodyDef);
	}
	
	@Override
	public void removeBody(Body body)
	{
		world.destroyBody(body);
	}
		
	@Override
	public List<Fixture> createFixtures(Body body, List<FixtureDef> fixtureDefs) 
	{
		List<Fixture> fixtures = new ArrayList<Fixture>();
		fixtureDefs.forEach((def) -> 
		{
			body.createFixture(def);
			def.shape.dispose();
			def.shape = null;
		});
		return fixtures;
	}
	
	@Override
	public void step(float delta)
	{
		world.step(delta, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
	}
	
	@Override
	public void dispose() 
	{
		world.dispose();
		world = null;
	}

	public World getWorld() 
	{
		return world;
	}	
}

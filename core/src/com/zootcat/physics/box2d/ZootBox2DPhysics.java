package com.zootcat.physics.box2d;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.physics.ZootPhysics;
import com.zootcat.physics.ZootPhysicsBody;
import com.zootcat.physics.ZootPhysicsBodyDef;
import com.zootcat.physics.ZootPhysicsBodyType;
import com.zootcat.physics.ZootPhysicsFixture;

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
	public ZootPhysicsBody createBody(ZootPhysicsBodyDef bodyDefinition)
	{
		//create box2d entities		
		BodyDef bodyDef = createBox2DBodyDef(bodyDefinition);
		Body body = createBox2DBody(bodyDef, bodyDefinition);		
		
		List<Fixture> fixtures = new ArrayList<Fixture>(bodyDefinition.fixtures.length);
		for(ZootPhysicsFixture fixture : bodyDefinition.fixtures)
		{			
			Shape fixtureShape = createBox2DShape(fixture);
			FixtureDef fixtureDef = createBox2DFixtureDef(bodyDefinition, fixtureShape);		
			fixtures.add(body.createFixture(fixtureDef));
			fixtureShape.dispose();
		}
		
		//create physics body
		ZootBox2DPhysicsBody physicsBody = new ZootBox2DPhysicsBody(body, fixtures, bodyDefinition.width, bodyDefinition.height);
						
		//result
		return physicsBody;
	}

	@Override
	public void removeBody(ZootPhysicsBody body)
	{
		if(ClassReflection.isInstance(ZootBox2DPhysicsBody.class, body))
		{
			world.destroyBody(((ZootBox2DPhysicsBody)body).getBox2DBody());
		}
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
	
	private BodyType toBox2DBodyType(ZootPhysicsBodyType type) 
	{
		switch(type)
		{
		case DYNAMIC:
			return BodyType.DynamicBody;
			
		case KINEMATIC:
			return BodyType.KinematicBody;
		
		case STATIC:
		default:
			return BodyType.StaticBody;
		}
	}
	
	private Shape createBox2DShape(ZootPhysicsFixture fixtureDef) 
	{
		switch(fixtureDef.type)
		{
		case CIRCLE:
			CircleShape circle = new CircleShape();
			circle.setRadius(fixtureDef.width);
			circle.setPosition(new Vector2(fixtureDef.x, fixtureDef.y));
			return circle;
			
		case BOX:
		default:
			PolygonShape rect = new PolygonShape();
			float halfWidth = fixtureDef.width / 2;
			float halfHeight = fixtureDef.height / 2;
			rect.setAsBox(halfWidth, halfHeight, new Vector2(fixtureDef.x, fixtureDef.y), 0.0f);
			return rect;
		}
	}
	
	private Body createBox2DBody(BodyDef box2dBodyDef, ZootPhysicsBodyDef bodyDefinition) 
	{
		Body body = world.createBody(box2dBodyDef);
		Vector2 bottomLeft = calculateBodyBottomLeftPosition(bodyDefinition);
		body.setTransform(bottomLeft.x, bottomLeft.y, bodyDefinition.rotation * MathUtils.degreesToRadians);
		body.setGravityScale(bodyDefinition.sensor ? 0.0f : 1.0f);
		return body;
	}
	
	private FixtureDef createBox2DFixtureDef(ZootPhysicsBodyDef bodyDefinition, Shape shape)
	{
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape; 
		fixtureDef.density = bodyDefinition.density; 
		fixtureDef.friction = bodyDefinition.friction;
		fixtureDef.restitution = bodyDefinition.restitution;
		fixtureDef.isSensor = bodyDefinition.sensor;
		return fixtureDef;
	}
	
	private BodyDef createBox2DBodyDef(ZootPhysicsBodyDef bodyDefinition) 
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = toBox2DBodyType(bodyDefinition.type);		
		Vector2 bottomLeft = calculateBodyBottomLeftPosition(bodyDefinition);
		bodyDef.position.set(bottomLeft.x, bottomLeft.y);
		return bodyDef;
	}
	
	private Vector2 calculateBodyBottomLeftPosition(ZootPhysicsBodyDef bodyDefinition)
	{
		float x = bodyDefinition.x + bodyDefinition.width * 0.5f;
		float y = bodyDefinition.y + bodyDefinition.height * 0.5f;
		return new Vector2(x, y);
	}
	
}

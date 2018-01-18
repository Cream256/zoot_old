package com.zootcat.controllers.physics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.zootcat.controllers.Controller;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.physics.ZootBodyShape;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class PhysicsBodyController implements Controller
{
	@CtrlParam protected float density = 1.0f;
	@CtrlParam protected float friction = 0.0f;
	@CtrlParam protected float restitution = 0.0f;
	@CtrlParam protected float linearDamping = 0.0f;
	@CtrlParam protected float angularDamping = 0.0f;	
	@CtrlParam protected float gravityScale = 1.0f;
	@CtrlParam protected float width = 0.0f;
	@CtrlParam protected float height = 0.0f;
	@CtrlParam protected boolean sensor = false;	
	@CtrlParam protected boolean bullet = false;
	@CtrlParam protected boolean canRotate = true;
	@CtrlParam protected boolean canSleep = true;
	@CtrlParam protected BodyType type = BodyType.DynamicBody;
	@CtrlParam protected ZootBodyShape shape = ZootBodyShape.BOX;
	@CtrlParam(global = true) protected ZootScene scene;	
	
	private Body body;
	private List<Fixture> fixtures;
			
	@Override
	public void init(ZootActor actor, AssetManager assetManager)
	{
		body = scene.getPhysics().createBody(createBodyDef(actor));
		fixtures = scene.getPhysics().createFixtures(body, createFixtureDefs(actor));
		body.setUserData(actor);
	}
	
	@Override
	public void onAdd(ZootActor actor) 
	{
		//noop
	}

	@Override
	public void onRemove(ZootActor actor)
	{
		scene.getPhysics().removeBody(body);
		body = null;
	}

	@Override
	public void onUpdate(float delta, ZootActor actor) 
	{
		float bottomLeftX = body.getPosition().x - actor.getWidth() * 0.5f; 
		float bottomLeftY = body.getPosition().y - actor.getHeight() * 0.5f;
		actor.setPosition(bottomLeftX, bottomLeftY);
		actor.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
	}
		
	public Body getBody()
	{
		return body;
	}
	
	public List<Fixture> getFixtures()
	{
		return Collections.unmodifiableList(fixtures);
	}
	
	public void setCollisionFilter(Filter collisionFilter) 
	{
		fixtures.forEach((fixture) -> fixture.setFilterData(collisionFilter));
	}	
	
	public void setVelocity(float vx, float vy)
	{
		setVelocity(vx, vy);
	}
	
	public void setVelocity(float vx, float vy, boolean setX, boolean setY)
	{
		Vector2 velocity = body.getLinearVelocity();
		body.setLinearVelocity(setX ? vx : velocity.x, setY ? vy : velocity.y);	
	}
	
	protected BodyDef createBodyDef(ZootActor actor) 
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.x = actor.getX() + actor.getWidth() * 0.5f;
		bodyDef.position.y = actor.getY() + actor.getHeight() * 0.5f;
		bodyDef.angle = actor.getRotation() * MathUtils.degreesToRadians;		
		bodyDef.active = true;
		bodyDef.allowSleep = canSleep;
		bodyDef.angularDamping = angularDamping;
		bodyDef.angularVelocity = 0.0f;
		bodyDef.awake = true;
		bodyDef.bullet = bullet;
		bodyDef.fixedRotation = !canRotate;
		bodyDef.gravityScale = gravityScale;
		bodyDef.linearDamping = linearDamping;
		bodyDef.type = type;		
		return bodyDef;
	}
	
	protected List<FixtureDef> createFixtureDefs(ZootActor actor) 
	{
		List<FixtureDef> fixtureDefs = new ArrayList<FixtureDef>(1);		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = density;
		fixtureDef.friction = friction;
		fixtureDef.restitution = restitution;
		fixtureDef.isSensor = sensor;
		fixtureDef.shape = createShape(actor, shape);
		fixtureDefs.add(fixtureDef);
		return fixtureDefs;
	}
	
	protected Shape createShape(ZootActor actor, ZootBodyShape shape)
	{		
		if(shape == ZootBodyShape.BOX)
		{
			PolygonShape polygon = new PolygonShape();
			polygon.setAsBox(getBodyWidth(actor) / 2, getBodyHeight(actor) / 2);			
			return polygon;
		}
		else if (shape == ZootBodyShape.CIRCLE)
		{
			CircleShape circle = new CircleShape();
			circle.setRadius(getBodyWidth(actor));
			return circle;
		}
		else
		{
			throw new RuntimeZootException("Unknown fixture type for for actor: " + actor);
		}
	}
	
	protected float getBodyWidth(ZootActor actor)
	{
		return width == 0.0f ? actor.getWidth() : width * scene.getUnitScale();
	}
	
	protected float getBodyHeight(ZootActor actor)
	{
		return height == 0.0f ? actor.getHeight() : height * scene.getUnitScale();
	}
}

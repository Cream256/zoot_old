package com.zootcat.controllers.physics;

import com.badlogic.gdx.math.MathUtils;
import com.zootcat.controllers.Controller;
import com.zootcat.physics.ZootPhysicsBody;
import com.zootcat.physics.ZootPhysicsBodyDef;
import com.zootcat.physics.ZootPhysicsBodyType;
import com.zootcat.physics.ZootPhysicsFixture;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public abstract class PhysicsBodyController implements Controller
{
	private float density;
	private float friction;
	private float restitution;
	private ZootScene scene;
	private ZootPhysicsBody body;
		
	public PhysicsBodyController(ZootActor actor, ZootScene scene)
	{
		this(1.0f, 0.0f, 0.0f, actor, scene);
	}
	
	public PhysicsBodyController(float density, float friction, float restitution, ZootActor actor, ZootScene scene)
	{
		this.scene = scene;
		this.density = density;
		this.friction = friction;
		this.restitution = restitution;
	}

	@Override
	public void onAdd(ZootActor actor) 
	{
		body = scene.getPhysics().createBody(createBodyDefinition(actor));
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
		actor.setRotation(body.getAngle() * MathUtils.radiansToDegrees);		
		actor.setPosition(body.getX(), body.getY());
	}
	
	public void applyImpulse(float fx, float fy, float fz)
	{
		body.applyImpulse(fx, fy, fz);
	}
	
	protected ZootPhysicsBody getPhysicsBody()
	{
		return body;
	}
	
	protected ZootPhysicsBodyDef createBodyDefinition(ZootActor actor) 
	{
		ZootPhysicsBodyDef bodyDef = new ZootPhysicsBodyDef();
		bodyDef.x = actor.getX();
		bodyDef.y = actor.getY();
		bodyDef.width = actor.getWidth();
		bodyDef.height = actor.getHeight();
		bodyDef.rotation = actor.getRotation();
		bodyDef.density = density;
		bodyDef.friction = friction;
		bodyDef.restitution = restitution;
		
		bodyDef.type = getBodyType();		
		bodyDef.fixtures = createFixtures(actor);
		return bodyDef;
	}
	
	protected abstract ZootPhysicsFixture[] createFixtures(ZootActor actor);	
	
	protected abstract ZootPhysicsBodyType getBodyType();	
}

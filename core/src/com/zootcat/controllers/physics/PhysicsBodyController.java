package com.zootcat.controllers.physics;

import com.badlogic.gdx.math.MathUtils;
import com.zootcat.controllers.Controller;
import com.zootcat.physics.ZootPhysicsBody;
import com.zootcat.physics.ZootPhysicsBodyDef;
import com.zootcat.physics.ZootPhysicsBodyShape;
import com.zootcat.physics.ZootPhysicsBodyType;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;
import com.zootcat.utils.ZootUtils;

public abstract class PhysicsBodyController implements Controller
{
	private ZootScene scene;
	private ZootPhysicsBody body;
	private ZootPhysicsBodyDef bodyDef;
	
	public PhysicsBodyController(String type, ZootActor actor, ZootScene scene)
	{
		this(1.0f, ZootPhysicsBodyShape.RECTANGLE.toString(), type, actor, scene);
	}
	
	public PhysicsBodyController(float density, String shape, String type, ZootActor actor, ZootScene scene)
	{
		bodyDef = new ZootPhysicsBodyDef();
		bodyDef.x = actor.getX();
		bodyDef.y = actor.getY();
		bodyDef.width = actor.getWidth();
		bodyDef.height = actor.getHeight();
		bodyDef.rotation = actor.getRotation();
		bodyDef.density = density;		
		bodyDef.type = ZootUtils.searchEnum(ZootPhysicsBodyType.class, type);
		bodyDef.shape = ZootUtils.searchEnum(ZootPhysicsBodyShape.class, shape);		
		this.scene = scene;	
	}

	@Override
	public void onAdd(ZootActor actor) 
	{
		body = scene.getPhysics().createBody(bodyDef);
		bodyDef = null;
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
	
}

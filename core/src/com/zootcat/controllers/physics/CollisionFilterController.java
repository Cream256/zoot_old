package com.zootcat.controllers.physics;

import com.badlogic.gdx.physics.box2d.Filter;
import com.zootcat.controllers.Controller;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;
import com.zootcat.utils.BitMaskConverter;

public class CollisionFilterController implements Controller 
{
	private static final BitMaskConverter bitMaskConverter = new BitMaskConverter();
	
	@CtrlParam(required = true) private String category;
	@CtrlParam(required = true) private String mask;
		
	@Override
	public void init(ZootActor actor)
	{
		
	}

	@Override
	public void onAdd(ZootActor actor)
	{
		Filter collisionFilter = new Filter();
		collisionFilter.categoryBits = bitMaskConverter.fromString(category);
		collisionFilter.maskBits = convertMaskBits(mask);		
		actor.getController(PhysicsBodyController.class).getPhysicsBody().setCollisionFilter(collisionFilter);		
	}

	@Override
	public void onRemove(ZootActor actor) 
	{
		//noop
	}

	@Override
	public void onUpdate(float delta, ZootActor actor) 
	{
		//noop
	}

	private short convertMaskBits(String mask) 
	{
		short bitMask = 0;				
		for(String category : mask.split("|"))
		{
			short categoryBit = bitMaskConverter.fromString(category.trim());
			bitMask |= categoryBit;
		}		
		return bitMask;
	}
	
}

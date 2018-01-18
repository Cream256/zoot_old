package com.zootcat.controllers.physics;

import com.badlogic.gdx.physics.box2d.Filter;
import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;
import com.zootcat.utils.BitMaskConverter;

public class CollisionFilterController extends ControllerAdapter 
{
	private static final int MASK_COLLIDE_WITH_ALL = -1;
	private static final int MASK_COLLIDE_WITH_NONE = 0;
	private static final BitMaskConverter bitMaskConverter = new BitMaskConverter();
	
	@CtrlParam private String mask = "";
	@CtrlParam(required = true) private String category = "";
			
	public CollisionFilterController()
	{
		//noop
	}
	
	public CollisionFilterController(String category, String mask)
	{
		this.mask = mask;
		this.category = category;
	}
	
	@Override
	public void onAdd(ZootActor actor)
	{
		Filter collisionFilter = new Filter();
		collisionFilter.categoryBits = bitMaskConverter.fromString(category);
		collisionFilter.maskBits = convertMaskBits(mask);		
		actor.getController(PhysicsBodyController.class).setCollisionFilter(collisionFilter);		
	}

	private short convertMaskBits(String mask) 
	{		
		if(mask.isEmpty())
		{
			return MASK_COLLIDE_WITH_ALL;
		}
		
		short bitMask = MASK_COLLIDE_WITH_NONE;			
		for(String category : mask.split("\\|"))
		{
			short categoryBit = bitMaskConverter.fromString(category.trim());
			bitMask |= categoryBit;
		}		
		return bitMask;
	}	
}

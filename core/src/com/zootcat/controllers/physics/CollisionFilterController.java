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
	
	@CtrlParam(required = true) private String category = "";
	@CtrlParam private String mask = "";
		
	@Override
	public void onAdd(ZootActor actor)
	{
		Filter collisionFilter = new Filter();
		collisionFilter.categoryBits = BitMaskConverter.Instance.fromString(category);
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
			short categoryBit = BitMaskConverter.Instance.fromString(category.trim());
			bitMask |= categoryBit;
		}		
		return bitMask;
	}
	
}

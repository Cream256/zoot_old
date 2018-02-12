package com.zootcat.controllers.physics;

import com.badlogic.gdx.physics.box2d.Filter;
import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;
import com.zootcat.utils.BitMaskConverter;

public class CollisionFilterController extends ControllerAdapter
{
	@CtrlParam(required = true) private String category = "";
	@CtrlParam private String mask = "";
		
	@Override
	public void onAdd(ZootActor actor)
	{
		Filter collisionFilter = new Filter();
		collisionFilter.categoryBits = BitMaskConverter.Instance.convertMask(category);
		collisionFilter.maskBits = BitMaskConverter.Instance.convertMask(mask);
		actor.getController(PhysicsBodyController.class).setCollisionFilter(collisionFilter);		
	}	
}

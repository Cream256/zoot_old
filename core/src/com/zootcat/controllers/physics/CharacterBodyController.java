package com.zootcat.controllers.physics;

import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.physics.ZootPhysicsBodyType;
import com.zootcat.physics.ZootPhysicsFixture;
import com.zootcat.scene.ZootActor;
import com.zootcat.utils.ZootUtils;

public class CharacterBodyController extends PhysicsBodyController
{	
	@CtrlParam private boolean vertical = true;
	
	@Override
	public void onAdd(ZootActor actor) 
	{
		super.onAdd(actor);
		getPhysicsBody().setCanRotate(false);
	}
	
	@Override
	protected ZootPhysicsFixture[] createFixtures(ZootActor actor)
	{
		return vertical ? createVerticalCharacterFixtures(actor) : createHorizontalCharacterFixtures(actor);
	}
	
	@Override
	protected ZootPhysicsBodyType getBodyType() 
	{
		return ZootPhysicsBodyType.DYNAMIC;
	}
	
	private ZootPhysicsFixture[] createVerticalCharacterFixtures(ZootActor actor)
	{
		ZootPhysicsFixture[] shapes = new ZootPhysicsFixture[2];

		//feet shape
		float circleRadius = actor.getWidth() / 2.0f;
		float feetY = actor.getHeight() / 2.0f - circleRadius;		
		shapes[0] = ZootPhysicsFixture.createCircle(0, -feetY, circleRadius);
		
		//body shape
		float bodyWidth = actor.getWidth();
		float bodyHeight = actor.getHeight() - circleRadius;
		shapes[1] = ZootPhysicsFixture.createBox(0, circleRadius / 2.0f, bodyWidth, bodyHeight);
		
		return shapes;		
	}
	
	private ZootPhysicsFixture[] createHorizontalCharacterFixtures(ZootActor actor) 
	{		
		float circleRadius = actor.getHeight() / 2.0f;		
		int circleCount = ZootUtils.trunc(actor.getWidth() / circleRadius);
			
		ZootPhysicsFixture[] shapes = new ZootPhysicsFixture[circleCount + 1];	

		//feet shape
		float sx = -(actor.getWidth() / 2.0f) + circleRadius;
		float ex = actor.getWidth() / 2.0f - circleRadius;
		float y = actor.getHeight() / 2.0f - circleRadius;		
		for(int circleIndex = 0; circleIndex < circleCount; ++circleIndex)
		{
			shapes[circleIndex] = ZootPhysicsFixture.createCircle(sx, -y, circleRadius);
			sx = Math.min(ex, sx + circleRadius);
		}
		
		//body shape
		float bodyWidth = actor.getWidth();
		float bodyHeight = actor.getHeight() - circleRadius;
		shapes[shapes.length - 1] = ZootPhysicsFixture.createBox(0, circleRadius / 2.0f, bodyWidth, bodyHeight);
		
		return shapes;
	}
	
}

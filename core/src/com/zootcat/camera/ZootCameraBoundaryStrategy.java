package com.zootcat.camera;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.zootcat.scene.ZootActor;

//TODO rename, maybe to FollowingStrategy?
//TODO add tests
public class ZootCameraBoundaryStrategy implements ZootCameraStrategy
{	
	private static final float POSITION_BLEND_FACTOR = 4.0f;
	
	private Viewport viewport;
	private Vector2 actorWorldPosition = new Vector2();
	private Vector2 screenSize = new Vector2();

	public ZootCameraBoundaryStrategy(Viewport viewport)
	{
		this.viewport = viewport;
	}

	@Override
	public void calculateCameraPosition(ZootActor actor, Vector3 outPosition, float dt)
	{
		if(actor == null) return;
				
		//so the camera won't wonder into space at first frame
		dt = Math.min(1.0f, dt);
		
		//calculate actor position		
		actorWorldPosition.x = actor.getX();
		actorWorldPosition.y = actor.getY();
		
		screenSize.x = viewport.getWorldWidth();
		screenSize.x = viewport.getWorldHeight();
		
		//calculate actor movement boundary
		Vector2 topLeft = new Vector2(outPosition.x - screenSize.x / 4.0f, outPosition.y + screenSize.y / 4.0f);
		Vector2 bottomRight = new Vector2(outPosition.x + screenSize.x / 4.0f, outPosition.y - screenSize.y / 4.0f);
						
		//check if actor is outside of boundary
		Vector3 cameraLookAt = outPosition.cpy();				
		if(actorWorldPosition.x > bottomRight.x)
		{
			cameraLookAt.x += actorWorldPosition.x - bottomRight.x;			
		}
		else if(actorWorldPosition.x < topLeft.x)
		{
			cameraLookAt.x -= topLeft.x - actorWorldPosition.x;
		}
		if(actorWorldPosition.y > bottomRight.y)
		{
			cameraLookAt.y += actorWorldPosition.y - bottomRight.y;
		}
		else if(actorWorldPosition.y < topLeft.y)
		{
			cameraLookAt.y -= topLeft.y - actorWorldPosition.y;
		}
						
		//lerp to new position
		float positionBlend = Math.min(POSITION_BLEND_FACTOR * dt, 1.0f);		
		outPosition.x = MathUtils.lerp(outPosition.x, cameraLookAt.x, positionBlend);
		outPosition.y = MathUtils.lerp(outPosition.y, cameraLookAt.y, positionBlend);
	}
}

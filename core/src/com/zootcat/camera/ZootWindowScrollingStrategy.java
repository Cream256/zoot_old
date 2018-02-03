package com.zootcat.camera;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.zootcat.scene.ZootActor;

public class ZootWindowScrollingStrategy implements ZootCameraScrollingStrategy
{	
	private static final float LERP_SPEED = 4.0f;
	
	private float windowSizeX = 4.0f;
	private float windowSizeY = 4.0f;
	private Vector2 topLeft = new Vector2();
	private Vector2 bottomRight = new Vector2();
	
	public ZootWindowScrollingStrategy(float windowScaleX, float windowScaleY)
	{
		this.windowSizeX = windowScaleX;
		this.windowSizeY = windowScaleY;
	}
		
	@Override
	public void scrollCamera(ZootCamera camera, float delta)
	{
		ZootActor actor = camera.getTarget();
		if(actor == null) return;
								
		//calculate window boundary
		float screenWidth = camera.getViewportWidth();
		float screenHeight = camera.getViewportHeight();
		
		Vector3 cameraPosition = camera.getPosition();
		topLeft.x = cameraPosition.x - screenWidth / windowSizeX;
		topLeft.y = cameraPosition.y + screenHeight / windowSizeY;
		
		bottomRight.x = cameraPosition.x + screenWidth / windowSizeX;
		bottomRight.y = cameraPosition.y - screenHeight / windowSizeY;
						
		//check if actor is outside of boundary	
		float actorX = actor.getX();
		float actorY = actor.getY();
		
		Vector3 cameraLookAt = camera.getPosition();				
		if(actorX > bottomRight.x)
		{
			cameraLookAt.x += actorX - bottomRight.x;			
		}
		else if(actorX < topLeft.x)
		{
			cameraLookAt.x -= topLeft.x - actorX;
		}
		if(actorY < bottomRight.y)
		{
			cameraLookAt.y += actorY - bottomRight.y;
		}
		else if(actorY > topLeft.y)
		{
			cameraLookAt.y -= topLeft.y - actorY;
		}
							
		//lerp to new position
		float dt = Math.min(1.0f, delta);	//so the camera won't wonder into space at first call		
		float lerpProgress = Math.min(LERP_SPEED * dt, 1.0f);		
		float newX = MathUtils.lerp(cameraPosition.x, cameraLookAt.x, lerpProgress);
		float newY = MathUtils.lerp(cameraPosition.y, cameraLookAt.y, lerpProgress);		
		camera.setPosition(newX, newY);
	}
}

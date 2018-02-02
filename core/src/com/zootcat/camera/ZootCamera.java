package com.zootcat.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.zootcat.scene.ZootActor;

//TODO add test
public class ZootCamera extends OrthographicCamera
{
	private ZootActor target;
	private boolean clipToLevel = false;
	private ZootCameraStrategy movementStrategy = ZootCameraNullStrategy.Instance;
	private float worldWidth;
	private float worldHeight;
	
	public ZootCamera(float worldWidth, float worldHeight)
	{
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
	}
	
	public ZootActor getTarget()
	{
		return target;
	}
	
	public void setTarget(ZootActor target)
	{
		this.target = target;
	}
	
	public void zoom(float amount)
	{
		this.zoom += amount;
	}
	
	public void setZoom(float zoom)
	{
		this.zoom = zoom;
	}
	
	public void update(float delta, boolean updateFrustum)
	{
		movementStrategy.calculateCameraPosition(target, position, delta);		
		
		if(isClippingToBoundary()) clipCameraToBoundary();
				
		super.update(updateFrustum);
	}
		
	public void setStrategy(ZootCameraStrategy strategy)
	{
		movementStrategy = strategy != null ? strategy : ZootCameraNullStrategy.Instance;
	}
	
	public void setClipToBoundary(boolean value)
	{
		clipToLevel = value;
	}
	
	public boolean isClippingToBoundary()
	{
		return clipToLevel;
	}
	
	private void clipCameraToBoundary()
	{		
		float minX = zoom * (viewportWidth / 2.0f);
		float maxX = worldWidth - minX;		
		float minY = zoom * (viewportHeight / 2.0f);
		float maxY = worldHeight - minY;
		
		position.x = MathUtils.clamp(position.x, minX, maxX);
		position.y = MathUtils.clamp(position.y, minY, maxY);		
	}	
}

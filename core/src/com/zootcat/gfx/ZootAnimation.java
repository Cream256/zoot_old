package com.zootcat.gfx;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ZootAnimation 
{	
	private String name;	
	private float animationTime;	
	private boolean playing;
	private Animation<TextureRegion> animation;
		
	public ZootAnimation(TextureRegion[] frames, float frameDuration)
	{
		this.name = "";
		this.playing = false;
		this.animationTime = 0.0f;
		animation = new Animation<TextureRegion>(frameDuration, frames);		
	}
		
	public TextureRegion getKeyFrame()
	{
		return animation.getKeyFrame(animationTime);
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
		
	public float getAnimationTime()
	{
		return animationTime;
	}
	
	public void step(float delta)
	{
		if(!playing)
		{
			return;
		}
		
		animationTime += delta;
	}
	
	public void start()
	{
		animationTime = 0.0f;
		playing = true;
	}
	
	public void stop()
	{
		playing = false;
	}
	
	public void restart()
	{
		stop();
		start();
	}
	
	public boolean isPlaying()
	{
		return playing;
	}
}

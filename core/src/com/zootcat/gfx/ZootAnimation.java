package com.zootcat.gfx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ZootAnimation 
{	
	private final String name;	//name must be final in order to make getId() immutable	
	private float animationTime;	
	private boolean playing;
	private Animation<TextureRegion> animation;
		
	public ZootAnimation(String name, TextureRegion[] frames, float frameDuration)
	{
		this.name = name;
		this.playing = false;
		this.animationTime = 0.0f;
		animation = new Animation<TextureRegion>(frameDuration, frames);		
	}
		
	public void setPlayMode(PlayMode playMode)
	{
		animation.setPlayMode(playMode);
	}
	
	public PlayMode getPlayMode()
	{
		return animation.getPlayMode();
	}
	
	public TextureRegion getKeyFrame()
	{
		return animation.getKeyFrame(animationTime);
	}
	
	public Texture getKeyFrameTexture()
	{
		return getKeyFrame().getTexture();
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
		playing = true;
	}
	
	public void pause()
	{
		playing = false;
	}
	
	public void stop()
	{
		playing = false;
		animationTime = 0.0f;
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
	
	public int getId()
	{
		return name.hashCode();
	}
	
	@Override
	public int hashCode()
	{
		return getId();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == this) return true;
		if(!(obj instanceof ZootAnimation)) return false;
		return getId() == ((ZootAnimation)obj).getId();
	}
}

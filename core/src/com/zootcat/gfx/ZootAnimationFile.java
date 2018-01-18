package com.zootcat.gfx;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.zootcat.exceptions.ZootException;
import com.zootcat.textdata.TextDataFile;
import com.zootcat.textdata.TextDataSection;

public class ZootAnimationFile
{
    private TextDataFile animationFile;
    private TextDataSection settings;
    private List<TextDataSection> animationDataX;
	
	public ZootAnimationFile(File file) throws ZootException
	{
		animationFile = new TextDataFile(file);
		settings = animationFile.readSections(":Settings", ":EndSettings").get(0);
		animationDataX = animationFile.readSections(":StartAnimation", ":EndAnimation");
	}

	public String getSpriteSheetPath()
	{        
		return settings.get("Image");
	}
	
	public Map<Integer, ZootAnimation> createAnimations(Texture spriteSheet)
	{
		Map<Integer, ZootAnimation> animations = new HashMap<Integer, ZootAnimation>();
		for(TextDataSection data : animationDataX)
		{
			/*
			 * TODO complete rest of the functionality like frameorder etc.
			int rightOffsetX = animationData.getInt("rightoffsetx", 0);
			int rightOffsetY = animationData.getInt("rightoffsety", 0);			
			int leftOffsetX = animationData.getInt("leftoffsetx", 0);
			int leftOffsetY = animationData.getInt("leftoffsety", 0);			
			Vector2[] rightOffsets = animationData.getVectors("rightoffsets");
			Vector2[] leftOffsets = animationData.getVectors("leftoffsets");
			*/        
			//
					
			TextureRegion[] frames = buildFrames(data, spriteSheet);
			TextureRegion[] orderedFrames = orderFrames(data, frames);
			
			ZootAnimation animation = new ZootAnimation(getName(data), orderedFrames, getFrameDuration(data));		
			animation.setPlayMode(getPlayMode(data));
			
			animations.put(animation.getId(), animation);
		}
		return animations;
	}

	private float getFrameDuration(TextDataSection data)
	{
		return data.getInt("Speed", 100) / 1000.0f;
	}
	
	private String getName(TextDataSection data)
	{
		return data.get("Type");
	}
	
	private PlayMode getPlayMode(TextDataSection data)
	{
		boolean loop = data.getBoolean("Loop", false);
		boolean pingPong = data.getBoolean("PingPong", false);
		if(!loop)
		{
			return PlayMode.NORMAL;
		}
		return pingPong ? PlayMode.LOOP_PINGPONG : PlayMode.LOOP;
	}
	
	private TextureRegion[] orderFrames(TextDataSection data, TextureRegion[] frames)
	{
		int[] frameOrder = data.getIntArray("FrameOrder");
		if(frameOrder.length == 0)
		{
			return frames;
		}
		
		TextureRegion[] orderedFrames = new TextureRegion[frameOrder.length];
		for(int i = 0; i < frameOrder.length; ++i)
		{
			int frameIndex = frameOrder[i];
			orderedFrames[i] = frames[frameIndex];
		}
		return orderedFrames;
	}
	
	private TextureRegion[] buildFrames(TextDataSection data, Texture spriteSheet)
	{
		int totalFrames = data.getInt("Frames", 0);
		int framesPerRow = data.getInt("FramesPerRow", totalFrames);
		int cols = framesPerRow;
		int rows = totalFrames / framesPerRow;
		
		ZootAnimationFrameBuilder frameBuilder = new ZootAnimationFrameBuilder();
		return frameBuilder.setFrameWidth(data.getInt("FrameWidth", 0))
						   .setFrameHeight(data.getInt("FrameHeight", 0))
						   .setOffsetX(data.getInt("FirstFrameX", 0))
						   .setOffsetY(data.getInt("FirstFrameY", 0))
						   .setRows(rows)
						   .setCols(cols)
						   .setFrameLimit(totalFrames)
						   .build(spriteSheet);
	}
}

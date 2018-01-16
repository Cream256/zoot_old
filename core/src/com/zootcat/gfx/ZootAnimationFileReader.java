package com.zootcat.gfx;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.zootcat.exceptions.ZootException;
import com.zootcat.textdata.TextDataFile;
import com.zootcat.textdata.TextDataSection;

public class ZootAnimationFileReader
{
    public static Map<Integer, ZootAnimation> readFromFile(File textDataFile) throws IOException, ZootException
    {
        TextDataFile animationFile = new TextDataFile(textDataFile);            
        
        TextDataSection settings = animationFile.readSections(":Settings", ":EndSettings").get(0);        
        Texture spriteSheet = new Texture(settings.get("Image"));
        
        Map<Integer, ZootAnimation> animations = new HashMap<Integer, ZootAnimation>();    
        for(TextDataSection animationData : animationFile.readSections(":StartAnimation", ":EndAnimation"))
        {
			/*
        	int frameWidth = animationData.getInt("framewidth", 0);
			int frameHeight = animationData.getInt("frameheight", 0);
			int firstFrameX = animationData.getInt("firstframex", 0);
			int firstFrameY = animationData.getInt("firstframey", 0); 
			int frameCount = animationData.getInt("frames", 0); 
			int frameDuration = animationData.getInt("speed", 100);
			int rightOffsetX = animationData.getInt("rightoffsetx", 0);
			int rightOffsetY = animationData.getInt("rightoffsety", 0);
			
			int leftOffsetX = animationData.getInt("leftoffsetx", 0);
			int leftOffsetY = animationData.getInt("leftoffsety", 0);
			
			Vector2[] rightOffsets = animationData.getVectors("rightoffsets");
			Vector2[] leftOffsets = animationData.getVectors("leftoffsets");
			 
			int framesPerRow = animationData.getInt("framesperrow", frameCount);     
			boolean loop = animationData.getBoolean("loop", true);
			int[] frameOrder = animationData.getIntArray("frameorder");
			*/        
        	//TODO complete rest of the functionality like frameorder etc.
        	
        	int totalFrames = animationData.getInt("frames", 0);
        	int framesPerRow = animationData.getInt("framesperrow", totalFrames);
        	int cols = framesPerRow;
        	int rows = totalFrames / framesPerRow;
        	float frameDuration = animationData.getInt("speed", 100) / 1000.0f;
        	
			ZootAnimationFrameBuilder frameBuilder = new ZootAnimationFrameBuilder();
			frameBuilder.setFrameWidth(animationData.getInt("framewidth", 0))
						.setFrameHeight(animationData.getInt("frameheight", 0))
						.setOffsetX(animationData.getInt("firstframex", 0))
						.setOffsetY(animationData.getInt("firstframey", 0))
						.setRows(rows)
						.setCols(cols);
			
			ZootAnimation animation = new ZootAnimation(animationData.get("type"), frameBuilder.build(spriteSheet), frameDuration);
			
			boolean loop = animationData.getBoolean("loop", false);
			boolean pingPong = animationData.getBoolean("pingpong", false);
			PlayMode playMode = loop ? (pingPong ? PlayMode.LOOP_PINGPONG : PlayMode.LOOP) : PlayMode.NORMAL;
			animation.setPlayMode(playMode);
			
			animations.put(animation.getId(), animation);
        }
        return animations;
    }
}

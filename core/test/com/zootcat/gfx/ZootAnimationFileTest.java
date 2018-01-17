package com.zootcat.gfx;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.zootcat.exceptions.ZootException;

public class ZootAnimationFileTest
{
	private Texture textureMock;
	private ZootAnimationFile anmFile;
	
	@Before
	public void setup() throws ZootException
	{
    	textureMock = mock(Texture.class);
		String animationFilePath = getClass().getClassLoader()
	  	        .getResource("testResources/textdata/AnimationFile.txt")
	  	        .getPath();
    	anmFile = new ZootAnimationFile(new File(animationFilePath));
	}
	
	@Test
	public void getSpriteSheetPathTest() throws ZootException
	{
		assertEquals("/data/sprites/SpriteAnimation.png", anmFile.getSpriteSheetPath());
	}
	
	@Test
	public void createAnimationsTest()
	{
		Map<Integer, ZootAnimation> animations = anmFile.createAnimations(textureMock);
		
		assertNotNull(animations);
		assertEquals(2, animations.size());
		
		ZootAnimation anim1 = animations.get("ANIMATION_1".hashCode());
		assertNotNull(anim1);
		assertEquals("ANIMATION_1", anim1.getName());
		assertEquals(10, anim1.getFrameCount());
		assertEquals(0.0f, anim1.getAnimationTime(), 0.0f);
		assertEquals(0.1f, anim1.getFrameDuration(), 0.0f);
		assertEquals(PlayMode.NORMAL, anim1.getPlayMode());
		
		ZootAnimation anim2 = animations.get("ANIMATION_2".hashCode());
		assertNotNull(anim2);
		assertEquals("ANIMATION_2", anim2.getName());
		assertEquals(4, anim2.getFrameCount());
		assertEquals(0.0f, anim2.getAnimationTime(), 0.0f);
		assertEquals(0.25f, anim2.getFrameDuration(), 0.0f);
		assertEquals(PlayMode.LOOP_PINGPONG, anim2.getPlayMode());
	}
}

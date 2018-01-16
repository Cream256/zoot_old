package com.zootcat.gfx;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ZootAnimationTest 
{
	private static final int FRAME_WIDTH = 0;
	private static final int FRAME_HEIGHT = 0;
	private static final float FRAME_DURATION = 0.25f;
	
	private Texture textureMock;
	private TextureRegion[] frames;
	private ZootAnimation animation;
	
	@Before
	public void setup()
	{
		textureMock = mock(Texture.class);
		frames = new TextureRegion[3]; 
		frames[0] = new TextureRegion(textureMock, 0, 0, FRAME_WIDTH, FRAME_HEIGHT);	
		frames[1] = new TextureRegion(textureMock, FRAME_WIDTH, 0, FRAME_WIDTH, FRAME_HEIGHT);
		frames[2] = new TextureRegion(textureMock, FRAME_WIDTH * 2, 0, FRAME_WIDTH, FRAME_HEIGHT);
		animation = new ZootAnimation(frames, FRAME_DURATION);
	}
	
	@Test
	public void setNameTest()
	{
		assertEquals("", animation.getName());
		
		animation.setName("Test Name");
		assertEquals("Test Name", animation.getName());
	}
		
	@Test
	public void stepTest()
	{
		assertEquals(0.0f, animation.getAnimationTime(), 0.0f);
		
		animation.start();		
		animation.step(0.25f);
		assertEquals(0.25f, animation.getAnimationTime(), 0.0f);
		
		animation.step(0.25f);
		assertEquals(0.50f, animation.getAnimationTime(), 0.0f);
		
		animation.stop();
		animation.step(0.25f);
		assertEquals("After stop animation should not step further", 0.50f, animation.getAnimationTime(), 0.0f);
	}
	
	@Test
	public void getKeyFrameTest()
	{
		assertEquals(frames[0], animation.getKeyFrame());
		
		animation.start();
		animation.step(0.0f);
		assertEquals("After start should be at first frame", frames[0], animation.getKeyFrame());
		
		animation.step(FRAME_DURATION / 2.0f);
		assertEquals("During 1/2 frame duration should be at first frame", frames[0], animation.getKeyFrame());
		
		animation.step(FRAME_DURATION / 2.0f);
		assertEquals("After frame duration passes should go to next frame", frames[1], animation.getKeyFrame());
		
		animation.step(FRAME_DURATION);
		assertEquals("After frame duration passes go to last frame", frames[2], animation.getKeyFrame());
	}
	
	@Test
	public void startTest()
	{
		assertFalse(animation.isPlaying());
		
		animation.start();
		assertTrue(animation.isPlaying());
	}
	
	@Test
	public void stopTest()
	{
		animation.start();
		assertTrue(animation.isPlaying());
		
		animation.stop();
		assertFalse(animation.isPlaying());
	}
	
	@Test
	public void restartTest()
	{
		assertEquals(0.0f, animation.getAnimationTime(), 0.0f);
		
		animation.start();
		animation.step(0.50f);
		assertTrue(animation.isPlaying());
		assertEquals(0.50f, animation.getAnimationTime(), 0.0f);
		
		animation.restart();
		assertTrue(animation.isPlaying());
		assertEquals(0.00f, animation.getAnimationTime(), 0.0f);
	}
}

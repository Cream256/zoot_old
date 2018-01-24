package com.zootcat.gfx;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;
import com.zootcat.exceptions.ZootException;
import com.zootcat.testing.ZootTestUtils;

public class ZootAnimationFileTest
{
	private Texture textureMock;
	private ZootAnimationFile anmFile;
	
	@Before
	public void setup() throws ZootException
	{
    	textureMock = mock(Texture.class);
		String animationFilePath = ZootTestUtils.getResourcePath("testResources/textdata/AnimationFile.txt", this);
    	anmFile = new ZootAnimationFile(new File(animationFilePath));
	}
	
	@Test
	public void getSpriteSheetFileNameTest() throws ZootException
	{
		assertEquals("/data/sprites/SpriteAnimation.png", anmFile.getSpriteSheetFileName());
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
		assertEquals(anim1.getFrameCount(), anim1.getOffsets().length);
		
		Vector2 emptyVector = new Vector2();
		for(int offsetIndex = 0; offsetIndex < anim1.getFrameCount(); ++offsetIndex)
		{
			assertEquals(emptyVector, anim1.getOffsets()[offsetIndex].right);
			assertEquals(emptyVector, anim1.getOffsets()[offsetIndex].left);
		}
		
		ZootAnimation anim2 = animations.get("ANIMATION_2".hashCode());
		assertNotNull(anim2);
		assertEquals("ANIMATION_2", anim2.getName());
		assertEquals(4, anim2.getFrameCount());
		assertEquals(0.0f, anim2.getAnimationTime(), 0.0f);
		assertEquals(0.25f, anim2.getFrameDuration(), 0.0f);
		assertEquals(PlayMode.LOOP_PINGPONG, anim2.getPlayMode());
		assertEquals(5, anim2.getOffsets().length);
		
		assertEquals(0.0f, anim2.getOffsets()[0].right.x, 0.0f);
		assertEquals(1.0f, anim2.getOffsets()[0].right.y, 0.0f);
		assertEquals(2.0f, anim2.getOffsets()[1].right.x, 0.0f);
		assertEquals(3.0f, anim2.getOffsets()[1].right.y, 0.0f);
		assertEquals(4.0f, anim2.getOffsets()[2].right.x, 0.0f);
		assertEquals(5.0f, anim2.getOffsets()[2].right.y, 0.0f);
		assertEquals(6.0f, anim2.getOffsets()[3].right.x, 0.0f);
		assertEquals(7.0f, anim2.getOffsets()[3].right.y, 0.0f);
		assertEquals(8.0f, anim2.getOffsets()[4].right.x, 0.0f);
		assertEquals(9.0f, anim2.getOffsets()[4].right.y, 0.0f);
		
		assertEquals(10.0f, anim2.getOffsets()[0].left.x, 0.0f);
		assertEquals(11.0f, anim2.getOffsets()[0].left.y, 0.0f);
		assertEquals(12.0f, anim2.getOffsets()[1].left.x, 0.0f);
		assertEquals(13.0f, anim2.getOffsets()[1].left.y, 0.0f);
		assertEquals(14.0f, anim2.getOffsets()[2].left.x, 0.0f);
		assertEquals(15.0f, anim2.getOffsets()[2].left.y, 0.0f);
		assertEquals(16.0f, anim2.getOffsets()[3].left.x, 0.0f);
		assertEquals(17.0f, anim2.getOffsets()[3].left.y, 0.0f);
		assertEquals(18.0f, anim2.getOffsets()[4].left.x, 0.0f);
		assertEquals(19.0f, anim2.getOffsets()[4].left.y, 0.0f);		
	}
}

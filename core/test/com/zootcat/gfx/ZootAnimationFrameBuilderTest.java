package com.zootcat.gfx;

import static org.mockito.Mockito.*;

import java.util.Arrays;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.zootcat.exceptions.RuntimeZootException;

public class ZootAnimationFrameBuilderTest
{
	private Texture spriteSheet;
	private ZootAnimationFrameBuilder builder;
		
	@Before
	public void setup()
	{
		spriteSheet = mock(Texture.class);
		builder = new ZootAnimationFrameBuilder();
	}
	
	@Test
	public void buildTest()
	{
		final int cols = 5;
		final int rows = 6;
		final int frameWidth = 10;
		final int frameHeight = 25;
		final int offsetX = 100;
		final int offsetY = 200;
		
		TextureRegion[] frames = builder.setCols(cols)
										.setRows(rows)
										.setFrameWidth(frameWidth)
										.setFrameHeight(frameHeight)
										.setOffsetX(offsetX)
										.setOffsetY(offsetY)
										.build(spriteSheet);			
		assertNotNull(frames);
		assertEquals(cols * rows, frames.length);
		
		Arrays.stream(frames).forEach((frame) ->
		{		
			assertEquals(spriteSheet, frame.getTexture());			
			assertEquals(frameWidth, frame.getRegionWidth());
			assertEquals(frameHeight, frame.getRegionHeight());
		});
		
		//after succesfull build, the values should reset
		assertEquals(0, builder.getCols());
		assertEquals(0, builder.getRows());
		assertEquals(0, builder.getFrameWidth());
		assertEquals(0, builder.getFrameHeight());
		assertEquals(0, builder.getOffsetX());
		assertEquals(0, builder.getOffsetY());
	}
	
	@Test
	public void buildShouldThrowIfNotAllParametersAreSetTest()
	{
		shouldThrowOnBuildWithNotAllParams(builder.setCols(0).setRows(1).setFrameWidth(1).setFrameHeight(1));
		shouldThrowOnBuildWithNotAllParams(builder.setCols(1).setRows(0).setFrameWidth(1).setFrameHeight(1));
		shouldThrowOnBuildWithNotAllParams(builder.setCols(1).setRows(1).setFrameWidth(0).setFrameHeight(1));
		shouldThrowOnBuildWithNotAllParams(builder.setCols(1).setRows(1).setFrameWidth(1).setFrameHeight(0));
	}
		
	@Test(expected = RuntimeZootException.class)
	public void buildShouldThrowIfSpriteSheetIsNotGivenTest()
	{
		builder.setCols(1).setRows(1).setFrameHeight(1).setFrameWidth(1);
		builder.build(null);
	}
	
	@Test
	public void setRowsTest()
	{
		assertEquals(0, builder.getRows());		
		assertEquals(256, builder.setRows(256).getRows());
	}
	
	@Test
	public void setColsTest()
	{
		assertEquals(0, builder.getCols());		
		assertEquals(128, builder.setCols(128).getCols());
	}
	
	@Test
	public void setOffsetXTest()
	{
		assertEquals(0, builder.getOffsetX());		
		assertEquals(64, builder.setOffsetX(64).getOffsetX());
	}
	
	@Test
	public void setOffsetYTest()
	{
		assertEquals(0, builder.getOffsetY());		
		assertEquals(32, builder.setOffsetY(32).getOffsetY());
	}
	
	@Test
	public void setFrameWidthTest()
	{
		assertEquals(0, builder.getFrameWidth());		
		assertEquals(10, builder.setFrameWidth(10).getFrameWidth());
	}
	
	@Test
	public void setFrameHeightTest()
	{
		assertEquals(0, builder.getFrameHeight());	
		assertEquals(5, builder.setFrameHeight(5).getFrameHeight());
	}
	
	private void shouldThrowOnBuildWithNotAllParams(ZootAnimationFrameBuilder builder)
	{
		try
		{
			builder.build(spriteSheet);
			fail("Builder did not throw when all required parameters were not set");
		}
		catch(Exception e)
		{
			//ok
		}
	}
}

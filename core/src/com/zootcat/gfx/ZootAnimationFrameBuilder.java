package com.zootcat.gfx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.zootcat.exceptions.RuntimeZootException;

public class ZootAnimationFrameBuilder 
{
	private int rows = 0;
	private int cols = 0;
	private int offsetX = 0;
	private int offsetY = 0;
	private int frameWidth = 0;
	private int frameHeight = 0;
	
	public TextureRegion[] build(Texture spriteSheet)
	{
		validate(spriteSheet);
		
		int animationWidth = frameWidth * cols;
		int animationHeight = frameHeight * rows;
		
		TextureRegion animationRegion = new TextureRegion(spriteSheet, offsetX, offsetY, animationWidth, animationHeight);
		TextureRegion[][] framesTable = animationRegion.split(frameWidth, frameHeight);
		
		TextureRegion[] frames = new TextureRegion[rows * cols];
		int index = 0;
		for (int row = 0; row < rows; row++) 
		{
			for (int col = 0; col < cols; col++) 
			{
				frames[index++] = framesTable[row][col];
			}
		}
		
		reset();
		return frames;
	}
	
	public int getRows() 
	{
		return rows;
	}

	public ZootAnimationFrameBuilder setRows(int rows)
	{
		this.rows = rows;
		return this;
	}
	
	public int getCols() 
	{
		return cols;
	}
	
	public ZootAnimationFrameBuilder setCols(int cols) 
	{
		this.cols = cols;
		return this;
	}

	public int getOffsetX() 
	{
		return offsetX;
	}

	public ZootAnimationFrameBuilder setOffsetX(int offsetX) 
	{
		this.offsetX = offsetX;
		return this;
	}
	
	public int getOffsetY() 
	{
		return offsetY;
	}

	public ZootAnimationFrameBuilder setOffsetY(int offsetY) 
	{
		this.offsetY = offsetY;
		return this;
	}

	public int getFrameWidth() 
	{
		return frameWidth;
	}

	public ZootAnimationFrameBuilder setFrameWidth(int frameWidth)
	{
		this.frameWidth = frameWidth;
		return this;
	}
	
	public int getFrameHeight() 
	{
		return frameHeight;
	}

	public ZootAnimationFrameBuilder setFrameHeight(int frameHeight)
	{
		this.frameHeight = frameHeight;
		return this;
	}
	
	private void reset() 
	{
		rows = 0;
		cols = 0;
		offsetX = 0;
		offsetY = 0;
		frameWidth = 0;
		frameHeight = 0;
	}
	
	private void validate(Texture spriteSheet) 
	{
		int validationResult = rows * cols * frameWidth * frameHeight;
		if(validationResult == 0 || spriteSheet == null)
		{
			throw new RuntimeZootException("Frame builder does not have all required fields set up.");
		}
	}
}
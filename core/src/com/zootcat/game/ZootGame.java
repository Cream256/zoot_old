package com.zootcat.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.zootcat.assets.ZootAssetManager;

public abstract class ZootGame extends Game
{		
	private float unitPerTile = 1.0f;
	private float viewportWidth = 16.0f;
	private float viewportHeight = 9.0f;
	private ZootAssetManager assetManager;
	
	public ZootGame()
	{
		assetManager = new ZootAssetManager();
	}
	    
    @Override
    public void dispose()
    {
    	super.dispose();
    	
    	assetManager.dispose();
    	assetManager = null;
    }
    
    public AssetManager getAssetManager()
    {
    	return assetManager;
    }
    
    public void setViewportWidth(float width)
    {
    	viewportWidth = width;
    }
    
    public float getViewportWidth()
    {
    	return viewportWidth;
    }
    
    public void setViewportHeight(float height)
    {
    	viewportHeight = height;
    }
    
    public float getViewportHeight()
    {
    	return viewportHeight;
    }
    
    public void setUnitPerTile(float unitPerTile)
    {
    	this.unitPerTile = unitPerTile;
    }
    
    public float getUnitPerTile()
    {
    	return unitPerTile;
    }
}

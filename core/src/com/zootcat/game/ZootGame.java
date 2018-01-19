package com.zootcat.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.zootcat.assets.ZootAssetManager;
import com.zootcat.screen.ZootSceneLoadingScreen;

public class ZootGame extends Game
{	
	private static final String MAP_FILE = "data/TestBed.tmx";
	
	private ZootAssetManager assetManager;
	private float viewportWidth = 16.0f;
	private float viewportHeight = 9.0f;
	private float unitPerTile = 1.0f;
	
	public ZootGame(float viewportWidth, float viewportHeight, float unitPerTile)
	{
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
		this.unitPerTile = unitPerTile;
		this.assetManager = new ZootAssetManager();
		this.assetManager.getLogger().setLevel(Application.LOG_DEBUG);
	}
	
    @Override
    public void create()
    {    	
    	ZootSceneLoadingScreen loadingScreen = new ZootSceneLoadingScreen(this, MAP_FILE); 	
    	setScreen(loadingScreen);
    }
    
    public AssetManager getAssetManager()
    {
    	return assetManager;
    }
    
    public float getViewportWidth()
    {
    	return viewportWidth;
    }
    
    public float getViewportHeight()
    {
    	return viewportHeight;
    }
    
    public float getUnitPerTile()
    {
    	return unitPerTile;
    }
}

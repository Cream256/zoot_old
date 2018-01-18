package com.zootcat.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.zootcat.assets.ZootAssetManager;
import com.zootcat.controllers.factory.ControllerFactory;
import com.zootcat.screen.ZootSceneLoadingScreen;

public class ZootGame extends Game
{	
	private static final float VIEWPORT_WIDTH = 7.0f;
	private static final float VIEWPORT_HEIGHT = 4.0f;
	private static final float UNIT_PER_TILE = 0.17f;	//1 tile = 17cm
	
	private AssetManager assetManager;
	private ControllerFactory controllerFactory;
	
    @Override
    public void create()
    {       	
    	//create asset manager
    	assetManager = new ZootAssetManager();
    	
    	//create controller factory
    	controllerFactory = new ControllerFactory();
    	
    	//loading screen
    	ZootSceneLoadingScreen screen = new ZootSceneLoadingScreen(this, "data/TestBed.tmx", VIEWPORT_WIDTH, VIEWPORT_HEIGHT, UNIT_PER_TILE);    	
    	setScreen(screen);
    }
    
	@Override
	public void dispose () 
	{
		super.dispose();
		
		assetManager.dispose();
		assetManager = null;
		
		controllerFactory = null;
	}
	
	public AssetManager getAssetManager()
	{
		return assetManager;
	}
	
	public ControllerFactory getControllerFactory()
	{
		return controllerFactory;
	}
}

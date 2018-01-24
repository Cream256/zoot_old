package com.zootcat.screen;

import com.zootcat.game.ZootGame;
import com.zootcat.map.tiled.ZootTiledMap;
import com.zootcat.scene.ZootScene;
import com.zootcat.scene.tiled.ZootTiledScene;

public class ZootSceneLoadingScreen extends ZootLoadingScreen
{
	private ZootGame game;
	private String mapFileName;
		
	public ZootSceneLoadingScreen(ZootGame game, String mapFileName)
	{
		super(game.getAssetManager());
		this.game = game;
		this.mapFileName = mapFileName;		
		addTask((Void) -> {game.getAssetManager().load(mapFileName, ZootTiledMap.class); });
	}
	
	protected void onFinishLoading()
	{
		ZootTiledMap tiledMap = getAssetManager().get(mapFileName, ZootTiledMap.class);
		ZootScene scene = new ZootTiledScene(tiledMap, 
											 getAssetManager(),
											 game.getViewportWidth(),
											 game.getViewportHeight(), 
											 game.getUnitPerTile());
		game.setScreen(new ZootSceneScreen(scene));
	}
}

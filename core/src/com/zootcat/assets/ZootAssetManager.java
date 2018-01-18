package com.zootcat.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class ZootAssetManager extends AssetManager
{
	public ZootAssetManager()
	{
		setLoader(TiledMap.class, new TmxMapLoader());
	}
}
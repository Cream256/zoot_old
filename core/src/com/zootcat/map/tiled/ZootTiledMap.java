package com.zootcat.map.tiled;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.map.ZootMap;

public class ZootTiledMap implements ZootMap
{	
	private static final String COLLISION_LAYER_NAME = "Collision";
	private static final String COLLIDABLE_PROPERTY = "collidable";
	
	private final TiledMap tiledMap;
	
	public ZootTiledMap(TiledMap tiledMap)
	{
		this.tiledMap = tiledMap;
	}
	
	public TiledMap getTiledMap()
	{
		return tiledMap;
	}
	
	public List<MapObject> getAllObjects()
	{
		List<MapObject> result = new ArrayList<MapObject>();
		for(MapLayer layer : tiledMap.getLayers())
		{
			for(MapObject obj : layer.getObjects())
			{
				result.add(obj);
			}
		}
		return result;
	}
	
	public List<ZootTiledMapCell> getCollidableCells()
	{
		return getLayerCells(COLLISION_LAYER_NAME).stream().filter(cell -> cell.collidable > 0).collect(Collectors.toList());
	}
	
	public List<ZootTiledMapCell> getLayerCells(String layerName)
	{
		if(!ClassReflection.isInstance(TiledMapTileLayer.class, tiledMap.getLayers().get(layerName)))
		{
			return Collections.<ZootTiledMapCell>emptyList();
		}
		
		TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(layerName);
		int layerWidth = layer.getWidth();
		int layerHeight = layer.getHeight();
		
		List<ZootTiledMapCell> result = new ArrayList<ZootTiledMapCell>();
		for(int row = 0; row < layerHeight; ++row)
		{
			for(int col = 0; col < layerWidth; ++col)
			{
				Cell cell = layer.getCell(col, row);
				if(cell == null)
				{
					continue;
				}
				
				int collidable = Integer.valueOf(cell.getTile().getProperties().get(COLLIDABLE_PROPERTY, "0", String.class));
				result.add(new ZootTiledMapCell(col, row, layer.getTileWidth(), layer.getTileHeight(), collidable, cell));
			}
		}
		return result;
	}

	@Override
	public void dispose() 
	{
		tiledMap.dispose();
	}
}

package com.zootcat.map.tiled;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.reflect.ClassReflection;

public class ZootTiledMap implements Disposable
{	
	public static final String COLLISION_LAYER_NAME = "Collision";
	public static final String BACKGROUND_COLOR_PROPERTY = "backgroundcolor";
	public static final String TILE_WIDTH_PROPERTY = "tilewidth";
	public static final String TILE_HEIGHT_PROPERTY = "tilewidth";
		
	private TiledMap tiledMap;
	
	public ZootTiledMap(final TiledMap tiledMap)
	{
		this.tiledMap = tiledMap;
	}
	
	public int getTileWidth()
	{
		return tiledMap.getProperties().get(TILE_WIDTH_PROPERTY, Integer.class);
	}
	
	public int getTileHeight()
	{
		return tiledMap.getProperties().get(TILE_HEIGHT_PROPERTY, Integer.class);
	}
	
	public Color getBackgroundColor() 
	{
		return Color.valueOf(tiledMap.getProperties().get(BACKGROUND_COLOR_PROPERTY, String.class));
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
				MapObject objectWithProperties = createMapObjectWithProperties(obj, layer);
				result.add(objectWithProperties);
			}
		}
		return result;
	}
	
	public TiledMapTileLayer getLayer(String layerName)
	{
		return (TiledMapTileLayer) tiledMap.getLayers().get(layerName);		
	}
	
	public List<ZootTiledMapCell> getLayerCells(String layerName)
	{
		if(!ClassReflection.isInstance(TiledMapTileLayer.class, tiledMap.getLayers().get(layerName)))
		{
			return Collections.<ZootTiledMapCell>emptyList();
		}
		
		TiledMapTileLayer layer = getLayer(layerName);
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
				result.add(new ZootTiledMapCell(col, row, layer.getTileWidth(), layer.getTileHeight(), cell));
			}
		}
		return result;
	}

	@Override
	public void dispose() 
	{
		if(tiledMap != null)
		{
			tiledMap.dispose();
			tiledMap = null;
		}
	}
	
	private MapObject createMapObjectWithProperties(final MapObject obj, final MapLayer layer) 
	{
		MapObject result = new MapObject();
		result.setName(obj.getName());
		result.setColor(obj.getColor());
		result.setOpacity(obj.getOpacity());
		result.setVisible(obj.isVisible());
		
		int gid = obj.getProperties().get("gid", -1, Integer.class);
		TiledMapTile tile = tiledMap.getTileSets().getTile(gid);
		if(tile != null)
		{
			result.getProperties().putAll(tile.getProperties());	
		}
		result.getProperties().putAll(obj.getProperties());		
		return result;
	}	
}

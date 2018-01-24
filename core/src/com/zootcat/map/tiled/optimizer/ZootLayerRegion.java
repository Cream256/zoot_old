package com.zootcat.map.tiled.optimizer;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public class ZootLayerRegion
{
	public int x;
	public int y;
	public int width;
	public int height;
	public Cell cell;
	
	public ZootLayerRegion(int x, int y, Cell cell)
	{
		this.x = x;
		this.y = y;
		this.width = 1;
		this.height = 1;
		this.cell = cell;
	}
}

package com.zootcat.map.tiled.optimizer;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public class ZootLayerOptimizer
{
	public static List<ZootLayerRegion> optimize(TiledMapTileLayer layer, ZootTiledCellComparator comparator)
	{
		List<ZootLayerRegion> regions = new ArrayList<ZootLayerRegion>();
		
		boolean[][] takenCells = new boolean[layer.getWidth()][layer.getHeight()];
		
		for(int y = 0; y < layer.getHeight(); ++y)
		{	
			for(int x = 0; x < layer.getWidth(); ++x)
			{
				if(takenCells[x][y] == true)
				{
					continue;
				}
				
				takenCells[x][y] = true;				
				Cell firstCell = layer.getCell(x, y);
				if(firstCell == null) continue;
				
				ZootLayerRegion region = new ZootLayerRegion(x, y, firstCell);
				for(int rx = x + 1; rx < layer.getWidth(); ++rx)
				{
					Cell cell = layer.getCell(rx, y);
					if(cell == null || takenCells[rx][y] == true || !comparator.areEqual(firstCell, cell))
					{
						break;
					}
					takenCells[rx][y] = true;
					++region.width;				
				}
				regions.add(region);
			}
		}
		return regions;
	}
}

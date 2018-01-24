package com.zootcat.map.tiled.optimizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.zootcat.assets.ZootAssetRecognizer;
import com.zootcat.map.tiled.ZootTiledMap;
import com.zootcat.map.tiled.ZootTiledMapLoader;
import com.zootcat.testing.GdxTestRunner;
import com.zootcat.testing.ZootTestUtils;

@RunWith(GdxTestRunner.class)
public class ZootLayerOptimizerTest
{	
	@Test
	public void optimizeSingleVerticalTilesTest()
	{
		//given
		String mapFile = ZootTestUtils.getResourcePath("testResources/tiled/SingleVerticalTiles.tmx", this);		
		ZootTiledMap map = new ZootTiledMapLoader(new ZootAssetRecognizer()).load(mapFile);		
		TiledMapTileLayer layer = map.getLayer("Layer1");
		ZootTiledCellTileComparator comparator = new ZootTiledCellTileComparator();		
		
		//when
		List<ZootLayerRegion> regions = ZootLayerOptimizer.optimize(layer, comparator);
		
		//then		
		assertNotNull(regions);
		assertEquals(1, regions.size());
		assertEquals(3, regions.get(0).x);
		assertEquals(5, regions.get(0).y);
		assertEquals(4, regions.get(0).width);
		assertEquals(1, regions.get(0).height);
	}
	
	@Test
	public void optimizeVerticalTilesTest()
	{
		//given
		String mapFile = ZootTestUtils.getResourcePath("testResources/tiled/VerticalTiles.tmx", this);		
		ZootTiledMap map = new ZootTiledMapLoader(new ZootAssetRecognizer()).load(mapFile);		
		TiledMapTileLayer layer = map.getLayer("Layer1");
		ZootTiledCellTileComparator comparator = new ZootTiledCellTileComparator();		
		
		//when
		List<ZootLayerRegion> regions = ZootLayerOptimizer.optimize(layer, comparator);
		
		//then		
		assertNotNull(regions);
		assertEquals(6, regions.size());
		
		assertEquals(0, regions.get(0).x);
		assertEquals(0, regions.get(0).y);
		assertEquals(2, regions.get(0).width);
		assertEquals(1, regions.get(0).height);	
		
		assertEquals(4, regions.get(1).x);
		assertEquals(0, regions.get(1).y);
		assertEquals(2, regions.get(1).width);
		assertEquals(1, regions.get(1).height);
		
		assertEquals(8, regions.get(2).x);
		assertEquals(0, regions.get(2).y);
		assertEquals(2, regions.get(2).width);
		assertEquals(1, regions.get(2).height);
		
		assertEquals(0, regions.get(3).x);
		assertEquals(3, regions.get(3).y);
		assertEquals(3, regions.get(3).width);
		assertEquals(1, regions.get(3).height);
		
		assertEquals(3, regions.get(4).x);
		assertEquals(3, regions.get(4).y);
		assertEquals(4, regions.get(4).width);
		assertEquals(1, regions.get(4).height);
		
		assertEquals(7, regions.get(5).x);
		assertEquals(3, regions.get(5).y);
		assertEquals(3, regions.get(5).width);
		assertEquals(1, regions.get(5).height);
	}
}
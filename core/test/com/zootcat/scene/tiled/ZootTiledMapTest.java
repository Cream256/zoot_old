package com.zootcat.scene.tiled;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.zootcat.map.tiled.ZootTiledMap;
import com.zootcat.map.tiled.ZootTiledMapCell;

public class ZootTiledMapTest 
{	
	private TiledMap tiledMapMock;
	private TiledMapTileSets tilesetsMock;
	private MapLayers mapLayersMock;
	private ZootTiledMap map;
	
	@Before
	public void setup()
	{		
		mapLayersMock = new MapLayers();
		tilesetsMock = new TiledMapTileSets();
		tiledMapMock = mock(TiledMap.class);	
		
		mapLayersMock.add(new MapLayer());
		mapLayersMock.add(new MapLayer());
		
		when(tiledMapMock.getLayers()).thenReturn(mapLayersMock);
		when(tiledMapMock.getTileSets()).thenReturn(tilesetsMock);
		
		map = new ZootTiledMap(tiledMapMock);
	}
	
	@Test
	public void disposeTest()
	{
		map.dispose();
		assertNull(map.getTiledMap());
		verify(tiledMapMock, times(1)).dispose();
	}
	
	@Test
	public void getCollidableCellsTest()
	{
		//given
		final int tileWidth = 32;
		final int tileHeight = 48;
		
		mapLayersMock.get(0).setName("First Layer");
		mapLayersMock.get(1).setName("Second layer");
	
		TiledMapTile notCollidableTile = mock(TiledMapTile.class); 
		when(notCollidableTile.getProperties()).thenReturn(new MapProperties());
		
		Cell notCollidableCell = new Cell();
		notCollidableCell.setTile(notCollidableTile);
		
		MapProperties collidableTileProperties = new MapProperties();
		collidableTileProperties.put(ZootTiledMap.COLLIDABLE_PROPERTY, "1");
		TiledMapTile collidableTile = mock(TiledMapTile.class); 
		when(collidableTile.getProperties()).thenReturn(collidableTileProperties);
				
		Cell collidableCell = new Cell();
		collidableCell.setTile(collidableTile);
		
		TiledMapTileLayer tiledLayer = new TiledMapTileLayer(100, 80, tileWidth, tileHeight); 
		tiledLayer.setName(ZootTiledMap.COLLISION_LAYER_NAME);
		tiledLayer.setCell(0, 0, collidableCell);
		tiledLayer.setCell(10, 20, collidableCell);
		tiledLayer.setCell(30, 40, notCollidableCell);
		
		mapLayersMock.add(tiledLayer);
		
		//when
		List<ZootTiledMapCell> cells = map.getCollidableCells();
		
		//then
		assertEquals(2, cells.size());
		assertEquals(1, cells.get(0).collidable);
		assertEquals(0, cells.get(0).x, 0.0f);
		assertEquals(0, cells.get(0).y, 0.0f);
		assertEquals(tileWidth, cells.get(0).width, 0.0f);
		assertEquals(tileHeight, cells.get(0).height, 0.0f);
		assertEquals(1, cells.get(1).collidable);
		assertEquals(10, cells.get(1).x);
		assertEquals(20, cells.get(1).y);
		assertEquals(tileWidth, cells.get(1).width, 0.0f);
		assertEquals(tileHeight, cells.get(1).height, 0.0f);
	}
	
	@Test
	public void getAllObjectsShouldReturnValidObjects()
	{
		//given
		MapObject mapObject = new MapObject();
		mapObject.setName("Object");
		mapObject.setColor(Color.RED);
		mapObject.setOpacity(0.5f);
		mapObject.setVisible(false);		
		mapLayersMock.get(0).getObjects().add(mapObject);
		
		//when
		List<MapObject> mapObjects = map.getAllObjects();
		
		//then
		assertEquals(1, mapObjects.size());
		assertEquals("Object", mapObjects.get(0).getName());
		assertEquals(Color.RED, mapObjects.get(0).getColor());
		assertEquals(0.5f, mapObjects.get(0).getOpacity(), 0.0f);
		assertEquals(false, mapObjects.get(0).isVisible());
	}
	
	@Test
	public void getObjectsShouldReturnObjectWithCombinedProperties()
	{
		//given
		MapObject mapObject = new MapObject();
		mapObject.setName("Obj");
		mapObject.getProperties().put("LocalProp", "1");
		mapObject.getProperties().put("GlobalProp", "A");
		mapObject.getProperties().put("gid", 1);		
		mapLayersMock.get(0).getObjects().add(mapObject);
		
		TiledMapTile tile = new StaticTiledMapTile(mock(TextureRegion.class));
		tile.setId(1);
		tile.getProperties().put("UniqueProperty", "U");
		tile.getProperties().put("GlobalProp", "B");
		
		TiledMapTileSet tileset = new TiledMapTileSet();
		tileset.putTile(1, tile);
		
		tilesetsMock.addTileSet(tileset);
		
		//when
		List<MapObject> mapObjects = map.getAllObjects();
		
		//then
		assertEquals(1, mapObjects.size());
		
		MapObject obj = mapObjects.get(0);
		assertEquals("1", obj.getProperties().get("LocalProp"));
		assertEquals("Global property should be overriden by local one", "A", obj.getProperties().get("GlobalProp"));
		assertEquals(1, obj.getProperties().get("gid"));
		assertEquals("U", obj.getProperties().get("UniqueProperty"));		
	}

	@Test
	public void getObjectsShouldReturnObjectsFromAllLayers()
	{
		//given
		MapObject obj1 = new MapObject();
		obj1.setName("Obj1");
		
		MapObject obj2 = new MapObject();
		obj2.setName("Obj2");
		
		mapLayersMock.get(0).getObjects().add(obj1);
		mapLayersMock.get(1).getObjects().add(obj2);
		
		//when
		List<MapObject> mapObjects = map.getAllObjects();
		
		//then
		assertEquals(2, mapObjects.size());
		assertEquals("Obj1", mapObjects.get(0).getName());
		assertEquals("Obj2", mapObjects.get(1).getName());
	}
	
	
}

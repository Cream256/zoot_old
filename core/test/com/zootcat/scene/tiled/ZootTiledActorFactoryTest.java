package com.zootcat.scene.tiled;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.zootcat.controllers.factory.ControllerFactory;
import com.zootcat.controllers.physics.StaticBodyController;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.map.tiled.ZootTiledMapCell;
import com.zootcat.physics.ZootPhysics;
import com.zootcat.scene.ZootActor;

public class ZootTiledActorFactoryTest
{	
	private static final int ACTOR_ID = 1;
	private static final String ACTOR_NAME = "abc";
	private static final Color ACTOR_COLOR = Color.RED;
	private static final float ACTOR_X = 1.0f;
	private static final float ACTOR_Y = 2.0f;
	private static final float ACTOR_WIDTH = 100.0f;
	private static final float ACTOR_HEIGHT = 200.0f;
	private static final float ACTOR_OPACITY = 0.0f;
	private static final float ACTOR_ROTATION = 45.0f;
	private static final boolean ACTOR_VISIBLE = false;
	private static final int CELL_X = 5;
	private static final int CELL_Y = 7;
	private static final float CELL_WIDTH = 32;
	private static final float CELL_HEIGHT = 48;
			
	@Mock private ZootTiledScene sceneMock;	
	@Mock private ZootPhysics physicsMock;
	@Mock private TiledMapTile tile;
	@Mock private Cell innerCell;
	private MapProperties tileProperties;
	private ControllerFactory ctrlFactory;
	private ZootTiledActorFactory factory;
		
	@Before
	public void setup()
	{				
		MockitoAnnotations.initMocks(this);
		
		//mock scene
		when(physicsMock.createBody(any(BodyDef.class))).thenReturn(mock(Body.class));		
		when(sceneMock.getPhysics()).thenReturn(physicsMock);
		
		//mock tile and inner cell
		tileProperties = new MapProperties();
		
		tile = mock(TiledMapTile.class);
		when(tile.getProperties()).thenReturn(tileProperties);
		
		innerCell = mock(Cell.class);
		when(innerCell.getTile()).thenReturn(tile);
		
		//create factory
		ctrlFactory = new ControllerFactory();
		factory = new ZootTiledActorFactory(1.0f);
	}
			
	@Test(expected = RuntimeZootException.class)
	public void createFromMapObjectShuoldThrowIfBasicPropertiesAreNotInPlaceTest()
	{
		//given
		MapObject mapObject = new MapObject();
				
		//then
		factory.createFromMapObject(mapObject);
	}
	
	@Test
	public void createFromMapObjectShouldSetActorBasicPropertiesTest() 
	{
		//given
		MapObject mapObject = createDefaultMapObject();
		
		//when
		ZootActor actor = factory.createFromMapObject(mapObject);
		
		//then
		assertNotNull(actor);
		assertEquals(mapObject.getName(), actor.getName());
		assertEquals(mapObject.getColor(), actor.getColor());
		assertEquals(mapObject.getOpacity(), actor.getOpacity(), 0.0f);
		assertEquals(mapObject.isVisible(), actor.isVisible());
		assertEquals(ACTOR_ID, actor.getId());
		assertEquals(ACTOR_X, actor.getX(), 0.0f);
		assertEquals(ACTOR_Y, actor.getY(), 0.0f);
		assertEquals(ACTOR_WIDTH, actor.getWidth(), 0.0f);
		assertEquals(ACTOR_HEIGHT, actor.getHeight(), 0.0f);
		assertEquals(ACTOR_ROTATION, actor.getRotation(), 0.0f);
	}	
	
	@Test
	public void createFromMapCellShouldSetBasicPropertiesTestCellTest()
	{
		//given
		ZootTiledMapCell cell = createDefaultCell();
		
		//when
		ZootActor actor = factory.createFromMapCell(cell);
		
		//then
		assertNotNull(actor);
		assertEquals(CELL_X * CELL_WIDTH, actor.getX(), 0.0f);
		assertEquals(CELL_Y * CELL_HEIGHT, actor.getY(), 0.0f);
		assertEquals(CELL_WIDTH, actor.getWidth(), 0.0f);
		assertEquals(CELL_HEIGHT, actor.getHeight(), 0.0f);
		assertEquals(0.0f, actor.getRotation(), 0.0f);
	}

	@Test(expected = RuntimeZootException.class)
	public void createFromMapCellShouldNotSetAnyControllersWhenThereAreNoneInPropertiesTest()
	{
		//given
		ZootTiledMapCell cell = createDefaultCell();
		
		//when
		ZootActor actor = factory.createFromMapCell(cell);
		
		//then
		assertNotNull(actor);
		actor.getController(StaticBodyController.class);
	}
			
	private ZootTiledMapCell createDefaultCell()
	{		
		return new ZootTiledMapCell(CELL_X, CELL_Y, CELL_WIDTH, CELL_HEIGHT, innerCell);
	}
	
	private MapObject createDefaultMapObject()
	{		
		MapObject mapObject = new MapObject();
		mapObject.setName(ACTOR_NAME);
		mapObject.setColor(ACTOR_COLOR);
		mapObject.setOpacity(ACTOR_OPACITY);
		mapObject.setVisible(ACTOR_VISIBLE);
		mapObject.getProperties().put("id", ACTOR_ID);
		mapObject.getProperties().put("x",ACTOR_X);
		mapObject.getProperties().put("y", ACTOR_Y);
		mapObject.getProperties().put("width", ACTOR_WIDTH);
		mapObject.getProperties().put("height", ACTOR_HEIGHT);
		mapObject.getProperties().put("rotation", ACTOR_ROTATION);
		return mapObject;
	}
}

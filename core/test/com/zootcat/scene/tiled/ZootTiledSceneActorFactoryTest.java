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
import com.zootcat.controllers.physics.StaticBodyController;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.map.tiled.ZootTiledMapCell;
import com.zootcat.physics.ZootPhysics;
import com.zootcat.physics.ZootPhysicsBody;
import com.zootcat.physics.ZootPhysicsBodyDef;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.mocks.Mock1Controller;
import com.zootcat.scene.mocks.Mock2Controller;
import com.zootcat.scene.mocks.inner.Mock3Controller;

public class ZootTiledSceneActorFactoryTest
{	
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
	private ZootTiledSceneActorFactory factory;
	
	@Before
	public void setup()
	{		
		MockitoAnnotations.initMocks(this);
		
		//mock scene
		when(physicsMock.createBody(any(ZootPhysicsBodyDef.class))).thenReturn(mock(ZootPhysicsBody.class));		
		when(sceneMock.getPhysics()).thenReturn(physicsMock);
		
		//mock tile and inner cell
		tileProperties = new MapProperties();
		
		tile = mock(TiledMapTile.class);
		when(tile.getProperties()).thenReturn(tileProperties);
		
		innerCell = mock(Cell.class);
		when(innerCell.getTile()).thenReturn(tile);
		
		//create factory
		factory = new ZootTiledSceneActorFactory(sceneMock);
	}
	
	@Test
	public void addControllersTestTest()
	{
		assertEquals("Should include subpackages and skip interfaces", 3, factory.addControllersFromPackage("com.zootcat.scene.mocks", true));
		assertEquals("Should not include subpackages and skip interfaces", 2, factory.addControllersFromPackage("com.zootcat.scene.mocks", false));
	}
		
	@Test(expected = RuntimeZootException.class)
	public void createFromMapObjectShuoldThrowIfBasicPropertiesAreNotInPlaceTest()
	{
		//given
		MapObject mapObject = new MapObject();
				
		//then
		factory.createFromMapObject(mapObject);
	}
	
	@Test(expected = NumberFormatException.class)
	public void createFromMapObjectShouldThrowNumberFormatExceptionIfIntegerValuesAreWrong()
	{
		//given
		MapObject mapObject = new MapObject();
		mapObject.getProperties().put("x", "wrongValue");
				
		//then
		factory.createFromMapObject(mapObject);
	}
	
	@Test
	public void createFromMapObjectShouldAddControllerWithNotParamsTest()
	{
		//given
		factory.addControllersFromPackage("com.zootcat.scene.mocks", true);
		MapObject mapObject = createDefaultMapObject();
		mapObject.getProperties().put("Mock1Controller", "");
		
		//when
		ZootActor actor = factory.createFromMapObject(mapObject);
		actor.act(0.0f);
		
		//then
		Mock1Controller ctrl = actor.getController(Mock1Controller.class);
		assertNotNull(ctrl);
	}

	@Test
	public void createFromMapObjectShouldAddControllerWithParamsTest()
	{
		//given
		factory.addControllersFromPackage("com.zootcat.scene.mocks", true);
		MapObject mapObject = createDefaultMapObject();
		mapObject.getProperties().put("Mock2Controller", "a=1, b=2.2f, c=string");
		
		//when
		ZootActor actor = factory.createFromMapObject(mapObject);
		actor.act(0.0f);
		
		//then
		Mock2Controller ctrl = actor.getController(Mock2Controller.class);
		assertNotNull(ctrl);
		assertEquals(1, ctrl.a);
		assertEquals(2.2f, ctrl.b, 0.0f);
		assertEquals("string", ctrl.c);
	}
	
	@Test
	public void createFromMapObjectShouldAddControllerWithSceneGlobalParamTest()
	{
		//given
		factory.addControllersFromPackage("com.zootcat.scene.mocks", true);
		MapObject mapObject = createDefaultMapObject();
		mapObject.getProperties().put("Mock3Controller", "param = 100");
		
		//then
		ZootActor actor = factory.createFromMapObject(mapObject);
		actor.act(0.0f);
		
		//then
		Mock3Controller ctrl = actor.getController(Mock3Controller.class);
		assertNotNull(ctrl);
		assertEquals(100, ctrl.param);
		assertEquals(sceneMock, ctrl.scene);
	}
	
	@Test(expected = RuntimeZootException.class)
	public void createFromMapObjectShouldThrowWhenControllerParamsAreWrongTest()
	{
		//given
		factory.addControllersFromPackage("com.zootcat.scene.mocks", true);
		MapObject mapObject = createDefaultMapObject();
		mapObject.getProperties().put("Mock2Controller", "1, string");
		
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
	
	@Test
	public void createFromMapCellShouldCreateControllersForActorTest()
	{
		//given				
		ZootTiledMapCell cell = createDefaultCell();
		tileProperties.put(StaticBodyController.class.getSimpleName(), "");
				
		//when
		ZootActor actor = factory.createFromMapCell(cell);
		
		//then
		assertNotNull(actor);
		assertNotNull(actor.getController(StaticBodyController.class));
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
		mapObject.getProperties().put("x",ACTOR_X);
		mapObject.getProperties().put("y", ACTOR_Y);
		mapObject.getProperties().put("width", ACTOR_WIDTH);
		mapObject.getProperties().put("height", ACTOR_HEIGHT);
		mapObject.getProperties().put("rotation", ACTOR_ROTATION);
		return mapObject;
	}
}

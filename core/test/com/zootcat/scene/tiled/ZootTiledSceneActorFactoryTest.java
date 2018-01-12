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
	private static final int CELL_COLLIDABLE = 1;
		
	private ZootTiledSceneActorFactory factory;	
	@Mock private ZootTiledScene sceneMock;	
	@Mock private ZootPhysics physicsMock;
	
	@Before
	public void setup()
	{		
		MockitoAnnotations.initMocks(this);
		
		when(physicsMock.createBody(any(ZootPhysicsBodyDef.class))).thenReturn(mock(ZootPhysicsBody.class));		
		when(sceneMock.getPhysics()).thenReturn(physicsMock);
		
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
		mapObject.getProperties().put("Mock2Controller", "1, 2.2f, string");
		
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
	public void createFromMapObjectShouldAddControllerWithGlobalParamTest()
	{
		//given
		factory.addControllersFromPackage("com.zootcat.scene.mocks", true);
		MapObject mapObject = createDefaultMapObject();
		mapObject.getProperties().put("Mock3Controller", "100");
		
		//then
		ZootActor actor = factory.createFromMapObject(mapObject);
		actor.act(0.0f);
		
		//then
		Mock3Controller ctrl = actor.getController(Mock3Controller.class);
		assertNotNull(ctrl);
		assertEquals(Mock3Controller.INVOKED_SECOND_CTOR, ctrl.ctorInvoked, 0.0f);
	}

	@Test
	public void createFromMapObjectShouldAddControllerWithTwoDefaultParamsTest()
	{
		//given
		factory.addControllersFromPackage("com.zootcat.scene.mocks", true);
		MapObject mapObject = createDefaultMapObject();
		mapObject.getProperties().put("Mock3Controller", "100, 200");
		
		//then
		ZootActor actor = factory.createFromMapObject(mapObject);
		actor.act(0.0f);
		
		//then
		Mock3Controller ctrl = actor.getController(Mock3Controller.class);
		assertNotNull(ctrl);
		assertEquals(Mock3Controller.INVOKED_THIRD_CTOR, ctrl.ctorInvoked, 0.0f);
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
	public void createFromMapCellShouldNotSetStaticBodyControllerForNotCollidableCellTest()
	{
		//given
		ZootTiledMapCell cell = createDefaultCell();
		cell.collidable = 0;
		
		//when
		ZootActor actor = factory.createFromMapCell(cell);
		actor.act(0.0f);
		
		//then
		assertNotNull(actor);
		actor.getController(StaticBodyController.class);
	}
	
	@Test
	public void createFromMapCellShouldSetStaticBodyControllerForCollidableCellTest()
	{
		//given
		ZootTiledMapCell cell = createDefaultCell();
		cell.collidable = 1;
		
		//when
		ZootActor actor = factory.createFromMapCell(cell);
		actor.act(0.0f);
		
		//then
		assertNotNull(actor);
		assertNotNull(actor.getController(StaticBodyController.class));
	}
	
	private ZootTiledMapCell createDefaultCell()
	{
		Cell innerCell = mock(Cell.class);
		return new ZootTiledMapCell(CELL_X, CELL_Y, CELL_WIDTH, CELL_HEIGHT, CELL_COLLIDABLE, innerCell);
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

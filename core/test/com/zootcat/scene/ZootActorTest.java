package com.zootcat.scene;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.zootcat.controllers.ChangeListenerController;
import com.zootcat.controllers.Controller;
import com.zootcat.controllers.gfx.RenderController;

public class ZootActorTest 
{	
	private Controller mockCtrl1;
	private Controller mockCtrl2;
	private Controller mockCtrl3;
	
	@BeforeClass
	public static void setupClass()
	{		
		Gdx.graphics = mock(Graphics.class);		
	}
	
	@AfterClass
	public static void tearDownClass()
	{
		Gdx.graphics = null;
	}
	
	@Before
	public void setup()
	{
		mockCtrl1 = mock(Controller.class);
		mockCtrl2 = mock(Controller.class);
		mockCtrl3 = mock(Controller.class);	
	}
	
    @Test
    public void addAndRemoveTypeTest()
    {
        //given
        ZootActor actor = new ZootActor();
        
        //then
        assertTrue("New actor should have empty types", actor.getTypes().isEmpty());
        
        //when
        actor.addType("newType");
        
        //then
        assertEquals(1, actor.getTypes().size());
        assertTrue(actor.isType("newType"));
        assertTrue(actor.isType("newtype"));
        assertTrue(actor.isType("NEWTYPE"));
        assertFalse(actor.isType(" newType "));
        assertFalse(actor.isType("xyz"));
        
        //when
        actor.removeType("newType");
        
        //then
        assertEquals(0, actor.getTypes().size());
        assertFalse(actor.isType("newType"));
        assertFalse(actor.isType("newtype"));
        assertFalse(actor.isType("NEWTYPE"));
        assertFalse(actor.isType(" newType "));
        assertFalse(actor.isType("xyz"));
    }
	
	@Test
	public void addControllerTest()
	{
		ZootActor actor = new ZootActor();
		assertEquals("After creation controller list should be empty", 0, actor.getControllers().size());
		
		actor.addController(mockCtrl1);
		assertEquals("Controllers should not be added immediatelly", 0, actor.getControllers().size());
		
		actor.act(0.0f);		
		assertEquals("Controllers should add after actor update", 1, actor.getControllers().size());
		
		actor.addController(mockCtrl2);
		assertEquals("Controllers should not be added immediatelly", 1, actor.getControllers().size());
		
		actor.act(0.0f);
		assertEquals("Controllers should add after actor update", 2, actor.getControllers().size());
	}
	
	@Test
	public void addControllerShouldCreateDuplicatesIfInsertedTest()
	{
		ZootActor actor = new ZootActor();
		actor.addController(mockCtrl1);
		actor.addController(mockCtrl2);
		actor.addController(mockCtrl1);
		actor.addController(mockCtrl2);
		actor.act(0.0f);
		
		List<Controller> actual = actor.getControllers();
		List<Controller> expected = Arrays.asList(mockCtrl1, mockCtrl2, mockCtrl1, mockCtrl2);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void removeControllerTest()
	{
		//given
		ZootActor actor = new ZootActor();
		
		//when
		actor.addController(mockCtrl1);
		actor.addController(mockCtrl2);
		actor.act(0.0f);		
		
		//then
		assertEquals(2, actor.getControllers().size());
		
		//when, then
		actor.removeController(mockCtrl1);
		assertEquals("Controllers should be removed after act() is called", 2, actor.getControllers().size());

		//when, then
		actor.act(0.0f);
		assertEquals(1, actor.getControllers().size());
		assertEquals(mockCtrl2, actor.getControllers().get(0));
		
		//when, then		
		actor.removeController(mockCtrl2);
		actor.act(0.0f);
		assertEquals(0, actor.getControllers().size());
	}	
	
	@Test
	public void removeControllerOnEmptyControllerListTest()
	{
		ZootActor actor = new ZootActor();
		assertEquals(0, actor.getControllers().size());
		
		actor.removeController(mockCtrl1);
		actor.removeController(mockCtrl2);
		assertEquals(0, actor.getControllers().size());
		
		actor.act(0.0f);
		assertEquals(0, actor.getControllers().size());
	}
	
	@Test
	public void removeControllerShouldRemoveProperControllerAmongDuplicatesTest()
	{
		//given
		ZootActor actor = new ZootActor();
		
		//when
		actor.addController(mockCtrl1);
		actor.addController(mockCtrl2);
		actor.addController(mockCtrl3);
		actor.addController(mockCtrl1);
		actor.addController(mockCtrl2);
		actor.addController(mockCtrl3);
		actor.act(0.0f);
		
		//then
		assertEquals(6, actor.getControllers().size());
		
		//when
		actor.removeController(mockCtrl1);
		actor.act(0.0f);
		
		//then
		List<Controller> expected = Arrays.asList(mockCtrl2, mockCtrl3, mockCtrl2, mockCtrl3);
		assertEquals(4, actor.getControllers().size());
		assertEquals(expected, actor.getControllers());
	}
	
	@Test
	public void addControllerShouldInitializeControllersAfterSceneIsReadyAndActIsInvokedTest()
	{
		//given
		ZootActor actor = new ZootActor();
		
		//then
		verify(mockCtrl1, times(0)).onAdd(actor);
		verify(mockCtrl2, times(0)).onAdd(actor);
		
		//when
		actor.addController(mockCtrl1);
		actor.addController(mockCtrl2);	
		
		//then
		verify(mockCtrl1, times(0)).onAdd(actor);
		verify(mockCtrl2, times(0)).onAdd(actor);
		
		//when
		actor.act(0.0f);
		
		//then
		verify(mockCtrl1, times(1)).onAdd(actor);
		verify(mockCtrl2, times(1)).onAdd(actor);
	}
	
	@Test
	public void removeControllerShouldDeinitializeControllersAfterActIsInvokedTest()
	{
		//given
		ZootActor actor = new ZootActor();
		actor.addController(mockCtrl1);
		actor.addController(mockCtrl2);
		actor.act(0.0f);
		
		//then
		verify(mockCtrl1, times(1)).onAdd(actor);
		verify(mockCtrl2, times(1)).onAdd(actor);
		
		//when
		actor.removeController(mockCtrl1);
		actor.removeController(mockCtrl2);	
		
		//then
		verify(mockCtrl1, times(0)).onRemove(actor);
		verify(mockCtrl2, times(0)).onRemove(actor);
		
		//when
		actor.act(0.0f);
		
		//then
		verify(mockCtrl1, times(1)).onRemove(actor);
		verify(mockCtrl2, times(1)).onRemove(actor);
	}
	
	@Test
	public void changeListenerControllerShouldBeNotifiedWhenActorChangesTest()
	{
		//given
		ZootActor actor = new ZootActor();
		ChangeListenerController controller = mock(ChangeListenerController.class);
		
		//when
		actor.addController(controller);
		actor.act(0.0f);
		actor.setPosition(5, 5);
		
		//then
		verify(controller, times(1)).onPositionChange(actor);
		
		//when
		actor.setSize(1.0f, 1.0f);
		
		//then
		verify(controller, times(1)).onSizeChange(actor);
		
		//when
		actor.setRotation(256.0f);
		
		//then
		verify(controller, times(1)).onRotationChange(actor);	
	}
	
	@Test
	public void drawShouldExecuteAllRenderControllersTest()
	{
		//given
		final float parentAlpha = 1.0f;
		final Batch batch = mock(Batch.class);
		
		ZootActor actor = new ZootActor();
		RenderController renderCtrl1 = mock(RenderController.class);
		RenderController renderCtrl2 = mock(RenderController.class);
		ChangeListenerController changeListenerController = mock(ChangeListenerController.class);
		
		//when
		actor.addController(renderCtrl1);
		actor.addController(changeListenerController);
		actor.addController(renderCtrl2);
		actor.act(0.0f);
		
		//then
		verify(renderCtrl1, times(0)).onRender(anyObject(), anyFloat(), anyObject(), anyFloat());;
		verify(renderCtrl2, times(0)).onRender(anyObject(), anyFloat(), anyObject(), anyFloat());;
		
		//when
		actor.draw(batch, 1.0f);
				
		//then
		verify(renderCtrl1, times(1)).onRender(eq(batch), eq(parentAlpha), anyObject(), anyFloat());;
		verify(renderCtrl2, times(1)).onRender(eq(batch), eq(parentAlpha), anyObject(), anyFloat());;
		
		//when
		actor.draw(batch, 1.0f);
		
		//then
		verify(renderCtrl1, times(2)).onRender(eq(batch), eq(parentAlpha), anyObject(), anyFloat());;
		verify(renderCtrl2, times(2)).onRender(eq(batch), eq(parentAlpha), anyObject(), anyFloat());;
	}
	
}

package com.zootcat.camera;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.math.Vector3;
import com.zootcat.scene.ZootActor;

public class ZootWindowScrollingStrategyTest
{
	private static final float WINDOW_SCALE_X = 6;
	private static final float WINDOW_SCALE_Y = 4;
	private static final float VIEWPORT_WIDTH = 320.0f;
	private static final float VIEWPORT_HEIGHT = 240.0f;
	private static final float CAMERA_START_X = VIEWPORT_WIDTH / 2.0f;
	private static final float CAMERA_START_Y = VIEWPORT_HEIGHT / 2.0f;
		
	@Mock private ZootActor actor;
	@Mock private ZootCamera camera;	
	private Vector3 cameraPosition;
	private ZootWindowScrollingStrategy strategy;
		
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		cameraPosition = new Vector3(CAMERA_START_X, CAMERA_START_Y, 0.0f);		
		strategy = new ZootWindowScrollingStrategy(WINDOW_SCALE_X, WINDOW_SCALE_Y);
		
		when(camera.getViewportWidth()).thenReturn(VIEWPORT_WIDTH);
		when(camera.getViewportHeight()).thenReturn(VIEWPORT_HEIGHT);
		when(camera.getPosition()).thenReturn(cameraPosition);
		when(camera.getTarget()).thenReturn(actor);
	}
	
	@Test
	public void scrollCameraOnNullTargetShouldNotChangeCameraPositionTest()
	{		
		//when
		when(camera.getTarget()).thenReturn(null);	
		strategy.scrollCamera(camera, 1.0f);
		
		//then
		verify(camera, times(1)).getTarget();
		verifyNoMoreInteractions(camera);
	}
	
	@Test
	public void scrollCameraActorInCenterOfWindowBoundaryShouldNotChangePositionTest()
	{
		//given		
		when(actor.getX()).thenReturn(CAMERA_START_X);
		when(actor.getY()).thenReturn(CAMERA_START_Y);
		
		//when
		strategy.scrollCamera(camera, 1.0f);
		
		//then
		assertEquals("X position should not change", CAMERA_START_X, camera.getPosition().x, 0.0f);
		assertEquals("Y position should not change", CAMERA_START_Y, camera.getPosition().y, 0.0f);
	}
	
	@Test
	public void scrollCameraActorOutsideOfLeftBoundaryTest()
	{
		//given		
		when(actor.getX()).thenReturn(0.0f);
		when(actor.getY()).thenReturn(CAMERA_START_Y);
		
		//when
		strategy.scrollCamera(camera, 1.0f);		
		
		//then
		assertEquals("X position should tract actor X position", 53, camera.getPosition().x, 1.0f);
		assertEquals("Y position should not change", CAMERA_START_Y, camera.getPosition().y, 0.0f);
	}
	
	@Test
	public void scrollCameraActorOutsideOfRightBoundaryTest()
	{
		//given		
		when(actor.getX()).thenReturn(VIEWPORT_WIDTH);
		when(actor.getY()).thenReturn(CAMERA_START_Y);
		
		//when
		strategy.scrollCamera(camera, 1.0f);		
		
		//then
		assertEquals("X position should tract actor X position", 266.0f, camera.getPosition().x, 1.0f);
		assertEquals("Y position should not change", CAMERA_START_Y, camera.getPosition().y, 0.0f);
	}
	
	@Test
	public void scrollCameraActorOutsideOfTopBoundaryTest()
	{
		//given		
		when(actor.getX()).thenReturn(CAMERA_START_X);
		when(actor.getY()).thenReturn(VIEWPORT_HEIGHT);
		
		//when
		strategy.scrollCamera(camera, 1.0f);		
		
		//then
		assertEquals("X position should not change", CAMERA_START_X, camera.getPosition().x, 0.0f);
		assertEquals("Y position should tract actor Y position", 180, camera.getPosition().y, 0.5f);		
	}
	
	@Test
	public void scrollCameraActorOutsideOfBottomBoundaryTest()
	{
		//given		
		when(actor.getX()).thenReturn(CAMERA_START_X);
		when(actor.getY()).thenReturn(-VIEWPORT_HEIGHT);
		
		//when
		strategy.scrollCamera(camera, 1.0f);		
		
		//then
		assertEquals("X position should not change", CAMERA_START_X, camera.getPosition().x, 0.0f);
		assertEquals("Y position should tract actor Y position", -180, camera.getPosition().y, 0.5f);		
	}
}

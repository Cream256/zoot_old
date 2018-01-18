package com.zootcat.screen;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.function.Consumer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;

public class ZootLoadingScreenTest
{
	private ZootLoadingScreen screen;
	private Consumer<Void> task;
	private AssetManager assetManager;
	
	@Before
	public void setup()
	{
		Gdx.gl = mock(GL20.class);
		task = (Void) -> {};
		assetManager = mock(AssetManager.class);
		when(assetManager.update()).thenReturn(true);
		screen = new ZootLoadingScreen(assetManager);		
	}
	
	@After
	public void tearDown()
	{
		Gdx.gl = null;
	}
	
	@Test
	public void getProgressTest()
	{		
		assertEquals(0.0f, screen.getProgress(), 0.0f);
		
		screen.addTask(task);
		screen.addTask(task);
		screen.addTask(task);
		screen.addTask(task);
		assertEquals(0.0f, screen.getProgress(), 0.0f);
		
		screen.show();
		assertEquals(0.00f, screen.getProgress(), 0.0f);
		
		screen.render(0.0f);
		assertEquals(0.25f, screen.getProgress(), 0.0f);
		
		screen.render(0.0f);
		assertEquals(0.50f, screen.getProgress(), 0.0f);
		
		screen.render(0.0f);
		assertEquals(0.75f, screen.getProgress(), 0.0f);
		
		screen.render(0.0f);
		assertEquals(1.0f, screen.getProgress(), 0.0f);
		
		screen.render(0.0f);
		assertEquals("Should not go over 100%", 1.0f, screen.getProgress(), 0.0f);
	}
	
	
}

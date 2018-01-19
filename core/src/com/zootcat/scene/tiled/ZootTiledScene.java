package com.zootcat.scene.tiled;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.zootcat.controllers.factory.ControllerFactory;
import com.zootcat.gfx.ZootRender;
import com.zootcat.map.tiled.ZootTiledMap;
import com.zootcat.map.tiled.ZootTiledMapActorFactory;
import com.zootcat.map.tiled.ZootTiledMapRender;
import com.zootcat.map.tiled.ZootTiledMapRenderConfig;
import com.zootcat.map.tiled.ZootTiledWorldScaleCalculator;
import com.zootcat.physics.ZootPhysics;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class ZootTiledScene implements ZootScene
{
	private static final float FIXED_TIME_STEP = 1.0f / 60.0f;
	private static final float MIN_TIME_STEP = 1.0f / 4.0f;
		
	private Stage stage;
	private ZootTiledMapRender mapRender;
	private OrthographicCamera camera;
	private ZootPhysics physics;	
	private float timeAccumulator = 0.0f;	
	private boolean isDebugMode = false;
	private Box2DDebugRenderer debugRender = new Box2DDebugRenderer();
	private float unitScale;
	
	public ZootTiledScene(ZootTiledMap map, float viewportWidth, float viewportHeight, float worldUnitPerTile)
	{						
    	//scale
    	unitScale = ZootTiledWorldScaleCalculator.calculate(worldUnitPerTile, map.getTileWidth());
    	
    	//physics
    	physics = new ZootPhysics();
    	
		//render
    	ZootTiledMapRenderConfig renderConfig = new ZootTiledMapRenderConfig();
		renderConfig.renderRectangleObjects = false;
		renderConfig.renderTextureObjects = false;	
		renderConfig.unitScale = unitScale;
		mapRender = new ZootTiledMapRender(map, renderConfig);
				
		//stage
		Viewport viewport = new StretchViewport(viewportWidth, viewportHeight);
		camera = (OrthographicCamera) viewport.getCamera();
		stage = new Stage(viewport);
		
		//actors
		ControllerFactory ctrlFactory = new ControllerFactory();
    	ZootTiledMapActorFactory actorFactory = new ZootTiledMapActorFactory(this, ctrlFactory);
		List<ZootActor> actors = actorFactory.createFromMapObjects(map.getAllObjects());		
		List<ZootActor> cellActors = actorFactory.createFromMapCells(map.getLayerCells(ZootTiledMap.COLLISION_LAYER_NAME));
		
		cellActors.forEach(cellActor -> stage.addActor(cellActor));
		actors.forEach(actor -> stage.addActor(actor));
	}
	
	@Override
	public OrthographicCamera getCamera()
	{
		return camera;
	}
	
	@Override
	public ZootPhysics getPhysics()
	{
		return physics;
	}
	
	@Override
	public ZootRender getRender() 
	{
		return mapRender;
	}
	
	@Override
	public void addActor(ZootActor zootActor)
	{
		stage.addActor(zootActor);
	}
	
	@Override
	public void removeActor(ZootActor actor) 
	{
		if(actor.getParent().equals(stage))
		{
			actor.remove();
		}
	}

	@Override
	public List<ZootActor> getActors() 
	{		
		return getActors((act) -> true);
	}
	
	@Override
	public List<ZootActor> getActors(Predicate<Actor> filter) 
	{				
		return StreamSupport.stream(stage.getActors().spliterator(), false)
							 .filter(filter)
							 .map((act) -> (ZootActor)act)
							 .collect(Collectors.toList());
	}
	
	@Override
	public void update(float delta)
	{		
		timeAccumulator += Math.min(MIN_TIME_STEP, delta);       
		while(timeAccumulator >= FIXED_TIME_STEP)
		{
			stage.act(FIXED_TIME_STEP);
			physics.step(FIXED_TIME_STEP);
			timeAccumulator -= FIXED_TIME_STEP;
		}
		stage.getCamera().update();
	}
	
	@Override
	public void render(float delta)
	{			
		mapRender.setView((OrthographicCamera)getCamera());
		mapRender.render(delta);
		stage.draw();
		
		if(isDebugMode())
		{
			debugRender.setDrawAABBs(false);
			debugRender.setDrawBodies(true);
			debugRender.setDrawInactiveBodies(true);
			debugRender.setDrawJoints(true);
			debugRender.render(physics.getWorld(), getCamera().combined);
		}
	}

	@Override
	public void dispose() 
	{
		mapRender.dispose();
		mapRender = null;
		
		stage.dispose();
		stage = null;
		
		debugRender.dispose();
		debugRender = null;
	}

	@Override
	public boolean isDebugMode() 
	{
		return isDebugMode;
	}
	
	@Override
	public void setDebugMode(boolean debug)
	{
		isDebugMode = debug;
	}

	@Override
	public void setFocusedActor(ZootActor actor)
	{
		stage.setKeyboardFocus(actor);
	}

	@Override
	public void resize(int width, int height) 
	{		
		//noop
	}

	@Override
	public void addListener(EventListener listener) 
	{
		stage.addListener(listener);
	}

	@Override
	public void removeListener(EventListener listener) 
	{
		stage.removeListener(listener);
	}

	@Override
	public InputProcessor getInputProcessor() 
	{
		return stage;
	}

	@Override
	public float getUnitScale()
	{
		return unitScale;
	}
}

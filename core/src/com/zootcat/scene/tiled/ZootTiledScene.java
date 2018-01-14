package com.zootcat.scene.tiled;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.zootcat.input.ZootInputProcessor;
import com.zootcat.map.tiled.ZootTiledMap;
import com.zootcat.map.tiled.ZootTiledMapRender;
import com.zootcat.map.tiled.ZootTiledMapRenderConfig;
import com.zootcat.physics.ZootPhysics;
import com.zootcat.physics.box2d.ZootBox2DPhysics;
import com.zootcat.render.ZootRender;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class ZootTiledScene implements ZootScene
{
	private static final float FIXED_TIME_STEP = 1.0f / 60.0f;
	private static final float MIN_TIME_STEP = 1.0f / 4.0f;
		
	private Stage stage;
	private ZootTiledMapRender render;
	private ZootBox2DPhysics physics;	
	private ZootInputProcessor inputProcessor;
	private float timeAccumulator = 0.0f;	
	private boolean isDebugMode = false;
	private Box2DDebugRenderer debugRender = new Box2DDebugRenderer();
	
	public ZootTiledScene(String tiledMapPath, ZootInputProcessor inputProcessor, float viewportWidth, float viewportHeight, float worldUnitPerTile)
	{				
		//map    	
    	ZootTiledMap map = new ZootTiledMap(new TmxMapLoader().load(tiledMapPath));
		
    	//physics
    	physics = new ZootBox2DPhysics();
    	
		//render
    	ZootTiledMapRenderConfig renderConfig = new ZootTiledMapRenderConfig();
		renderConfig.renderRectangleObjects = false;
		renderConfig.renderTextureObjects = false;	
		renderConfig.tilesPerWorldUnit = worldUnitPerTile;
		render = new ZootTiledMapRender(map, renderConfig);
				
		//stage
		Viewport viewport = new StretchViewport(viewportWidth, viewportHeight);
		stage = new Stage(viewport);
		
		//actors
    	ZootTiledSceneActorFactory actorFactory = new ZootTiledSceneActorFactory(this);
		List<ZootActor> actors = actorFactory.createFromMapObjects(map.getAllObjects());		
		List<ZootActor> cellActors = actorFactory.createFromMapCells(map.getCollidableCells());
		
		cellActors.forEach(cellActor -> stage.addActor(cellActor));
		actors.forEach(actor -> stage.addActor(actor));
		
		//input
		InputMultiplexer inputMultiplexer = new InputMultiplexer(inputProcessor, stage);
		Gdx.input.setInputProcessor(inputMultiplexer);
		this.inputProcessor = inputProcessor;
	}
	
	@Override
	public Camera getCamera()
	{
		return stage.getViewport().getCamera();
	}
	
	@Override
	public ZootPhysics getPhysics()
	{
		return physics;
	}
	
	@Override
	public ZootRender getRender() 
	{
		return render;
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
			inputProcessor.processPressedKeys();
			stage.act(FIXED_TIME_STEP);
			physics.step(FIXED_TIME_STEP);
			timeAccumulator -= FIXED_TIME_STEP;
		}
	}
	
	@Override
	public void render(float delta)
	{	
		render.setView((OrthographicCamera)getCamera());
		render.render(delta);
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
		render.dispose();
		render = null;
		
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
}

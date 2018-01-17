package com.zootcat.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.zootcat.controllers.input.InputProcessorController;
import com.zootcat.game.GameCharacterInputProcessor;
import com.zootcat.input.ZootBindableInputProcessor;
import com.zootcat.input.ZootInputManager;
import com.zootcat.input.ZootInputProcessorListener;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;
import com.zootcat.scene.tiled.ZootTiledScene;

public class ZootLevelScreen implements Screen
{
	private boolean paused = false;
	private ZootScene scene;
	private ZootInputManager inputManager;
	
	private float viewportWidth;
	private float viewportHeight;
	private float unitPerTile;
	private String levelPath;
	
	public ZootLevelScreen(String levelPath, float viewportWidth, float viewportHeight, float unitPerTile)
	{
		this.levelPath = levelPath;
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
		this.unitPerTile = unitPerTile;
	}
	
	public void resetLevel()
	{
		//dispose previous scene
		dispose();
		
		//create scene
    	scene = new ZootTiledScene(levelPath, viewportWidth, viewportHeight, unitPerTile);
				
    	//debug input
    	final float camMove = 0.1f;
    	final float zoom = 0.01f;
    	OrthographicCamera camera = scene.getCamera();
    	ZootBindableInputProcessor debugInputProcessor = new ZootBindableInputProcessor();
    	debugInputProcessor.bindDown(Input.Keys.NUMPAD_8, () -> { camera.translate(0, camMove, 0); return true; });
    	debugInputProcessor.bindDown(Input.Keys.NUMPAD_2, () -> { camera.translate(0, -camMove, 0); return true; });
    	debugInputProcessor.bindDown(Input.Keys.NUMPAD_4, () -> { camera.translate(-camMove, 0, 0); return true; });
    	debugInputProcessor.bindDown(Input.Keys.NUMPAD_6, () -> { camera.translate(camMove, 0, 0); return true; });
    	debugInputProcessor.bindDown(Input.Keys.NUMPAD_5, () -> { camera.zoom -= zoom; return true; });
    	debugInputProcessor.bindDown(Input.Keys.NUMPAD_0, () -> { camera.zoom += zoom; return true; });
    	debugInputProcessor.bindDown(Input.Keys.PERIOD, () -> { camera.zoom = 1.0f; return true; });    	
    	debugInputProcessor.bindUp(Input.Keys.F9, () -> { scene.setDebugMode(!scene.isDebugMode()); return true; });
    	debugInputProcessor.bindUp(Input.Keys.F12, () -> { resetLevel(); return true; });
    	scene.addListener(new ZootInputProcessorListener(debugInputProcessor));
    	
    	//character input    	
    	ZootActor player = scene.getActors((act) -> act.getName().equalsIgnoreCase("Frisker")).get(0);
    	GameCharacterInputProcessor characterInputProcessor = new GameCharacterInputProcessor(player);
    	characterInputProcessor.setMovementVelocity(1.0f);
    	characterInputProcessor.setJumpVelocity(5.0f);    	
    	player.addController(new InputProcessorController(characterInputProcessor));
    	scene.setFocusedActor(player);
    	
    	//input    	
    	inputManager = new ZootInputManager();    	
    	inputManager.addProcessor(debugInputProcessor);
    	inputManager.addProcessor(characterInputProcessor);
    	inputManager.addProcessor(scene.getInputProcessor());
    	assignInput();
	}
	
	@Override
	public void resize (int width, int height) 
	{
		scene.resize(width, height);
	}
    
	@Override
	public void pause () 
	{
		paused = true;
	}

	@Override
	public void resume () 
	{
		paused = false;
	}
	
	@Override
	public void dispose() 
	{
		if(scene != null)
		{		
			scene.dispose();
			scene = null;
		}
	}

	@Override
	public void show()
	{
		resetLevel();
	}
	
	@Override
	public void hide()
	{
		dispose();
		deassignInput();
	}

	@Override
	public void render(float delta)
	{
        if(paused || scene == null)
        {
        	return;
        }
    	
        //update
        inputManager.processPressedKeys(delta);
        scene.update(delta);
    	
    	//render
    	scene.render(delta);
	}
	
	private void assignInput()
	{
		Gdx.input.setInputProcessor(inputManager);
	}
	
	private void deassignInput()
	{
		Gdx.input.setInputProcessor(null);
	}
}

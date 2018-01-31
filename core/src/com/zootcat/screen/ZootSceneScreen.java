package com.zootcat.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.zootcat.controllers.input.InputProcessorController;
import com.zootcat.game.GameCharacterInputProcessor;
import com.zootcat.hud.ZootDebugHud;
import com.zootcat.hud.ZootDebugWindowListener;
import com.zootcat.input.ZootBindableInputProcessor;
import com.zootcat.input.ZootInputManager;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class ZootSceneScreen implements Screen
{
	private ZootScene scene;
	private ZootDebugHud debugHud;
	private boolean paused = false;
	private ZootInputManager inputManager;
	
	public ZootSceneScreen(ZootScene scene)
	{
		this.scene = scene;
		debugHud = new ZootDebugHud();
	}
			
	@Override
	public void resize (int width, int height) 
	{
		scene.resize(width, height);
		debugHud.resize(width, height);
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
		assignInput();
	}
	
	@Override
	public void hide()
	{
		deassignInput();
		dispose();		
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
    	debugHud.render(delta);
	}
	
	private void reloadScene()
	{
		deassignInput();
		scene.reload();
		assignInput();
	}
	
	private void assignInput()
	{
		//debug input
    	final float camMove = 0.1f;
    	final float zoom = 0.05f;
    	OrthographicCamera camera = scene.getCamera();
    	ZootBindableInputProcessor debugInputProcessor = new ZootBindableInputProcessor();
    	debugInputProcessor.bindDown(Input.Keys.NUMPAD_8, () -> { camera.translate(0, camMove, 0); return true; });
    	debugInputProcessor.bindDown(Input.Keys.NUMPAD_2, () -> { camera.translate(0, -camMove, 0); return true; });
    	debugInputProcessor.bindDown(Input.Keys.NUMPAD_4, () -> { camera.translate(-camMove, 0, 0); return true; });
    	debugInputProcessor.bindDown(Input.Keys.NUMPAD_6, () -> { camera.translate(camMove, 0, 0); return true; });
    	debugInputProcessor.bindDown(Input.Keys.NUMPAD_5, () -> { camera.zoom -= zoom; return true; });
    	debugInputProcessor.bindDown(Input.Keys.NUMPAD_0, () -> { camera.zoom += zoom; return true; });
    	debugInputProcessor.bindDown(Input.Keys.PERIOD, () -> { camera.zoom = 1.0f; return true; });    	
    	debugInputProcessor.bindUp(Input.Keys.F9, () -> 
    	{ 
    		scene.setDebugMode(!scene.isDebugMode()); 
    		debugHud.setWindowVisible(scene.isDebugMode()); 
    		return true; 
    	});
    	debugInputProcessor.bindUp(Input.Keys.F12, () -> { reloadScene(); return true; });
    	
    	//character input    	
    	ZootActor player = scene.getActors((act) -> act.getName().equalsIgnoreCase("Frisker")).get(0);
    	GameCharacterInputProcessor characterInputProcessor = new GameCharacterInputProcessor(player);    	
    	player.addController(new InputProcessorController(characterInputProcessor));
    	scene.setFocusedActor(player);
    	
    	//input  	
    	inputManager = new ZootInputManager();    	
    	inputManager.addProcessor(debugInputProcessor);
    	inputManager.addProcessor(characterInputProcessor);
    	inputManager.addProcessor(scene.getInputProcessor());
    	inputManager.addProcessor(debugHud.getInputProcessor());
    	Gdx.input.setInputProcessor(inputManager);
    	
    	//debug
		scene.addListener(new ZootDebugWindowListener(debugHud));
	}
	
	private void deassignInput()
	{
		Gdx.input.setInputProcessor(null);
	}
}

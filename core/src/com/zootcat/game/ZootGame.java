package com.zootcat.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.zootcat.controllers.input.InputProcessorController;
import com.zootcat.input.ZootBindableInputProcessor;
import com.zootcat.input.ZootInputManager;
import com.zootcat.input.ZootInputProcessorListener;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.tiled.ZootTiledScene;

public class ZootGame extends ApplicationAdapter
{	
	private static final float VIEWPORT_WIDTH = 7.0f;
	private static final float VIEWPORT_HEIGHT = 4.0f;
	private static final float UNIT_PER_TILE = 0.17f;	//1 tile = 17cm
	
	private ZootTiledScene scene;
	private ZootInputManager inputManager;
		
    @Override
    public void create()
    {                    	
    	//create scene
    	scene = new ZootTiledScene("data/TestBed.tmx", VIEWPORT_WIDTH, VIEWPORT_HEIGHT, UNIT_PER_TILE);
    	
    	//debug input
    	final float camMove = 0.1f;
    	ZootBindableInputProcessor debugInputProcessor = new ZootBindableInputProcessor();
    	debugInputProcessor.bindDown(Input.Keys.NUMPAD_8, () -> { scene.getCamera().translate(0, camMove, 0); return true; });
    	debugInputProcessor.bindDown(Input.Keys.NUMPAD_2, () -> { scene.getCamera().translate(0, -camMove, 0); return true; });
    	debugInputProcessor.bindDown(Input.Keys.NUMPAD_4, () -> { scene.getCamera().translate(-camMove, 0, 0); return true; });
    	debugInputProcessor.bindDown(Input.Keys.NUMPAD_6, () -> { scene.getCamera().translate(camMove, 0, 0); return true; });
    	debugInputProcessor.bindUp(Input.Keys.F9, () -> { scene.setDebugMode(!scene.isDebugMode()); return true; });
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
    	Gdx.input.setInputProcessor(inputManager);
    }

    @Override
    public void render()
    {        
        float delta = Gdx.graphics.getDeltaTime();
    	
        //update
        inputManager.processPressedKeys(delta);
        scene.update(delta);
    	
    	//render
    	scene.render(delta);
    }

	@Override
	public void resize (int width, int height) 
	{
		scene.resize(width, height);
	}
    
	@Override
	public void dispose() 
	{
		scene.dispose();
		scene = null;
	}
}

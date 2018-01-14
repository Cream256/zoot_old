package com.zootcat.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.zootcat.controllers.input.InputProcessorController;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.input.ZootBindableInputProcessor;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.tiled.ZootTiledScene;

public class ZootGame extends ApplicationAdapter
{	
	private static final float VIEWPORT_WIDTH = 7.0f;
	private static final float VIEWPORT_HEIGHT = 4.0f;
	private static final float UNIT_PER_TILE = 0.17f;	//1 tile = 17cm
	
	private ZootTiledScene scene;
		
    @Override
    public void create()
    {                
    	//create input
    	ZootBindableInputProcessor globalInputProcessor = new ZootBindableInputProcessor();
    	
    	//create scene
    	scene = new ZootTiledScene("data/TestBed.tmx", globalInputProcessor, VIEWPORT_WIDTH, VIEWPORT_HEIGHT, UNIT_PER_TILE);
    	
    	//configure global input
    	final float camMove = 0.1f;
    	globalInputProcessor.bindDown(Input.Keys.NUMPAD_8, () -> { scene.getCamera().translate(0, camMove, 0); return true; });
    	globalInputProcessor.bindDown(Input.Keys.NUMPAD_2, () -> { scene.getCamera().translate(0, -camMove, 0); return true; });
    	globalInputProcessor.bindDown(Input.Keys.NUMPAD_4, () -> { scene.getCamera().translate(-camMove, 0, 0); return true; });
    	globalInputProcessor.bindDown(Input.Keys.NUMPAD_6, () -> { scene.getCamera().translate(camMove, 0, 0); return true; });
    	globalInputProcessor.bindUp(Input.Keys.F9, () -> { scene.setDebugMode(!scene.isDebugMode()); return true; });
    	
    	//configure character input    	
    	final float charMove = 0.1f;
    	ZootActor player = scene.getActors((act) -> act.getName().equalsIgnoreCase("Frisker")).get(0);
    	ZootBindableInputProcessor characterInputProcessor = new ZootBindableInputProcessor(); 
    	characterInputProcessor.bindDown(Input.Keys.RIGHT, () -> 
    	{ 
    		PhysicsBodyController ctrl = player.getController(PhysicsBodyController.class);
    		ctrl.applyImpulse(charMove, 0.0f, 0.0f);
    		return true;
    	});
    	characterInputProcessor.bindDown(Input.Keys.LEFT, () -> 
    	{ 
    		PhysicsBodyController ctrl = player.getController(PhysicsBodyController.class);
    		ctrl.applyImpulse(-charMove, 0.0f, 0.0f);
    		return true;
    	});
    	characterInputProcessor.bindDown(Input.Keys.UP, () -> 
    	{ 
    		PhysicsBodyController ctrl = player.getController(PhysicsBodyController.class);
    		ctrl.applyImpulse(0.0f, charMove * 5, 0.0f);
    		return true;
    	});
    	
    	player.addController(new InputProcessorController(characterInputProcessor));
    	scene.setFocusedActor(player);
    }

    @Override
    public void render()
    {        
        float delta = Gdx.graphics.getDeltaTime();
    	scene.update(delta);
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

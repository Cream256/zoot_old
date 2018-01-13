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
	private ZootTiledScene scene;
	
    @Override
    public void create()
    {                
    	//create input
    	ZootBindableInputProcessor globalInputProcessor = new ZootBindableInputProcessor();
    	
    	//create scene
    	scene = new ZootTiledScene("data/TestBed.tmx", globalInputProcessor);
    	
    	//configure global input
    	globalInputProcessor.bindDown(Input.Keys.NUMPAD_8, () -> { scene.getCamera().translate(0, 10, 0); return true; });
    	globalInputProcessor.bindDown(Input.Keys.NUMPAD_2, () -> { scene.getCamera().translate(0, -10, 0); return true; });
    	globalInputProcessor.bindDown(Input.Keys.NUMPAD_4, () -> { scene.getCamera().translate(-10, 0, 0); return true; });
    	globalInputProcessor.bindDown(Input.Keys.NUMPAD_6, () -> { scene.getCamera().translate(10, 0, 0); return true; });
    	globalInputProcessor.bindUp(Input.Keys.F9, () -> { scene.setDebugMode(!scene.isDebugMode()); return true; });
    	
    	//configure character input    	
    	ZootActor player = scene.getActors((act) -> act.getName().equalsIgnoreCase("Frisker")).get(0);
    	
    	ZootBindableInputProcessor characterInputProcessor = new ZootBindableInputProcessor(); 
    	characterInputProcessor.bindDown(Input.Keys.RIGHT, () -> 
    	{ 
    		PhysicsBodyController ctrl = player.getController(PhysicsBodyController.class);
    		ctrl.applyImpulse(10000.0f, 0.0f, 0.0f);
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
	public void dispose() 
	{
		scene.dispose();
		scene = null;
	}
}

package com.zootcat.game;

import java.util.function.Supplier;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.zootcat.input.LambdaInputCommand;
import com.zootcat.input.ZootBindableInputProcessor;
import com.zootcat.scene.tiled.ZootTiledScene;

public class ZootGame extends ApplicationAdapter
{	
	private ZootTiledScene scene;
	
    @Override
    public void create()
    {                
    	//create input
    	ZootBindableInputProcessor inputProcessor = new ZootBindableInputProcessor();
    	
    	//create scene
    	scene = new ZootTiledScene("data/TestBed.tmx", inputProcessor);
    	
    	//configure input
        Supplier<Boolean> noImpl = () -> { return false; };
        Supplier<Boolean> moveCameraUp = () -> { scene.getCamera().translate(0, 10, 0); return true; };
    	Supplier<Boolean> moveCameraDown = () -> { scene.getCamera().translate(0, -10, 0); return true; };
    	Supplier<Boolean> moveCameraLeft = () -> { scene.getCamera().translate(-10, 0, 0); return true; };
    	Supplier<Boolean> moveCameraRight = () -> { scene.getCamera().translate(10, 0, 0); return true; };
    	Supplier<Boolean> switchDebugMode = () -> { scene.setDebugMode(!scene.isDebugMode()); return true; };
    	inputProcessor.bindCommand(Input.Keys.UP, new LambdaInputCommand(moveCameraUp));
    	inputProcessor.bindCommand(Input.Keys.DOWN, new LambdaInputCommand(moveCameraDown));
    	inputProcessor.bindCommand(Input.Keys.LEFT, new LambdaInputCommand(moveCameraLeft));
    	inputProcessor.bindCommand(Input.Keys.RIGHT, new LambdaInputCommand(moveCameraRight));
    	inputProcessor.bindCommand(Input.Keys.F9, new LambdaInputCommand(noImpl, noImpl, switchDebugMode));
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

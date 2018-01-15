package com.zootcat.input;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

public class ZootInputManager extends InputAdapter 
{
	private Set<Integer> pressedKeys = new HashSet<Integer>();
	private InputMultiplexer multiplexer = new InputMultiplexer();
	
	@Override
	public boolean keyDown (int keycode) 
	{
		pressedKeys.add(keycode);		
		return multiplexer.keyDown(keycode);
	}

	@Override
	public boolean keyUp (int keycode) 
	{
		pressedKeys.remove(keycode);
		return multiplexer.keyUp(keycode);
	}
	
	public void processPressedKeys(float delta)
	{
		pressedKeys.forEach((key) -> keyDown(key));
	}

	public void addProcessor(InputProcessor inputProcessor) 
	{
		multiplexer.addProcessor(inputProcessor);
	}
	
	public void removeProcessor(InputProcessor inputProcessor)
	{
		multiplexer.removeProcessor(inputProcessor);
	}
}

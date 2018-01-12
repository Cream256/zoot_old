package com.zootcat.input;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.InputAdapter;

public class ZootBindableInputProcessor extends InputAdapter implements ZootInputProcessor
{
	private Map<Integer, ZootInputCommand> bindings = new HashMap<Integer, ZootInputCommand>();
	private Set<Integer> pressedKeys = new HashSet<Integer>();
	
	@Override
	public boolean keyDown (int keycode) 
	{
		pressedKeys.add(keycode);
		if(bindings.containsKey(keycode))
		{
			return bindings.get(keycode).executeOnDown();
		}		
		return false;
	}
	
	@Override
	public boolean keyUp (int keycode) 
	{
		if(pressedKeys.contains(keycode))
		{
			if(bindings.containsKey(keycode))
			{
				bindings.get(keycode).executeOnPress();
			}
			pressedKeys.remove(keycode);
		}
		
		if(bindings.containsKey(keycode))
		{
			return bindings.get(keycode).executeOnRelease();
		}
		return false;
	}
		
	@Override
	public void processPressedKeys()
	{
		for(int keycode : pressedKeys)
		{
			keyDown(keycode);
		}
	}
	
	public void bindCommand(int keycode, ZootInputCommand command)
	{
		bindings.put(keycode, command);
	}
	
}

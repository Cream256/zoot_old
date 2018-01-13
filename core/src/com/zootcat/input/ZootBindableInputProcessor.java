package com.zootcat.input;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import com.badlogic.gdx.InputAdapter;
import com.zootcat.exceptions.RuntimeZootException;

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
		validateNewBinding(keycode);
		bindings.put(keycode, command);
	}
	
	public void bindDown(int keycode, Supplier<Boolean> downCommand)
	{
		bindCommand(keycode, new LambdaInputCommand(downCommand));
	}
	
	public void bindUp(int keycode, Supplier<Boolean> upCommand)
	{
		bindCommand(keycode, new LambdaInputCommand(() -> false, upCommand, () -> false));
	}
	
	public boolean hasBinding(int keycode)
	{
		return bindings.containsKey(keycode);
	}
	
	private void validateNewBinding(int keycode)
	{
		if(hasBinding(keycode))
		{
			throw new RuntimeZootException("Binding already present for keycode " + keycode);
		}
	}
}

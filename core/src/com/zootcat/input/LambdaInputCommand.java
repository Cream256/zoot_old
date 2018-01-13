package com.zootcat.input;

import java.util.function.Supplier;

public class LambdaInputCommand implements ZootInputCommand 
{
	private Supplier<Boolean> onDown;
	private Supplier<Boolean> onPress;	
	private Supplier<Boolean> onRelease;
	
	public LambdaInputCommand(Supplier<Boolean> down)
	{
		this(down, () -> false, () -> false);
	}
	
	public LambdaInputCommand(Supplier<Boolean> down, Supplier<Boolean> release, Supplier<Boolean> press)
	{
		this.onDown = down;
		this.onPress = press;
		this.onRelease = release;		
	}
	
	@Override
	public boolean executeOnPress() 
	{
		return onPress.get();
	}

	@Override
	public boolean executeOnDown() 
	{
		return onDown.get();
	}

	@Override
	public boolean executeOnRelease() 
	{
		return onRelease.get();
	}
}
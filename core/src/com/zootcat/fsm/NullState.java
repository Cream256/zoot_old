package com.zootcat.fsm;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.zootcat.scene.ZootActor;

public class NullState implements State
{
	public static final NullState INSTANCE = new NullState();
	
	private NullState()
	{
		//use instance
	}
	
	@Override
	public int getId()
	{
		return 0;
	}

	@Override
	public void onEnter(ZootActor actor)
	{
		//noop	
	}

	@Override
	public void onLeave(ZootActor actor)
	{
		//noop
	}

	@Override
	public void update(ZootActor actor, float delta)
	{
		//noop
	}

	@Override
	public boolean handle(Event event)
	{
		return false;
	}
}

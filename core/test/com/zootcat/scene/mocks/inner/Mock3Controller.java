package com.zootcat.scene.mocks.inner;

import com.zootcat.controllers.Controller;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class Mock3Controller implements Controller 
{
	public static final int INVOKED_FIRST_CTOR = 1;
	public static final int INVOKED_SECOND_CTOR = 2;
	public static final int INVOKED_THIRD_CTOR = 3;
	
	public int ctorInvoked;
	
	public Mock3Controller()
	{
		ctorInvoked = INVOKED_FIRST_CTOR;
	}
	
	public Mock3Controller(int a, ZootScene globalParameter)
	{
		this.ctorInvoked = INVOKED_SECOND_CTOR;
	}
	
	public Mock3Controller(int a, int b, ZootActor globalActorParam, ZootScene globalSceneParam)
	{
		this.ctorInvoked = INVOKED_THIRD_CTOR;		
	}
	
	@Override
	public void onAdd(ZootActor actor) 
	{
		//noop
	}

	@Override
	public void onRemove(ZootActor actor) 
	{
		//noop	
	}

	@Override
	public void onUpdate(float delta, ZootActor actor) 
	{
		//noop
	}
}

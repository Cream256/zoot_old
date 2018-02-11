package com.zootcat.controllers.logic;

import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.scene.ZootActor;

public class LifeController extends IntValueController
{
	public static final int DEFAULT_LIFE = 3;
	
	@CtrlDebug boolean sendDeadEvent = false;
	@CtrlDebug boolean deadEventSend = false;
	
	private ZootEvent deadEvent;
	
	@Override
	public void init(ZootActor actor) 
	{
		deadEvent = new ZootEvent();
		setMaxValue(DEFAULT_LIFE);
		setMinValue(0);
		setValue(DEFAULT_LIFE);
	}
	
	@Override
	public void setValue(int value)
	{
		super.setValue(value);
		sendDeadEvent = getValue() == 0;
		deadEventSend = deadEventSend && getValue() == 0;
	}
	
	@Override
	public void setMaxValue(int newValue)
	{
		super.setMaxValue(Math.max(newValue, 1));
	}
	
	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		if(sendDeadEvent && !deadEventSend)
		{
			deadEvent.reset();
			deadEvent.setType(ZootEventType.Dead);			
			actor.fire(deadEvent);			
			deadEventSend = true;
		}		
	}
	
	public boolean isAlive()
	{
		return getValue() > 0;
	}
}
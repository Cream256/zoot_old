package com.zootcat.controllers.logic;

import com.badlogic.gdx.math.MathUtils;
import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.scene.ZootActor;

public class LifeController extends ControllerAdapter
{
	public static final int DEFAULT_LIFE = 3;
	
	@CtrlParam(debug = true) private int life = DEFAULT_LIFE;
	@CtrlParam(debug = true) private int maxLife = DEFAULT_LIFE;
	@CtrlDebug private boolean sendDeadEvent = false;
	@CtrlDebug private boolean deadEventSend = false;
	
	private ZootEvent deadEvent;
	
	@Override
	public void init(ZootActor actor) 
	{
		deadEvent = new ZootEvent();
		setMaxLife(maxLife);
		setLife(life);
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
	
	public int getLife()
	{
		return life;
	}
	
	public int getMaxLife()
	{
		return maxLife;
	}
	
	public void addLife(int value)
	{
		setLife(getLife() + value);
	}
	
	public void addMaxLife(int value)
	{
		setMaxLife(getMaxLife() + value);
	}
	
	public void setLife(int value)
	{
		life = MathUtils.clamp(value, 0, maxLife);
		sendDeadEvent = life == 0;
		deadEventSend = deadEventSend && life == 0;
	}
	
	public void setMaxLife(int value)
	{
		maxLife = MathUtils.clamp(value, 1, Integer.MAX_VALUE);
		life = MathUtils.clamp(life, 0, maxLife);
	}
	
	public boolean isAlive()
	{
		return life > 0;
	}
}

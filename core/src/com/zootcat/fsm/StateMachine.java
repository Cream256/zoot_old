package com.zootcat.fsm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.scene.ZootActor;

public class StateMachine implements EventListener
{
	private ZootActor owner = null;
	private State previousState = null;
	private State currentState = NullState.INSTANCE;	
	private Map<Integer, State> states = new HashMap<Integer, State>();
	
	public void init(State state)
	{
		previousState = null;
		currentState = state;
		if(!states.containsKey(state.getId()))
		{
			addState(state);
		}
		currentState.onEnter(owner);		
	}
	
	public void addState(State state)
	{
		states.put(state.getId(), state);		
	}
	
	public Set<State> getStates()
	{
		return new HashSet<State>(states.values());
	}
	
	public State getStateById(int id)
	{
		State state = states.get(id);
		if(state == null)
		{
			throw new RuntimeZootException("No state with id: " + id);
		}
		return state;
	}
	
	public void setOwner(ZootActor actor)
	{
		owner = actor;
	}
	
	public ZootActor getOwner()
	{
		return owner;
	}
	
	public State getCurrentState()
	{
		return currentState;
	}
	
	public State getPreviousState()
	{
		return previousState;
	}
	
	public void update(float delta)
	{
		currentState.update(owner, delta);
	}

	public void changeState(State newState)
	{
        if(newState == null)
        {
            throw new RuntimeZootException("Cannot change to null state");
        }
        
        if(newState != currentState)
        {
            currentState.onLeave(owner);
            previousState = currentState;
            currentState = newState;
            newState.onEnter(owner);            
        }
	}
	
	@Override
	public boolean handle(Event event)
	{
        return currentState.handle(event);
	}	
}

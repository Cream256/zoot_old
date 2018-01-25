package com.zootcat.controllers.ai;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.fsm.ZootStateMachine;
import com.zootcat.fsm.states.FallState;
import com.zootcat.fsm.states.IdleState;
import com.zootcat.fsm.states.JumpState;
import com.zootcat.fsm.states.WalkState;
import com.zootcat.scene.ZootActor;

//TODO rename this class
public class BasicAI extends ControllerAdapter
{
	@Override
	public void init(ZootActor actor) 
	{
		ZootStateMachine sm = actor.getStateMachine();
		sm.init(new IdleState());
		sm.addState(new WalkState());
		sm.addState(new JumpState());
		sm.addState(new FallState());
	}
}

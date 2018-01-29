package com.zootcat.controllers.logic;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.zootcat.scene.ZootActor;

public class DieOnCollideFromAboveController extends OnCollideFromAboveController
{
	@Override
	public void onCollidedFromAbove(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		getControllerActor().addAction(new RemoveActorAction());
	}
}

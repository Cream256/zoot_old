package com.zootcat.controllers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.zootcat.scene.ZootActor;

public interface RenderController extends Controller, ChangeListenerController
{
	void onRender(Batch batch, float parentAlpha, ZootActor actor, float delta);
}

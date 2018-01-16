package com.zootcat.controllers.gfx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;

public class SpriteController implements RenderController
{
	@CtrlParam(required = true) private String file;
	
	private Sprite sprite;
	
	@Override
	public void init(ZootActor actor)
	{
		sprite = new Sprite(new Texture(file));
	}
	
	@Override
	public void onAdd(ZootActor actor) 
	{
		updateSprite(actor);
	}
	
	@Override
	public void onRemove(ZootActor actor) 
	{
		sprite = null;
	}
	
	@Override
	public void onUpdate(float delta, ZootActor actor) 
	{
		//noop
	}

	@Override
	public void onRender(Batch batch, float parentAlpha, ZootActor actor, float delta) 
	{		
		updateSprite(actor);
		sprite.draw(batch);
	}
	
	protected void updateSprite(ZootActor actor)
	{
		sprite.setPosition(actor.getX(), actor.getY());
		sprite.setSize(actor.getWidth(), actor.getHeight());
		sprite.setOriginCenter();		
		sprite.setRotation(actor.getRotation());
	}
}

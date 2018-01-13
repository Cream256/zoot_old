package com.zootcat.controllers.gfx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.zootcat.scene.ZootActor;

public class SpriteController implements RenderController
{
	private Sprite sprite;
	
	public SpriteController(String spriteFileName)
	{
		sprite = new Sprite(new Texture(spriteFileName));
	}

	@Override
	public void onAdd(ZootActor actor) 
	{
		onSizeChange(actor);
		onPositionChange(actor);
		onRotationChange(actor);
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

	@Override
	public void onSizeChange(ZootActor actor) 
	{
		sprite.setSize(actor.getWidth(), actor.getHeight());
		sprite.setOriginCenter();
	}

	@Override
	public void onPositionChange(ZootActor actor) 
	{
		sprite.setPosition(actor.getX(), actor.getY());
	}

	@Override
	public void onRotationChange(ZootActor actor) 
	{
		sprite.setRotation(actor.getRotation());
	}

	@Override
	public void onRender(Batch batch, float parentAlpha, ZootActor actor, float delta) 
	{		
		sprite.draw(batch);
	}
}
package com.zootcat.controllers.gfx;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.exceptions.ZootException;
import com.zootcat.gfx.ZootAnimation;
import com.zootcat.gfx.ZootAnimationFile;
import com.zootcat.scene.ZootActor;

public class AnimatedSpriteController implements RenderController 
{
	@CtrlParam(required = true) private String file;
	@CtrlParam private boolean useActorSize = true;
	
	private Sprite sprite;
	private Map<Integer, ZootAnimation> animations;
		
	@Override
	public void init(ZootActor actor)
	{
		try
		{
			sprite = new Sprite();			
			ZootAnimationFile animationFile = new ZootAnimationFile(Gdx.files.internal(file).file());
			animations = animationFile.createAnimations();
		}
		catch (ZootException e)
		{
			throw new RuntimeZootException("Unable to initialize animated sprite: " + e.getMessage(), e);
		}
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
		String type = "IDLE";		
		ZootAnimation animation = animations.get(type.hashCode());
		animation.start();
		animation.step(delta);
	}

	@Override
	public void onRender(Batch batch, float parentAlpha, ZootActor actor, float delta)
	{
		String type = "IDLE";
		
		ZootAnimation animation = animations.get(type.hashCode());
		TextureRegion animationFrame = animation.getKeyFrame();
		
		sprite.setTexture(animationFrame.getTexture());
		sprite.setRegion(animationFrame);		
		sprite.setPosition(actor.getX(), actor.getY());
		
		if(useActorSize)
		{
			sprite.setSize(actor.getWidth(), actor.getHeight());
			sprite.setOriginCenter();
		}
		else
		{
			//sprite.setSize(animationFrame.getRegionWidth(), animationFrame.getRegionHeight());
			//sprite.setOriginCenter();
		}
				
		sprite.setRotation(actor.getRotation());
		sprite.draw(batch);
	}

}

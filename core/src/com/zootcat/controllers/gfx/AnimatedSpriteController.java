package com.zootcat.controllers.gfx;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.exceptions.ZootException;
import com.zootcat.gfx.ZootAnimation;
import com.zootcat.gfx.ZootAnimationFile;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class AnimatedSpriteController implements RenderController 
{
	@CtrlParam(required = true) private String file;
	@CtrlParam private boolean useActorSize = true;
	@CtrlParam private String startingAnimation = "";
	@CtrlParam(global = true) private ZootScene scene;
	
	private Sprite sprite;
	private ZootAnimation currentAnimation;
	private Map<Integer, ZootAnimation> animations;
	private AssetManager assetManager;
	private ZootAnimationFile zootAnimationFile;
			
	public void setAnimation(String animationName)
	{
		if(currentAnimation != null)
		{
			currentAnimation.stop();
		}
		
		int newId = ZootAnimation.getAnimationId(animationName);		
		currentAnimation = animations.get(newId);
		currentAnimation.restart();
	}
	
	@Override
	public void init(ZootActor actor, AssetManager assetManager)
	{
		try
		{
			this.assetManager = assetManager;
			zootAnimationFile = new ZootAnimationFile(Gdx.files.internal(file).file());
			assetManager.load(zootAnimationFile.getSpriteSheetPath(), Texture.class);
		}
		catch(ZootException e)
		{
			throw new RuntimeZootException("Unable to initialize animated sprite", e);
		}
	}

	@Override
	public void onAdd(ZootActor actor)
	{
		String spriteSheetPath = zootAnimationFile.getSpriteSheetPath();
		
		animations = zootAnimationFile.createAnimations(assetManager.get(spriteSheetPath, Texture.class));
		setAnimation(startingAnimation);
		
		sprite = new Sprite();
		updateSprite(actor);
		
		zootAnimationFile = null;
	}

	@Override
	public void onRemove(ZootActor actor)
	{
		//noop
	}

	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		if(currentAnimation == null)
		{
			return;
		}
		
		currentAnimation.step(delta);
		updateSprite(actor);
	}

	@Override
	public void onRender(Batch batch, float parentAlpha, ZootActor actor, float delta)
	{
		if(currentAnimation == null)
		{
			return;
		}
		
		sprite.draw(batch);
	}
	
	private void updateSprite(ZootActor actor)
	{
		if(currentAnimation == null)
		{
			return;
		}
		
		TextureRegion frame = currentAnimation.getKeyFrame();		
		sprite.setTexture(frame.getTexture());
		sprite.setRegion(frame);				
		
		if(useActorSize)
		{
			sprite.setBounds(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
		}
		else
		{			
			float scale = scene.getUnitScale();
			sprite.setBounds(actor.getX(), actor.getY(), frame.getRegionWidth() * scale, frame.getRegionHeight() * scale);
		}
		
		sprite.setOriginCenter();
		sprite.setRotation(actor.getRotation());
	}
}

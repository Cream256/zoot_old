package com.zootcat.screen;

import java.util.LinkedList;
import java.util.function.Consumer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;

public class ZootLoadingScreen implements Screen
{
	private int allCount;
	private int finishedCount;
	private Consumer<AssetManager> task;
	private AssetManager assetManager;
	private LinkedList<Consumer<AssetManager>> loadTasks = new LinkedList<Consumer<AssetManager>>();
		
	public ZootLoadingScreen(AssetManager assetManager)
	{
		this.assetManager = assetManager;
	}
	
	@Override
	public void show()
	{
		finishedCount = 0;
		allCount = loadTasks.size();		
		task = loadTasks.remove();
		task.accept(assetManager);
	}

	@Override
	public void render(float delta)
	{				
		if(task == null && loadTasks.isEmpty())
		{
			onRenderAfterLoading(delta);
			return;
		}
		
		onRenderWhileLoading(delta);
		if(task != null)
		{
			boolean taskFinished = assetManager.update();
			if(taskFinished)
			{
				++finishedCount;
				if(loadTasks.isEmpty())
				{
					onFinishLoading();
					task = null;
					return;
				}
				task = loadTasks.remove();
				task.accept(assetManager);
			}
		}
	}
	
	public void addTask(Consumer<Void> loadTask)
	{
		loadTasks.add((assetManager) -> loadTask.accept(null));
	}
	
	public float getProgress()
	{		
		float progress = finishedCount / (float)allCount;		
		return Float.isNaN(progress) ? 0.0f : progress;
	}
	
	@Override
	public void resize(int width, int height)
	{
		//noop		
	}

	@Override
	public void pause()
	{
		//noop		
	}

	@Override
	public void resume()
	{
		//noop		
	}

	@Override
	public void hide()
	{
		//noop
	}

	@Override
	public void dispose()
	{
		//noop
	}
	
	protected void onFinishLoading()
	{
		//noop
	}
	
	protected void onRenderAfterLoading(float delta)
	{
		Gdx.gl.glClearColor(1, 1, 1, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	}
	
	protected void onRenderWhileLoading(float delta)
	{
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	}
}

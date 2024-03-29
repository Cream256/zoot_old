/*******************************************************************************
 * Copyright 2015 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.zootcat.testing;

import java.util.HashMap;
import java.util.Map;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class WindowedGdxTestRunner extends BlockJUnit4ClassRunner implements ApplicationListener 
{
	private Map<FrameworkMethod, RunNotifier> invokeInRender = new HashMap<FrameworkMethod, RunNotifier>();

	public WindowedGdxTestRunner(Class<?> klass) throws InitializationError 
	{
		super(klass);
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = klass.getSimpleName();
		config.width = 320;
		config.height = 240;		
		config.x = 0;
		config.y = 0;
		new LwjglApplication(this, config);
	}

	@Override
	public void create() 
	{
		//noop
	}

	@Override
	public void resume() 
	{
		//noop
	}

	@Override
	public void render() 
	{
		synchronized (invokeInRender) 
		{
			for (Map.Entry<FrameworkMethod, RunNotifier> each : invokeInRender.entrySet()) 
			{
				super.runChild(each.getKey(), each.getValue());
			}
			invokeInRender.clear();
		}
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
	public void dispose() 
	{
		//noop
	}

	@Override
	protected void runChild(FrameworkMethod method, RunNotifier notifier) 
	{
		synchronized (invokeInRender) 
		{
			// add for invoking in render phase, where gl context is available
			invokeInRender.put(method, notifier);
		}
		// wait until that test was invoked
		waitUntilInvokedInRenderMethod();
	}

	private void waitUntilInvokedInRenderMethod() 
	{
		try 
		{
			while (true) 
			{
				Thread.sleep(10);
				synchronized (invokeInRender) 
				{
					if (invokeInRender.isEmpty()) break;
				}
			}
		}
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
}
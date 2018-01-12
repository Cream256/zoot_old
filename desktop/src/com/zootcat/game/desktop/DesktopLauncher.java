package com.zootcat.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.zootcat.game.ZootGame;

public class DesktopLauncher 
{
	public static void main (String[] arg) 
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Witchcats";
		config.width = 1366;
		config.height = 768;
		config.vSyncEnabled = true;
		
		new LwjglApplication(new ZootGame(), config);
	}
}

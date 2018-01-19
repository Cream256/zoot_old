package com.zootcat.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.zootcat.game.ZootGame;

public class DesktopLauncher 
{
	private static final float VIEWPORT_WIDTH = 7.0f;
	private static final float VIEWPORT_HEIGHT = 4.0f;
	private static final float UNIT_PER_TILE = 0.17f;	//1 tile = 17cm
	
	public static void main (String[] arg) 
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Witchcats";
		config.width = 1366;
		config.height = 768;
		config.vSyncEnabled = true;
		
		new LwjglApplication(new ZootGame(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, UNIT_PER_TILE), config);
	}
}

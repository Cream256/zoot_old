package com.zootcat.game;

import com.badlogic.gdx.Game;
import com.zootcat.screen.ZootLevelScreen;

public class ZootGame extends Game
{	
	private static final float VIEWPORT_WIDTH = 7.0f;
	private static final float VIEWPORT_HEIGHT = 4.0f;
	private static final float UNIT_PER_TILE = 0.17f;	//1 tile = 17cm
	
    @Override
    public void create()
    {    	
    	ZootLevelScreen screen = new ZootLevelScreen("data/TestBed.tmx", VIEWPORT_WIDTH, VIEWPORT_HEIGHT, UNIT_PER_TILE);    	
    	setScreen(screen);
    }
}

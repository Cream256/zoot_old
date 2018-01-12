package com.zootcat.map;

import java.util.List;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.utils.Disposable;

public interface ZootMap extends Disposable
{
	List<MapObject> getAllObjects();
}

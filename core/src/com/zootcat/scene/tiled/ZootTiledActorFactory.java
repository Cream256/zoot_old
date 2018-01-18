package com.zootcat.scene.tiled;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.maps.MapObject;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.map.tiled.ZootTiledMapCell;
import com.zootcat.scene.ZootActor;

public class ZootTiledActorFactory 
{
	private float scale;
				
	public ZootTiledActorFactory(float scale)
	{
		this.scale = scale;
	}
	
	public ZootActor createFromMapObject(final MapObject mapObject)
	{		
		ZootActor actor = new ZootActor();		
		setActorBasicProperties(mapObject, actor);
		return actor;
	}
	
	public ZootActor createFromMapCell(final ZootTiledMapCell cell)
	{
		ZootActor cellActor = new ZootActor();
		cellActor.setId(cell.cell.getTile().getId());
		cellActor.setName("Cell " + cell.x + "x" + cell.y);
		cellActor.setBounds(cell.x * cell.width * scale, cell.y * cell.height * scale, cell.width * scale, cell.height * scale);		
		return cellActor;
	}
	
	public List<ZootActor> createFromMapCells(final List<ZootTiledMapCell> cells) 
	{
		return cells.stream().map(cell -> createFromMapCell(cell)).collect(Collectors.toList());
	}
	
	public List<ZootActor> createFromMapObjects(final Collection<MapObject> objects)
	{
		return objects.stream().map(obj -> createFromMapObject(obj)).collect(Collectors.toList());
	}
			
	protected void setActorBasicProperties(final MapObject mapObject, ZootActor actor) 
	{
		actor.setName(mapObject.getName());
		actor.setColor(mapObject.getColor());
		actor.setVisible(mapObject.isVisible());
		actor.setOpacity(mapObject.getOpacity());
		
		int id = Integer.valueOf(getPropertyOrThrow(mapObject, "id"));
		float x = Float.valueOf(getPropertyOrThrow(mapObject, "x")) * scale;
		float y = Float.valueOf(getPropertyOrThrow(mapObject, "y")) * scale;
		float width = Float.valueOf(getPropertyOrThrow(mapObject, "width")) * scale;
		float height = Float.valueOf(getPropertyOrThrow(mapObject, "height")) * scale; 		
		float rotation = Float.valueOf(getPropertyOrDefault(mapObject, "rotation", "0.0f"));		
		actor.setBounds(x, y, width, height);
		actor.setRotation(rotation);
		actor.setId(id);
	}
		
	protected String getPropertyOrDefault(MapObject mapObject, String key, String defaultValue)
	{
		Object value = mapObject.getProperties().get(key);
		return value != null ? value.toString() : defaultValue;	
	}
	
	protected String getPropertyOrThrow(MapObject mapObject, String key)
	{
		if(!mapObject.getProperties().containsKey(key))
		{
			throw new RuntimeZootException("Object property missing: " + key + " for object with name: " + mapObject.getName());
		}
		return mapObject.getProperties().get(key).toString();
	}
}

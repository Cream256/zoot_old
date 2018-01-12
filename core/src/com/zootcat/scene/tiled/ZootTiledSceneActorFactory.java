package com.zootcat.scene.tiled;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.classcontainer.ClassContainer;
import com.zootcat.classcontainer.ClassContainerException;
import com.zootcat.controllers.Controller;
import com.zootcat.controllers.physics.StaticBodyController;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.map.tiled.ZootTiledMapCell;
import com.zootcat.scene.ZootActor;
import com.zootcat.utils.ArgumentParser;
import com.zootcat.utils.ArgumentParserException;
import com.zootcat.utils.ClassFinder;

public class ZootTiledSceneActorFactory 
{
	private static final String CONTROLLER_SUFFIX = "Controller";
	
	private ZootTiledScene scene;
	private ClassContainer controllers = new ClassContainer();
		
	public ZootTiledSceneActorFactory(ZootTiledScene scene)
	{
		this.scene = scene;
		controllers.addGlobalParameter(scene);
		addControllersFromPackage("com.zootcat.controllers", true);
	}
	
	public ZootActor createFromMapObject(final MapObject mapObject)
	{		
		ZootActor actor = new ZootActor();		
		setActorBasicProperties(mapObject, actor);		
		setActorControllers(mapObject, actor);
		return actor;
	}
	
	public ZootActor createFromMapCell(final ZootTiledMapCell cell)
	{
		ZootActor cellActor = new ZootActor();
		cellActor.setBounds(cell.x * cell.width, cell.y * cell.height, cell.width, cell.height);
		if(cell.collidable > 0)
		{
			cellActor.addController(new StaticBodyController(cellActor, scene));
		}
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
	
	public int addControllersFromPackage(String packageName, boolean includeSubDirs)
	{
		List<Class<?>> found = ClassFinder.find(packageName, includeSubDirs);
		
		Predicate<Class<?>> filterAssignable = cls -> ClassReflection.isAssignableFrom(Controller.class, cls) && !ClassReflection.isInterface(cls);		
		Predicate<Class<?>> filterByName = cls -> cls.getSimpleName().endsWith(CONTROLLER_SUFFIX);
		
		List<Class<?>> filtered = found.stream()
									   .filter(filterAssignable)
									   .filter(filterByName)
									   .collect(Collectors.toList());
		
		filtered.forEach(cls -> controllers.add(cls));
		return filtered.size();
	}
	
	protected void setActorBasicProperties(final MapObject mapObject, ZootActor actor) 
	{
		actor.setName(mapObject.getName());
		actor.setColor(mapObject.getColor());
		actor.setVisible(mapObject.isVisible());
		actor.setOpacity(mapObject.getOpacity());
		
		float x = Float.valueOf(getPropertyOrThrow(mapObject, "x"));
		float y = Float.valueOf(getPropertyOrThrow(mapObject, "y"));
		float width = Float.valueOf(getPropertyOrThrow(mapObject, "width"));
		float height = Float.valueOf(getPropertyOrThrow(mapObject, "height")); 		
		float rotation = Float.valueOf(getPropertyOrDefault(mapObject, "rotation", "0.0f"));
		actor.setBounds(x, y, width, height);
		actor.setRotation(rotation);
	}

	protected void setActorControllers(final MapObject mapObject, ZootActor actor)
	{
		controllers.addGlobalParameter(actor);		
		mapObject.getProperties().getKeys().forEachRemaining(ctrlName ->
		{
			String normalizedCtrlName = normalizeControllerName(ctrlName); 
			if(controllers.contains(normalizedCtrlName))
			{
				try 
				{									
					String[] controllerArguments = mapObject.getProperties().get(ctrlName, String.class).split(",");
					List<Object> arguments = ArgumentParser.parse(controllerArguments, actor);
					Controller controller = (Controller) controllers.create(normalizedCtrlName, arguments);
					actor.addController(controller);
				}
				catch(ArgumentParserException | ClassContainerException e)
                {
                    throw new RuntimeZootException("Error while trying to create " + ctrlName, e);
                }
			}
		});
		controllers.removeGlobalParameter(actor);
	}
	
	protected String normalizeControllerName(String controllerName)
	{
		return controllerName.endsWith(CONTROLLER_SUFFIX) ? controllerName : controllerName + CONTROLLER_SUFFIX;
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

package com.zootcat.map.tiled;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.controllers.Controller;
import com.zootcat.controllers.factory.ControllerFactory;
import com.zootcat.controllers.factory.ControllerFactoryException;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.tiled.ZootTiledScene;
import com.zootcat.utils.ArgumentParser;
import com.zootcat.utils.ArgumentParserException;
import com.zootcat.utils.ClassFinder;

public class ZootTiledMapActorFactory 
{
	private static final String ACTOR_GLOBAL_PARAM = "actor";
	private static final String SCENE_GLOBAL_PARAM = "scene";	
	private static final String CONTROLLER_SUFFIX = "Controller";

	private float scale;
	private ControllerFactory controllerFactory = new ControllerFactory();		
		
	public ZootTiledMapActorFactory(ZootTiledScene scene)
	{
		this(scene, 1.0f);
	}
	
	public ZootTiledMapActorFactory(ZootTiledScene scene, float scale)
	{
		this.scale = scale;
		controllerFactory.addGlobalParameter(SCENE_GLOBAL_PARAM, scene);
		addControllersFromPackage("com.zootcat.controllers", true);
	}
	
	public ZootActor createFromMapObject(final MapObject mapObject)
	{		
		ZootActor actor = new ZootActor();		
		setActorBasicProperties(mapObject, actor);		
		setActorControllers(mapObject.getProperties(), actor);
		return actor;
	}
	
	public ZootActor createFromMapCell(final ZootTiledMapCell cell)
	{
		ZootActor cellActor = new ZootActor();
		cellActor.setName("Cell " + cell.x + "x" + cell.y);
		cellActor.setBounds(cell.x * cell.width * scale, cell.y * cell.height * scale, cell.width * scale, cell.height * scale);
		setActorControllers(cell.cell.getTile().getProperties(), cellActor);		
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
		
		filtered.forEach(cls -> controllerFactory.add(cls));
		return filtered.size();
	}
	
	protected void setActorBasicProperties(final MapObject mapObject, ZootActor actor) 
	{
		actor.setName(mapObject.getName());
		actor.setColor(mapObject.getColor());
		actor.setVisible(mapObject.isVisible());
		actor.setOpacity(mapObject.getOpacity());
		
		float x = Float.valueOf(getPropertyOrThrow(mapObject, "x")) * scale;
		float y = Float.valueOf(getPropertyOrThrow(mapObject, "y")) * scale;
		float width = Float.valueOf(getPropertyOrThrow(mapObject, "width")) * scale;
		float height = Float.valueOf(getPropertyOrThrow(mapObject, "height")) * scale; 		
		float rotation = Float.valueOf(getPropertyOrDefault(mapObject, "rotation", "0.0f"));
		actor.setBounds(x, y, width, height);
		actor.setRotation(rotation);
	}

	protected void setActorControllers(final MapProperties actorProperties, ZootActor actor)
	{
		controllerFactory.addGlobalParameter(ACTOR_GLOBAL_PARAM, actor);		
		actorProperties.getKeys().forEachRemaining(ctrlName ->
		{
			String normalizedCtrlName = normalizeControllerName(ctrlName); 
			if(controllerFactory.contains(normalizedCtrlName))
			{
				try 
				{									
					String[] controllerArguments = actorProperties.get(ctrlName, String.class).split(",");
					Map<String, Object> arguments = ArgumentParser.parse(controllerArguments);
					
					Controller controller = (Controller) controllerFactory.create(normalizedCtrlName, arguments);										
					controller.init(actor);		//initialize first, then assign
					actor.addController(controller);	
				}
				catch(ArgumentParserException | ControllerFactoryException e)
                {
                    throw new RuntimeZootException("Error while trying to create " + ctrlName, e);
                }
			}
		});
		controllerFactory.removeGlobalParameter(ACTOR_GLOBAL_PARAM);
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

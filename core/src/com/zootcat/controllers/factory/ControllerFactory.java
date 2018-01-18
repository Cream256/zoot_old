package com.zootcat.controllers.factory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.controllers.Controller;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.utils.ArgumentParser;
import com.zootcat.utils.ArgumentParserException;
import com.zootcat.utils.ClassFinder;
import com.zootcat.utils.ZootUtils;

public class ControllerFactory
{
	public static final String CONTROLLER_SUFFIX = "Controller";
	public static final String SCENE_GLOBAL_PARAM = "scene";
	
	Map<String, Class<? extends Controller>> classes = new HashMap<String, Class<? extends Controller>>();
    Map<String, Object> globalParameters = new HashMap<String, Object>();
    
    public ControllerFactory()
    {
    	this(true);
    }
    
    public ControllerFactory(boolean addDefaultControllers)
    {
    	if(addDefaultControllers)
    	{
    		addFromPackage("com.zootcat.controllers", true);
    	}
    }
    
    public void add(Class<? extends Controller> clazz)
    {
    	if(!ClassReflection.isAbstract(clazz) && !ClassReflection.isInterface(clazz))
    	{
    		classes.put(clazz.getSimpleName().toLowerCase(), clazz);
    	}
    }
    
    public void remove(Class<? extends Controller> clazz)
    {
    	classes.remove(clazz.getSimpleName().toLowerCase());
    }
    
    public Class<? extends Controller> get(String name)
    {
        return classes.get(name.toLowerCase());
    }

	public boolean contains(String className) 
	{
		return classes.containsKey(className.toLowerCase());
	}
    
    public int getSize()
    {
        return classes.size();
    }
    
    public void addGlobalParameter(String name, Object param)
    {
        globalParameters.put(name, param);
    }
    
    public void removeGlobalParameter(String paramName)
    {
    	globalParameters.remove(paramName);
    }
    
    public Map<String, Object> getGlobalParameters()
    {
    	return globalParameters;
    }
        
	@SuppressWarnings("unchecked")
	public int addFromPackage(String packageName, boolean includeSubDirs)
	{
		List<Class<?>> found = ClassFinder.find(packageName, includeSubDirs);		
		Predicate<Class<?>> filterByType = cls -> ClassReflection.isAssignableFrom(Controller.class, cls) && !ClassReflection.isInterface(cls);		
		Predicate<Class<?>> filterByName = cls -> cls.getSimpleName().endsWith(CONTROLLER_SUFFIX);		
		List<Class<? extends Controller>> filtered = found.stream()
									   .filter(filterByType)
									   .filter(filterByName)
									   .map(cls -> (Class<? extends Controller>)cls)
									   .collect(Collectors.toList());		
		filtered.forEach(cls -> add(cls));
		return filtered.size();
	}
    
	//TODO add test for it
	public List<Controller> createFromProperties(final MapProperties actorProperties)
	{
		List<Controller> controllers = new ArrayList<Controller>();
		actorProperties.getKeys().forEachRemaining(ctrlName ->
		{			
			try
			{
				Controller controller = normalizeNameAndCreate(ctrlName, actorProperties);
				if(controller != null)
				{
					controllers.add(controller);
				}
			}
			catch (ControllerFactoryException e)
			{
				throw new RuntimeZootException("Error while creating controllers", e);
			}
		});
		return controllers;
	}
	
	public Controller normalizeNameAndCreate(final String controllerName, final MapProperties actorProperties) throws ControllerFactoryException
	{
		String normalizedCtrlName = normalizeControllerName(controllerName); 
		if(!contains(normalizedCtrlName))
		{
			return null;
		}
		try 
		{									
			String[] controllerArguments = actorProperties.get(controllerName, "", String.class).split(",");
			Map<String, Object> arguments = ArgumentParser.parse(controllerArguments);			
			Controller controller = (Controller) create(normalizedCtrlName, arguments);
			return controller;
		}
		catch(ArgumentParserException e)
        {
            throw new ControllerFactoryException(e.getMessage(), e);
        }
	}
    
    public Object create(String name, Map<String, Object> params) throws ControllerFactoryException
    {
        if(!contains(name))
        {
            throw new ControllerFactoryException("No controller for given name: " + name + ".");
        }
        
        try
        {
        	Class<? extends Controller> classToCreate = get(name);
			Controller createdController = (Controller) classToCreate.newInstance();
			assignParamsToClassFields(createdController, params);
			return createdController;
		}
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException e) 
        {
        	throw new ControllerFactoryException(e.getMessage(), e);
		}
    }
    
	private String normalizeControllerName(String controllerName)
	{
		return controllerName.endsWith(CONTROLLER_SUFFIX) ? controllerName : controllerName + CONTROLLER_SUFFIX;
	}
    
    private void assignParamsToClassFields(Object createdClass, Map<String, Object> params) throws IllegalArgumentException, IllegalAccessException 
    {
		List<Field> allClassFields = new ArrayList<Field>();
    	
		Class<?> currentClass = createdClass.getClass();
		while(currentClass != null)
		{
			allClassFields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
			currentClass = currentClass.getSuperclass();
		}
			
		for(Field field : allClassFields)
		{			
			if(!field.isAnnotationPresent(CtrlParam.class))
			{
				continue;
			}
									
			String fieldName = field.getName();
			CtrlParam ctrlParam = field.getAnnotation(CtrlParam.class);								
			Object param = ctrlParam.global() ? globalParameters.get(fieldName) : params.get(fieldName);
			
			if(param == null)
			{
				if(ctrlParam.required() || ctrlParam.global())
				{				
					throw new IllegalArgumentException("Parameter " + field.getName() + " is required for " + createdClass.getClass().getSimpleName());
				}
				continue;
			}
			field.setAccessible(true);
							
			if(field.getType().isEnum())
			{
				@SuppressWarnings("unchecked")
				Enum<?> enumeration = ZootUtils.searchEnum((Class<? extends Enum<?>>)field.getType(), param.toString());					
				field.set(createdClass, enumeration);
			}
			else
			{				
				field.set(createdClass, param);
			}			
		}
	}
}

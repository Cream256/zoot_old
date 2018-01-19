package com.zootcat.controllers.factory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zootcat.controllers.Controller;
import com.zootcat.utils.ZootUtils;

public class ControllerFactory
{
    Map<String, Class<?>> classes = new HashMap<String, Class<?>>();
    Map<String, Object> globalParameters = new HashMap<String, Object>();
    
    public void add(Class<?> clazz)
    {
        classes.put(clazz.getSimpleName().toLowerCase(), clazz);
    }
    
    public Class<?> get(String name)
    {
        return classes.get(name.toLowerCase());
    }

	public boolean contains(String className) 
	{
		return classes.containsKey(removeSuffix(className.toLowerCase()));
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
        
    private String removeSuffix(String nameWithSuffix)
    {
        String[] result = nameWithSuffix.split("_"); 
        return result[0];
    }
    
    public Object create(String name, Map<String, Object> params) throws ControllerFactoryException
    {
        if(!contains(name))
        {
            throw new ControllerFactoryException("No controller for given name: " + name + ".");
        }
        
        String nameWithoutSuffix = removeSuffix(name);
        Class<?> classToCreate = classes.get(nameWithoutSuffix.toLowerCase());
        
        try 
        {
			Controller createdController = (Controller) classToCreate.newInstance();
			assignParamsToClassFields(createdController, params);
			return createdController;
			
		}
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException e) 
        {
        	throw new ControllerFactoryException(e.getMessage(), e);
		}
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

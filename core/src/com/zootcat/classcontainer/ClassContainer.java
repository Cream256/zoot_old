package com.zootcat.classcontainer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassContainer
{
    Map<String, Class<?>> classes = new HashMap<String, Class<?>>();
    Set<Object> globalParameters = new HashSet<Object>();
    
    public void add(Class<?> clazz)
    {
        classes.put(clazz.getSimpleName().toLowerCase(), clazz);
    }
    
    public Class<?> get(String name)
    {
        return classes.get(name.toLowerCase());
    }
    
    public int getSize()
    {
        return classes.size();
    }
    
    public void addGlobalParameter(Object param)
    {
        globalParameters.add(param);
    }
    
    public void removeGlobalParameter(Object param)
    {
    	globalParameters.remove(param);
    }
    
    public Set<Object> getGlobalParameters()
    {
    	return globalParameters;
    }
        
    private String removeSuffix(String nameWithSuffix)
    {
        String[] result = nameWithSuffix.split("_"); 
        return result[0];
    }
    
    public Object create(String name, List<Object> arguments) throws ClassContainerException
    {
        if(!contains(name))
        {
            throw new ClassContainerException("No controller for given name: " + name + ".");
        }
     
        String nameWithoutSuffix = removeSuffix(name);
        Class<?> classToCreate = classes.get(nameWithoutSuffix.toLowerCase());
        Class<?>[] parameterTypes = createParameterTypes(arguments);
        
        Constructor<?> constructor = findConstructor(classToCreate, parameterTypes);
        if(constructor == null)
        {
            throw new ClassContainerException("No class " + nameWithoutSuffix + " constructor found for arguments: " + arguments.toString());
        }
        
        try 
        {            
            List<Object> argsWithGlobals = new ArrayList<Object>(arguments);			   

            int numberOfArguments = constructor.getParameterTypes().length;		    
		    if(numberOfArguments > argsWithGlobals.size())
		    {
		        int startIndex = argsWithGlobals.size();		        
		        for(int argIndex = startIndex; argIndex < numberOfArguments; ++argIndex)
		        {
		            Class<?> currentArgType = constructor.getParameterTypes()[argIndex];		            
		            for(Object globalArg : globalParameters)
		            {
		                if (currentArgType.isAssignableFrom(globalArg.getClass()))
		                {
		                    argsWithGlobals.add(globalArg);
		                    break;
		                }
		            }
		            
		        }
		    }
		    
		    return constructor.newInstance(argsWithGlobals.toArray());
		} 
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) 
		{
        	throw new ClassContainerException(e.getMessage(), e);
		}
    }

    private Constructor<?> findConstructor(Class<?> classToCreate, Class<?>[] parameterTypes)
    {
        Constructor<?> foundCtor = null;
        
        for(Constructor<?> ctor : classToCreate.getConstructors())
        {
            Class<?>[] ctorTypes = ctor.getParameterTypes();
            if(ctorTypes.length < parameterTypes.length)
            {
                continue;
            }
            
            boolean match = true;
            for(int i = 0; i < ctorTypes.length; ++i)
            {
                Class<?> needed = ctorTypes[i];
                if(isGlobalArgumentType(needed))
                {
                    continue;
                }
                
                if(i >= parameterTypes.length)
                {
                    match = false;
                    break;
                }
                
                Class<?> actual = parameterTypes[i];
                if(!ctorTypes[i].isAssignableFrom(parameterTypes[i]))
                {
                    if(ctorTypes[i].isPrimitive())
                    {
                        boolean isInt = int.class.equals(needed) && Integer.class.equals(actual);
                        boolean isFloat = float.class.equals(needed) && Float.class.equals(actual);
                        boolean isDouble = double.class.equals(needed) && Double.class.equals(actual);
                        boolean isBoolean = boolean.class.equals(needed) && Boolean.class.equals(actual);
                        boolean isChar = char.class.equals(needed) && Character.class.equals(actual);
                        boolean isByte = byte.class.equals(needed) && Byte.class.equals(actual);
                        boolean isLong = long.class.equals(needed) && Long.class.equals(actual);
                        boolean isShort = int.class.equals(needed) && Short.class.equals(actual);
                        match = isInt || isFloat || isDouble || isBoolean || isChar || isByte || isLong || isShort;
                    }
                    else
                    {
                        match = false;
                        break;
                    }
                }
            }   //for
            
            if(!match)
            {
                continue;
            }
            
            if(foundCtor == null || foundCtor.getParameterTypes().length < ctor.getParameterTypes().length)
            {
                foundCtor = ctor;
            }
            
        }
        
        return foundCtor;
    }

    private boolean isGlobalArgumentType(Class<?> needed)
    {
        for(Object globalArg : globalParameters)
        {
            Class<?> argClass = globalArg.getClass();
            if(needed.isAssignableFrom(argClass))
            {
                return true;
            }
        }
        
        return false;
    }

    private Class<?>[] createParameterTypes(List<Object> arguments) throws ClassContainerException
    {        
        Class<?>[] types = new Class[arguments.size()];
        
        int index = 0;
        for(Object arg : arguments)
        {
            if(arg == null)
            {
                throw new ClassContainerException("Null passed as one of arguments.");
            }            
            types[index++] = arg.getClass();
        }
        
        return types;
    }

	public boolean contains(String className) 
	{
		return classes.containsKey(removeSuffix(className.toLowerCase()));
	}
    
}

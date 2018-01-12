package com.zootcat.utils;

import java.util.ArrayList;
import java.util.List;

import com.zootcat.scene.ZootActor;

public class ArgumentParser
{        
    public static List<Object> parse(String[] arguments, ZootActor thisActor) throws ArgumentParserException
    {
        List<Object> result = new ArrayList<Object>();
        
        if(arguments.length == 1 && arguments[0].length() == 0)
        {
        	return result;
        }
        
        for(String arg : arguments)
        {
            String trimmedArg = arg.trim();
            if(trimmedArg.isEmpty())
            {
                throw new ArgumentParserException("Empty argument!");
            }
                      
            //Integer argument
            if(trimmedArg.matches("[-+]?[0-9]+"))
            {
                result.add(Integer.valueOf(trimmedArg));
            }
            
            //Float argument
            else if(trimmedArg.matches("[-+]?[0-9]*\\.[0-9]+f"))
            { 
                result.add(Float.valueOf(trimmedArg));
            }
            
            //Double argument
            else if(trimmedArg.matches("[-+]?[0-9]*\\.[0-9]+d"))
            { 
                result.add(Double.valueOf(trimmedArg));
            }
            
            //Boolean
            else if(trimmedArg.equalsIgnoreCase("true") || trimmedArg.equalsIgnoreCase("false"))
            {
            	result.add(Boolean.valueOf(trimmedArg));
            }
                        
            //This ZootActor
            else if(trimmedArg.equalsIgnoreCase("%this"))
            {
                if(thisActor == null)
                {
                    throw new ArgumentParserException("No 'this' pointer have been assigned.");
                }
                result.add(thisActor);
            }
            
            //else treat as string argument
            else
            {
                result.add(new String(ZootUtils.unquoteString(trimmedArg)));
            } 
        }

        return result;
    }
}

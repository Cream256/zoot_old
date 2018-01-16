package com.zootcat.textdata;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.zootcat.exceptions.ZootException;

public class TextDataFile
{
    private File file;
    
    public TextDataFile(File file) throws ZootException
    {
        this.file = file;
        if(!file.exists())
        {
        	throw new ZootException("File " + file.getName() + " does not exist!");
        }
    }
    
    public List<TextDataSection> readSections(String startToken, String endToken) throws ZootException
    {
        try
        {
            TextDataSection currentDataSection = null;
            List<TextDataSection> result = new ArrayList<TextDataSection>();
            
            List<String> lines = Files.readAllLines(Paths.get(file.getAbsolutePath()), Charset.forName("UTF-8"));
            for(String line : lines)
            {
                line = line.trim();
                if(line.isEmpty() || line.startsWith("#"))
                {
                    continue;
                }   
                else if(line.equalsIgnoreCase(startToken))
                {
                    currentDataSection = new TextDataSection();
                    currentDataSection.setStartToken(startToken);
                    currentDataSection.setEndToken(endToken);
                    currentDataSection.setFile(file);
                }
                else if(line.equalsIgnoreCase(endToken))
                {
                    result.add(currentDataSection);
                    currentDataSection = null;
                }
                else
                {
                    String[] lineData = line.split("=");
                    if(lineData.length == 2 && currentDataSection != null)
                    {                    
                        String key = lineData[0].trim();
                        String value = lineData[1].trim();                    
                        currentDataSection.set(key, value);
                    }
                }
            }
            return result;
        }
        catch(IOException e)
        {
            throw new ZootException(e.getMessage(), e);
        }
    }
    
	public Vector2[] readVectors(String value) 
    {
    	if(value.trim().isEmpty())
    	{
    		return new Vector2[0];
    	}
    	
    	String[] pairs = value.split(";");
    	Vector2[] result = new Vector2[pairs.length];
    	for(int i = 0; i < pairs.length; ++i)
    	{
    		String[] numbers = pairs[i].split(",");
    		if(numbers.length != 2)
    		{
    			continue;
    		}
    		int x = Integer.valueOf(numbers[0].trim());
    		int y = Integer.valueOf(numbers[1].trim());    		
    		result[i] = new Vector2(x, y);    		
    	}
		return result;
	}
    
}

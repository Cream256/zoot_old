package com.zootcat.textdata;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextDataSection
{
    private File file;
    private String startToken = "";  
    private String endToken = "";    
    private Map<String, String> valueMap = new HashMap<String, String>();
    
    public String getStartToken()
    {
        return startToken;
    }

    public void setStartToken(String startToken)
    {
        this.startToken = startToken;
    }

    public String getEndToken()
    {
        return endToken;
    }

    public void setEndToken(String endToken)
    {
        this.endToken = endToken;
    }
    
    public String getValue(String key, String defaultValue)
    {
        if(valueMap.containsKey(key.toLowerCase()))
        {
            return valueMap.get(key.toLowerCase());
        }
        return defaultValue;
    }
    
    public void setValue(String key, String value)
    {
        valueMap.put(key.toLowerCase(), value);
    }
    
    public List<String> getAllValues()
    {
        return new ArrayList<String>(valueMap.values());
    }
    
    public List<String> getAllKeys()
    {
        return new ArrayList<String>(valueMap.keySet());
    }

    public File getFile()
    {
        return file;
    }

    public void setFile(File file)
    {
        this.file = file;
    }
}

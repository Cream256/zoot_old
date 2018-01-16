package com.zootcat.textdata;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class TextDataSectionTest
{
    private TextDataSection section;
    
    @Before
    public void setup()
    {
        section = new TextDataSection();
    }
    
    @Test
    public void ctorTest()
    {
        assertTrue(section.getAllKeys().isEmpty());
        assertTrue(section.getAllValues().isEmpty());
        assertEquals("", section.getStartToken());
        assertEquals("", section.getEndToken());
        assertEquals(null, section.getFile());
        assertEquals("", section.getValue("xyz", ""));
    }
    
    @Test
    public void accessorsTest()
    {
        section.setEndToken("endToken");
        assertEquals("endToken", section.getEndToken());
        
        section.setStartToken("startToken");
        assertEquals("startToken", section.getStartToken());
        
        File file = new File(".");
        section.setFile(file);
        assertEquals(file, section.getFile());
        
        section.setValue("abc", "123");
        assertEquals("123", section.getValue("abc", ""));
        assertEquals("321", section.getValue("def", "321"));
    }
    
}

package com.zootcat.textdata;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.Test;

import com.zootcat.exceptions.ZootException;

public class TextDataFileTest
{
    @Test(expected=ZootException.class)
    public void shouldThrowErrorWhenFileDoesNotExist() throws ZootException
    {
        File file = new File("");    	
    	new TextDataFile(file);
    }
        
    @Test
    public void readSectionsTest() throws ZootException
    {
    	String textDataFilePath = getClass().getClassLoader()
    							  	        .getResource("testResources/textdata/TextDataFile.txt")
    							  	        .getPath();
        TextDataFile file = new TextDataFile(new File(textDataFilePath));
        
        List<TextDataSection> result = file.readSections(":SectionStart", ":SectionEnd");
        
        assertEquals(2, result.size());
        assertEquals(":SectionStart", result.get(0).getStartToken());
        assertEquals(":SectionEnd", result.get(0).getEndToken());
        assertEquals("CatName", result.get(0).getValue("cat", "error"));
        assertEquals("DogName", result.get(0).getValue("dog", "error"));
        
        assertEquals(":SectionStart", result.get(1).getStartToken());
        assertEquals(":SectionEnd", result.get(1).getEndToken());
        assertEquals("Tom", result.get(1).getValue("cat", "error"));
        assertEquals("Jerry", result.get(1).getValue("mouse", "error"));
        
        result = file.readSections(":OtherSectionStart", ":OtherSectionEnd");
        assertEquals(1, result.size());
    }
    
}

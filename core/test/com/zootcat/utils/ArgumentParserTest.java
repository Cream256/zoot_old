package com.zootcat.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.zootcat.classcontainer.ClassContainerException;
import com.zootcat.scene.ZootActor;
import com.zootcat.utils.ArgumentParser;
import com.zootcat.utils.ArgumentParserException;
import com.zootcat.utils.ZootUtils;

public class ArgumentParserTest
{
    private static final String INT_ARG = "123";
    private static final String POSITIVE_INT_ARG = "+123";
    private static final String NEGATIVE_INT_ARG = "-123";
    private static final String FLOAT_ARG = "0.123f";
    private static final String FLOAT_ARG_NOZERO = ".123f";
    private static final String FLOAT_ARG_NEGATIVE = "-.123f";
    private static final String DOUBLE_ARG = "0.123d";
    private static final String STRING_ARG = "'String argument'";
    private static final String STRING_WHITESPACES = " ";
    private static final String STRING_SPACES = "' '";
	private static final String BOOL_ARG_TRUE = "true";
	private static final String BOOL_ARG_FALSE = "false";
                
    @Test
    public void noArgsTest() throws ArgumentParserException
    {
        List<Object> result = ArgumentParser.parse(new String[0], null);        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void parseThisZootActorArgTest() throws ArgumentParserException
    {
        List<Object> result = ArgumentParser.parse(new String[]{"%this"}, new ZootActor()); 
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof ZootActor);
    }
    
    @Test
    public void booleanTrueTest() throws ArgumentParserException
    {
    	List<Object> result = ArgumentParser.parse(new String[]{BOOL_ARG_TRUE}, null);        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof Boolean);
        assertEquals(BOOL_ARG_TRUE.toString(), result.get(0).toString());
    }
    
    @Test
    public void booleanFalseTest() throws ArgumentParserException
    {
    	List<Object> result = ArgumentParser.parse(new String[]{BOOL_ARG_FALSE}, null);        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof Boolean);
        assertEquals(BOOL_ARG_FALSE.toString(), result.get(0).toString());
    }
    
    @Test(expected=ArgumentParserException.class)
    public void emptyStringArgTest() throws ArgumentParserException
    {
    	ArgumentParser.parse(new String[]{STRING_WHITESPACES}, null);        
    }
    
    @Test
    public void stringArgTest() throws ArgumentParserException
    {                
        List<Object> result = ArgumentParser.parse(new String[]{STRING_ARG}, null);        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof String);
        assertEquals(ZootUtils.unquoteString(STRING_ARG), result.get(0).toString());
    }

    @Test
    public void stringWithSpacesTest() throws ArgumentParserException
    {                
        List<Object> result = ArgumentParser.parse(new String[]{STRING_SPACES}, null);
        assertNotNull(result);        
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof String);
        assertEquals(ZootUtils.unquoteString(STRING_SPACES.toString()), result.get(0).toString());
    }   
                    
    @Test
    public void integerArgTest() throws ArgumentParserException
    {
        List<Object> result = ArgumentParser.parse(new String[]{INT_ARG}, null);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof Integer);
        assertEquals(INT_ARG, result.get(0).toString());
    }
    
    @Test
    public void negativeIntegerArgTest() throws ArgumentParserException
    {
        List<Object> result = ArgumentParser.parse(new String[]{NEGATIVE_INT_ARG}, null);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof Integer);
        assertEquals(NEGATIVE_INT_ARG, result.get(0).toString());
    }
    
    @Test
    public void positiveIntegerArgTest() throws ArgumentParserException
    {
        List<Object> result = ArgumentParser.parse(new String[]{POSITIVE_INT_ARG}, null);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof Integer);
        assertEquals(POSITIVE_INT_ARG.substring(1, POSITIVE_INT_ARG.length()), result.get(0).toString());
    }
        
    @Test
    public void floatArgTest() throws ArgumentParserException
    {
        List<Object> result = ArgumentParser.parse(new String[]{FLOAT_ARG}, null);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof Float);
        assertEquals(FLOAT_ARG.substring(0, FLOAT_ARG.length() - 1), result.get(0).toString());
    }
    
    @Test
    public void floatNoLeadingZeroArgTest() throws ArgumentParserException
    {
        List<Object> result = ArgumentParser.parse(new String[]{FLOAT_ARG_NOZERO}, null);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof Float);
        assertEquals("0" + FLOAT_ARG_NOZERO.substring(0, FLOAT_ARG_NOZERO.length() - 1), result.get(0).toString());
    }
    
    @Test
    public void floatNegativeArgTest() throws ArgumentParserException
    {
        List<Object> result = ArgumentParser.parse(new String[]{FLOAT_ARG_NEGATIVE}, null);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof Float);
        assertEquals("-" + FLOAT_ARG.substring(0, FLOAT_ARG_NEGATIVE.length() - 1), result.get(0).toString());
    }
    
    @Test
    public void doubleArgTest() throws ArgumentParserException
    {
        List<Object> result = ArgumentParser.parse(new String[]{DOUBLE_ARG}, null);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof Double);
        assertEquals(DOUBLE_ARG.substring(0, DOUBLE_ARG.length() - 1), result.get(0).toString());
    }

    @Test
    public void ActorTypeArgumentShouldBeTreatedAsStringArgumenTest() throws ArgumentParserException
    {               
        List<Object> result = ArgumentParser.parse(new String[]{"*player"}, null);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("*player", result.get(0));        
    }
        
    @Test
    public void ActorInstanceArgumentShouldBeTreatedAsStringArgumentTest() throws ArgumentParserException
    {                
        List<Object> result = ArgumentParser.parse(new String[]{"@player1"}, null);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("@player1", result.get(0));
    }
              
    @Test
    public void multiplyArgumentsTest() throws ArgumentParserException
    {        
        String[] arguments = {"@player1", "@little bunny", "'DUH'", "3.14f", "6.14d"};
        List<Object> result = ArgumentParser.parse(arguments, null); 
        assertNotNull(result);
        assertEquals(5, result.size());
        assertEquals("@player1", result.get(0));
        assertEquals("@little bunny", result.get(1));
        assertEquals("DUH", result.get(2));
        assertEquals(Float.valueOf(3.14f), result.get(3));
        assertEquals(Double.valueOf(6.14), result.get(4));
    }
               
    @Test(expected=ArgumentParserException.class)
    public void noThisZootActorShouldThrow() throws ArgumentParserException
    {
    	ArgumentParser.parse(new String[]{"%this"}, null);
    }
    
    @Test
    public void classContainerExceptionTest()
    {
        Exception e = new ClassContainerException("Message", null);
        assertEquals("Message", e.getMessage());
        assertNull(e.getCause());
    }
}


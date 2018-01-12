package com.zootcat.classcontainer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zootcat.classcontainer.ClassContainer;
import com.zootcat.classcontainer.ClassContainerException;
import com.zootcat.classcontainer.mocks.EmptyConstructorMock;
import com.zootcat.classcontainer.mocks.GlobalConstructorMock;
import com.zootcat.classcontainer.mocks.ManyConstructorsMock;
import com.zootcat.classcontainer.mocks.MixedConstructorMock;
import com.zootcat.classcontainer.mocks.PrimitiveConstructorMock;
import com.zootcat.scene.ZootActor;

public class ClassContainerTest
{
    @Test
    public void defaultConstructorTest() throws ClassContainerException
    {
        //given
        ClassContainer container = new ClassContainer();
        
        //then
        assertEquals(0, container.getSize());
        
        //when
        container.add(EmptyConstructorMock.class);
        
        //then
        assertEquals(1, container.getSize());
        
        //when
        Object created = container.create(EmptyConstructorMock.class.getSimpleName(), new ArrayList<Object>());        
        
        //then
        assertTrue(created != null);
        assertTrue(created instanceof EmptyConstructorMock);
        
        assertEquals(EmptyConstructorMock.class, container.get(EmptyConstructorMock.class.getSimpleName()));
        
    }
      
    @Test(expected=ClassContainerException.class)
    public void unknownContructorTest() throws ClassContainerException
    {
        //given
        ClassContainer container = new ClassContainer();
        List<Object> arguments = new ArrayList<Object>();
        arguments.add("This won't work");
        
        //when
        container.add(PrimitiveConstructorMock.class);
        
        //then
        container.create(PrimitiveConstructorMock.class.getSimpleName(), arguments);
    }
    
    @Test
    public void primitiveConstructorTest() throws ClassContainerException
    {
        //given
        ClassContainer container = new ClassContainer();
        List<Object> arguments = new ArrayList<Object>();
        arguments.add(1);
        arguments.add(2.2f);
        arguments.add(3.3);
        
        //when
        container.add(PrimitiveConstructorMock.class);
        Object created = container.create(PrimitiveConstructorMock.class.getSimpleName(), arguments);
        
        //then
        assertTrue(created != null);
        assertTrue(created instanceof PrimitiveConstructorMock);
        assertEquals(6.5f, ((PrimitiveConstructorMock)created).value, 0.01f);
        assertEquals(PrimitiveConstructorMock.class, container.get(PrimitiveConstructorMock.class.getSimpleName()));
    }
    
    @Test
    public void manyConstructorsTest() throws ClassContainerException
    {
        //given
        ClassContainer container = new ClassContainer();
        
        //when
        container.add(ManyConstructorsMock.class);
        Object created = container.create(ManyConstructorsMock.class.getSimpleName(), new ArrayList<Object>());
        
        //then
        assertTrue(created != null);
        assertTrue(created instanceof ManyConstructorsMock);     
        assertEquals(ManyConstructorsMock.class, container.get(ManyConstructorsMock.class.getSimpleName()));
        
        //when
        created = container.create(ManyConstructorsMock.class.getSimpleName(), new ArrayList<Object>(new Integer(1)));
        
        //then
        assertTrue(created != null);
        assertTrue(created instanceof ManyConstructorsMock);
        assertEquals(1, ((ManyConstructorsMock)created).value);
        
        //when
        List<Object> arguments = new ArrayList<Object>();
        arguments.add(new Integer(1));
        arguments.add(new Integer(2));
        created = container.create(ManyConstructorsMock.class.getSimpleName(), arguments);
        
        //then
        assertTrue(created != null);
        assertTrue(created instanceof ManyConstructorsMock);
        assertEquals(3, ((ManyConstructorsMock)created).value);
    }
    
    
    @Test
    public void mixedConstructorTest() throws ClassContainerException
    {
        //given
        ClassContainer container = new ClassContainer();
        ZootActor actor = new ZootActor();
        List<Object> arguments = new ArrayList<Object>();
        arguments.add(new String("test"));
        arguments.add(actor);
        arguments.add(3.14f);
        arguments.add(10);
        
        //when
        container.add(MixedConstructorMock.class);
        Object created = container.create(MixedConstructorMock.class.getSimpleName(), arguments);
        
        //then
        assertTrue(created != null);
        assertTrue(created instanceof MixedConstructorMock);
        assertEquals(MixedConstructorMock.class, container.get(MixedConstructorMock.class.getSimpleName()));
        
        MixedConstructorMock stub = (MixedConstructorMock)created;
        assertEquals("test", stub.name);
        assertEquals(actor, stub.actor);
        assertEquals(3.14f, stub.value, 0.01f);
        assertEquals(10, stub.points);
    }
    
    @Test(expected = Exception.class)
    public void mixedConstructorNullArgumentTest() throws ClassContainerException
    {
        //given
        ClassContainer container = new ClassContainer();
        List<Object> arguments = new ArrayList<Object>();
        arguments.add(new String("test"));
        arguments.add(null);
        arguments.add(3.14f);
        arguments.add(10);
        
        //when
        container.add(MixedConstructorMock.class);
        
        //then
        container.create(MixedConstructorMock.class.getSimpleName(), arguments);
    }
    
    @Test
    public void constructorShouldFindDefaultCtorWhileClassHasGlobalParamsPresentTest() throws ClassContainerException
    {
        //given
        ClassContainer container = new ClassContainer();       
        
        //when
        container.add(GlobalConstructorMock.class);
        
        //then
        Object result = container.create(GlobalConstructorMock.class.getSimpleName(), new ArrayList<Object>());
        assertTrue(result instanceof GlobalConstructorMock);
        assertEquals("default ctor should be called", 0, ((GlobalConstructorMock)result).value);
        assertEquals(GlobalConstructorMock.class, container.get(GlobalConstructorMock.class.getSimpleName()));
    }
    
    @Test
    public void constructorShouldFindFirstCtorWhileClassHasGlobalParamsPresentTest() throws ClassContainerException
    {
        //given
        ClassContainer container = new ClassContainer();       
        List<Object> arguments = new ArrayList<Object>();
        
        //when
        arguments.add(new Integer(1));
        container.add(GlobalConstructorMock.class);
        
        //then
        Object result = container.create(GlobalConstructorMock.class.getSimpleName(), arguments);
        assertTrue(result instanceof GlobalConstructorMock);
        assertEquals("ctor with one normal param should be called", 1, ((GlobalConstructorMock)result).value);
        assertEquals(GlobalConstructorMock.class, container.get(GlobalConstructorMock.class.getSimpleName()));
    }
    
    @Test
    public void shouldRecognizeNamesWithSuffixTest() throws ClassContainerException
    {
        //given
        ClassContainer container = new ClassContainer();
        List<Object> arguments = new ArrayList<Object>();
        
        //when
        container.add(EmptyConstructorMock.class);
        
        //then        
        assertTrue(container.contains(EmptyConstructorMock.class.getSimpleName()));
        assertTrue(container.contains(EmptyConstructorMock.class.getSimpleName()+"_0123"));
        assertEquals(EmptyConstructorMock.class, container.get(EmptyConstructorMock.class.getSimpleName()));
        
        Object result = container.create(EmptyConstructorMock.class.getSimpleName(), arguments);       
        assertTrue(result instanceof EmptyConstructorMock);
        
        Object result2 = container.create(EmptyConstructorMock.class.getSimpleName()+"_2", arguments);
        assertTrue(result2 instanceof EmptyConstructorMock);
    }
    
    @Test(expected=ClassContainerException.class)
    public void shouldThrowOnInvalidClassName() throws ClassContainerException
    {
        ClassContainer container = new ClassContainer();
        container.create("ClassNotInContainer", new ArrayList<Object>());
    }
    
    @Test
    public void addGlobalParameterShouldNotAddDuplicatesTest()
    {
    	//given
    	Integer integerParam = new Integer(1);
    	ClassContainer container = new ClassContainer();
    	
    	//when
    	container.addGlobalParameter(integerParam);
    	container.addGlobalParameter(integerParam);
    	
    	//then
    	assertEquals(1, container.getGlobalParameters().size());
    	assertTrue(container.getGlobalParameters().contains(integerParam));
    }
    
    @Test
    public void addGlobalParameterTest()
    {
    	//given    	
    	Float floatParam = new Float(1.23f);    	
    	Integer integerParam = new Integer(1);
    	Integer secondIntegerParam = new Integer(2);
    	ClassContainer container = new ClassContainer();
    	
    	//then
    	assertEquals(0, container.getGlobalParameters().size());
    	
    	//when
    	container.addGlobalParameter(integerParam);
    	container.addGlobalParameter(floatParam);
    	container.addGlobalParameter(secondIntegerParam);
    	
    	//then
    	assertEquals(3, container.getGlobalParameters().size());
    	assertTrue(container.getGlobalParameters().contains(integerParam));
    	assertTrue(container.getGlobalParameters().contains(secondIntegerParam));
    	assertTrue(container.getGlobalParameters().contains(floatParam));
    }
    
    @Test
    public void removeGlobalParameterTest()
    {
    	//given    	
    	Float floatParam = new Float(1.23f);    	
    	Integer integerParam = new Integer(1);
    	Integer secondIntegerParam = new Integer(2);
    	ClassContainer container = new ClassContainer();
    	
    	//then
    	assertEquals(0, container.getGlobalParameters().size());
    	
    	//when
    	container.addGlobalParameter(integerParam);
    	container.addGlobalParameter(floatParam);
    	container.addGlobalParameter(secondIntegerParam);
    	
    	//then
    	assertEquals(3, container.getGlobalParameters().size());

    	//when
    	container.removeGlobalParameter(floatParam);
    	
    	//then
    	assertEquals(2, container.getGlobalParameters().size());
    	assertTrue(container.getGlobalParameters().contains(integerParam));
    	assertTrue(container.getGlobalParameters().contains(secondIntegerParam));
    	assertFalse(container.getGlobalParameters().contains(floatParam));
    	
    	//when
    	container.removeGlobalParameter(integerParam);
    	container.removeGlobalParameter(secondIntegerParam);
    	
    	//then
    	assertEquals(0, container.getGlobalParameters().size());
    }
        
}

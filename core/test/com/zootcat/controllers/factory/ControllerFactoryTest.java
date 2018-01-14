package com.zootcat.controllers.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.controllers.factory.mocks.DerivedControllerMock;
import com.zootcat.controllers.factory.mocks.EmptyControllerMock;
import com.zootcat.controllers.factory.mocks.EnumParam;
import com.zootcat.controllers.factory.mocks.EnumParamControllerMock;
import com.zootcat.controllers.factory.mocks.GlobalParamControllerMock;
import com.zootcat.controllers.factory.mocks.PrimitiveParamsControllerMock;
import com.zootcat.controllers.factory.mocks.RequiredParamControllerMock;

public class ControllerFactoryTest
{
    private ControllerFactory factory;
	
	@Before
    public void setup()
    {
		factory = new ControllerFactory();
    }
	
	@Test
    public void controllerWithNoParamsTest() throws ControllerFactoryException
    {
        //given
        ControllerFactory factory = new ControllerFactory();
        
        //then
        assertEquals(0, factory.getSize());
        
        //when
        factory.add(EmptyControllerMock.class);
        
        //then
        assertEquals(1, factory.getSize());
        
        //when
        Object created = factory.create(EmptyControllerMock.class.getSimpleName(), new HashMap<String, Object>());        
        
        //then
        assertTrue(created instanceof EmptyControllerMock);
        assertEquals(EmptyControllerMock.class, factory.get(EmptyControllerMock.class.getSimpleName()));
    }
    
	@Test
	public void controllerWithPrimitiveParamsTest() throws ControllerFactoryException
	{
		//given
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("intParam", 1);
		params.put("floatParam", 2.0f);
		params.put("doubleParam", 3.0);
		params.put("stringParam", "test");
		params.put("booleanParam", true);
		
		//when
		factory.add(PrimitiveParamsControllerMock.class);
		Object created = factory.create(PrimitiveParamsControllerMock.class.getSimpleName(), params);
		
		//then
		assertNotNull(created);
		assertTrue(created instanceof PrimitiveParamsControllerMock);
		
		PrimitiveParamsControllerMock ctrl = (PrimitiveParamsControllerMock)created;
		assertEquals(1, ctrl.getIntParam());
		assertEquals(2.0f, ctrl.getFloatParam(), 0.0f);
		assertEquals(3.0, ctrl.getDoubleParam(), 0.0f);
		assertEquals("test", ctrl.getStringParam());
		assertEquals(true, ctrl.getBooleanParam());
	}
    
	@Test
	public void requiredParamShouldBeProcessedCorrectlyTest() throws ControllerFactoryException
	{
		//given
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("optional", 1);
		params.put("required", 2);
		
		//when
		factory.add(RequiredParamControllerMock.class);
		Object created = factory.create(RequiredParamControllerMock.class.getSimpleName(), params);
		
		//then
		assertNotNull(created);
		assertTrue(created instanceof RequiredParamControllerMock);
		
		RequiredParamControllerMock ctrl = (RequiredParamControllerMock)created;
		assertEquals(1, ctrl.getOptional());
		assertEquals(2, ctrl.getRequired());
	}
	
	@Test(expected = ControllerFactoryException.class)
	public void requiredParamShouldThrowIfNotPresentTest() throws ControllerFactoryException
	{
		//given
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("optional", 1);
		
		//when
		factory.add(RequiredParamControllerMock.class);
		
		//then
		factory.create(RequiredParamControllerMock.class.getSimpleName(), params);
	}
    
	@Test
	public void globalParamShouldBeProcessedCorrectlyTest() throws ControllerFactoryException
	{
		//given
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("local", 1);
		
		//when
		factory.add(GlobalParamControllerMock.class);
		factory.addGlobalParameter("global", 2);
		Object created = factory.create(GlobalParamControllerMock.class.getSimpleName(), params);
		
		//then
		assertNotNull(created);
		assertTrue(created instanceof GlobalParamControllerMock);
		
		GlobalParamControllerMock ctrl = (GlobalParamControllerMock)created;
		assertEquals(1, ctrl.getLocal());
		assertEquals(2, ctrl.getGlobal());		
	}
	
	@Test(expected = ControllerFactoryException.class)
	public void globalParamShouldThrowIfNotPresentTest() throws ControllerFactoryException
	{
		//given
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("local", 1);
		
		//when
		factory.add(GlobalParamControllerMock.class);
		factory.create(GlobalParamControllerMock.class.getSimpleName(), params);		
	}
    
  
    @Test(expected=ControllerFactoryException.class)
    public void shouldThrowOnInvalidClassName() throws ControllerFactoryException
    {
        ControllerFactory factory = new ControllerFactory();
        factory.create("ClassNotInContainer", new HashMap<String, Object>());
    }
    
    @Test
    public void addGlobalParameterShouldNotAddDuplicatesTest()
    {
    	//given
    	Integer integerParam = new Integer(1);
    	
    	//when
    	factory.addGlobalParameter("Int", integerParam);
    	factory.addGlobalParameter("Int", integerParam);
    	
    	//then
    	assertEquals(1, factory.getGlobalParameters().size());
    	assertTrue(factory.getGlobalParameters().containsKey("Int"));
    }
    
    @Test
    public void removeGlobalParameterTest()
    {
    	//when
    	factory.addGlobalParameter("Int", 1);
    	factory.addGlobalParameter("Bool", true);
    	
    	//then
    	assertEquals(2, factory.getGlobalParameters().size());
    	
    	//when
    	factory.removeGlobalParameter("Int");
    	
    	//then
    	assertEquals(1, factory.getGlobalParameters().size());
    	assertEquals(true, factory.getGlobalParameters().get("Bool"));
    	
    	//when
    	factory.removeGlobalParameter("Bool");
    	
    	//then
    	assertEquals(0, factory.getGlobalParameters().size());
    }
    
    @Test
    public void enumParameterTest() throws ControllerFactoryException
    {
		//given
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("enumParam", EnumParam.ENUM_VALUE_2.toString());
		
		//when
		factory.add(EnumParamControllerMock.class);
		Object created = factory.create(EnumParamControllerMock.class.getSimpleName(), params);
		
		//then
		assertNotNull(created);
		assertTrue(created instanceof EnumParamControllerMock);
		
		EnumParamControllerMock ctrl = (EnumParamControllerMock)created;
		assertEquals(EnumParam.ENUM_VALUE_2, ctrl.getEnum());    	
    }
    
    @Test
    public void nullParameterShouldBeSetToDefaultTest() throws ControllerFactoryException
    {
		//given
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("intParam", null);
		
		//when
		factory.add(PrimitiveParamsControllerMock.class);
		Object created = factory.create(PrimitiveParamsControllerMock.class.getSimpleName(), params);
		
		//then
		assertNotNull(created);
		assertTrue(created instanceof PrimitiveParamsControllerMock);
		
		PrimitiveParamsControllerMock ctrl = (PrimitiveParamsControllerMock)created;
		assertEquals(0, ctrl.getIntParam());
    }
    
    @Test
    public void parameterInheritanceTest() throws ControllerFactoryException
    {
		//given
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("baseParam", 1);
		params.put("derivedParam", 2);
		
		//when
		factory.add(DerivedControllerMock.class);
		Object created = factory.create(DerivedControllerMock.class.getSimpleName(), params);
		
		//then
		assertNotNull(created);
		assertTrue(created instanceof DerivedControllerMock);
		
		DerivedControllerMock ctrl = (DerivedControllerMock)created;
		assertEquals(1, ctrl.getBaseParam());
		assertEquals(2, ctrl.getDerivedParam());
    }
}
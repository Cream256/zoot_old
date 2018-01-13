package com.zootcat.input;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import com.zootcat.exceptions.RuntimeZootException;

public class ZootBindableInputProcessorTest 
{
	@Test
	public void hasBindingTest()
	{
		//given
		ZootInputCommand mockCommand = mock(ZootInputCommand.class);		
		ZootBindableInputProcessor processor = new ZootBindableInputProcessor();
		
		//then
		assertFalse(processor.hasBinding(0));
		assertFalse(processor.hasBinding(1));
		
		//when
		processor.bindCommand(0, mockCommand);
		
		//then
		assertTrue(processor.hasBinding(0));
		assertFalse(processor.hasBinding(1));
	}
	
	@Test
	public void processorShouldProperlyHandleAllCommandMethodsTest()
	{
		//given
		ZootInputCommand mockCommand = mock(ZootInputCommand.class);		
		ZootBindableInputProcessor processor = new ZootBindableInputProcessor();
		
		//when
		processor.bindCommand(0, mockCommand);		
		
		//then
		verify(mockCommand, times(0)).executeOnDown();
		verify(mockCommand, times(0)).executeOnRelease();
		verify(mockCommand, times(0)).executeOnPress();
		
		//when
		processor.keyDown(0);
		
		//then
		verify(mockCommand, times(1)).executeOnDown();
		verify(mockCommand, times(0)).executeOnRelease();
		verify(mockCommand, times(0)).executeOnPress();
		
		//when
		processor.keyUp(0);
		
		//then
		verify(mockCommand, times(1)).executeOnDown();
		verify(mockCommand, times(1)).executeOnRelease();
		verify(mockCommand, times(1)).executeOnPress();
		
		//when
		processor.keyDown(1);
		processor.keyUp(1);
		
		//then nothing changes
		verify(mockCommand, times(1)).executeOnDown();
		verify(mockCommand, times(1)).executeOnRelease();
		verify(mockCommand, times(1)).executeOnPress();
	}
	
	@Test
	public void processPressedKeysTest()
	{
		//given
		ZootInputCommand mockCommand1 = mock(ZootInputCommand.class);
		ZootInputCommand mockCommand2 = mock(ZootInputCommand.class);
		ZootBindableInputProcessor processor = new ZootBindableInputProcessor();
		
		//when
		processor.bindCommand(1, mockCommand1);
		processor.bindCommand(2, mockCommand2);
		processor.processPressedKeys();
		
		//then
		verify(mockCommand1, times(0)).executeOnDown();
		verify(mockCommand2, times(0)).executeOnDown();
		
		//when
		processor.keyDown(1);
		processor.keyDown(2);
		processor.processPressedKeys();
		
		//then
		verify(mockCommand1, times(2)).executeOnDown();
		verify(mockCommand2, times(2)).executeOnDown();
		
		//when
		processor.keyUp(1);
		processor.processPressedKeys();
		
		//then
		verify(mockCommand1, times(2)).executeOnDown();
		verify(mockCommand2, times(3)).executeOnDown();
	}
	
	@Test(expected = RuntimeZootException.class)
	public void bindCommandShouldThrowIfKeycodeAlreadyBindedTest()
	{
		//given
		ZootBindableInputProcessor processor = new ZootBindableInputProcessor();
		processor.bindCommand(0, mock(ZootInputCommand.class));
		
		//then
		processor.hasBinding(0);
		
		//when
		processor.bindCommand(0, mock(ZootInputCommand.class));
	}
	
	@Test
	public void bindDownCommandTest()
	{
		//given		
		ZootBindableInputProcessor processor = new ZootBindableInputProcessor();
		
		//when
		processor.bindDown(0, () -> true);
		
		//then
		assertTrue(processor.keyDown(0));
		assertFalse(processor.keyUp(0));
		assertFalse(processor.keyTyped('0'));
	}
	
	@Test(expected = RuntimeZootException.class)
	public void bindDownShouldThrowIfKeycodeAlreadyBindedTest()
	{
		//given		
		ZootBindableInputProcessor processor = new ZootBindableInputProcessor();
		
		//when
		processor.bindDown(0, () -> true);
		
		//then
		processor.hasBinding(0);
		
		//when
		processor.bindDown(0, () -> true);
	}
	
	@Test
	public void bindUpCommandTest()
	{
		//given		
		ZootBindableInputProcessor processor = new ZootBindableInputProcessor();
		
		//when
		processor.bindUp(0, () -> true);
		
		//then
		assertTrue(processor.keyUp(0));
		assertFalse(processor.keyDown(0));
		assertFalse(processor.keyTyped('0'));
	}
	
	@Test(expected = RuntimeZootException.class)
	public void bindUpShouldThrowIfKeycodeAlreadyBindedTest()
	{
		//given		
		ZootBindableInputProcessor processor = new ZootBindableInputProcessor();
		
		//when
		processor.bindUp(0, () -> true);
		
		//then
		processor.hasBinding(0);
		
		//when
		processor.bindUp(0, () -> true);
	}
	
}

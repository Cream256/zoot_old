package com.zootcat.input;

import static org.mockito.Mockito.*;

import org.junit.Test;

public class ZootBindableInputProcessorTest 
{
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
}

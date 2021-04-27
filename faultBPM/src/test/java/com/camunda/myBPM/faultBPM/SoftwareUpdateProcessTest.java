package com.camunda.myBPM.faultBPM;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.Test;

public class SoftwareUpdateProcessTest {

	@Test
	public void verifyThatTheDelegateSetsVariable() {
		SoftwareUpdateProcess delegate = new SoftwareUpdateProcess();
		
		DelegateExecution mockExecution = mock(DelegateExecution.class);
		
		try {
			delegate.execute(mockExecution);
		}catch (Exception e) {
			e.printStackTrace();
			fail("throw exception: " + e.getMessage());
		}
		verify(mockExecution, times(1)).setVariable(eq("updateOK"), any(Boolean.class));
	}
	
}
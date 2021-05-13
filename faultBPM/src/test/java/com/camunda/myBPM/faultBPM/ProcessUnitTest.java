package com.camunda.myBPM.faultBPM;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.init;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.processEngine;

import org.apache.ibatis.logging.LogFactory;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.assertions.TaskAssert;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRuleBuilder;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test case starting an in-memory database-backed Process Engine.
 */
public class ProcessUnitTest {

  @ClassRule
  @Rule
  public static ProcessEngineRule rule = TestCoverageProcessEngineRuleBuilder.create().build();
  
  private static final String PROCESS_DEFINITION_KEY = "faultBPM";
  
  static {
	  LogFactory.useSlf4jLogging(); // Setup logging
  }

  @Before
  public void setup() {
    init(rule.getProcessEngine());
  }
  
  /**
   * Tests if the process definition is deployable.
   */
  @Test
  @Deployment(resources = "process.bpmn")
  public void testParsingAndDeployment() {
	    // nothing is done here, as we just want to check for exceptions during deployment
	  }
  
    @Test
    @Deployment(resources = "process.bpmn")
    public void testGatewayOne() {
	  
  	  ProcessInstanceWithVariables processInstance = (ProcessInstanceWithVariables) processEngine().getRuntimeService().startProcessInstanceByKey(PROCESS_DEFINITION_KEY);
  	  
  	  boolean hardwareOK = (boolean) processInstance.getVariables().get("hardwareOK");
  	  System.out.println("hardwareOK: " + hardwareOK);
  
  	  TaskAssert task = assertThat(processInstance).task();
	  
  	  if (hardwareOK) {
  		  assertThat(processInstance).isWaitingAt("Activity_16pgm4r");
  		  task.hasName("Software update");
  		  task.isNotAssigned();
  	  }else {
  		  assertThat(processInstance).isWaitingAt("Activity_16y89yw");
  		  task.hasName("Test hardware");
  		  task.isNotAssigned();
  	  }
    }

	  
    @Test
    @Deployment(resources = "process.bpmn")
    public void testCompletionOftask() {
    		
    	// Obtain test run of BPMN
    	ProcessInstanceWithVariables processInstance = 
             (ProcessInstanceWithVariables) processEngine()	
    .getRuntimeService()
    .startProcessInstanceByKey(PROCESS_DEFINITION_KEY);

    	// Obtain a reference to the current task
    TaskAssert taskAssert = 
    assertThat(processInstance).task();
    		
    	TaskEntity task = (TaskEntity)taskAssert.getActual();
    	task.delegate("user");
    		
    	task.resolve();
    
    assertThat(processInstance.isEnded());
  }
 }
    


package com.garanti.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class DefaultStepDefinitions {

    private static final String PROCESS_DEFINITION_ID = "loanapproval.approval";
    
    private JbpmTestUtil testUtil;

    public DefaultStepDefinitions(JbpmTestUtil testUtil) {
        assertNotNull(testUtil);
        this.testUtil = testUtil;
    }

    @Before
    public void cucumberBefore() throws Exception {
        testUtil.setUp();
    }

    @After
    public void cucumberAfter() throws Exception {
        testUtil.tearDown();
    }

    
    @Given("^a customer approached for loan$")
    public void addBPMNs() {
    	List<String> bpmns = new ArrayList<String>();
    	bpmns.add("com/garanti/loanapproval/approval.bpmn");
        assertTrue(bpmns.size() > 0);
        testUtil.initialize(bpmns);
        
        boolean hasDefinition = testUtil.useProcessDefinition(PROCESS_DEFINITION_ID);
        assertTrue(hasDefinition);
        assertNotNull(testUtil.getKieSession());
        
        
        System.out.println("The URL is >>>>>>>>>>>>" + System.getProperty("org.url"));
    }

    @When("^a customer approached for loan with the following data$")
    public void startProcess() {
        assertNotNull(testUtil.getKieSession());
        assertNotNull(testUtil.getProcessId());
        testUtil.startProcess();
        assertNotNull(testUtil.getProcessInstance());
    }

    
    @Then("^the Loan Approval status is$")
    public void currentStatus() {
        assertNotNull(testUtil.getKieSession());
        assertNotNull(testUtil.getProcessInstance());
    }
    
    @Then("the current node is {string}")
    public void currentNode(String nodeName) {
        assertNotNull(testUtil.getKieSession());
        assertNotNull(testUtil.getProcessInstance());
        testUtil.assertNodeActive(testUtil.getProcessInstance().getId(), testUtil.getKieSession(), nodeName);
    }

    @Then("the following nodes were triggered:")
    public void nodesTriggered(List<String> nodeNames) {
        assertNotNull(testUtil.getProcessInstance());
        testUtil.assertNodeTriggered(testUtil.getProcessInstance().getId(), nodeNames.toArray(new String[nodeNames.size()]));
    }

    @Then("the process completed")
    public void processCompleted() {
        assertNotNull(testUtil.getProcessInstance());
        testUtil.assertProcessInstanceCompleted(testUtil.getProcessInstance().getId());
    }
    
    @And("^the human task '(.*?)' is Completed by '(.*?)' with parameters$")
    public void completeHumanTask (String taskName, String userId,  DataTable table) {
        assertNotNull(testUtil.getProcessInstance());
        final List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        assertEquals(true, testUtil.completeHumanTask(taskName, userId, rows));
    }

    @And("^the human task '(.*?)' is claimable by '(.*?)'$")
    public void isHumanTaskClaimableByTrue (String taskName, String userId) {
        assertNotNull(testUtil.getProcessInstance());
        assertEquals(true, testUtil.isHumanTaskClaimableBy(taskName, userId));
    }
    

    @Then("user {string} cannot claim the human task {string}")
    public void isHumanTaskClaimableByFalse (String userId, String taskName) {
        assertNotNull(testUtil.getProcessInstance());
        assertEquals(false, testUtil.isHumanTaskClaimableBy(taskName, userId));
    }

    @Then("user {string} is assigned the human task {string}")
    public void isHumanTaskAssignedToTrue (String userId, String taskName) {
        assertNotNull(testUtil.getProcessInstance());
        assertEquals(true, testUtil.isHumanTaskAssignedTo(taskName, userId));
    }

    @Then("user {string} is not assigned the human task {string}")
    public void isHumanTaskAssignedToFalse (String userId, String taskName) {
        assertNotNull(testUtil.getProcessInstance());
        assertEquals(false, testUtil.isHumanTaskAssignedTo(taskName, userId));
    }
}

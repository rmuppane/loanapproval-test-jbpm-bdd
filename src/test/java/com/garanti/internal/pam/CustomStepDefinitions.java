package com.garanti.internal.pam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.garanti.internal.JbpmTestUtil;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CustomStepDefinitions {
    private JbpmTestUtil testUtil;

    public CustomStepDefinitions(JbpmTestUtil testUtil) {
        assertNotNull(testUtil);
        this.testUtil = testUtil;
    }
    
    @Then("the process is not active")
    public void processNotActive() {
        assertNotNull(testUtil.getProcessInstance());
        assertNotNull(testUtil.getKieSession());
        testUtil.assertProcessInstanceNotActive(testUtil.getProcessInstance().getId(), testUtil.getKieSession());
    }

    @When("user {string} completes the human task {string} with the following boolean data")
    public void completeHumanTask (String userId, String taskName, DataTable data) {
        assertNotNull(testUtil.getProcessInstance());
        final List<Map<String, String>> params = new ArrayList<Map<String, String>>();
        Map<String, String> dataMap = data.asMap(String.class, String.class);

        assertEquals(true, testUtil.completeHumanTask(taskName, userId, params));
    }
}

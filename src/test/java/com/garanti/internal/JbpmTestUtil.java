package com.garanti.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.kie.api.KieBase;
import org.kie.api.definition.process.Process;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Status;
import org.kie.api.task.model.TaskSummary;
import org.kie.internal.runtime.manager.context.EmptyContext;

import com.garanti.loanapproval.Person;

public class JbpmTestUtil extends JbpmJUnitBaseTestCase {
    private RuntimeManager runtimeManager;
    private ProcessInstance processInstance;
    private String processId;
    
    private Map<String, Object> processParams = new HashMap<>();

    public JbpmTestUtil() {
        super(true, true);
    }

    public void initialize(List<String> bpmnFiles) {
        runtimeManager = createRuntimeManager(bpmnFiles.toArray(new String[bpmnFiles.size()]));
    }

    public boolean useProcessDefinition(String processId) {
        this.processId = processId;
        RuntimeEngine runtimeEngine = getRuntimeEngine(EmptyContext.get());
        if (runtimeEngine != null) {
        	KieSession kieSession = runtimeEngine.getKieSession();
	        if (kieSession == null) {
	            return false;
	        }
	        KieBase kieBase = kieSession.getKieBase();
	        if (kieBase == null) {
	            return false;
	        }
	        Process process = kieBase.getProcess(processId);
	        if (process == null) {
	            return false;
	        }
        }
        return true;
    }

    public void startProcess() {
    	RuntimeEngine runtimeEngine = getRuntimeEngine(EmptyContext.get());
        if (runtimeEngine != null) {
        	KieSession kieSession = runtimeEngine.getKieSession();
	        if (kieSession != null && processId != null) {
	            processInstance = kieSession.startProcess(processId, processParams);
	        }
        }
    }

    public RuntimeManager getRuntimeManager() {
        return runtimeManager;
    }

    public void setRuntimeManager(RuntimeManager runtimeManager) {
        this.runtimeManager = runtimeManager;
    }

    public KieSession getKieSession() {
    	RuntimeEngine runtimeEngine = getRuntimeEngine(EmptyContext.get());
        KieSession kieSession = runtimeEngine.getKieSession();
        return kieSession;
    }

    public ProcessInstance getProcessInstance() {
        return processInstance;
    }

    public void setProcessInstance(ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public Map<String, Object> getProcessParams() {
        return processParams;
    }

    public void setProcessParams(Map<String, Object> processParams) {
        this.processParams = processParams;
    }

    public void addProcessParam(String name, Object value) {
        this.processParams.putIfAbsent(name, value);
    }
    
    // tasks
    public boolean isHumanTaskAssignedTo (String taskName, String userId) {
        return hasHumanTaskStatus(taskName, userId, Status.Reserved);
    }

    public boolean isHumanTaskClaimableBy (String taskName, String userId) {
        return hasHumanTaskStatus(taskName, userId, Status.Ready);
    }

    public boolean isHumanTaskStarted (String taskName, String userId) {
        return hasHumanTaskStatus(taskName, userId, Status.InProgress);
    }
    
    public boolean completeHumanTask (String taskName, String userId, List<Map<String, String>> rows) {
    	RuntimeEngine runtimeEngine = getRuntimeEngine(EmptyContext.get());
        if (runtimeEngine != null) {
        	TaskService taskService = runtimeEngine.getTaskService();
	        List<TaskSummary> tasks = taskService.getTasksAssignedAsPotentialOwner(userId, "en-UK");
	        TaskSummary found = tasks.stream().filter(task -> task.getName().equals(taskName)).findAny().orElse(null);
	        if (found != null) {
	            switch (found.getStatus()) {
	                case Ready:
	                    taskService.claim(found.getId(), userId);
	                case Reserved:
	                    taskService.start(found.getId(), userId);
	                case InProgress: {
	                	final Map<String, Object> parameters = new HashMap<>();
		                	if(taskName.equals("Data Entry")) {
		                         final Person person = new Person();
		                         person.setFirstName(rows.get(0).get("firstName"));
		                         person.setLastName(rows.get(0).get("lastName"));
		                         person.setDob(rows.get(0).get("DOB"));
		                         person.setCreditScore(Float.parseFloat(rows.get(0).get("CreditScore")));
		                         person.setIncome(Integer.parseInt(rows.get(0).get("income")));
		                         parameters.put("person", person);
		                    } 
		                	else if(taskName.equals("Approval Documentation")) {
		                     	
		                    } 
		                    else if(taskName.equals("Reject Documentation")) {
		                    	 //
		                    }
	                    	taskService.complete(found.getId(), userId, parameters);
	                	}
	                    return true;
	                default:
	                    return false;
	            }
	        }
        }
        return false;
    }
    
    private boolean hasHumanTaskStatus (String taskName, String userId, Status status) {
    	RuntimeEngine runtimeEngine = getRuntimeEngine(EmptyContext.get());
        TaskService taskService = runtimeEngine.getTaskService();
        List<TaskSummary> tasks = taskService.getTasksAssignedAsPotentialOwner(userId, "en-UK");
        TaskSummary found = tasks.stream().filter(task -> task.getName().equals(taskName)).findAny().orElse(null);
        return found != null ? found.getStatus() == status : false;
    }

}

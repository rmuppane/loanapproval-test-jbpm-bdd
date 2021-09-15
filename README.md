# JBPM Process Testing Using Cucumber

This project to test the JBPM processes with Cucumber by implementing some useful integrations and default step definitions.  Here are the features:

1. BPMN assets to be tested are included as a dependency in the [pom.xml](pom.xml) file.  This way the tests can be separate from the process definitions.
  - The example KJAR in the pom.xml can be found at https://github.com/rmuppane/loanapproval
2. Default [step definitions](src/test/java/com/garanti/internal/DefaultStepDefinitions.java) and [feature file](src/test/resources/features/loanapproval.feature) define an easy way to interact with processes.
  - As of version 1.1, the default step definitions include some task steps
4. A [JBPM Test Utility class](src/test/java/com/garanti/internal/JbpmTestUtil.java) is used for both default and custom step definitions and is based on the [JbpmUnitBaseTestCase class](https://github.com/kiegroup/jbpm/blob/master/jbpm-test/src/main/java/org/jbpm/test/JbpmJUnitBaseTestCase.java) so many of the useful assertions can be leveraged
  - As of version 1.1, the test utility includes a TaskService as well as some human task methods
5. The [Cucumber Picocontainer](https://github.com/cucumber/cucumber-jvm/tree/master/picocontainer) is used to share the running process state (in the JbpmTestUtil class) between the default and custom step definition files.

Instructions:

1. Clone this repo.
2. If using and IDE, recommend adding the [Cucumber plugin](https://cucumber.io/docs/tools/java/)
3. Include your own KJAR(s) in the [pom.xml](pom.xml) file.  The [loanapproval.feature](src/test/resources/features/loanapproval.feature) file uses the KJAR from  https://github.com/rmuppane/loanapproval. So clone it and perform `mvn clean install`.
4. Run the test using `mvn clean test` or test through your IDE.
5. Modify the [user groups properties file](src/test/resources/usergroups.properties) to customize user group callback.

Environment:

1. Java 8 or 11
2. Product version and librarys: 7.11, 7.52.0.Final-redhat-00008 or 7.11.1.redhat-00001
3. Junit 4
4. Cucumber 6.11.0

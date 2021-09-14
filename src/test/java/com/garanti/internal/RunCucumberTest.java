package com.garanti.internal;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions( //
	    features = "src/test/resources/insurance-quote.feature", //
	    plugin = {"pretty", "junit:target/cucumber-report.xml",
								"json:target/cucumber-report.json",
								"html:target/cucumber"} //
	)

public class RunCucumberTest {

}

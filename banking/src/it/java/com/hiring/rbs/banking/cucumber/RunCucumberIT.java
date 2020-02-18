package com.hiring.rbs.banking.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty","html:target/cucmber","json:target/cucumber/cucumber.json","junit:target/cucumber/cucumber.xml"},
        features = "src/it/resources")
public class RunCucumberIT {
    private static Logger LOG = LoggerFactory.getLogger(RunCucumberIT.class);

}

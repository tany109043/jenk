package hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;

import io.cucumber.java.Scenario;   // ✅ ADD THIS

import utils.DriverFactory;
import utils.ExtentManager;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;   // ✅ ADD THIS

public class Hooks {

    public static ExtentReports extent;
    public static ExtentTest test;   // ✅ ADD THIS

    @BeforeAll
    public static void setupReport() {
        extent = ExtentManager.getInstance();
    }

    @Before
    public void setUp(Scenario scenario) {   // ✅ updated
        DriverFactory.initDriver();

        // ✅ Create test in report
        test = extent.createTest(scenario.getName());
    }

    @After
    public void tearDown(Scenario scenario) {   // ✅ updated
        if (scenario.isFailed()) {
            test.fail("Test Failed");
        } else {
            test.pass("Test Passed");
        }

        DriverFactory.quitDriver();
    }

    @AfterAll
    public static void tearDownReport() {
        extent.flush();
    }
}

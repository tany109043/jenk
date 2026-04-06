package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.LoginPage;
import utils.DriverFactory;
import utils.ExcelReader;

import java.util.Map;

public class LoginSteps {
    private static final String TEST_DATA_FILE = "testdata/demoqa-login-data-simple.xlsx";

    private LoginPage loginPage;
    private Map<String, String> testData;

    @Given("the user opens the DemoQA login page")
    public void theUserOpensTheDemoQALoginPage() {
        loginPage = new LoginPage(DriverFactory.getDriver());
        loginPage.open();
    }

    @When("the user logs in with row {int} from sheet {string}")
    public void theUserLogsInWithRowFromSheet(int rowNumber, String sheetName) {
        testData = ExcelReader.getRowData(TEST_DATA_FILE, sheetName, rowNumber);
        loginPage.login(testData.getOrDefault("username", ""), testData.getOrDefault("password", ""));
    }

    @Then("the login result should match the Excel data")
    public void theLoginResultShouldMatchTheExcelData() {
        String expectedResult = testData.getOrDefault("result", "").trim();

        if ("success".equalsIgnoreCase(expectedResult)) {
            Assert.assertTrue(loginPage.isLoginSuccessful(), "Expected the login to succeed.");
            Assert.assertEquals(
                    loginPage.getLoggedInUsername(),
                    testData.getOrDefault("username", ""),
                    "Logged in username did not match."
            );
            return;
        }

        if ("fail".equalsIgnoreCase(expectedResult) || "failure".equalsIgnoreCase(expectedResult)) {
            Assert.assertFalse(loginPage.isLoginSuccessful(), "Expected the login to fail.");
            Assert.assertEquals(
                    loginPage.getErrorMessage(),
                    "Invalid username or password!",
                    "Failure message did not match."
            );
            return;
        }

        throw new IllegalArgumentException("Unsupported expectedResult value: " + expectedResult);
    }
}

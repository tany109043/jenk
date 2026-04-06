package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class LoginPage {
    private static final String LOGIN_URL = "https://demoqa.com/login";

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By usernameInput = By.id("userName");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login");
    private final By errorMessage = By.id("name");
    private final By loggedInUsername = By.id("userName-value");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        driver.get(LOGIN_URL);
        removeObstructions();
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput));
    }

    public void login(String username, String password) {
        type(usernameInput, username);
        type(passwordInput, password);
        click(loginButton);
        waitForLoginOutcome();
    }

    public boolean isLoginSuccessful() {
        return !driver.findElements(loggedInUsername).isEmpty()
                && driver.findElement(loggedInUsername).isDisplayed()
                && !driver.findElement(loggedInUsername).getText().trim().isEmpty();
    }

    public String getLoggedInUsername() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(loggedInUsername)).getText().trim();
    }

    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText().trim();
    }

    private void waitForLoginOutcome() {
        try {
            wait.until(webDriver -> {
                List<WebElement> successElements = webDriver.findElements(loggedInUsername);
                boolean successVisible = !successElements.isEmpty()
                        && successElements.get(0).isDisplayed()
                        && !successElements.get(0).getText().trim().isEmpty();

                List<WebElement> errorElements = webDriver.findElements(errorMessage);
                boolean errorVisible = !errorElements.isEmpty()
                        && errorElements.get(0).isDisplayed()
                        && !errorElements.get(0).getText().trim().isEmpty();

                return successVisible || errorVisible;
            });
        } catch (TimeoutException exception) {
            throw new AssertionError("Login outcome was not visible on the page.", exception);
        }
    }

    private void type(By locator, String value) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        scrollIntoView(element);
        element.clear();
        element.sendKeys(value);
    }

    private void click(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        scrollIntoView(element);
        element.click();
    }

    private void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});",
                element
        );
    }

    private void removeObstructions() {
        ((JavascriptExecutor) driver).executeScript(
                "const fixedBanner = document.getElementById('fixedban');" +
                        "if (fixedBanner) { fixedBanner.remove(); }" +
                        "const footer = document.querySelector('footer');" +
                        "if (footer) { footer.style.position = 'static'; }"
        );
    }
}

package base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ExtentManager;
import utils.ScreenshotUtil;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BaseTest {
	
//	protected WebDriver driver;
	
	public static ThreadLocal<WebDriver> threadDriver = new ThreadLocal<>();
    private static ExtentReports extent;
    protected ExtentTest test;
    
    public WebDriver getDriver() {
    	return threadDriver.get();
    }

	@BeforeSuite
	public void setupReport() {
		extent = ExtentManager.getInstance();
	}

	@BeforeMethod
	public void setupDriver(Method method) {
		WebDriverManager.chromedriver().setup();
		threadDriver.set(initializeChromeDriver()); 
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		getDriver().manage().window().maximize();

		// Ensure ExtentReports is initialized before creating a test
		if (extent == null) {
			extent = ExtentManager.getInstance();
		}

		test = extent.createTest(method.getName());
	}

	@AfterMethod
	public void tearDownTest(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE) {
			String screenshotPath = ScreenshotUtil.captureScreenShots(getDriver(), result.getName());
			test.fail(result.getThrowable());
			test.addScreenCaptureFromPath(screenshotPath);
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			test.pass("Test passed");
		} else {
			test.skip("Test skipped");
		}
		 if (getDriver() != null) {
			 getDriver().quit();
		 }
	
	}
	public WebDriver initializeChromeDriver() {
	    ChromeOptions options = new ChromeOptions();

	    options.addArguments("--disable-infobars");
	    options.addArguments("--disable-notifications");
	    options.addArguments("--no-sandbox");                   // Required in Linux CI
	    options.addArguments("--disable-dev-shm-usage");        // Required in Linux CI
	    options.addArguments("--headless=new");                  // Headless mode for CI

	    // Use a unique user data directory to avoid conflicts in parallel or repeated runs
	    String userDataDir = "/tmp/chrome-user-data-" + java.util.UUID.randomUUID();
	    options.addArguments("--user-data-dir=" + userDataDir);

	    Map<String, Object> prefs = new HashMap<>();
	    prefs.put("credentials_enable_service", false);
	    prefs.put("profile.password_manager_enabled", false);
	    options.setExperimentalOption("prefs", prefs);
	    options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));

	    return new ChromeDriver(options);
	}

	 public WebElement waitForElementVisible(By locator, int timeInSec) {
	        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeInSec));
	        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	    }
	
//	 @AfterClass
//	    public void tearDown() {
//	        if (driver != null) {
//	            driver.quit();
//	        }
//	    }
//

	@AfterSuite
	public void generateReport() {
		extent.flush();
	}
}

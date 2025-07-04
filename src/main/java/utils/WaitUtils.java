package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class WaitUtils {

    public static WebElement waitForVisibility(WebDriver driver, By locator, int seconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(seconds)).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    
    public static WebElement waitForClickability(WebDriver driver, By locator, int seconds) {
    	return new WebDriverWait(driver, Duration.ofSeconds(seconds)).until(ExpectedConditions.elementToBeClickable(locator));
    }
    public static WebElement waitForClickability(WebDriver driver, WebElement element, int seconds) {
    	return new WebDriverWait(driver, Duration.ofSeconds(seconds)).until(ExpectedConditions.elementToBeClickable(element));
    }
    
    public static void waitForStaleness(WebDriver driver, WebElement element, int seconds) {
        new WebDriverWait(driver, Duration.ofSeconds(seconds)).until(ExpectedConditions.stalenessOf(element));
    }
}

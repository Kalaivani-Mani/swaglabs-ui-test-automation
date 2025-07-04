package utils;

import org.openqa.selenium.WebDriver;

public class DriverFactory {
	
	 private static ThreadLocal<WebDriver> tDriver = new ThreadLocal<>();

	    public static WebDriver getDriver() {
	        return tDriver.get();
	    }

	    public static void setDriver(WebDriver driver) {
	        tDriver.set(driver);
	    }

	    public static void unloadDriver() {
	        tDriver.remove();
	    }


	
}

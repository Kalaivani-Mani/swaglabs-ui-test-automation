package utils;

// Import necessary libraries for handling files, dates, and Selenium WebDriver
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import org.openqa.selenium.*;

public class ScreenshotUtil {

    /**
     * Captures a screenshot of the current browser window and saves it to a specified directory.
     * @param driver WebDriver instance controlling the browser
     * @param testName Name of the test case (used in naming the screenshot file)
     * @return The path of the saved screenshot file
     */
    public static String captureScreenShots(WebDriver driver, String testName) {
        // Take a screenshot using WebDriver's TakesScreenshot interface
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        // Generate a timestamp to ensure unique file names
        String timeStamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());

        // Define the path where the screenshot should be saved
        String screenshotPath = "reports/screenshots/" + testName + "_" + timeStamp + ".png";  // Ensure proper directory structure

        // Create destination file object
        File destFile = new File(screenshotPath);

        try {
            // Ensure that the parent directories exist; create if necessary
            Files.createDirectories(destFile.getParentFile().toPath());

            // Copy the screenshot file from the temporary location to the target location
            Files.copy(srcFile.toPath(), destFile.toPath());

        } catch (IOException e) {
            // Print an error message if file saving fails
            System.err.println("Failed to save screenshot: " + e.getMessage());
        }

        // Return the screenshot file path for reference in reports or logging
        return screenshotPath;
    }
}
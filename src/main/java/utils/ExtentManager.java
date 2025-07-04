package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter("reports/extent-report.html");
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setDocumentTitle("SauceDemo Automation Report");
            spark.config().setReportName("Functional Test Report");

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("Tester", "Kalaivani");
        }
		return extent;
        
    }
}

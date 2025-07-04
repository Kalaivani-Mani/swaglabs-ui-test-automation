package tests;

import pages.InventoryPage;
import pages.LoginPage;
import utils.ConfigReader;


import org.testng.Assert;
import org.testng.annotations.Test;
import base.BaseTest;
import data.LoginDataProvider;
import io.qameta.allure.*;


public class LoginTest extends BaseTest {
	
	@Description("Test login functionality with valid credentials")
	@Severity(SeverityLevel.CRITICAL)
	@Test(dataProvider = "loginCredentials", dataProviderClass = LoginDataProvider.class)
	public void loginTest(String username, String Password, boolean shouldLoginSucceed) {

		getDriver().get(ConfigReader.getProperty("baseUrl"));

		LoginPage login = new LoginPage(getDriver());
		login.login(username, Password);

		InventoryPage inventory = new InventoryPage(getDriver());

		if (shouldLoginSucceed) {
			Assert.assertTrue(inventory.isInventoryVisible());
			test.pass("Login succeed for " + username);
		} else {
			Assert.assertTrue(inventory.isErrorDisplayed());
			test.pass("Login failed for " + username);
		}
	}

}
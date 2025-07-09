package pages;

import java.util.*;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utils.WaitUtils;

public class CheckoutPage {

	private ThreadLocal<WebDriver> tLocal = new ThreadLocal<>();

	// Checkout Info
	private By checkoutTitle = By.xpath("//span[text() = 'Checkout: Your Information']");
	private By firstName = By.id("first-name");
	private By lastName = By.id("last-name");
	private By postalCode = By.id("postal-code");
	private By cancelBtn = By.id("cancel");
	private By continueBtn = By.id("continue");
	private By errorMessage = By.className("error-button");

	// checkout:Overview
	private By title = By.xpath("//span[text()='Checkout: Overview']");
	private By finishButton = By.id("finish");
	private By itemNames = By.className("inventory_item_name");
	private By subtotalLabel = By.className("summary_subtotal_label");
	private By taxLabel = By.className("summary_tax_label");
	private By totalLabel = By.className("summary_total_label");

	public WebDriver getDriver() {
		return tLocal.get();
	}

	public CheckoutPage(WebDriver driver) {
		tLocal.set(driver);
	}

//	Sanity Scenarios for Checkout

	// 1. Checkout info: Your Information Page
	public boolean isOnCheckoutInformationPage() {
		WaitUtils.waitForVisibility(getDriver(), checkoutTitle, 10);
		return getDriver().findElement(checkoutTitle).isDisplayed();
	}

	// 2. Checkout info: fillCheckoutForm
	public void fillCheckoutForm(String first, String last, String zip) {
		getDriver().findElement(firstName).sendKeys(first);
		getDriver().findElement(lastName).sendKeys(last);
		getDriver().findElement(postalCode).sendKeys(zip);

	}

	// 3. Checkout info: Click continue button
	public void clickContinue() {
		WaitUtils.waitForClickability(getDriver(), continueBtn, 5);
		getDriver().findElement(continueBtn).click();

	}
	
	public void clickCancel() {
		WaitUtils.waitForClickability(getDriver(), cancelBtn, 5);
		getDriver().findElement(cancelBtn).click();

	}


	// 4. Checkout overview: Overview visibility
	public boolean isOnOverviewPage() {
		WaitUtils.waitForVisibility(getDriver(), title, 5);
		return getDriver().findElement(title).isDisplayed();
	}

	// 5. Checkout overview: getItemNames
	public List<String> getItemNames() {
		return getDriver().findElements(itemNames).stream().map(e -> e.getText().trim()).collect(Collectors.toList());
	}

	// 6. Checkout overview: getSubtotal
	public double getSubtotal() {
		return Double.parseDouble(getDriver().findElement(subtotalLabel).getText().replace("Item total: $", "").trim());
	}

	// 7. Checkout overview: getTax
	public double getTax() {
		return Double.parseDouble(getDriver().findElement(taxLabel).getText().replace("Tax: $", "").trim());
	}
	// 8. Checkout overview: getTotal
	public double getTotal() {
		return Double.parseDouble(getDriver().findElement(totalLabel).getText().replace("Total: $", "").trim());
	}
	
	// 9. Checkout overview: clickFinish
	public void clickFinish() {
		WaitUtils.waitForClickability(getDriver(), finishButton, 5);
		getDriver().findElement(finishButton).click();
	}
	
	//10 Checkout Info: Error 
	public boolean isErrorMessageVisible() {
		return getDriver().findElements(errorMessage).size()>0;
	}

}

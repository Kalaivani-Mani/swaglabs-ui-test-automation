package pages;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import utils.WaitUtils;

public class CartPage {

	private ThreadLocal<WebDriver> tDriver = new ThreadLocal<>();

	public WebDriver getDriver() {
		return tDriver.get();
	}

	public CartPage(WebDriver driver) {
		tDriver.set(driver);
	}

	private By cartButton = By.className("shopping_cart_link");
	private By cartTitle = By.xpath("//span[text()='Your Cart']");
	private By cartItemCount = By.className("cart_item_label");
	private By checkoutButton = By.xpath("//button[text()='Checkout']");
	private By productNames = By.className("inventory_item_name");
	private By removeButton = By.xpath(".//button[text()='Remove']");
	private By priceList = By.className("inventory_item_price");
	private By continueShoppingButton = By.id("continue-shopping");
	

	/*
	 * 📌 Step 1: Understand Sanity Test Goals For the Cart page, these are the
	 * typical sanity test goals:
	 * 
	 * Add one or more items to the cart.
	 * 
	 * Navigate to the cart page.
	 * 
	 * Verify that the correct items are displayed.
	 * 
	 * Verify the cart page loads properly.
	 * 
	 * (Optional) Click on checkout to ensure navigation works.
	 * 
	 */

	// 1. Click cart icon to navigate to cart page
	public void openCart() {
		WaitUtils.waitForClickability(getDriver(), cartButton, 10);
		getDriver().findElement(cartButton).click();
	}

	// 2. Get cart page title
	public boolean getCartTitle() {
		try {
	        WaitUtils.waitForVisibility(getDriver(), cartTitle, 10);  // bump timeout
	        return getDriver().findElement(cartTitle).isDisplayed();
	    } catch (Exception e) {
	        System.out.println("⚠️ Cart title not found. URL: " + getDriver().getCurrentUrl());
	        throw e;
	    }
	}

	// 3. Get Cart item count
	public int getCartItemCount() {
		return getDriver().findElements(cartItemCount).size();
	}

	// 4. Click Checkout Button
	public boolean checkCheckoutButtonVisibility() {
		return getDriver().findElement(checkoutButton).isDisplayed();
	}

	// 5. Click Checkout Button
	public void clickCheckoutButton() {
		WaitUtils.waitForVisibility(getDriver(), checkoutButton, 5);
		getDriver().findElement(checkoutButton).click();
	}
	
	// 6. Click Continue Shopping Button
	public void clickContinueShopping() {
		try {
		WebElement button = WaitUtils.waitForClickability(getDriver(), continueShoppingButton, 5);
		button.click();
		 System.out.println("✅ Clicked 'Continue Shopping' button.");
		}catch (Exception e) {
			System.out.println("Continue Shopping button is not clickable");
			throw e;
		}
		
	}

	/*
	 * End-to-end testing includes coverage for item integrity, remove flow, price
	 * calculations, navigation links, and overall UI/UX validation.
	 */

	// 7. Integrity Testing - Multiple different/same items added show up correctly
	public List<String> fetchUniqueCartItems() {
		try {
			WaitUtils.waitForVisibility(getDriver(), cartTitle, 10);
		} catch (Exception e) {
			System.out.println("DEBUG: Cart page did not load or selector is incorrect.");
			throw e;
		}
		List<WebElement> products = getDriver().findElements(productNames);
		System.out.println("Cart items: " + products);
		return products.stream().map(WebElement::getText).collect(Collectors.toList());
	}

	public void removeItem(String productName) {
		List<WebElement> products = getDriver().findElements(productNames);

		for (WebElement product : products) {
			String name = product.getText().trim();
			if (name.equals(productName)) {
				// Step 1: Find cart row container
				WebElement itemRow = product.findElement(By.xpath("./ancestor::div[contains(@class, 'cart_item')]"));

				// Step 2: Click Remove button *inside the row*
				WebElement removeBtn = itemRow.findElement(removeButton);
				WaitUtils.waitForClickability(getDriver(), removeBtn, 5);
				removeBtn.click();

				// Step 3: Wait for the **product name element** itself to become stale (not the
				// row)
				WaitUtils.waitForStaleness(getDriver(), product, 5);

				break;
			}
		}
	}

	// 8. Get all Prices
	public List<Double> getAllItemPrices() {
		return getDriver().findElements(priceList).stream()
				.map(e -> Double.parseDouble(e.getText().replace("$", "").trim())).collect(Collectors.toList());

	}

	// 8. Sum of all item prices
	public double getTotalItemPrice() {
		return getAllItemPrices().stream().mapToDouble(d -> d.doubleValue()).sum();
	}

	// 9. remove products from cart before proceeding any testing
	public void removeAllItems() {
		List<String> items = fetchUniqueCartItems(); // returns list of product names
		for (String name : items) {
			removeItem(name);
		}
	}

	
	
}

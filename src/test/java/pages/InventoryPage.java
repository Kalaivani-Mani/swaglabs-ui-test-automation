package pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import utils.WaitUtils;

public class InventoryPage {
	
	public static ThreadLocal<WebDriver> threadDriver = new ThreadLocal<>();

	  public WebDriver getDriver() {
	    	return threadDriver.get();
	    }
	//private WebDriver driver;

	private By inventoryContainer = By.id("inventory_container");
	private By errorMessage = By.cssSelector(".error-message-container");
	private By productCount = By.className("inventory_item");
	private By productNames = By.className("inventory_item_name");
	private By addToCartButton = By.xpath("//button[text()= 'Add to cart']");
	private By countCartIcon = By.className("shopping_cart_badge");
	private By removeCartItems = By.xpath("//button[text()= 'Remove']");
	private By sortIcon = By.className("product_sort_container");
	private By productPrice = By.className("inventory_item_price");


	public InventoryPage(WebDriver driver) {
		threadDriver.set(driver);
	}

	// 1. Page Load & Visibility
	// 1.1 Page loads successful
	public boolean isInventoryVisible() {
		return getDriver().findElements(inventoryContainer).size() > 0;
	}

	// 1.2 Page loads unsuccessful
	public boolean isErrorDisplayed() {
		return getDriver().findElements(errorMessage).size() > 0;
	}

	// 2. Get product count
	public int getProductCount() {
		return getDriver().findElements(productCount).size();
	}

	// 3. Get list products name available in inventory
	public List<String> getProductName() {

		// Storing all the elements in to web element list
		List<WebElement> elements = getDriver().findElements(productNames);

		// to store the text content of each found web element
		List<String> names = new ArrayList<>();

		// - The loop extracts information from each element one by one
		for (WebElement pn : elements) {

			// - Adds this text to the names list.
			names.add(pn.getText());

		}
		return names;

	}

	// Add Method to Add First Item to Cart
	public void addFirstItemToCart() {
		List<WebElement> buttons = getDriver().findElements(addToCartButton);

		if (!buttons.isEmpty()) {
			buttons.get(0).click();
		}

	}

	// Add Method to Get Cart Badge Count
	public int getCartBadgeCount() {
		List<WebElement> badges = getDriver().findElements(countCartIcon);

		if (badges.isEmpty()) {
			System.out.println("Cart badge is not visible (assumed 0)" + badges);
			return 0;
		}
		String badgeText = badges.get(0).getText().trim();
		System.out.println("Badge is visble with the value " + badgeText);
		try {
			return Integer.parseInt(badgeText);
		} catch (NumberFormatException e) {
			System.out.println("⚠️ Cart badge text is not a number: " + badgeText);
			return 0;
		}
	}

	// Add All the products to cart
	public int addAllProduct() {
		int count = 0;
		List<WebElement> list = getDriver().findElements(addToCartButton);
		// List<WebElement> allList = new ArrayList<>();
		for (WebElement webElement : list) {
			WaitUtils.waitForClickability(getDriver(), addToCartButton, 5);
			webElement.click();
			count++;
		}
		return count;
	}

	// Remove all the products from cart
	public int removeAllTheProducts() {
		int RCount = 0;
		List<WebElement> removeList = getDriver().findElements(removeCartItems);
		for (WebElement webElement : removeList) {
			WaitUtils.waitForClickability(getDriver(), removeCartItems, 10);
			webElement.click();
			RCount++;

		}
		return RCount;

	}

	// remove first product from cart
	public int removeItemFromCart() {
		List<WebElement> elements = getDriver().findElements(removeCartItems);
		int removeCount = 0;
		if (!elements.isEmpty()) {
			elements.remove(0);

		}
		return removeCount;

	}

	// Add a specific product to cart
	public void addSpecificProductToCart(String specificProductName) {
	    // Ensure inventory page is fully loaded
	    WaitUtils.waitForVisibility(getDriver(), By.id("inventory_container"), 10);

	    List<WebElement> productCards = getDriver().findElements(By.className("inventory_item"));
	    boolean found = false;

	    // Normalize the expected name
	    String expected = specificProductName.trim().replace("\u00A0", " ").toLowerCase();

	    System.out.println("🔍 Looking for product: " + expected);

	    for (WebElement card : productCards) {
	        String actualName = card.findElement(By.className("inventory_item_name")).getText()
	                .trim()
	                .replace("\u00A0", " ")
	                .toLowerCase();

	        System.out.println("🛒 Found product: " + actualName);

	        if (actualName.equals(expected)) {
	            WebElement addButton = card.findElement(By.xpath(".//button[contains(text(), 'Add to cart')]"));
	            WaitUtils.waitForClickability(getDriver(), addButton, 10).click();
	            found = true;
	            System.out.println("✅ Added to cart: " + actualName);
	            break;
	        }
	    }

	    if (!found) {
	        throw new RuntimeException("❌ Product not found: " + specificProductName);
	    }
	}


	// Sort option
	public void selectSortOption(String options) {
		WebElement dropdown = getDriver().findElement(sortIcon);
		new Select(dropdown).selectByVisibleText(options);

	}

	public boolean isProductNameSortingAscending() {
		List<String> names = getProductName();
		List<String> arrayList = new ArrayList<>(names);
		Collections.sort(arrayList);
		return names.equals(arrayList);
	}

	public boolean isProductNameSortingDesending() {
		List<String> names = getProductName();
		List<String> arrayList = new ArrayList<>(names);
		arrayList.sort(Collections.reverseOrder());
		return names.equals(arrayList);
	}

	public boolean isProductPriceSortingAscending() {
		List<WebElement> elements = getDriver().findElements(productPrice);
		List<Double> prices = new ArrayList<>();

		for (WebElement pp : elements) {

			prices.add(Double.parseDouble(pp.getText().replace("$", "").trim()));

		}

		List<Double> sorted = new ArrayList<>(prices);
		Collections.sort(sorted);

		return prices.equals(sorted);
	}
	
	public boolean isProductPriceSortingDesending() {
		List<WebElement> element = getDriver().findElements(productPrice);
		List<Double> prices = new ArrayList<>();
		for (WebElement pp : element) {
			prices.add(Double.parseDouble(pp.getText().replace("$", "").trim()));
		}
		
		List<Double> sorted = new ArrayList<>(prices);
		sorted.sort(Collections.reverseOrder());
		
		return prices.equals(sorted);
	}
	

}

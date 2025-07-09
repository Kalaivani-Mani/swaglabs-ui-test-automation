package tests;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.*;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import pages.InventoryPage;
import pages.LoginPage;
import utils.ConfigReader;
import utils.ScreenshotUtil;

public class InventoryTest extends BaseTest {

	@BeforeMethod
	public void cleanCart() {

		InventoryPage inventoryPage = new InventoryPage(getDriver());
		inventoryPage.removeAllTheProducts(); // or navigate to cart and clear
	}

	@Description("Test the number of products available in inventory page")
	@Severity(SeverityLevel.NORMAL)
	@Test(priority = 1, groups = { "inventory", "regression" })
	public void getNumberOfProductsAvailableInInventoryPage() {
		getDriver().get(ConfigReader.getProperty("baseUrl"));
		new LoginPage(getDriver()).login("standard_user", "secret_sauce");

		InventoryPage inventory = new InventoryPage(getDriver());
		int productCount = inventory.getProductCount();

		try {
			Assert.assertTrue(productCount > 0, "Expected to list at least one product but got " + productCount);
			test.pass("Product count displayed successfully: " + productCount);
			System.out.println("Product count displayed successfully: " + productCount);
		} catch (AssertionError e) {
			ScreenshotUtil.captureScreenShots(getDriver(), "ProductCountFailed");
			throw e;
		}
	}

	@Description("Verify the list of available product names on inventory page")
	@Severity(SeverityLevel.NORMAL)
	@Test(priority = 2, groups = { "inventory", "regression" })
	public void getListofAvailableProductNameFromInventoryPage() {
		getDriver().get(ConfigReader.getProperty("baseUrl"));
		new LoginPage(getDriver()).login("standard_user", "secret_sauce");

		InventoryPage inventory = new InventoryPage(getDriver());
		List<String> productName = inventory.getProductName();

		try {
			Assert.assertTrue(productName.size() > 0,
					"Expected to get at least one product name but got " + productName.size());
			test.pass("Product names retrieved: " + productName);
			System.out.println("Product names retrieved: " + productName);
		} catch (AssertionError e) {
			ScreenshotUtil.captureScreenShots(getDriver(), "ProductNamesFailed");
			throw e;
		}
	}

	@Description("Verify adding first product to cart updates cart badge")
	@Severity(SeverityLevel.CRITICAL)
	@Test(priority = 3, groups = { "cart", "regression" })
	public void addFirstProductToCartFromInventoryPage() {
		getDriver().get(ConfigReader.getProperty("baseUrl"));
		new LoginPage(getDriver()).login("standard_user", "secret_sauce");

		InventoryPage inventoryPage = new InventoryPage(getDriver());
		inventoryPage.addFirstItemToCart();
	}

	// Add all the products to cart
	@Description("Verify adding all the available inventory page products to cart")
	@Severity(SeverityLevel.NORMAL)
	@Test(dependsOnMethods = "addFirstProductToCartFromInventoryPage", groups = { "cart", "regression" })
	public void addAllTheProductsToCartFromInventoryPage() {
		getDriver().get(ConfigReader.getProperty("baseUrl"));
		new LoginPage(getDriver()).login("standard_user", "secret_sauce");

		InventoryPage inventoryPage = new InventoryPage(getDriver());

		int expected = inventoryPage.getProductCount();
		int actual = inventoryPage.addAllProduct();
		try {
			Assert.assertEquals(expected, actual, "Cart should reflect all added items");
			test.pass("\"All products added to cart. Expected = Actual = \" + actual");
		} catch (AssertionError e) {
			ScreenshotUtil.captureScreenShots(getDriver(), "BulkAddToCartFaild");
			throw e;

		}

	}

	@Description("Verify deleting all the available inventory page products from cart")
	@Severity(SeverityLevel.NORMAL)
	@Test(dependsOnMethods = { "addAllTheProductsToCartFromInventoryPage", "removeFirstItemFromCart" }, groups = {
			"cart", "regression" })
	public void removeAllTheProductsFromCart() {
		getDriver().get(ConfigReader.getProperty("baseUrl"));
		new LoginPage(getDriver()).login("standard_user", "secret_sauce");

		InventoryPage inventoryPage = new InventoryPage(getDriver());
		int added = inventoryPage.addAllProduct();
		System.out.println("All the products were added to cart. now the cart contains "+added);
		int removed = inventoryPage.removeAllTheProducts();

		try {
			Assert.assertEquals(removed, 0, "All the  cart items removed");
			test.pass("All the products removed from cart");

		} catch (Exception e) {
			ScreenshotUtil.captureScreenShots(getDriver(), "RemoveProductsFromCart");
		}

	}

	// Remove first item from cart
	@Description("Remove First Item from cart")
	@Severity(SeverityLevel.CRITICAL)
	@Test(dependsOnMethods = "addFirstProductToCartFromInventoryPage", groups = { "cart", "regression" })
	public void removeFirstItemFromCart() {
		getDriver().get(ConfigReader.getProperty("baseUrl"));
		new LoginPage(getDriver()).login("standard_user", "secret_sauce");

		InventoryPage inventoryPage = new InventoryPage(getDriver());
		inventoryPage.addFirstItemToCart();
		// int before = inventoryPage.getCartBadgeCount();

		inventoryPage.removeItemFromCart();
		int after = inventoryPage.getCartBadgeCount();

		try {
			Assert.assertNotEquals(after, 0, "Before deleting operation count should be 1 after 0" + after);
			test.pass("Removed first item from the cart");

		} catch (Exception e) {
			ScreenshotUtil.captureScreenShots(getDriver(), "Failed to remove first product from cart");
			throw e;
		}
	}

	@Description("Add specific products to cart")
	@Severity(SeverityLevel.NORMAL)
	@Test(groups = { "cart", "regression" })
	public void addSpecificProductToCart() {
		getDriver().get(ConfigReader.getProperty("baseUrl"));
		new LoginPage(getDriver()).login("standard_user", "secret_sauce");

		InventoryPage inventoryPage = new InventoryPage(getDriver());
		inventoryPage.removeAllTheProducts(); // Clean slate

		int before = inventoryPage.getCartBadgeCount();
		inventoryPage.addSpecificProductToCart("Sauce Labs Backpack");
		int after = inventoryPage.getCartBadgeCount();

		System.out.println("Cart count before: " + before + ", after: " + after);
		Assert.assertEquals(after, before + 1, "Cart count should increase by 1");
	}

	@DataProvider(name = "sortOptions")
	public Object[][] getSortOptions() {
		return new Object[][] {
				// {"<sortedText>", "<sortType>"//
				{ "Name (A to Z)", "nameAsc" }, { "Name (Z to A)", "nameDesc" }, { "Price (low to high)", "priceAsc" },
				{ "Price (high to low)", "priceDesc" } };
	}

	@Description("Sort Functionality test validation")
	@Severity(SeverityLevel.CRITICAL)
	@Test(dataProvider = "sortOptions", groups = { "sort", "regression" })
	public void testInventorySort(String sortText, String sortType) {
		getDriver().get(ConfigReader.getProperty("baseUrl"));
		new LoginPage(getDriver()).login("standard_user", "secret_sauce");
		InventoryPage inventoryPage = new InventoryPage(getDriver());
		inventoryPage.selectSortOption(sortText);

		boolean isSorted = false;

		switch (sortType) {
		case "nameAsc":
			isSorted = inventoryPage.isProductNameSortingAscending();
			break;
		case "nameDesc":
			isSorted = inventoryPage.isProductNameSortingDesending();
			break;
		case "priceAsc":
			isSorted = inventoryPage.isProductPriceSortingAscending();
			break;
		case "priceDesc":
			isSorted = inventoryPage.isProductPriceSortingDesending();
			break;
		default:
			throw new IllegalArgumentException("Unsupported sort type: " + sortType);

		}

		Assert.assertTrue(isSorted, "❌ Products not sorted correctly for: " + sortText);
		test.pass("✅ Sort validated: " + sortText);

	}
}
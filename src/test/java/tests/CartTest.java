package tests;


import org.testng.Assert;
import org.testng.annotations.*;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import pages.CartPage;
import pages.InventoryPage;
import pages.LoginPage;
import utils.ConfigReader;

public class CartTest extends BaseTest {

	@Description("Cart Page Sanity Check")
	@Severity(SeverityLevel.CRITICAL)
	@Test(priority = 1)
	public void sanityTest_CartPage() {
		getDriver().get(ConfigReader.getProperty("baseUrl"));
		new LoginPage(getDriver()).login("standard_user", "secret_sauce");

		InventoryPage inventoryPage = new InventoryPage(getDriver());
		inventoryPage.addSpecificProductToCart("Sauce Labs Bolt T-Shirt");
		int cartIconCount = inventoryPage.getCartBadgeCount();

		CartPage cartPage = new CartPage(getDriver());
		cartPage.openCart();
		cartPage.getCartTitle();
		int cartListCount = cartPage.getCartItemCount();

		Assert.assertEquals(cartListCount, cartIconCount,
				"Cart Icon count shouldn't match with Cart page product counts");

		Assert.assertTrue(cartPage.checkCheckoutButtonVisibility(), "Checkout button is clicable");

		test.pass("Sanity test completed");

	}

	@Description("Multiple different items show up correctly")
	@Severity(SeverityLevel.CRITICAL)
	@Test(groups = { "Regression", "cart", "integrity" })
	public void addMultipleItems_displaysAllItems() {
		getDriver().get(ConfigReader.getProperty("baseUrl"));
		new LoginPage(getDriver()).login("standard_user", "secret_sauce");

		InventoryPage inventoryPage = new InventoryPage(getDriver());
		inventoryPage.addSpecificProductToCart("Sauce Labs Bike Light");
		inventoryPage.addSpecificProductToCart("Sauce Labs Backpack");

		CartPage cartPage = new CartPage(getDriver());
		cartPage.openCart();
		cartPage.fetchUniqueCartItems();
		int unique = cartPage.getCartItemCount();

		Assert.assertEquals(unique, 2, "Multiple different items not show up correctly");
		test.pass("Multiple different items show up correctly");

	}

	@Description("Remove single item from dual cart items")
	@Severity(SeverityLevel.CRITICAL)
	@Test(groups = { "Regression", "cart", "remove-flow" })
	public void removeItem_updatesRowCount() {
		// Step 1: Open site and log in
		getDriver().get(ConfigReader.getProperty("baseUrl"));
		new LoginPage(getDriver()).login("standard_user", "secret_sauce");

		// Step 2: Add two products
		InventoryPage inventoryPage = new InventoryPage(getDriver());
		inventoryPage.addSpecificProductToCart("Sauce Labs Bike Light");
		inventoryPage.addSpecificProductToCart("Sauce Labs Backpack");

		// Step 3: Navigate to Cart
		CartPage cartPage = new CartPage(getDriver());
		cartPage.openCart(); // Use this instead of isCartAccessible()

		// Step 4: Get item count before removal
		int beforeRemove = cartPage.getCartItemCount();
		System.out.println("Before removal: " + beforeRemove);

		// Step 5: Remove one item
		cartPage.removeItem("Sauce Labs Backpack");

		// Step 6: Get item count after removal
		int afterRemove = cartPage.getCartItemCount();
		System.out.println("After removal: " + afterRemove);

		// Step 7: Assert count is reduced by 1
		Assert.assertEquals(afterRemove, beforeRemove - 1, "Item count did not decrease as expected");

		// Step 8: Report test pass
		test.pass("✅ Removed one product from cart → item count decreased by 1");
	}
	
	/*
	 * @BeforeMethod public void clearCart() {
	 * getDriver().get(ConfigReader.getProperty("baseUrl")); new
	 * LoginPage(getDriver()).login("standard_user", "secret_sauce");
	 * 
	 * CartPage cartPage = new CartPage(getDriver());
	 * 
	 * cartPage.isCartAccessible(); cartPage.removeAllItems(); }
	 */

	@Description("Verify total item price calculation on Cart Page")
	@Severity(SeverityLevel.NORMAL)
	@Test(groups = { "Regression", "cart", "pricing" })
	public void verifyPriceCalculation_cartPage() {
		getDriver().get(ConfigReader.getProperty("baseUrl"));
		new LoginPage(getDriver()).login("standard_user", "secret_sauce");

		InventoryPage inventoryPage = new InventoryPage(getDriver());
		inventoryPage.addSpecificProductToCart("Sauce Labs Bike Light");
		inventoryPage.addSpecificProductToCart("Sauce Labs Backpack");

		CartPage cartPage = new CartPage(getDriver());
		cartPage.openCart();
	    System.out.println("Cart Items: " + cartPage.fetchUniqueCartItems());
		double total = cartPage.getTotalItemPrice();
	    System.out.println("Prices: " + cartPage.getAllItemPrices());


		Assert.assertEquals(total, 39.98, 0.01, "Cart total price is incorrect");

		test.pass("✅ Total item price on Cart Page calculated correctly");

	}
	
	@Description("Verify 'Continue Shopping' button navigates back to Inventory page")
	@Severity(SeverityLevel.NORMAL)
	@Test(groups = {"Regression", "cart", "navigation"})
	public void continueShopping_navigatesToInventory() {
		 getDriver().get(ConfigReader.getProperty("baseUrl"));
		    new LoginPage(getDriver()).login("standard_user", "secret_sauce");

		    InventoryPage inventoryPage = new InventoryPage(getDriver());
		    inventoryPage.addSpecificProductToCart("Sauce Labs Backpack");

		    CartPage cartPage = new CartPage(getDriver());
		    cartPage.openCart();
		    cartPage.clickContinueShopping();
		    
		    String currentUrl = getDriver().getCurrentUrl();
		    Assert.assertTrue(currentUrl.contains("inventory.html"), "Continue Shopping did not return to inventory");

		    test.pass("✅ 'Continue Shopping' button navigated to Inventory page successfully");
	}
}

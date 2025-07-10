package tests;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.*;

import base.BaseTest;
import io.qameta.allure.*;
import pages.CartPage;
import pages.CheckoutPage;
import pages.InventoryPage;
import pages.LoginPage;
import utils.ConfigReader;

public class CheckoutTest extends BaseTest {

	@Test(dataProvider = "checkoutInfoData", groups = { "DDT", "Checkout", "Validation" })
	@Description("Data-driven test for Checkout Info page form validation")
	@Severity(SeverityLevel.CRITICAL)
	@DataProvider(name = "checkoutInfoData")
	public Object[][] getCheckoutFormData() {
		return new Object[][] { { "John", "Doe", "12345", true }, // ✅ Valid input
				{ "", "Doe", "12345", false }, // ❌ Missing first name
				{ "John", "", "12345", false }, // ❌ Missing last name
				{ "John", "Doe", "", false }, // ❌ Missing zip
				{ "", "", "", false } // ❌ All fields blank
		};
	}

	@Test(dataProvider = "checkoutInfoData", groups = { "DDT", "Checkout", "Validation" })
	@Description("Data-driven test for Checkout Info page form validation")
	@Severity(SeverityLevel.CRITICAL)
	public void checkoutInformationPage(String first, String last, String zip, boolean shouldSucceed) {
		// getDriver().manage().deleteAllCookies(); // optional: clear session
		getDriver().get(ConfigReader.getProperty("baseUrl"));
		new LoginPage(getDriver()).login("standard_user", "secret_sauce");

		InventoryPage inventoryPage = new InventoryPage(getDriver());
		inventoryPage.addSpecificProductToCart("Sauce Labs Bolt T-Shirt");

		CartPage cartPage = new CartPage(getDriver());
		cartPage.openCart();
		System.out.println("➡️ URL before cart check: " + getDriver().getCurrentUrl());

		Assert.assertTrue(getDriver().getCurrentUrl().contains("cart.html"), "Cart page did not load");

		List<String> cartItems = cartPage.fetchUniqueCartItems();
		Assert.assertTrue(cartItems.contains("Sauce Labs Bolt T-Shirt"), "Product not found in cart");

		cartPage.clickCheckoutButton();
		Assert.assertTrue(getDriver().getCurrentUrl().contains("checkout-step-one.html"),
				"Checkout step one page did not load");

		CheckoutPage checkoutPage = new CheckoutPage(getDriver());
		Assert.assertTrue(checkoutPage.isOnCheckoutInformationPage(), "Checkout Information page not displayed");

		checkoutPage.fillCheckoutForm(first, last, zip);
		checkoutPage.clickContinue();

		boolean errorVisible = checkoutPage.isErrorMessageVisible();
		String testCaseDesc = String.format("[Input] First: '%s', Last: '%s', Zip: '%s'", first, last, zip);

		if (shouldSucceed) {
			Assert.assertFalse(errorVisible, "❌ Unexpected error for valid data: " + testCaseDesc);
			Assert.assertTrue(getDriver().getCurrentUrl().contains("checkout-step-two.html"),
					"Did not proceed to Overview");
			test.pass("✅ Form accepted valid data: " + testCaseDesc);
		} else {
			Assert.assertTrue(errorVisible, "❌ Error message not shown for invalid input: " + testCaseDesc);
			test.pass("✅ Error message shown as expected for invalid input: " + testCaseDesc);
		}
	}

	@DataProvider(name = "checkoutOverviewData")
	public Object[][] getOverviewData() {
		return new Object[][] { { new String[] { "Sauce Labs Backpack" }, 29.99 },
				{ new String[] { "Sauce Labs Bike Light", "Sauce Labs Bolt T-Shirt" }, 25.98 },
				{ new String[] { "Test.allTheThings() T-Shirt (Red)" }, 15.99 } };
	}

	@Description("Sanity check: Checkout Overview page loads, validates totals, and completes order")
	@Severity(SeverityLevel.CRITICAL)
	@Test(groups = { "Sanity", "Checkout" }, dataProvider = "checkoutOverviewData")
	public void sanityTest_CheckoutOverviewPage(String[] productNames, double expectedSubtotal) {
		getDriver().manage().deleteAllCookies();
		getDriver().get(ConfigReader.getProperty("baseUrl"));
		new LoginPage(getDriver()).login("standard_user", "secret_sauce");

		// Add product
		InventoryPage inventoryPage = new InventoryPage(getDriver());
		for (String products : productNames) {
			inventoryPage.addSpecificProductToCart(products);
			System.out.println("🧪 Trying to add: " + products);
			System.out.println("✅ Added to cart: " + products);

		}

		// Navigate to cart
		CartPage cartPage = new CartPage(getDriver());
		cartPage.openCart();
		Assert.assertTrue(getDriver().getCurrentUrl().contains("cart.html"), "Cart page did not load");

		// Validate product in cart
		List<String> cartItems = cartPage.fetchUniqueCartItems();
		for (String expectedItems : productNames) {
			Assert.assertTrue(cartItems.contains(expectedItems), "Missing item in cart: " + expectedItems);

		}

		// Proceed to checkout info
		cartPage.clickCheckoutButton();
		Assert.assertTrue(getDriver().getCurrentUrl().contains("checkout-step-one.html"),
				"Did not reach Checkout Info page");

		CheckoutPage checkout = new CheckoutPage(getDriver());
		checkout.fillCheckoutForm("Kalai", "Vani", "111-222");
		checkout.clickContinue();

		// Now on Overview page
		Assert.assertTrue(checkout.isOnOverviewPage(), "Not on Checkout Overview page");

		List<String> itemNames = checkout.getItemNames();
		Assert.assertFalse(itemNames.isEmpty(), "No items displayed on overview");

		double subtotal = checkout.getSubtotal();
		double tax = checkout.getTax();
		double total = checkout.getTotal();

		Assert.assertEquals(subtotal, expectedSubtotal, 0.01, "Subtotal mismatch");
		System.out.println("Subtotal: Expected = " + expectedSubtotal + ", Actual = " + subtotal);
		System.out.println("Total: Expected = " + (subtotal + tax) + ", Actual = " + total);

		Assert.assertEquals(subtotal + tax, total, 0.01, "Subtotal + tax does not match total");

		checkout.clickFinish();
		Assert.assertTrue(getDriver().getCurrentUrl().contains("checkout-complete.html"),
				"Order did not complete successfully");

		test.pass("✅ DDT: Products [" + String.join(", ", productNames) + "] → Checkout complete with subtotal: $"
				+ expectedSubtotal);
	}
	@Description("Complete checkout page validation")
	@Severity(SeverityLevel.CRITICAL)
	@Test
	public void validateCheckoutCompletePage() {
		getDriver().manage().deleteAllCookies();
		getDriver().get(ConfigReader.getProperty("baseUrl"));
		new LoginPage(getDriver()).login("standard_user", "secret_sauce");

		// Add product
		InventoryPage inventoryPage = new InventoryPage(getDriver());
		inventoryPage.addSpecificProductToCart("Sauce Labs Bolt T-Shirt");

		// Navigate to cart
		CartPage cartPage = new CartPage(getDriver());
		cartPage.openCart();
		Assert.assertTrue(getDriver().getCurrentUrl().contains("cart.html"), "Cart page did not load");

		
		// Proceed to checkout info
		cartPage.clickCheckoutButton();
		Assert.assertTrue(getDriver().getCurrentUrl().contains("checkout-step-one.html"),
				"Did not reach Checkout Info page");

		CheckoutPage checkout = new CheckoutPage(getDriver());
		checkout.fillCheckoutForm("Kalai", "Vani", "111-222");
		checkout.clickContinue();

		// Now on Overview page
		Assert.assertTrue(checkout.isOnOverviewPage(), "Not on Checkout Overview page");

		checkout.clickFinish();
		Assert.assertTrue(checkout.isCompleted(), "Checkout complete page did not open");
		
		checkout.backToHome();
		Assert.assertTrue(inventoryPage.isInventoryVisible(), "Back to home functionality failed");

		test.pass("Checkout completed");
	}
}
	

	
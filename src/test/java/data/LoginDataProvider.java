package data;

import org.testng.annotations.DataProvider;

public class LoginDataProvider {
	
	@DataProvider(name = "loginCredentials")
	public Object[][] getData(){
		
		
		return new Object[][] {
			  {"standard_user", "secret_sauce", true},
	            {"invalid_user", "secret_sauce", false},
	            {"standard_user", "wrong_pass", false},
	            {"", "secret_sauce", false},
	            {"standard_user", "", false}
		};
		
		
	}

}

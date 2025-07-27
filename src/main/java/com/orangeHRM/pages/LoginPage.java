package com.orangeHRM.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangeHRM.actiondriver.ActionDriver;
import com.orangeHRM.base.BaseClass;

public class LoginPage {

	private ActionDriver actionDriver;
	
	//Define Loacatores using By Class
	
	private By userNameField = By.name("username");
	private By passWordField = By.name("password");
	private By loginButton = By.xpath("(//button[normalize-space()='Login'])[1]");
	private By errorMessage = By.xpath("//p[text()='Invalid credentials']");
	
	
	//Initialize the ActionDriver object by passing WebDriver instance
 /*	public LoginPage(WebDriver driver)
	{
		this.actionDriver = new ActionDriver(driver);
	}
  */
	
	public LoginPage(WebDriver driver)
	{
		this.actionDriver = BaseClass.getActionDriver();
	}
	
	
	//Method to perfom login
	
	public void login(String userName, String password) 
	{
		actionDriver.enterText(userNameField, userName);
		actionDriver.enterText(passWordField, password);
		actionDriver.clcik(loginButton);
	}
	
	//Method to check if error message is displayed
	public boolean isErrorMessageDispalyed()
	{
		return actionDriver.isDisplayed(errorMessage);
	}
	
	//Method to get the text from ErrorMessage
	public String getErrorMessage() 
	{
		return actionDriver.getText(errorMessage);
	}
	
	//Verify if error is correct or not
	public boolean verifyErrorMessage(String expectedError)
	{
		return actionDriver.compareText(errorMessage, expectedError);
	}

}
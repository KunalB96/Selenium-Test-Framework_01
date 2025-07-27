package com.orangeHRM.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangeHRM.actiondriver.ActionDriver;
import com.orangeHRM.base.BaseClass;

public class HomePage {
	
	private ActionDriver actionDriver;
	
	//Initialize the ActionDriver object by passing WebDriver instance
	/*	public HomePage(WebDriver driver)
		{
			this.actionDriver = new ActionDriver(driver);
		}
		*/
	
	public HomePage(WebDriver driver)
	{
		this.actionDriver = BaseClass.getActionDriver();
	}
	
	//Define locators using By class
	private By adminTab = By.xpath("//span[text()='Admin']");
	private By userIDButton = By.className("oxd-userdropdown-name");
	private By logoutButton = By.xpath("//a[text()='Logout']");
	private By orangeHRMlogo = By.xpath("//div[@class='oxd-brand-banner']//img");
	
	
	//Method to verify is Admin tab is visible
	public boolean isAdminTabVisible()
	 {
		return actionDriver.isDisplayed(adminTab);
	 }
	
	public boolean verifyOrangeHRMlogo()
	{
		return actionDriver.isDisplayed(orangeHRMlogo);
	}
	
	//Method to perfom logout operation
	public void logout()
	{
		actionDriver.clcik(userIDButton);
		actionDriver.clcik(logoutButton);
	}
	
			
			

}

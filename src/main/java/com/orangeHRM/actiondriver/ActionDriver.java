package com.orangeHRM.actiondriver;

import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.utilities.ExtentManager;

public class ActionDriver {
	
	private WebDriver driver;
	private WebDriverWait wait;
	public static final Logger logger = BaseClass.logger;
	
	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
		logger.info("WebDriver instance is created");
	}
	
	//Method to click an element
	public void clcik(By by) {
		String elementDescription = getElementDescription(by);
		try {
			applyBorder(by,"green");
			waitForElementToBeClickable(by);
			driver.findElement(by).click();
			ExtentManager.logStep("clicked an element: " +elementDescription);
			logger.info("clicked an element"+elementDescription);
		} catch (Exception e) 
		{
			applyBorder(by,"red");
			System.out.println("Unable to clcik element:"+e.getMessage());
		//	logger.info("unable to clicked an element");
			ExtentManager.logFailure(BaseClass.getDriver(),"Unable to clcik element: ", elementDescription +"_unable to click");
			logger.info("unable to clicked an element");
		}
	}
	
	
	// Method to enter text  into an input field
	public void enterText(By by, String value) {
		try {
			applyBorder(by,"green");
			waitForElementToBeVisible(by);
		//	driver.findElement(by).clear();
		//	driver.findElement(by).sendKeys(value);
			WebElement element = driver.findElement(by);
			element.clear();
			element.sendKeys(value);	
			logger.info("Entered Text:" +getElementDescription(by)+""+ value);
		} catch (Exception e) 
		{
			applyBorder(by,"red");
			logger.error("Unable to enter value in input box"+e.getMessage());
			
		}
	}
	
	// Method to get text from an input field
	public String getText(By by) {
		try {
			applyBorder(by,"green");
			waitForElementToBeVisible(by);
			return driver.findElement(by).getText();
		} catch (Exception e) 
		{
			applyBorder(by,"red");
			logger.error("Unable to get the text"+e.getMessage());
			return "";
		}
	}
	
	
	//Method to compare Two Text
	public boolean compareText(By by, String expectedText) {
		try {
			
			waitForElementToBeVisible(by);
			String actualText = driver.findElement(by).getText();
			if(expectedText.equals(actualText)) 
			{
				applyBorder(by,"green");
				logger.info("Text is Matching: "+actualText+ " equals "+expectedText);
			    ExtentManager.logStepWithScreenShot(BaseClass.getDriver(),"Compare Text", "Text verified Successfully"+ actualText+ " equals"+ expectedText);
				return true;
			}
			else {
				applyBorder(by,"red");
				logger.error("Text is not Matching: "+actualText+ " not equals "+expectedText);
				 ExtentManager.logFailure(BaseClass.getDriver(),"Text comparison failed!", "Text comparison failed"+ actualText+ " not equals"+ expectedText);
				return false;
			}
		} catch (Exception e) {
			applyBorder(by,"red");
			logger.error("Unable to compare Texts:"+e.getMessage());
		    return false;
		}
	}
	
	
	// Method to check element isDisplayed
	/*
	public boolean isDisplayed(By by) {
		try {
			waitForElementToBeVisible(by);
			boolean isDisplayed = driver.findElement(by).isDisplayed();
			if(isDisplayed) {
				System.out.println("Element is displayed");
				return isDisplayed;
			}
			else {
				return isDisplayed;
			}
		} catch (Exception e) {
			System.out.println("Element is not displayed"+e.getMessage());
			return false;
		}
	}
	
	*/
	
	
	//Simplified the method and remove redundant conditions
	public boolean isDisplayed(By by) {
		try {
			applyBorder(by,"green");
			waitForElementToBeVisible(by);
			logger.info("Element is displayed:" +getElementDescription(by));
			ExtentManager.logStepWithScreenShot(BaseClass.getDriver(),"Element is displayed:","Element is displayed:"+getElementDescription(by));
			return driver.findElement(by).isDisplayed();
			
		}
		catch(Exception e) 
		{
			applyBorder(by,"red");
			logger.error("Element is not displayed"+e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(),"Element is not displayed:","Element is not displayed:"+getElementDescription(by));
			return false;
		}
	}
	
	
	//wait for the page to load
	public void waitForPageLoad(int timeOutInSec) {
		try{
			
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver ->((JavascriptExecutor) WebDriver)
					.executeScript("return document.readtState").equals("complete"));
		//	logger.info("Page load successfully");
			logger.info("Page load successfully");
		}
		
		catch(Exception e) 
		{
			
			logger.error("Page did not load within "+ timeOutInSec+ " seconds. Exception: "+e.getMessage());
		}
	}
	
	//Scroll to Element
	public void scrollToElement(By by) {
		try {
			applyBorder(by,"green");
			JavascriptExecutor js = (JavascriptExecutor)driver;
			WebElement element = driver.findElement(by);
			js.executeScript("arguments[0],scrollIntoView(true);", element);
		} catch (Exception e) 
		{
			applyBorder(by,"red");
			logger.error("Unable to locate Element"+e.getMessage());
			
		}
	}
	
	//Wait for element to be clickable
	private void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			logger.error("Element is not clickable"+e.getMessage());
		}
	}
	
	//Wait for Element to be Visible
	private void waitForElementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			logger.error("Element is not visible"+e.getMessage());
		}
	}
	
	
	//Method to get description of an element using By locator
	
	public String getElementDescription(By locator)
	{
		//check for null driver or locator to avoid nullpointerexception
		
		if(driver==null)
		
		return "driver is null";
		if(locator ==null)
			return"Locator is null";
		
		//find the element using the locator
		WebElement element= driver.findElement(locator);
		
		//Get element Description Attribute
		String name= element.getDomAttribute("name");
		String id= element.getDomAttribute("id");
		String text= element.getText();
		String className= element.getDomAttribute("class");
		String placeholder= element.getDomAttribute("placeholder");
		
		//Return the description based on element attributes
		try {
			if(isNotEmpty(name))
			{
				return "Element with name: "+name;
			}
			else if (isNotEmpty(id))
			{
				return "Element with id: "+id;
			}
			else if (isNotEmpty(text))
			{
				return "Element with text: "+truncate(text,60);
			}
			else if (isNotEmpty(className))
			{
				return "Element with className: "+className;
			}
			else if (isNotEmpty(placeholder))
			{
				return "Element with placeholder: "+placeholder;
			}
		} catch (Exception e) {
			logger.error("Unable to describe the element "+e.getMessage());
		}
		return "Unable to describe the element";
		
	}
	
	//Utility method to check a string is not NULL or Empty
	
	private boolean isNotEmpty(String value)
	{
		return value!=null && !value.isEmpty();
	}
	
	//Utility Method to truncate long string
	private String truncate(String value, int maxLength)
	{
		if(value==null || value.length()<=maxLength)
{
	return value;
}
		return value.substring(0,maxLength)+"....";
	}
	
	
	//Utility Method to Border an element 
	public void applyBorder(By by, String color)
	{
		try {
			//Locate the element
			WebElement element = driver.findElement(by);
			//Apply the Border
			String script = "arguments[0].style.border='3px solid "+color+"'";
			JavascriptExecutor js =(JavascriptExecutor) driver;
			js.executeScript(script, element);
			logger.info("Applied the border with color"+color+" to element "+getElementDescription(by));
		} catch (Exception e) {
			logger.warn("Failed to apply border to an element:"+getElementDescription(by),e);
			
		}
	}
	

}

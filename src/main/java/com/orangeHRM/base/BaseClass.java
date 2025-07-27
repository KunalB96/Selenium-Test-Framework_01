package com.orangeHRM.base;

import java.io.FileInputStream;

import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.orangeHRM.actiondriver.ActionDriver;
import com.orangeHRM.utilities.ExtentManager;
import com.orangeHRM.utilities.LoggerManager;

public class BaseClass {

	protected static Properties prop;
//	protected static WebDriver driver;
//	private static ActionDriver actionDriver;
	
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actiondriver = new ThreadLocal<>();
	
	
	public static final Logger logger= LoggerManager.getLogger(BaseClass.class);
	

	@BeforeSuite
	public void loafConfig() throws IOException {
		// load configuration file
		prop = new Properties();
		FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
		prop.load(fis);
		logger.info("config.properties file loaded");
	//	logger.info("config.properties file loaded");
		
		//Start the Extent Report
	//	ExtentManager.getReporter();  This has been implemented in TestListener
	}

	@BeforeMethod
	public synchronized void setup() throws IOException {
		System.out.println("Setting up WebDriver for:" + this.getClass().getSimpleName());
		launchBrowser();
		configureBrowser();
		staticWait(3);
		
		logger.info("WebDriver Initialized and Browser maximized");
		logger.trace("This is Trace message");
		logger.error("This is error message");
		logger.debug("This is debug message");
		logger.fatal("This is fatal message");
		logger.warn("This is warn message");
		
	/*	//Initialize the ActionDriver only once
		if(actionDriver==null) {
		actionDriver = new ActionDriver(driver);
		System.out.println("ActionDriver instance is created");
		}
		*/
		
		//Initialize ActionDriver for the current Thread
		actiondriver.set(new ActionDriver(getDriver()));
		logger.info("ActionDriver initlized for thread: "+Thread.currentThread().getId());

	}

	// Initialize the WebDriver based on browser defined in config.properties file
	private synchronized void launchBrowser() {

		String browser = prop.getProperty("browser");

		if (browser.equalsIgnoreCase("chrome")) {
		//	driver = new ChromeDriver();
			driver.set(new ChromeDriver());
			ExtentManager.registerDriver(getDriver());
			logger.info("ChromeDriver instance is created");		}

		else if (browser.equalsIgnoreCase("firefox")) {
		//	driver = new FirefoxDriver();
			driver.set(new FirefoxDriver());
			ExtentManager.registerDriver(getDriver());
			logger.info("FirefoxDriver instance is created");
		}

		else if (browser.equalsIgnoreCase("edge")) {
		//	driver = new EdgeDriver();
			driver.set(new EdgeDriver());
			ExtentManager.registerDriver(getDriver());
			logger.info("EdgeDriver instance is created");
		} else {
			throw new IllegalArgumentException("Browser Not Supported" + browser);
		}
	}

	// Configure browser settings setting such as implicit wait, maximize the
	// browser and navigate to the url
	private void configureBrowser() {
		// Implicit Wait
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

		// Maximize the browser
		getDriver().manage().window().maximize();

		// Navigate to URL
		try {
			getDriver().get(prop.getProperty("url"));
		} catch (Exception e) {
			System.out.println("Failed to Navigate to the URL" + e.getMessage());
		}
	}

	@AfterMethod
	public synchronized void tearDown() {

		if (driver != null) {
			try {
				getDriver().quit();
			} catch (Exception e) {
				System.out.println("Unable to quit the Browser" + e.getMessage());
			}
		}
		logger.info("WebDriver instance is closed");
	    driver.remove();
	    actiondriver.remove();
	//	driver=null;
	//	actiondriver=null;
	 //   ExtentManager.endTest();  This has been implemented in TestListener
	}
	
 
	//Getter Method for prop
	public static Properties  getProp() {
		return prop;
	}
	
 /*	
	//Driver getter method
	public WebDriver getDriver() {
		return driver;
	}
	
	*/
	
	
	//Getter Method for WebDriver
	public static WebDriver getDriver()
	{
		if(driver.get()==null) {
			System.out.println("WebDriver is not initialized");
			throw new IllegalStateException("WebDriver is not initialized");
		}
		return driver.get();
	}
	
	//Getter Method For ActionDriver
	public static ActionDriver getActionDriver()
	{
		if(actiondriver.get()==null) {
			System.out.println("ActionDriver is not initialized");
			throw new IllegalStateException("ActionDriver is not initialized");
		}
		return actiondriver.get();
	}
	
	//Driver setter Method
	public void setDriver(ThreadLocal<WebDriver> driver) {
		this.driver = driver;
	}
	
	//Static wait for pause
	public void staticWait(int seconds)
	{
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}
}

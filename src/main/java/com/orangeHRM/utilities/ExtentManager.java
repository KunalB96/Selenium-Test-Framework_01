package com.orangeHRM.utilities;

import static org.testng.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
	private static Map<Long,WebDriver> driverMap = new HashMap<>();
	
	
	//Initialize the Extent Report
	public synchronized static ExtentReports getReporter()
	{
		if(extent==null)
		{
			String reportPath = System.getProperty("user.dir")+"/src/test/resources/ExtentReport/ExtentReports.html";
			ExtentSparkReporter spark  = new ExtentSparkReporter(reportPath);
			spark.config().setReportName("Automation Test Report");
			spark.config().setDocumentTitle("OrangeHRM Report");
			spark.config().setTheme(Theme.DARK);
			
		extent	= new ExtentReports();
		extent.attachReporter(spark);
		
		//Adding system information
		extent.setSystemInfo("Operation System",System.getProperty("os.name"));
		extent.setSystemInfo("Java Version",System.getProperty("java.version"));
		extent.setSystemInfo("User Name",System.getProperty("user.name"));
		}
		
		return extent;
	}
	
	
	//Start the test
	public synchronized static ExtentTest startTest(String testName)
	{
	 ExtentTest extentTest = getReporter().createTest(testName);
	 test.set(extentTest);
	 return extentTest;
	}
	
	//End a Test
	public synchronized static void endTest()
	{
		getReporter().flush();
	}
	
	//Get Current Thread Test
	public synchronized static ExtentTest getTest()
	{
		return test.get();
	}
	
	//Method to get the name of the current test
	public static String getTestName()
	{
		ExtentTest	currentTest = getTest();
		if(currentTest==null)
		{
			return currentTest.getModel().getName();
		}
		else {
			return "No test is currently active for this thread";
		}
	}
	
	
	//Log a step
	public static void logStep(String logMessage)
	{
		getTest().info(logMessage);
	}
	
	//Log step validation with scrrenshot
	public static void logStepWithScreenShot(WebDriver driver, String logMessage, String screenShotMessage)
	{
		getTest().pass(logMessage);
		
		//Scrrenshot Method
		attachScreenshot(driver,screenShotMessage);
		
	}
	
	// Log a Failure
	public static void logFailure(WebDriver driver, String logMessage, String screenShotMessage)
	{
		String colorMessage = String.format("<span style='color:red;'>%s</span>", logMessage);
		getTest().fail(colorMessage);
		
		//Screenshot Method
		attachScreenshot(driver,screenShotMessage);
	}
	
	// Log skip
	public static void logskip(String logMessage)
	{
		String colorMessage = String.format("<span style='color:orange;'>%s</span>", logMessage);
		getTest().skip(colorMessage);
	}
	
	//Take a screenshot with date and time in the file
	public synchronized  static String takeScreenshot(WebDriver driver, String screenshotName)
	{
		TakesScreenshot ts = (TakesScreenshot)driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		
		//Fomat DATE and Time for file name
		String timeStamp= new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
	
		//Saving the Screenshots to a file
		String destPath = System.getProperty("user.dir")+"/src/test/resources/screenshots/"+screenshotName+"_"+timeStamp+".png";
	
		File finalPath = new File(destPath);
		try{
			FileUtils.copyFile(src, finalPath);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		//convert screenshot to Base64 for embedding in the report
		String base64Format = converToBase64(src);
		return base64Format;
	}
	
	//Convert scrrenshot to Base64Format
	public static String converToBase64(File screenShotFile)
	{
		String base64Format="";
		//ReAD THE FILE CONTENT into a byte array
		
		try {
			byte[] fileContent = FileUtils.readFileToByteArray(screenShotFile);
			base64Format = Base64.getEncoder().encodeToString(fileContent);
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return base64Format;
	}
	
	//Attach screenshot to report using Base64
	public  static void  attachScreenshot(WebDriver driver, String message)
	{
		try {
			String screenShotBase64 = takeScreenshot(driver, getTestName());
			getTest().info(message,com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String(screenShotBase64).build());
		} catch (Exception e) {
			getTest().fail("Failed to attach scrrenshot" + message);
			e.printStackTrace();
		}
		
	}
	
	
	
	//Register WebDriver for current Thread
	public static void registerDriver(WebDriver driver)
	{
		driverMap.put(Thread.currentThread().getId(), driver);
	}
	
}

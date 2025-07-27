package com.orangeHRM.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.utilities.ExtentManager;

public class TestListener implements ITestListener {

	//Triggered when a test start
	@Override
	public void onTestStart(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		ExtentManager.startTest(testName);
		ExtentManager.logStep("Test started: " +testName);
	}

	//Triggered when a test succeeds
	@Override
	public void onTestSuccess(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		ExtentManager.logStepWithScreenShot(BaseClass.getDriver(),"Test Passed Successfully","Test End: " +testName+" - ✔ Test Passed");
	}

	//Triggered when a test Failed
	@Override
	public void onTestFailure(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		String failureMessage = result.getThrowable().getMessage();
		ExtentManager.logStep(failureMessage);
		ExtentManager.logFailure(BaseClass.getDriver(),"Test Failed","Test End: "+ testName+ "- ❌ Test Failed");
	}

	//Triggered whent test skip
	@Override
	public void onTestSkipped(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		ExtentManager.logskip("Test Skipped "+testName);
	}

	//Triggerred when a suite starts
	@Override
	public void onStart(ITestContext context) {
		//Initialize the Extent Reports
		ExtentManager.getReporter();
		
	}

	//Triggerred when a suite ends
	@Override
	public void onFinish(ITestContext context) {
		//Flush the Extent Reports
		ExtentManager.endTest();
	}

	
	
	

}

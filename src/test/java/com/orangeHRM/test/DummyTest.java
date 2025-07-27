package com.orangeHRM.test;

import org.testng.SkipException;
import org.testng.annotations.Test;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.utilities.ExtentManager;

public class DummyTest extends BaseClass {
	
	@Test
	public void dummyTest() 
	{
 //	ExtentManager.startTest("Dummytest 1 start");  This has been implemented in TestListener
	String title = getDriver().getTitle();
	ExtentManager.logStep("verifying the title");
	assert title.equals("OrangeHRM"):"Test Failed - Title is Not Matching";

	System.out.println("Test Passed - Title is Matching");
	ExtentManager.logskip("This case is skip");
	throw new SkipException("Skipping the test as part of Testing");
	}

}

package com.orangeHRM.test;

import org.testng.annotations.Test;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.utilities.ExtentManager;

public class DummyTest2 extends BaseClass {
	
	@Test
	public void dummyTest2() 
	{
 //	ExtentManager.startTest("Dummytest 3 start");  This has been implemented in TestListener	
	String title = getDriver().getTitle();
	ExtentManager.logStep("verifying the title");
	assert title.equals("OrangeHRM"):"Test Failed - Title is Not Matching";

	System.out.println("Test Passed - Title is Matching");
	ExtentManager.logStep("Validation Successfull");
	}

}

package pageObject;

import org.openqa.selenium.JavascriptExecutor;

import com.aventstack.extentreports.ExtentTest;

import io.qameta.allure.Step;
import utility.Util;

public class AppGeneric extends Util{
	
	public AppGeneric(String propPath, ExtentTest logger) {
		super(propPath, logger);
	}

	/**
	 * Custom method to launch the Application underTest
	 */
	@Step("openApplication")
	public AppGeneric openApplication(){
		navigateTo("appurl");
		return this;
	}
	
	@Step("getpanelistId")
	public String getpanelistId(){
		return ((JavascriptExecutor)getDriver()).executeScript("return window.corona.respondentInfo.id").toString();
	}

	@Step("getNovaSession")
	public String getNovaSession() throws InterruptedException{
		return getDriver().manage().getCookieNamed("corona_session").getValue();
	}

}

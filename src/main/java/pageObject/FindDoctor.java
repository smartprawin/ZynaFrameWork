package pageObject;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.ExtentTest;

import io.qameta.allure.Step;

public class FindDoctor extends HomePage{
	
	public FindDoctor(String propPath, ExtentTest logger) {
		super(propPath,logger);

	}
	
	@Step("isFindDoctorPageloaded")
	public FindDoctor isFindDoctorPageloaded() throws Throwable {
		
		try {
			if (myFind("findoctorLogo_xpath").getText().contains("Find a Doctor")) {
				Log("FindDoctor Page loaded");
			}
		}catch (Exception e) {
			Log("FindDoctor Page not loaded");
		}
		return this;
	}


	@Step("clickPrimaryCareButton")
	public FindDoctor clickPrimaryCareButton() {
		myFind("primaryCareBtn_xpath").click();
		Log("Clicked on 'Primary Care Providers' Count : "+getproviderCount());
		return  this;
	}
	
	@Step("clickOBGYButton")
	public FindDoctor clickOBGYButton() {
		myFind("OBGYNBtn_xpath").click();
		Log("Clicked on 'Obstetrics and Gynecology' Count : "+getproviderCount());
		return  this;
	}
	
	@Step("clickOBGYButton")
	public FindDoctor AssertCount(int providercount) {
		Assert.assertEquals(providercount, Integer.parseInt(getproviderCount()));
		Log("Provider Count Matched : "+providercount);
		return  this;
	}
	
	@Step("clickCardiologyButton")
	public FindDoctor clickCardiologyButton() {
		myFind("cardiologyBtn_xpath").click();
		Log("Clicked on 'Cardiology' Count : "+getproviderCount());
		return  this;
	}
	
	@Step("clickOrthopedicsButton")
	public FindDoctor clickOrthopedicsButton() {
		myFind("orthopedicsBtn_xpath").click();
		Log("Clicked on 'Orthopedics' Count : "+getproviderCount());
		return  this;
	}
	
	@Step("getfirst10Healthprovider")
	public FindDoctor getfirst10Healthprovider() {
		myFindVisibility("findoctorLogo_xpath");
		List<WebElement> first10Healthprovider = getLocators("first10Healthprovider_xpath");
		System.out.println("** 10 Health Providers **");
		for (WebElement provider : first10Healthprovider) {
			Log(provider.getText()+",");
		}
		return  this;
	}
	
	public String getproviderCount() {
		myFindVisibility("searchResult_xpath");
		return  myFindVisibility("providerCount_xpath").getText().toString().split(" ")[0];
		
	}
	
	public FindDoctor back() {
		getDriver().navigate().back();
		myFindVisibility("findoctorLogo_xpath");
		return  this;
	}
	
}

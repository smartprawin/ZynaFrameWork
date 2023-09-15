package pageObject;


import java.util.Hashtable;

import org.openqa.selenium.Keys;

import com.aventstack.extentreports.ExtentTest;

import io.qameta.allure.Step;

public class HomePage extends AppGeneric{
	
	public HomePage(String propPath, ExtentTest logger) {
		super(propPath,logger);
		
	}
	
	@Step("isHomePageloaded")
	public HomePage isHomePageloaded(Hashtable<String,String> data) throws Throwable {
		launchInstance(data);
		threadSleep(1000);
		openApplication();
		threadSleep(2000);

		try {
			if (myFind("homepageLogo_xpath").getAttribute("alt").equalsIgnoreCase("Mercy Health")) {
				Log("HomePage loaded");

			}
		}catch (Exception e) {
			Log("HomePage not loaded");
		}
		return this;
	}
	
	
	
	@Step("clicklocationGeoButton")
	public HomePage clicklocationGeoButton() {
		threadSleep(3000);
		myFind("locationGeo_id").click();
		threadSleep(1000);
		Log("Clicked on Geo Location Button");
		return  this;
	}
	
	@Step("setDefaultlocation")
	public HomePage setDefaultlocation() {
		threadSleep(3000);
		myFind("locationSearchBox_xpath").sendKeys("Cincinnati, OH 45201, USA");
		threadSleep(2000);
		myFind("defaultLocation_xpath").sendKeys(Keys.DOWN);
		threadSleep(1000);
		myFind("defaultLocation_xpath").sendKeys(Keys.ENTER);
		Log("Location Set : Cincinnati, OH 45201, USA");
		return this;
	}
	
	@Step("setDefaultlocation")
	public HomePage setlocation(String Location) {
		threadSleep(3000);
		myFind("locationSearchBox_xpath").sendKeys(Location);
		threadSleep(2000);
		myFind("defaultLocation_xpath").sendKeys(Keys.DOWN);
		threadSleep(1000);
		myFind("defaultLocation_xpath").sendKeys(Keys.ENTER);
		Log("Location Set : Cincinnati, OH 45201, USA");
		return this;
	}
	
	@Step("clicksaveLocationButton")
	public HomePage clicksaveLocationButton() {
		threadSleep(2000);
		myFind("saveLocationButton_xpath").click();
		Log("Clicked on Save Location Button");
		return  this;
	}
	
	
	@Step("clickFindDoctorButton")
	public FindDoctor clickFindDoctorButton() {
		threadSleep(2000);
		myFind("findoctorButton_xpath").click();
		Log("Clicked on Find A Doctor Button");
		return (FindDoctor) this;
	}
	
	@Step("clickconditionsTreatments")
	public ConditionsTreatments clickconditionsTreatments() {
		threadSleep(2000);
		myFind("conditionsTreatments_xpath").click();
		Log("Clicked on Conditions & Treatments Link");
		return (ConditionsTreatments) this;
	}
}

package pageObject;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.ExtentTest;

import io.qameta.allure.Step;

public class ConditionsTreatments extends HomePage{
	
	public ConditionsTreatments(String propPath, ExtentTest logger) {
		super(propPath,logger);

	}
	
	
	
	@Step("isConditionsTreatmentsPageloaded")
	public ConditionsTreatments isConditionsTreatmentsPageloaded() throws Throwable {
		
		try {
			if (myFind("conditionsTreatmentsLogo_xpath").getText().contains("Conditions and Treatments")) {
				pass("ConditionsTreatments Page loaded");
			}
		}catch (Exception e) {
			Thread.sleep(2000);
			if (myFind("conditionsTreatmentsLogo_xpath").getText().contains("Conditions and Treatments")) {
					pass("ConditionsTreatments Page loaded");
			}else {
			error("ConditionsTreatment Page not loaded");}
		}
		return this;
	}

	@Step("ClickCondition")
	public ConditionsTreatments ClickCondition(String Name) {
		By ConditionName = By.xpath("//div[@class='h3 col mb0 relative pr4']/a[text()='"+Name+"']");
		myFind(ConditionName,1,10).click();
		Log("Condition : "+Name+" Clicked");
		return this;
	}
	
	@Step("getDoctorsForCondition")
	public ConditionsTreatments getDoctorsForCondition() {
		myFindVisibility("causeTitle_xpath");
		List<WebElement> healthproviders_Webelement= getLocators("findDoctorForCondition_xpath");
		List<String> healthproviders = new ArrayList<String>();
		for (WebElement provider : healthproviders_Webelement) {
			healthproviders.add(provider.getText());
		}
		System.out.println(healthproviders);
		Log("Health providers : "+healthproviders.size());
		return this;

	}
	

}

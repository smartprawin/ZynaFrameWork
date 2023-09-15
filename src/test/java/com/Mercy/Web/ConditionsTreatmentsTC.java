package com.Mercy.Web;

import java.util.Hashtable;

import org.testng.Reporter;
import org.testng.annotations.Test;

import com.Mercy.Base.BaseClass;

import excelHelper.ExcelReader;
import fileHandler.JsonHandler;
import io.qameta.allure.Description;
import pageObject.ConditionsTreatments;
import utility.Constants;

public class ConditionsTreatmentsTC extends BaseClass{
	
	private static ConditionsTreatments conditionsTreatments;
	private static String Location = "Cincinnati, OH 45201, USA";
	private static String ConditionName = "Abdominal Pain";

	public ConditionsTreatmentsTC() {
		xls = new ExcelReader(Constants.DATAFILE);
		jSon = new JsonHandler(Constants.JSONFILE);
		propPath = Constants.MercyObjectRepository;
		testName = "Mercy Conditions and Treatments";
	}

	@Test(dataProvider = "getWebData", description = "ConditionsTreatments", groups= {"xls"})
	@Description("TC_Description")
	@MyAnnotation(testName = "ConditionsTreatments")
	public void TC_Description_XLS(Hashtable<String,String> data) throws Throwable {
		if (!data.get("RunMode").equalsIgnoreCase("N")){
			node = test.createNode(data.get("TestName"));
			conditionsTreatments = new ConditionsTreatments(propPath, node);
			conditionsTreatments.setBrowserStackCap(data, "mercy", data.get("os")+" "+data.get("browser")+" "+data.get("TestName"));
			Reporter.getCurrentTestResult().setAttribute("object", conditionsTreatments);
			conditionsTreatments.isHomePageloaded(data)
			.clicklocationGeoButton()
			.setlocation(Location)
			.clicksaveLocationButton()
			.clickconditionsTreatments()
			.isConditionsTreatmentsPageloaded()
			.ClickCondition(ConditionName)
			.getDoctorsForCondition()
			.closeInstance();	
		}
	}
	

	
	@Test(dataProvider = "getJsonData", description = "ConditionsTreatments")
	@Description("TC_Description")
	@MyAnnotation(testName = "ConditionsTreatments")
	public void TC_Description_JSON(Hashtable<String,String> data) throws Throwable {
		if (!data.get("RunMode").equalsIgnoreCase("N")){
			node = test.createNode(data.get("TestName"));
			conditionsTreatments = new ConditionsTreatments(propPath, node);
			conditionsTreatments.setSauceLabsCaps(data, "mercy", data.get("TestName"));
			Reporter.getCurrentTestResult().setAttribute("object", conditionsTreatments);
			conditionsTreatments.isHomePageloaded(data)
			.clicklocationGeoButton()
			.setlocation(Location)
			.clicksaveLocationButton()
			.clickconditionsTreatments()
			.isConditionsTreatmentsPageloaded()
			.ClickCondition(ConditionName)
			.getDoctorsForCondition()
			.closeInstance();	
		}
	}
	
}
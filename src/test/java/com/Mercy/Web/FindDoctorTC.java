package com.Mercy.Web;

import java.util.Hashtable;

import org.testng.Reporter;
import org.testng.annotations.Test;

import com.Mercy.Base.BaseClass;

import excelHelper.ExcelReader;
import fileHandler.JsonHandler;
import io.qameta.allure.Description;
import pageObject.FindDoctor;
import utility.Constants;

public class FindDoctorTC extends BaseClass{
	
	private FindDoctor findDoctor;
	private static String Location = "Cincinnati, OH 45201, USA";

	public FindDoctorTC() {
		xls = new ExcelReader(Constants.DATAFILE);
		jSon = new JsonHandler(Constants.JSONFILE);
		propPath = Constants.MercyObjectRepository;
		testName = "Mercy FindDoctor";
	}

	@Test(dataProvider = "getWebData", description = "FindDoctor", groups= {"xls"})
	@Description("TC_FindDoctor")
	@MyAnnotation(testName = "FindDoctor")
	public void TC_FindDoctor_XLS(Hashtable<String,String> data) throws Throwable {
		if (!data.get("RunMode").equalsIgnoreCase("N")){
			node = test.createNode(data.get("TestName"));
			findDoctor = new FindDoctor(propPath, node);
			findDoctor.setBrowserStackCap(data, "mercy", data.get("os")+" "+data.get("browser")+" "+data.get("TestName"));
			Reporter.getCurrentTestResult().setAttribute("object", findDoctor);
			findDoctor.isHomePageloaded(data)
			.clicklocationGeoButton()
			.setlocation(Location)
			.clicksaveLocationButton()
			.clickFindDoctorButton()
			.isFindDoctorPageloaded()
			.clickPrimaryCareButton()
			.back()
			.clickCardiologyButton()
			.back()
			.clickOrthopedicsButton()
			.back()
			.clickOBGYButton()
			.AssertCount(60)
			.closeInstance();
		}
	}
	
	@Test(dataProvider = "getJsonData", description = "FindDoctor")
	@Description("TC_FindDoctor")
	@MyAnnotation(testName = "FindDoctor")
	public void TC_FindDoctor_JSON(Hashtable<String,String> data) throws Throwable {
		if (!data.get("RunMode").equalsIgnoreCase("N")){
			node = test.createNode(data.get("TestName"));
			findDoctor = new FindDoctor(propPath, node);
			findDoctor.setSauceLabsCaps(data, "mercy", data.get("TestName"));
			Reporter.getCurrentTestResult().setAttribute("object", findDoctor);
			findDoctor.isHomePageloaded(data)
			.clicklocationGeoButton()
			.setlocation(Location)
			.clicksaveLocationButton()
			.clickFindDoctorButton()
			.isFindDoctorPageloaded()
			.clickPrimaryCareButton()
			.back()
			.clickCardiologyButton()
			.back()
			.clickOrthopedicsButton()
			.back()
			.clickOBGYButton()
			.AssertCount(65)
			.closeInstance();
		}
	}
	
	
	
	
}

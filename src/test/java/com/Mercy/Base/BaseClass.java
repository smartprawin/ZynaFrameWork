package com.Mercy.Base;

import static java.lang.annotation.ElementType.METHOD;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.zeroturnaround.zip.ZipUtil;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import excelHelper.DataUtils;
import excelHelper.ExcelReader;
import fileHandler.JsonHandler;
import fileHandler.TextHandler;
import reporting_Analytics.SendEmail;
import utility.Constants;
import utility.Util;

public class BaseClass {

	protected static ExtentReports report;
	protected static ExtentSparkReporter spark;
	protected ExtentTest node;

	protected ExtentTest test;
	protected static ExcelReader xls;
	protected static JsonHandler jSon;
	protected String testName;
	protected String propPath;
	protected static String extentReportHtmlPath;


	@BeforeSuite(alwaysRun = true)
	public void initiateReport() throws IOException {
		extentReportHtmlPath = Constants.REPORT_PATH+getDate()+"/"+getTime()+".html";
		TextHandler TextHandler = new TextHandler();
		TextHandler.write("ExtentReport/currentLocation.txt");
		TextHandler.writeLine(extentReportHtmlPath);
		
		report = new ExtentReports();
		spark = new ExtentSparkReporter(extentReportHtmlPath);

		spark.config().setTheme(Theme.DARK);
		spark.config().setDocumentTitle("Mercy POC");
		report.attachReporter(spark);

	}

	@BeforeTest()
	public void setUpReport() throws IOException {
		if(spark==null) 
			initiateReport();
		test = report
				.createTest(testName)
				.assignAuthor(System.getProperty("user.name"));
	}

	@BeforeMethod (onlyForGroups= {"xls"})
	public void isTestRunable() {
		if (DataUtils.isSkip(xls, testName)){
			test.log(Status.SKIP, "Skipping test case" + testName + " as runmode set to NO in excel");
			throw new SkipException("Skipping test case" + testName + " as runmode set to NO in excel");
		}
	}

	@AfterMethod(alwaysRun = true)
	public void getResult(ITestResult result) throws Exception{
		if(result.getStatus() == ITestResult.FAILURE) {
			if(((Util) result.getAttribute("object")) != null) {
				((Util) result.getAttribute("object")).updateBrowserStack("failed", result.getThrowable().getMessage().toString().replaceAll("\"","'").split("\\r?\\n")[0].toString());
				node.fail(result.getThrowable().getMessage().toString().replaceAll("\"","'").split("\\r?\\n")[0].toString());
				node.fail("Snapshot below: ", 
						MediaEntityBuilder.createScreenCaptureFromPath(((Util) result.getAttribute("object")).capture("Failed")).build());
				((Util) result.getAttribute("object")).closeInstance();
			} else
				node.fail(result.getThrowable().getMessage().toString().replaceAll("\"","'").split("\\r?\\n")[0].toString());
		}else if(result.getStatus() == ITestResult.SUCCESS && node!=null){
			node.pass("Completed");
		}else if(result.getStatus() == ITestResult.SKIP && node!=null){
			node.skip("Skipped the TestCase as the flag is set to NO");
		}
	}

	@AfterSuite(alwaysRun = true)
	public void closeReport() {
		report.flush();
		// if(Constants.isEmailReport)
			// sendEmail();
	}

	@DataProvider()
	public Object[][] getApiData(Method method) {
		MyAnnotation annotation = method.getAnnotation(MyAnnotation.class);
		testName = annotation.testName();
		System.out.println("Data From XLS");
		return DataUtils.getData(xls, annotation.testName(), "Api");
	}

	@DataProvider()
	public Object[][] getWebData(Method method) {
		MyAnnotation annotation = method.getAnnotation(MyAnnotation.class);
		testName = annotation.testName();
		System.out.println("Data From XLS");
		return DataUtils.getData(xls, annotation.testName(), "Web");
	}
	
	@DataProvider()
	public Object[][] getJsonData(Method method) {
		MyAnnotation annotation = method.getAnnotation(MyAnnotation.class);
		testName = annotation.testName();
		System.out.println("Data From JSON");
		return jSon.getData(testName);
	}


	@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
	@Target({METHOD})
	public
	@interface MyAnnotation {
		String testName() default "";
	}

	protected String getDate() {
		return java.time.LocalDate.now().toString();
	}

	private String getTime() {
		String replace = java.time.LocalTime.now().toString().replace(":", "-").replace(".", "-");
		return replace;
	}

	//@Test
	// public void sendEmail() {
	// 	String path = Constants.REPORT_PATH+getDate();
	// 	SendEmail send = new SendEmail();
	// 	ZipUtil.pack(new File(path), new File(path+".zip"));
	// 	send.attach(path+".zip");
	// }
}

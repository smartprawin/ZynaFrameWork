package utility;


public class Constants {

	public static final String MercyObjectRepository = System.getProperty("user.dir")+"/src/test/resources/objectRepository/mercy.properties";
	
	/*** Util variables for WebDriver wait***/
	public static final int DEFAULT_DURATION = 20;
	public static final int DEFAULT_INTERVAL = 2000;
	
	//EMAIL DETAILS
	public static final String SENDTO = "vajinkyabhagwan.gangawane@cesltd.com";
	public static final String SENDFROM = "avignesh.dhakshnamoorthy@cesltd.com";
	public static final String EMAILPASSWORD = "Kmdv@1990";

	//Browser Stack
	public static final String BSTACK_USERNAME = "";
	public static final String BSTACK_ACCESS_KEY = "";
	public static final String BSTACK_URL = "https://" + BSTACK_USERNAME + ":" + BSTACK_ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub";

	//Sauce Labs
	public static final String SAUCE_USERNAME = "oauth-vigneshdhakshnamoorthy-b9a86";
	public static final String SAUCE_ACCESS_KEY = "88dac0ef-fc2b-4349-a3ca-a3f53492b2b2";
	public static final String SAUCE_URL = "https://"+SAUCE_USERNAME+":"+SAUCE_ACCESS_KEY+"@ondemand.eu-central-1.saucelabs.com:443/wd/hub";
		
	//Perfecto
	public static final String PERFECTO_HOST = "Mobilecloud.perfectomobile.com";
	public static final String PERFECTO_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI2ZDM2NmJiNS01NDAyLTQ4MmMtYTVhOC1kODZhODk4MDYyZjIifQ.eyJpYXQiOjE2NTU4Nzk3NTcsImp0aSI6Ijc2NTE2ODg0LWFhMTQtNDRmYS1hYzZiLTY0NGVlODBmNjBmYyIsImlzcyI6Imh0dHBzOi8vYXV0aDMucGVyZmVjdG9tb2JpbGUuY29tL2F1dGgvcmVhbG1zL3RyaWFsLXBlcmZlY3RvbW9iaWxlLWNvbSIsImF1ZCI6Imh0dHBzOi8vYXV0aDMucGVyZmVjdG9tb2JpbGUuY29tL2F1dGgvcmVhbG1zL3RyaWFsLXBlcmZlY3RvbW9iaWxlLWNvbSIsInN1YiI6IjI1Y2RiZTM2LWIwMTMtNDFmZC1iNmYzLWNlNDY5NDQ4ZDE3MiIsInR5cCI6Ik9mZmxpbmUiLCJhenAiOiJvZmZsaW5lLXRva2VuLWdlbmVyYXRvciIsIm5vbmNlIjoiMTlkNzRjZTktMDFlNS00NDQyLTg2MjQtNmQ5NDc0YTA3NjVmIiwic2Vzc2lvbl9zdGF0ZSI6IjU0MzRlZGU5LWJkMGQtNGNkMy1hOGU5LWFmY2Y3MTNmMDY1MyIsInNjb3BlIjoib3BlbmlkIG9mZmxpbmVfYWNjZXNzIHByb2ZpbGUgZW1haWwifQ.1Z3WfP6TsuRrv2HRoFmG0EDmmJZswgn6Ufhql8tx9tA";
	public static final String PERFECTO_URL = "https://" + PERFECTO_HOST + "/nexperience/perfectomobile/wd/hub/fast";
		
	/*** Docker hub url ***********/
	public static final String HUB_URL = "http://localhost:4444";

	/*** Browser stack flags and values **/
	public static final Long BSTACK_IDLE_TIMEOUT = 120L;
	public static final Long BSTACK_IDLE_AUTOWAIT = 20L;
	public static final Long TIMEOUT = 60L;
	public static final Long POOLTIME = 4000L;
	
	//Other flags
	public static final boolean isBrowserStackEnabled = false;
	public static final boolean isSauceLabEnabled = false;
	public static final boolean isPerfectoEnabled = false;
	public static final boolean isEmailReport = false;
	public static final boolean MOBILELOCAL = false;
	public static final boolean isGridEnabled = false;
	
	//Path
	public static final String REPORT_PATH = System.getProperty("user.dir")+"/ExtentReport/";
	public static final String SCREENSHOT_PATH = System.getProperty("user.dir")+"/ExtentReport/";
	public static final String DATAFILE = System.getProperty("user.dir")+"//src//test//resources//dataRepository//mercy.xlsx";
	public static final String JSONFILE = System.getProperty("user.dir")+"//src//test//resources//dataRepository//mercy.json";

	//All the Excel Related Values
	public static final String TESTCASES_SHEET = "TestCases";
	public static final String TCID_COL = "TCID";
	public static final String RESULTS_COL = "Result";
	public static final String ACTUALRESULTS_COL = "Actual Result";
	public static final String RUNMODE_COL = "RunMode";
	public static final String DESCRIPTION_COL = "Description";
	
	//API Values
	public static final String API_URI = "https://covid-193.p.rapidapi.com";
	public static final String RapidAPI_KEY = "59102f52fdmsh89e61c9ff7a9cdfp123964jsn4497fab810ed";
	public static final String RapidAPI_HOST = "covid-193.p.rapidapi.com";
	public static final String RapidAPI_METHOD = "GET";


}

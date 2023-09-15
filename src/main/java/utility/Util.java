package utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebDriver.When;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.openqa.selenium.support.locators.RelativeLocator.RelativeBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Step;
	
public class Util {

	private WebDriver driver;
	private FileInputStream fs;
	private DesiredCapabilities browserStackCap, perfectoCap;
	private MutableCapabilities sauceLabCap;
	private Properties prop;
	private Duration timeout = Duration.ofSeconds(Constants.DEFAULT_DURATION);
	private Duration pooltime = Duration.ofMillis(Constants.DEFAULT_INTERVAL);

	protected ExtentTest logger;
    public static Logger log = LogManager.getLogger();

	/**
	 * Constructor accepts the Properties file Path
	 * 
	 * @throws FileNotFoundException When the path of the file is wrong <br>
	 * please check the <b>CONSTANTS.java</b> file
	 * under <b>Main.Utility</b> package
	 * @param PropPath Accepts the Property file Path
	 */
	public Util(String PropPath, ExtentTest logger) {
		try {
			prop = new Properties();
			fs = new FileInputStream(PropPath);
			prop.load(fs);
			this.logger = logger;
		} catch (FileNotFoundException e) {
			System.err.println("File Not Found");
			throw new AssertionError("File Not Found");
		} catch (Exception e) {
			e.printStackTrace();
			throw new AssertionError("Execption occured");
		}
	}

	/**
	 * Setting up the browser stack capabilities
	 * 
	 * @param os             Operating System
	 * @param os_version     Operating System version
	 * @param browser        Browser Name
	 * @param browserVersion Browser Version
	 * @param projectName    Name of the project displayed on BR
	 * @param testRunName    Executing test Name e.x <b>Address :-: 2234324</b>
	 * @see Exception
	 */
	public DesiredCapabilities setBrowserStackCap(Hashtable<String, String> data, String projectName, String testRunName) {
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("browserName", data.get("browser"));
		caps.setCapability("browserVersion", data.get("browserVersion"));
		caps.setCapability("projectName", projectName);
		caps.setCapability("sessionName", testRunName);
		caps.setCapability("autoAcceptAlerts", "true");

		if (data.get("browser").equalsIgnoreCase("chrome")) {
			ChromeOptions options = new ChromeOptions();
			options.addArguments("use-fake-device-for-media-stream");
			options.addArguments("use-fake-ui-for-media-stream");
			options.addArguments("--disable-notifications");
			caps.setCapability(ChromeOptions.CAPABILITY, options);
		} else if (data.get("browser").equalsIgnoreCase("Edge")) {
			EdgeOptions options = new EdgeOptions();
			options.addArguments("use-fake-device-for-media-stream");
			options.addArguments("use-fake-ui-for-media-stream");   
			options.addArguments("--disable-notifications");
			caps.setCapability("edge", options);
			caps.setCapability("browserstack.edge.enablePopups", "false");
		} else if (data.get("browser").equalsIgnoreCase("Safari")) {
			HashMap<String, Object> safariOptions = new HashMap<String, Object>();
			safariOptions.put("enablePopups", "false");
			safariOptions.put("allowAllCookies", "false");
			caps.setCapability("browserstack.safari.enablePopups", "false");
			caps.setCapability("safari", safariOptions);
			caps.setCapability("autoGrantPermissions", "true");
		}

		HashMap<String, Object> browserstackOptions = new HashMap<String, Object>();
		if(data.get("isMobile").equalsIgnoreCase("N"))
			browserstackOptions.put("os", data.get("os"));
		else {
			browserstackOptions.put("deviceName", data.get("os"));
			browserstackOptions.put("realMobile", "true");
		}
		browserstackOptions.put("osVersion", data.get("os_version"));
		browserstackOptions.put("seleniumVersion", "4.1.2");
		browserstackOptions.put("telemetryLogs", "true");
		browserstackOptions.put("networkLogs", "true");
		browserstackOptions.put("local", false);
		browserstackOptions.put("resolution", "1600x1200");
		browserstackOptions.put("idleTimeout", Constants.BSTACK_IDLE_TIMEOUT.toString());
		browserstackOptions.put("autoWait", Constants.BSTACK_IDLE_AUTOWAIT.toString());
		browserstackOptions.put("wsLocalSupport", true);
		caps.setCapability("bstack:options", browserstackOptions);
		browserStackCap = caps;
		return browserStackCap;
	}

	public MutableCapabilities setSauceLabsCaps(Hashtable<String, String> data,String projectName, String testRunName) {
		
		MutableCapabilities sauceOptions = new MutableCapabilities();
        sauceOptions.setCapability("build", projectName);
        sauceOptions.setCapability("username", Constants.SAUCE_USERNAME);
        sauceOptions.setCapability("accessKey", Constants.SAUCE_ACCESS_KEY);
        sauceOptions.setCapability("name",testRunName);
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("browserName", data.get("browser"));
        capabilities.setCapability("browserVersion", data.get("browserVersion"));
        capabilities.setCapability("platformVersion", data.get("os")+" "+data.get("os_version"));
        capabilities.setCapability("sauce:options", sauceOptions);
        sauceLabCap = capabilities;
        return sauceLabCap;
	}
	
	public DesiredCapabilities setPerfectoCaps() {
		DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Windows");
        capabilities.setCapability("platformVersion", "10");
        capabilities.setCapability("browserName", "Chrome");
        capabilities.setCapability("browserVersion", "latest");
        capabilities.setCapability("resolution", "1280x1024");
        capabilities.setCapability("securityToken", Constants.PERFECTO_TOKEN);
        perfectoCap = capabilities;
        return perfectoCap;

	}
	/**
	 * Adding up the browser stack capabilities
	 * 
	 * @param key    Capability KEY
	 * @param values Capability VALUE
	 * @see Exception
	 */
	public Util addCapabilities(String key, String values) {
		if (browserStackCap != null)
			browserStackCap.setCapability(key, values);
		return this;
	}
	
	public void Log(String Message) {
		log.info(Message);
		logger.log(Status.INFO, Message);
	}
	
	public void pass(String Message) {
		log.info(Message);
		logger.log(Status.PASS, Message);
	}

	public void error(String Message) {
    	log.error(Message);
		logger.log(Status.WARNING, Message);
    }
    
    public void debug(String Message) {
    	log.debug(Message);
		logger.log(Status.INFO, Message);
    }
    
    public void fatal(String Message) {
    	log.fatal(Message);
		logger.log(Status.INFO, Message);
    }
    
    public void warn(String Message) {
    	log.warn(Message);
		logger.log(Status.WARNING, Message);
    }
    
	public WebDriver getDriver() {
		return this.driver;
	}

	public boolean isIframePresent() {
		return getDriver().getPageSource().contains("iframe");
	}

	public void updateBrowserStack(String status, String reason) {
		System.out.println(reason.replaceAll("\"","'"));
		System.out.println("===");
		if (isBrowserStackEnable() && browserStackCap != null) {
			((JavascriptExecutor) driver).executeScript(
					"browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \""
							+ status + "\", \"reason\": \"" + reason.replaceAll("\"","'") + "\"}}");
		}
	}

	/**
	 * Helper method to Launch the Browser instance based on the name passed as
	 * String
	 * 
	 * @param data Accepts the browser Name
	 * @return {@link WebDriver}
	 * @throws Exception 
	 */
	public void launchInstance(Hashtable<String, String> data) throws Exception {
		// Check the Browser-Stack is enabled or not
		if (isBrowserStackEnable() && browserStackCap != null) {
			driver = new RemoteWebDriver(new URL(Constants.BSTACK_URL), browserStackCap);
			((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
			if (browserStackCap.getBrowserName().equals("Safari")) {
				driver.manage().window().maximize();
			}
			driver.manage().window().maximize();
		} else if (isSauceLabEnable() && sauceLabCap != null) {
			driver = new RemoteWebDriver(new URL(Constants.SAUCE_URL), sauceLabCap);
			((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
			driver.manage().window().maximize();
		}  else if (isPerfectoEnable() && perfectoCap != null) {
			driver = new RemoteWebDriver(new URL(Constants.PERFECTO_URL), perfectoCap);
			((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
			driver.manage().window().maximize();
		} else if (isGridEnable()) {
			// Checking the Grid Flag
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.setCapability("browserVersion", "87.0.4280.88");
			chromeOptions.setCapability("platformName", "Windows 10");
			driver = new RemoteWebDriver(new URL(Constants.HUB_URL), chromeOptions);
			((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
			driver.manage().window().maximize();
		} else {
			// Local execution using browser Name
			if (data.get("browser").equalsIgnoreCase("Chrome")) {
				ChromeOptions options = new ChromeOptions();
				options.addArguments("use-fake-device-for-media-stream");
				options.addArguments("use-fake-ui-for-media-stream");
				options.addArguments("--disable-notifications");
				if (Constants.MOBILELOCAL) {
					Map<String, String> mobileEmulation = new HashMap<>();
					mobileEmulation.put("deviceName", "Nexus 5");
					options.setExperimentalOption("mobileEmulation", mobileEmulation);
				}
				WebDriverManager.chromedriver().setup();
				driver = new ChromeDriver(options);
				driver.manage().window().maximize();
			} else if (data.get("browser").equalsIgnoreCase("Firefox")) {
				FirefoxOptions options = new FirefoxOptions();
				WebDriverManager.firefoxdriver().setup();
				driver = new FirefoxDriver(options);
				driver.manage().window().maximize();
			} else if (data.get("browser").equalsIgnoreCase("Edge")) {
				WebDriverManager.edgedriver().setup();
				driver = new EdgeDriver();
				driver.manage().window().maximize();
				// driver.manage().window().setSize(new Dimension(1024, 768));
			} else if (data.get("browser").equalsIgnoreCase("Safari")) {
				driver = new SafariDriver();
			} else {
				System.err.println("BrowserName is NULL or wrong BrowserName is Passed");
				throw new SkipException("Browser did not launched");
			}
		}
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		logger.log(Status.PASS, "Launch the application in "+data.get("browser"));
	}

	public WebDriver launchInstance(String browser, int queue) throws Exception {
		if (isBrowserStackEnable() && browserStackCap != null) {
			driver = new RemoteWebDriver(new URL(Constants.BSTACK_URL), browserStackCap);
			((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
			if (browserStackCap.getBrowserName().equals("Safari")) {
				driver.manage().window().maximize();
			}
			return driver;
		} else
			return null;
	}

	/**
	 * Close the running session if any
	 * 
	 * @param driver Driver instance created
	 * @return {@link Boolean}
	 * @exception Exception Unable to Close the connection.
	 * @see Exception
	 */
	@Step("closeInstance")
	public boolean closeInstance() {
		if (driver != null) {
			switchDriverControl(0);
			if (isBrowserStackEnable() && browserStackCap != null) {
				updateBrowserStack("passed", "Successfully : Completed the Test");
			}
			//driver.close();
			driver.quit();
			Log("Closing browser Instance");
			return true;
		} else {
			System.err.println("======Driver is null======");
			return false;
		}
	}

	/**
	 * Helper Method to Verify the execution is on BrowserStack Environment
	 * 
	 * @return {@link Boolean}
	 */
	public boolean isBrowserStackEnable() {
		if (Constants.isBrowserStackEnabled)
			return true;
		else
			return false;
	}
	
	public boolean isSauceLabEnable() {
		if (Constants.isSauceLabEnabled)
			return true;
		else
			return false;
	}
	
	public boolean isPerfectoEnable() {
		if (Constants.isPerfectoEnabled)
			return true;
		else
			return false;
	}

	/**
	 * Helper Method to Add Cookies
	 * @param Name of cookie
	 * @param value of cookie
	 * 
	 */
	public void AddNewCookie(String Name,String value){
		driver.manage().addCookie(new Cookie(Name, value));
	}

	/**
	 * Helper Method to Verify the execution is on Grid Environment
	 * 
	 * @return Boolean .
	 */
	public boolean isGridEnable() {
		if (prop.get("Grid").toString().equalsIgnoreCase("Y"))
			return true;
		else
			return false;
	}

	/**
	 * Helper Method to verify the URL is active based on the error message's
	 * 
	 * @param uri URL string which need to verify
	 * @return {@link Boolean}
	 * @see Exception
	 */
	public boolean isSiteDown(String uri) {
		try {
			if (uri != null) {
				HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();
				HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).build();
				HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
				Assert.assertEquals(response.statusCode(), 200);
				String value = response.body().toString();
				if (value.replaceAll("<[^>]*>", "").trim().contains("Site is down for maintenance.")
						|| value.replaceAll("<[^>]*>", "").trim().contains("We'll be back in a few minutes")
						|| value.replaceAll("<[^>]*>", "").trim().contains("Something went wrong.")) {
					if (driver != null)
						closeInstance();
					throw new SkipException("Site is down");
				}
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new AssertionError("Site is down");
		} catch (Exception e) {
			if (driver != null)
				closeInstance();
			throw new AssertionError("Site is down");
		}
	}

	/**
	 * Navigate to given appUrl and return Null pointer exception
	 * @param appUrl : String application under test URL
	 */
	public void navigateTo(String appUrl){
		if(getDriver()!=null && appUrl !=null) {
			if(prop.getProperty(appUrl)!=null) {
				getDriver().get(prop.getProperty(appUrl).toString());
				Log("navigated to URL "+prop.getProperty(appUrl).toString());
			}else {
				getDriver().get(appUrl);
				Log("navigated to URL "+appUrl);
			}
		}else
			new SkipException("Driver instance is null");
	}

	/************************ Utility Functions ********************************/

	/**
	 * Common Method to Switch the frame accepts We
	 * 
	 * @param driver  Driver instance
	 * @param locator String name from property file
	 * @return {@link Boolean}
	 * @exception When no Frame is Found
	 * @see NoSuchFrameException
	 */
	public Boolean switchFrame(String locator) {
		try {
			new WebDriverWait(driver, timeout, pooltime)
			.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Use this method when you want to wait until the visibility of the element
	 * based on the <br>
	 * 
	 * @param driver  Sending the driver instance from the test file
	 * @param locator Sending the By instance from the Helper Class file
	 * @return {@link WebElement}
	 * @exception When no Element is found
	 * @see NoSuchElementException
	 */
	public WebElement myFind(String locator) {
		return new WebDriverWait(driver, timeout, pooltime)
				.until(ExpectedConditions.presenceOfElementLocated(getBy(locator)));
	}

	public WebElement myFind(WebElement locator) {
		return new WebDriverWait(driver, timeout, pooltime).until(ExpectedConditions.visibilityOf(locator));
	}

	public WebElement myFindIgnoreStaleException(String locator) {
		return new WebDriverWait(driver, timeout, pooltime)
				.ignoring(StaleElementReferenceException.class)
				.until(ExpectedConditions.presenceOfElementLocated(getBy(locator)));
	}

	/**
	 * Use this method when you want to wait until the visibility of the element
	 * based on the <br>
	 * <b>DEFAULT WAIT DURATION</b>
	 * 
	 * @param driver   Sending the driver instance from the test file
	 * @param locators Accepts the field name based on property file
	 * @param duration Amount of waiting before giving Error
	 * @return {@link WebElement}
	 * @exception When no Element is found
	 * @see NoSuchElementException
	 */
	public WebElement myFind(String locators, int duration) {
		return new WebDriverWait(driver, Duration.ofSeconds(duration), pooltime)
				.until(ExpectedConditions.presenceOfElementLocated(getBy(locators)));
	}

	/**
	 * Use this method when you want to wait until the visibility of the element
	 * based on the <br>
	 * <b>DEFAULT WAIT DURATION</b> <b>DEFAULT WAIT VALUE</b>
	 * 
	 * @param driver   Sending the driver instance from the test file
	 * @param by       Sending the By instance from the Helper Class file
	 * @param duration Amount of waiting before giving Error
	 * @param interval Amount of interval of poll
	 * @return {@link WebElement}
	 * @exception When no Element is found
	 * @see NoSuchElementException
	 */
	public WebElement myFind(By by, int duration, int timeout) {
		return new WebDriverWait(driver, Duration.ofSeconds(duration), Duration.ofSeconds(timeout))
				.until(ExpectedConditions.presenceOfElementLocated(by));
	}

	/**
	 * Use this method when you want to wait until the visibility of the element
	 * based on the <b>DEFAULT WAIT VALUE</b> for click <br>
	 * 
	 * @param driver   Sending the driver instance from the test file
	 * @param locators Sending the name of the locator from Property file
	 * @return {@link WebElement}
	 * @exception When no Element is found
	 * @see NoSuchElementException
	 */
	public WebElement myFindClickable(String locators) {
		return new WebDriverWait(driver, timeout, pooltime)
				.until(ExpectedConditions.elementToBeClickable(getBy(locators)));
	}

	public WebElement myFindClickable(WebElement locators, int timeout) {
		return new WebDriverWait(driver, Duration.ofSeconds(timeout), pooltime)
				.ignoring(ElementClickInterceptedException.class)
				.until(ExpectedConditions.elementToBeClickable(locators));
	}

	/**
	 * Use this method when you want to wait until the visibility of the element
	 * based on the <b>Given Time Interval</b> for click <br>
	 * 
	 * @param driver   Sending the driver instance from the test file
	 * @param locators sending the locator key from property file
	 * @param duration Sending the amount of time interval poll
	 * @return Nothing .
	 * @exception When no Element is found
	 * @see NoSuchElementException
	 */
	public WebElement myFindClickable(String locators, int timeout) {
		return new WebDriverWait(driver, Duration.ofSeconds(timeout), pooltime).ignoring(ElementClickInterceptedException.class)
				.until(ExpectedConditions.elementToBeClickable(getBy(locators)));
	}

	/**
	 * Use this method when you want to wait until the visibility of the element
	 * based on the <b>DEFAULT WAIT VALUE</b> <br>
	 * 
	 * @param driver  Sending the driver instance from the test file
	 * @param locator Sending the By instance from the Helper Class file
	 * @return Nothing .
	 * @exception When no Element is found
	 * @see NoSuchElementException
	 */
	public WebElement myFindVisibility(String locator) {
		return new WebDriverWait(driver, timeout, pooltime).ignoring(StaleElementReferenceException.class)
				.until(ExpectedConditions.visibilityOfElementLocated(getBy(locator)));
	}

	public WebElement myFindVisibility(String locator, int timeout) {
		return new WebDriverWait(driver, Duration.ofSeconds(timeout), pooltime).until(ExpectedConditions.visibilityOf(myFind(locator, timeout)));
	}

	public Boolean waitUntilInvisible(String locator) {
		return new WebDriverWait(driver, timeout, pooltime).until(ExpectedConditions.invisibilityOfElementLocated(getBy(locator)));

	}

	public Boolean waitUntilInvisible(WebElement locator) {
		return new WebDriverWait(driver, timeout, pooltime).until(ExpectedConditions.invisibilityOf(locator));

	}

	public void waitUntilInvisible(String locator, int timeout) {
		new WebDriverWait(driver, Duration.ofSeconds(timeout), pooltime).until(ExpectedConditions.invisibilityOfElementLocated(getBy(locator)));
	}

	/**
	 * Use this method when you want to wait until the visibility of the element
	 * based on the <b>DEFAULT WAIT VALUE</b> <br>
	 * 
	 * @param driver  Sending the driver instance from the test file
	 * @param locator Accepts the WebElement
	 * @return Nothing .
	 * @exception When no Element is found
	 * @see NoSuchElementException
	 */
	public WebElement myFindVisibility(WebElement locator) {
		return new WebDriverWait(driver, timeout, pooltime).until(ExpectedConditions.visibilityOf(locator));
	}

	/**
	 * Common For Clicking on any Element -- Case when No element is present or the
	 * Element click InterceptedExpection waits and retry the event
	 * 
	 * @param driver  Sending the driver instance from the test file
	 * @param locator Sending the String from the property file
	 * @exception SkipException
	 */
	public Util clickElement(String locator) {
		scrollForElementlocator(locator);
		myFind(locator).click();
		return this;
	}

	public Util clickElement(WebElement locator) {
		scrollForElementlocator(locator);
		myFind(locator).click();
		return this;
	}

	/**
	 * Common For Clicking on any Element -- Case when No element is present or the
	 * Element click InterceptedExpection waits and retry the event
	 * 
	 * @param driver   Sending the driver instance from the test file
	 * @param locator  Sending the String from the property file
	 * @param interval Integer the amount of time interval
	 * @exception SkipException
	 */
	public boolean clickElement(String locator, int timeout) {
		scrollForElementlocator(locator, timeout);
		myFindClickable(locator, timeout).click();
		return true;
	}

	/**
	 * Common function for sending Data
	 * 
	 * @param driver Sending the driver instance from the test file
	 * @param String Sending the locator key
	 * @return Nothing .
	 * @exception When no Element is found
	 * @see Exception
	 */
	public void sendKeys(String locator, String data) {
		scrollForElementlocator(locator);
		clearText(locator);
		myFindVisibility(locator).sendKeys(data);
	}

	public boolean textToBe(String locator, String data) {
		return new WebDriverWait(driver, timeout, pooltime)
				.until(ExpectedConditions.textToBePresentInElementValue(getBy(locator), data));
	}

	public boolean enteredTextToBe(String locator, String data) {
		try {
			return new WebDriverWait(driver, timeout, pooltime)
					.until(ExpectedConditions.textToBePresentInElement(myFind(locator), data));
		} catch (TimeoutException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean waituntilTextMatches(String locator) {
		return new WebDriverWait(driver, timeout, pooltime)
				.until(ExpectedConditions.textMatches(getBy(locator), Pattern.compile("^[a-zA-Z0-9]*$")));
	}

	public boolean waituntilTextMatches(WebElement locator) {
		return new WebDriverWait(driver, timeout, pooltime)
				.until(ExpectedConditions.textMatches((By) locator, Pattern.compile("^[a-zA-Z0-9]*$")));
	}

	public void sendKeys(WebElement locator, String data) {
		new Actions(driver).sendKeys(locator, data).build().perform();
	}

	public String getUrl() {
		return this.driver.getCurrentUrl();
	}

	/**
	 * Common For entering the data
	 * 
	 * @param driver  Sending the driver instance from the test file
	 * @param locator Accepts to locator string
	 * @return {@link Boolean}
	 * @exception When no Element is found
	 * @see Exception
	 */
	public boolean scrollForElementlocator(String locator) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(false);", myFind(locator));
		return true;
	}

	public boolean scrollForCenterElementlocator(String locator) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView({block: \"center\",inline: \"center\",behavior: \"smooth\"});",
				myFind(locator));
		return true;
	}

	public boolean scrollForElementlocator(String locator, int duration) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(false);", myFind(locator, duration));
		return true;
	}

	public boolean scrollForElementlocator(WebElement locator) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(false);", locator);
		return true;
	}

	public boolean scrollForCenterElementlocator(WebElement locator) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView({block: \"center\",inline: \"center\",behavior: \"smooth\"});",
				locator);
		return true;
	}

	public void scrollBottom() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
	}

	/**
	 * Mouse hover on a specific element
	 * 
	 * @param driver  Sending the driver instance from the test file
	 * @param locator Accepts to locator string
	 * @return {@link Boolean}
	 * @exception When no Element is found
	 * @see Exception
	 */
	public boolean mouseoverForElement(String locator) {
		try {
			Actions a = new Actions(driver);
			a.moveToElement(myFind(locator)).build().perform();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void actionClick(String locator) {
		Actions a = new Actions(driver);
		a.click(myFindVisibility(locator)).build().perform();
	}

	/**
	 * Scrolling on a specific element and clicking
	 * 
	 * @param driver  Sending the driver instance from the test file
	 * @param locator Accepts to locator string
	 * @exception When no Element is found
	 * @see Exception
	 */
	public void scrollForClick(String locator) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", myFind(locator));
	}

	public void scrollForClick(String locator, int timeout) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", myFind(locator, timeout));
	}

	public void scrollForClick(WebElement locator) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", locator);
	}

	/**
	 * Clearing the text based on the browser
	 * 
	 * @param driver  Sending the driver instance from the test file
	 * @param locator Accepts to locator string
	 * @exception When no Element is found
	 * @see Exception
	 */
	public void clearText(String locator) {
		WebElement el = myFind(locator);
		if (isBrowserStackEnable()) {
			String os = ((RemoteWebDriver) driver).getCapabilities().getCapability("platformName").toString();
			String browser = ((RemoteWebDriver) driver).getCapabilities().getCapability("browserName").toString();
			if (os.contains("MAC") || browser.equalsIgnoreCase("Safari")) {
				el.click();
				el.sendKeys(Keys.COMMAND + "A");
				el.sendKeys(Keys.DELETE);
				if(browser.equalsIgnoreCase("Safari")) {
					threadSleep(500);
					JavascriptExecutor js = (JavascriptExecutor) driver;
					js.executeScript("arguments[0].value=\"\";", el);
					threadSleep(500);
				}
			} else {
				el.sendKeys(Keys.CONTROL + "A");
				el.sendKeys(Keys.DELETE);
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("arguments[0].value=\"\";", el);
			}
		} else {
			String os = ((RemoteWebDriver) driver).getCapabilities().getCapability("platformName").toString();
			if (os.contains("MAC")) {
				el.click();
				el.sendKeys(Keys.COMMAND + "A");
				el.sendKeys(Keys.DELETE);
			} else {
				// Windows
				el.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("arguments[0].value=\"\";", el);
			}
		}
	}

	public void clearText(WebElement locator) {
		if (isBrowserStackEnable()) {
			String os = ((RemoteWebDriver) driver).getCapabilities().getCapability("platformName").toString();
			String browser = ((RemoteWebDriver) driver).getCapabilities().getCapability("browserName").toString();
			if (os.contains("MAC") || browser.equalsIgnoreCase("Safari")) {
				locator.click();
				locator.sendKeys(Keys.CONTROL + "A");
				locator.sendKeys(Keys.DELETE);
			} else {
				locator.sendKeys(Keys.CONTROL + "A");
				locator.sendKeys(Keys.DELETE);
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("arguments[0].value=\"\";", locator);
			}
		} else {
			String os = ((RemoteWebDriver) driver).getCapabilities().getCapability("platformName").toString();
			if (os.contains("MAC")) {
				locator.click();
				locator.sendKeys(Keys.COMMAND + "A");
				locator.sendKeys(Keys.DELETE);
			} else {
				locator.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("arguments[0].value=\"\";", locator);
			}
		}
	}

	public void selectText(WebElement el, String keyPress) {
		if (isBrowserStackEnable()) {
			String os = ((RemoteWebDriver) driver).getCapabilities().getCapability("platformName").toString();
			String browser = ((RemoteWebDriver) driver).getCapabilities().getCapability("browserName").toString();
			if (os.contains("MAC") && browser.equalsIgnoreCase("Safari")) {
				el.sendKeys(Keys.CONTROL + keyPress);
				el.sendKeys(Keys.DELETE);
			} else
				el.sendKeys(Keys.chord(Keys.CONTROL, keyPress));
		} else {
			String os = ((RemoteWebDriver) driver).getCapabilities().getCapability("platformName").toString();
			if (os.contains("MAC")) {
				el.sendKeys(Keys.chord(Keys.COMMAND, keyPress));
			} else {
				el.sendKeys(Keys.chord(Keys.CONTROL, keyPress));
			}
		}
	}

	public void selectText(String locator, String keyPress) {
		WebElement el = myFind(locator);
		selectText(el, keyPress);
	}

	/**
	 * Returning the by instance as a method
	 * 
	 * @param driver Sending the driver instance from the test file
	 * @return {@link By}
	 * @exception When no Element is found
	 * @see Exception
	 */
	public By getBy(String locator) {
		try {
			if (locator.endsWith("_xpath")) {
				return By.xpath(prop.getProperty(locator));
			} else if (locator.endsWith("_id")) {
				return By.id(prop.getProperty(locator));
			} else if (locator.endsWith("_name")) {
				return By.name(prop.getProperty(locator));
			} else if (locator.endsWith("_className")) {
				return By.className(prop.getProperty(locator));
			} else if (locator.endsWith("_css")) {
				return By.cssSelector(prop.getProperty(locator));
			} else {
				return By.xpath(locator);
			}
		} catch (Exception e) {
			e.printStackTrace();
			new SkipException("Skipped because of no locator found");
			return null;
		}
	}


	public RelativeBy getByRelativeLocator(String locator) {
		return RelativeLocator.with(getBy(locator));
	}

	public SearchContext getSearchContext(String locator) {
		return getDriver().findElement(getBy(locator)).getShadowRoot();
	}

	public SearchContext getSearchContext(SearchContext el, String locator) {
		return el.findElement(getBy(locator)).getShadowRoot();
	}
	public WebElement getSearchContext_WebElement(SearchContext el, String locator) {
		return el.findElement(getBy(locator));
	}

	public WebElement myFindRelative(RelativeBy locator) {
		return new WebDriverWait(driver, timeout, pooltime)
				.until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	public WebElement myFindRelative(String locator) {
		return new WebDriverWait(driver, timeout, pooltime)
				.until(ExpectedConditions.presenceOfElementLocated(getByRelativeLocator(locator)));
	}

	public List<WebElement> getLocators(String locator) {
		return new WebDriverWait(driver, timeout, pooltime)
				.until(ExpectedConditions.presenceOfAllElementsLocatedBy(getBy(locator)));
	}

	public List<WebElement> getLocators(String locator, int timeout) {
		return new WebDriverWait(driver, Duration.ofSeconds(timeout), pooltime)
				.until(ExpectedConditions.presenceOfAllElementsLocatedBy(getBy(locator)));
	}

	public List<WebElement> getLocatorsVisibility(String locator, int timeout) {
		return new WebDriverWait(driver, Duration.ofSeconds(timeout), pooltime)
				.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(getBy(locator)));
	}

	public Boolean getUrl(String locator) {
		try {
			return new WebDriverWait(driver, pooltime).until(ExpectedConditions.urlContains(locator));
		} catch (Exception e) {
			return false;
		}
	}

	public List<WebElement> isElementPresent(String locator) {
		return driver.findElements(getBy(locator));
	}

	/**
	 * Switching the driver controls
	 * 
	 * @param driver  Sending the driver instance from the test file
	 * @param tabNumb switchNumber
	 * @return {@link Boolean}
	 * @exception When no Element is found
	 * @see Exception
	 */
	public boolean switchDriverControl(Integer tabNumb) {
		ArrayList<String> numOfTabs = new ArrayList<String>(driver.getWindowHandles());
		if (numOfTabs.size() != 0) {
			driver.switchTo().window(numOfTabs.get(tabNumb));
			return true;
		} else {
			return false;
		}
	}

	public boolean switchDriverControl(String tabName) {
		if (tabName != null) {
			driver.switchTo().window(tabName);
			return true;
		} else {
			return false;
		}
	}

	public int getCurrentTime() {
		return java.time.LocalTime.now().toSecondOfDay();
	}

	public String getTimeDifference(int inital, int second) {
		return String.valueOf(inital - second);
	}

	public void threadSleep(long timeOut) {
		try {
			Thread.sleep(timeOut);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public boolean acceptAlert() {
		try {
			getDriver().switchTo().alert().accept();
			return true;
		} // try
		catch (NoAlertPresentException Ex) {
			return false;
		}
	}

	public Alert isAlertPresent() {
		return new WebDriverWait(driver, timeout, pooltime)
				.until(ExpectedConditions.alertIsPresent());
	}

	public void selectElement(WebElement element, String option) {
		Select select = new Select(element);
		select.selectByVisibleText(option);
	}	

	public String capture(String screenShotName) throws IOException {
        TakesScreenshot ts = (TakesScreenshot)driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String dest = Constants.REPORT_PATH+getDate()+"/"+getTime()+"_"+screenShotName+".png";
        File destination = new File(dest);
        FileUtils.copyFile(source, destination);
        return dest;
    }
	
	protected String getDate() {
		return java.time.LocalDate.now().toString();
	}
	
	private String getTime() {
		return java.time.LocalTime.now().toString();
	}
}

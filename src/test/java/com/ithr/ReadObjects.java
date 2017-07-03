package com.ithr;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/*---------------------------------------------------------------------------------------------------------------------------
 * Class Name  : ReadObjects
 * Description : This class reads the objects and its property from the MSJObjects / SQLQueries properties file
 * Author      :  Viswa 
 -----------------------------------------------------------------------------------------------------------------------------*/

public class ReadObjects {
    Properties p = new Properties();
    private Properties properties;
    String PROPERTY_FILENAME_KEY = "selenium.properties.filename";
    String PROPERTY_FILENAME_DEFAULT = "selenium.properties";
    private static final String SELENIUM_SERVER_HOST_KEY = "selenium.server.host";
    private static final String SELENIUM_SERVER_DEFAULT = "localhost";
    private static final String SELENIUM_SERVER_PORT_AND_URI = ":4444/wd/hub";
    private static final String HTTP_PREFIX = "http://";
    private static final String TARGET_BROWSER_KEY = "target.browser";
    private static final String TARGET_BROWSER_CHROME = "chrome";
    private static final String TARGET_BROWSER_FIREFOX = "firefox";
    private static final String TARGET_BROWSER_IE = "ie";
    private static final String TARGET_BROWSER_DEFAULT = TARGET_BROWSER_FIREFOX;
    String TargetURL;
    private static final String TARGET_HOST_KEY = "target.host";

    // Get Object Repository
    public Properties getObjectRepository() throws IOException {
	// Read object repository file
	InputStream stream = new FileInputStream(new File(
		System.getProperty("user.dir")
			+ "//objects//MySourceObjects.properties"));
	// load all objects
	p.load(stream);
	return p;
    }

    // Load Property files
    private void loadProperties() {
	properties = new Properties();
	try {
	    String propertyFilename = System.getProperty(PROPERTY_FILENAME_KEY);

	    if (propertyFilename != null && propertyFilename.length() > 1) {
		File propFile = new File(propertyFilename);
		properties.load(new FileInputStream(propFile));
	    } else {
		properties.load(this.getClass().getClassLoader()
			.getResourceAsStream(PROPERTY_FILENAME_DEFAULT));
	    }
	} catch (Exception e) {
	    e.printStackTrace();

	}
    }

    // Get Selenium Server URL--- Target Machine Name
    public String getSeleniumServerUrl() {
	System.out.println("SELENIUM_SERVER_HOST_KEY"
		+ SELENIUM_SERVER_HOST_KEY);
	System.out
		.println("URL Test: "
			+ HTTP_PREFIX
			+ properties.getProperty(SELENIUM_SERVER_HOST_KEY,
				SELENIUM_SERVER_DEFAULT)
			+ SELENIUM_SERVER_PORT_AND_URI);
	return HTTP_PREFIX
		+ properties.getProperty(SELENIUM_SERVER_HOST_KEY,
			SELENIUM_SERVER_DEFAULT) + SELENIUM_SERVER_PORT_AND_URI;
    }

    // Get Firefox Driver
    public WebDriver getFireFoxDriver() throws Exception {
	FirefoxProfile profile = new FirefoxProfile();
	DesiredCapabilities capabilities = DesiredCapabilities.firefox();
	capabilities.setCapability(FirefoxDriver.PROFILE, profile);
	WebDriver driver = new RemoteWebDriver(new URL(getSeleniumServerUrl()),
		capabilities);
	return driver;
    }

    /**
     * Get the IE Browser Selenium WebDriver used for remote control.
     */
    public WebDriver getIEDriver() throws Exception {

	DesiredCapabilities capabilities = DesiredCapabilities
		.internetExplorer();
	capabilities
		.setCapability(
			InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
			true);
	WebDriver driver = new RemoteWebDriver(new URL(getSeleniumServerUrl()),
		capabilities);
	return driver;
    }

    /**
     * Get the Chrome Browser Selenium WebDriver used for remote control.
     */
    public WebDriver getChromeDriver() throws Exception {
	DesiredCapabilities capabilities = DesiredCapabilities.chrome();
	WebDriver driver = new RemoteWebDriver(new URL(getSeleniumServerUrl()),
		capabilities);

	return driver;
    }

    // Get WebDriver based on the Browser
    public WebDriver getDriver() {
	WebDriver driver = null;
	try {
	    String targetBrowser = properties.getProperty(TARGET_BROWSER_KEY,
		    TARGET_BROWSER_DEFAULT);
	    if (targetBrowser.equals(TARGET_BROWSER_FIREFOX)) {
		driver = getFireFoxDriver();
	    } else if (targetBrowser.equals(TARGET_BROWSER_IE)) {
		driver = getIEDriver();

	    } else if (targetBrowser.equals(TARGET_BROWSER_CHROME)) {
		driver = getChromeDriver();
	    }
	} catch (Exception e) {

	    System.out.println(e);
	}

	return driver;
    }

    public String getPropertyValues() throws IOException {
	loadProperties();
	String targetUrl = properties.getProperty(TARGET_HOST_KEY);
	return targetUrl;
    }

    public Properties getSQLQueryObjectRepository(String var)
	    throws IOException {
	// Read object repository file
	InputStream stream;
	if ("SQL1".equals(var)) {
	    stream = new FileInputStream(
		    new File(
			    System.getProperty("user.dir")
				    + "\\objects\\TestDataSQLQueriesUpdated1.properties"));
	} else if ("SQL2".equals(var)) {
	    stream = new FileInputStream(
		    new File(
			    System.getProperty("user.dir")
				    + "\\objects\\TestDataSQLQueriesUpdated2.properties"));
	} else {
	    stream = new FileInputStream(new File(
		    System.getProperty("user.dir")
			    + "\\objects\\TestDataSQLQueries.properties"));
	}
	// load all objects
	p.load(stream);
	return p;
    }

}

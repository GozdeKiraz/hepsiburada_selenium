package com.hepsiburada.Tests;

import com.thoughtworks.gauge.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.rules.Timeout;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.Log4jLoggerAdapter;

import javax.swing.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.lang.System.getenv;

public class BaseTest {

    protected static WebDriver driver;
    protected static WebDriverWait webDriverWait;
    DesiredCapabilities capabilities;

    private static  Logger logger=LoggerFactory.getLogger(BaseTest.class);


    @BeforeScenario
    public void setUp(ExecutionContext executionContext) throws Exception{

       // logger.info("" + executionContext.getCurrentScenario().getName());
        String baseUrl = "https://www.hepsiburada.com/";


        if (StringUtils.isNotEmpty(getenv("key"))) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("test-type");
            options.addArguments("disable-popup-blocking");
            options.addArguments("ignore-certificate-errors");
            options.addArguments("disable-translate");
            options.addArguments("--start-maximized");
            options.addArguments("--no-sandbox");

            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
            capabilities.setCapability("key", System.getenv("key"));

            driver = new RemoteWebDriver(new URL("http://hub.testinium.io/wd/hub"), capabilities);
            ((RemoteWebDriver)driver).setFileDetector(new LocalFileDetector());
        } else {

            capabilities = DesiredCapabilities.chrome();
            Map<String, Object> prefs = new HashMap<String, Object>();
            prefs.put("profile.default_content_setting_values.notifications", 2);

//			capabilities = DesiredCapabilities.firefox();
//			FirefoxOptions options = new FirefoxOptions();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("disable-popup-blocking");

            System.setProperty("webdriver.chrome.driver", "web_driver/chromedriver");

//      options.addArguments("--kiosk");//FULLSCREEN FOR MAC

            driver = new ChromeDriver(options);

            driver.manage().timeouts().implicitlyWait(45, TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(25,TimeUnit.SECONDS);
            driver.manage().window().maximize();
            driver.get(baseUrl);

        }
        webDriverWait = new WebDriverWait(driver, 45, 150);




    }
    @BeforeStep
    public void beforeStep(ExecutionContext executionContext){

        logger.info(executionContext.getCurrentStep().getDynamicText());
    }

    @AfterStep
    public void afterStep(ExecutionContext executionContext){

        if (executionContext.getCurrentStep().getIsFailing()) {
            logger.error(executionContext.getCurrentScenario().getName());
            logger.error(executionContext.getCurrentStep().getDynamicText());
            logger.error(executionContext.getCurrentStep().getErrorMessage() + "\r\n"
                    + executionContext.getCurrentStep().getStackTrace());
        }
    }
    @AfterScenario
    public void tearDown() {

        driver.quit();
    }

}

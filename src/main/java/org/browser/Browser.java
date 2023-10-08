package org.browser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Browser {

    public static final String CHROME_DRIVER_PATH = "/drivers/chromedriver.exe";
    public static final String FIREFOX_DRIVER_PATH = "/drivers/geckodriver.exe";
    public static String downloadsPath = System.getProperties().getProperty("user.dir") + File.separator + "downloads";

    public static String browserName;

    public static WebDriver driver = null;
    public static WebDriverWait waitParam;

    public static String getChromePath() {
        String chromePath = "/opt/drivers/chromedriver";
        File tempFile = new File(chromePath);

        return tempFile.exists() ? chromePath : System.getProperties().getProperty("user.dir") + CHROME_DRIVER_PATH;
    }

    public static String getFirefoxPath() {
        String firefoxPath = "/opt/drivers/geckodriver";
        File tempFile = new File(firefoxPath);

        return tempFile.exists() ? firefoxPath : System.getProperties().getProperty("user.dir") + FIREFOX_DRIVER_PATH;
    }

    public static void openBrowser() throws Exception {

        if( browserName.isEmpty()) {
            browserName = System.getProperty("browser");
        }

        switch (browserName) {
            case "chrome":
                initChrome();
                break;
            case "firefox":
                initFirefox();
                break;
            default:
                throw new Exception("No such browser");
        }

        waitParam = new WebDriverWait(driver, Duration.ofSeconds(10));
        //Maximize window
        driver.manage().window().maximize();
    }

    private static void initChrome(){
        //System.setProperty("webdriver.chrome.driver", getChromePath());
        //System.setProperty("webdriver.chrome.whitelistedIps", "");

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", downloadsPath);
        prefs.put("plugins.always_open_pdf_externally", true);
        prefs.put("download.extensions_to_open", "application/xml");
        prefs.put("safebrowsing.enabled", true);

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("prefs", prefs);
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--safebrowsing-disable-download-protection");
        chromeOptions.addArguments("safebrowsing-disable-extension-blacklist");
        chromeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        chromeOptions.addArguments("--window-size=1920,1080");

        chromeOptions.addArguments("--disable-blink-features=AutomationControlled");

        //Exclude the collection of enable-automation switches
        String[] xArg = {"enable-automation"};
        chromeOptions.setExperimentalOption("excludeSwitches", xArg);

        //Turn-off userAutomationExtension
        chromeOptions.setExperimentalOption("useAutomationExtension", false);

        driver = new ChromeDriver(chromeOptions);
    }

    private static void initFirefox(){
        System.setProperty("webdriver.gecko.driver", getFirefoxPath());

        FirefoxBinary firefoxBinary = new FirefoxBinary();
        firefoxBinary.addCommandLineOptions("--no-sandbox");

        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(firefoxBinary);

        driver = new FirefoxDriver(firefoxOptions);
    }

    public static void tearDown() throws Exception {
        Browser.getDriver().quit();
    }

    public static WebDriver getDriver() {
        if( driver == null ) {
            try {
                openBrowser();
            } catch (Exception var2) {
                var2.printStackTrace();
            }
        }
        return driver;
    }


    //public static void confirmAlert(){
   //     wait.until(ExpectedConditions.alertIsPresent());
    //    driver.switchTo().alert().accept();
    //}

}

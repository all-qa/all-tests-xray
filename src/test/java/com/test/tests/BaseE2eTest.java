package com.test.tests;

import app.getxray.xray.testng.listeners.XrayListener;
import com.test.pages.GooglePage;
import com.test.util.AnnotationTransformerRetryListener;
import com.test.util.TestOrderRandomizer;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;

@Listeners({
        TestOrderRandomizer.class,
        AnnotationTransformerRetryListener.class,
        XrayListener.class
})
@Slf4j
public class BaseE2eTest implements IHookable {

    private static final ThreadLocal<GooglePage> TEST_PAGE_THREAD_LOCAL = new ThreadLocal<>();

    @BeforeMethod
    @Parameters("browser}")
    public void setUp(String browser) {
        WebDriver remoteWebDriver;
        String targetUrl = "https://google.com";
        try {
            URL seleniumHubUrl = new URL("http://selenium-router.selenium-grid.svc.cluster.local:4444");
            switch (browser) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.setCapability("platformName", "linux");
                    chromeOptions.setCapability("se:recordVideo", "true");
                    chromeOptions.setCapability("se:timeZone", "Europe/Zurich");
                    chromeOptions.setCapability("se:screenResolution", "1920x1080");
                    remoteWebDriver = new RemoteWebDriver(seleniumHubUrl, chromeOptions);
                    break;
                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.setProfile(new FirefoxProfile());
                    remoteWebDriver = new RemoteWebDriver(seleniumHubUrl, firefoxOptions);
                    break;
                case "edge":
                    remoteWebDriver = new RemoteWebDriver(seleniumHubUrl, new EdgeOptions());
                    break;
                default:
                    throw new RuntimeException(String.format("Browser %s not supported", browser));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("setup error");
        }
        remoteWebDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        log.info("setting up things for thread: {}", Thread.currentThread().getName());
        TEST_PAGE_THREAD_LOCAL.set(new GooglePage("en", remoteWebDriver, new WebDriverWait(remoteWebDriver, Duration.ofSeconds(4)), targetUrl));
        TEST_PAGE_THREAD_LOCAL.get().openPage();
    }

    @AfterMethod
    public void tearDown() {
        log.info("tearing down things for thread: {}", Thread.currentThread().getName());
        TEST_PAGE_THREAD_LOCAL.remove();
    }

    public GooglePage getTestPage() {
        return TEST_PAGE_THREAD_LOCAL.get();
    }

    @Override
    public void run(IHookCallBack callBack, ITestResult testResult) {

        callBack.runTestMethod(testResult);
        if (testResult.getThrowable() != null) {
            try {
                takeScreenShot(testResult.getMethod().getMethodName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] takeScreenShot(String methodName) throws IOException {
        return ((TakesScreenshot) getTestPage().getWebDriver()).getScreenshotAs(OutputType.BYTES);
    }
}

package com.test.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

@Slf4j
public class GoogleSearchResultsPage extends PageObject {

    public GoogleSearchResultsPage(String language, WebDriver webDriver, WebDriverWait webDriverWait) {
        super(language, webDriver, webDriverWait);
    }

    public String getPageTitle() {
        return getWebDriver().getTitle();
    }

    public WebElement secondLink() {
        return getWebDriver().findElement(By.cssSelector("body"));
    }

}

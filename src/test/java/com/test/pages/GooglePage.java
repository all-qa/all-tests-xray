package com.test.pages;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

@Slf4j
public class GooglePage extends PageObject {

    @Getter
    private String url;

    public GooglePage(@NonNull String language, @NonNull WebDriver webDriver, @NonNull WebDriverWait webDriverWait, @NonNull String url) {
        super(language, webDriver, webDriverWait);
        this.url=url;
    }

    public GooglePage openPage() {
        getWebDriver().get(url);
        String acceptButtonColor = "#4285f4";
        List<WebElement> elements = getWebDriver().findElements(By.xpath("//div[contains(text(), 'Alle akzeptieren')]"));
        for (WebElement element: elements) {
            String color = element.getCssValue("background-color");
            if (Color.fromString(color).asHex().equals(acceptButtonColor)) {
                element.click();
                break;
            }
        }
        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated(By.name("q")));
        return this;
    }

    public void closePage() {
        getWebDriver().quit();
    }

    public GoogleSearchResultsPage googleSearch(String keyword) {
        WebElement q = getWebDriver().findElement(By.name("q"));
        q.sendKeys(keyword);
        q.sendKeys(Keys.RETURN);
        return new GoogleSearchResultsPage(getLanguage(), getWebDriver(), getWebDriverWait());
    }

}

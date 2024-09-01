package com.test.pages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

@AllArgsConstructor
@Getter
public abstract class PageObject {

    public String language;
    private WebDriver webDriver;
    private WebDriverWait webDriverWait;

}

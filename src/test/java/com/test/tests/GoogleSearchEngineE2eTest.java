package com.test.tests;

import com.test.pages.GoogleSearchResultsPage;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import app.getxray.xray.testng.annotations.XrayTest;

public class GoogleSearchEngineE2eTest extends BaseE2eTest {

    @Test(description = "Search input quote is displayed in title", dataProvider = "keywords")
    @XrayTest(key = "CALC-5", labels = "core")
    public void testGoogleSearchInputReturnsResult(String keyword) {
        SoftAssert softAssert = new SoftAssert();
            GoogleSearchResultsPage googleSearchResultsPage = getTestPage().googleSearch(keyword);
                softAssert.assertTrue(googleSearchResultsPage.getPageTitle().contains(keyword));
        softAssert.assertAll();
    }

    @Test(description = "Google Search Results Second Link Is Displayed", dataProvider = "keywords")
    @XrayTest(key = "CALC-6", labels = "core")
    public void testGoogleSearchResultsSecondLinkIsNotDisplayed(String keyword) {
        SoftAssert softAssert = new SoftAssert();
            GoogleSearchResultsPage googleSearchResultsPage = getTestPage().googleSearch(keyword);
                softAssert.assertTrue(googleSearchResultsPage.secondLink().isDisplayed());
        softAssert.assertAll();
    }

    @DataProvider
    public Object[][] keywords() {
        return new Object[][]{
                {
                        "hello world"
                },
                {
                        "java programming language"
                },
                {
                        "qa automation"
                }
        };
    }

}

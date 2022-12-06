package test;

import aquality.selenium.browser.AqualityServices;
import aquality.selenium.browser.Browser;
import entity.ProductAndOS;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import util.data.ConfigHelper;
import util.data.TestDataHelper;

import java.util.Iterator;

public class BaseTest {


    @DataProvider(name = "ProductAndOS")
    public Iterator<ProductAndOS> productAndOSIterator() {
        return TestDataHelper.getInstance().getProductAndOSList().iterator();
    }

    @BeforeMethod
    public void beforeTestMethod() {
        Browser browser = AqualityServices.getBrowser();
        browser.goTo(ConfigHelper.getInstance().getUrlMainPage());
        browser.maximize();
    }

    @AfterMethod
    public void afterTestMethod() {
        Browser browser = AqualityServices.getBrowser();
        browser.quit();
    }

}

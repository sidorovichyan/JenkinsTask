package form;

import aquality.selenium.browser.AqualityServices;
import aquality.selenium.elements.interfaces.IButton;
import aquality.selenium.forms.Form;
import org.openqa.selenium.By;

public class DownloadForm extends Form {

    private static String xpathCard = "//div[@data-at-selector ='downloadApplicationCard' and contains(string(),'%s')]";

    private static String xpathButtonSendToEmailXpath = "//button[@data-at-selector='appInfoSendToEmail']";

    private static String xpathButtonOS = "//div[@data-at-selector='osName' and contains(string(), '%s')]";

    public DownloadForm() {
        super(By.xpath("//div[@data-at-selector='osName' and contains(string(), 'Windows')]"), "Download form");
    }

    public void clickButtonOS(String os) {
        String osButtonXpath = String.format(xpathButtonOS, os);
        IButton osButton = AqualityServices.getElementFactory().getButton(By.xpath(osButtonXpath), "Download button");
        osButton.state().waitForClickable();
        osButton.click();
    }

    public void clickSendToEmailButtonInCard(String product) {
        String emailButtonXpath = String.format(xpathCard + xpathButtonSendToEmailXpath, product);
        IButton emailButton = AqualityServices.getElementFactory().getButton(By.xpath(emailButtonXpath), "Download button");
        emailButton.state().waitForClickable();
        emailButton.click();
    }

}

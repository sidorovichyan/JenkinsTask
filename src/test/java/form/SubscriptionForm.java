package form;

import aquality.selenium.browser.AqualityServices;
import aquality.selenium.elements.interfaces.ILabel;
import aquality.selenium.elements.interfaces.ILink;
import aquality.selenium.forms.Form;
import org.openqa.selenium.By;

public class SubscriptionForm extends Form {

    private static ILabel activationBlock = AqualityServices.getElementFactory().getLabel(By.xpath("//activation-code-block"), "Activation block");

    private ILink downloadLink = AqualityServices.getElementFactory().getLink(By.xpath("//a[@data-at-menu='Downloads']"), "Download link");

    public SubscriptionForm() {
        super(activationBlock.getLocator(), "Subscriptions form");
    }

    public void clickDownloadLink() {
        downloadLink.click();
    }

}

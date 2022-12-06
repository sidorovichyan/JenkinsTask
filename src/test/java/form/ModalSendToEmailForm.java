package form;

import aquality.selenium.browser.AqualityServices;
import aquality.selenium.elements.ElementType;
import aquality.selenium.elements.interfaces.IButton;
import aquality.selenium.elements.interfaces.ILabel;
import aquality.selenium.elements.interfaces.ITextBox;
import aquality.selenium.forms.Form;
import org.openqa.selenium.By;

import java.util.List;

public class ModalSendToEmailForm extends Form {

    private static String xpathModalBody = "//kl-modal-body";
    private String xpathLabelWithProductName = "//h2[contains(string(),'%s')]";
    private static ILabel modalBody = AqualityServices.getElementFactory().getLabel(By.xpath(xpathModalBody), "Modal body");
    private ITextBox inputEmail = AqualityServices.getElementFactory().getTextBox(By.xpath(xpathModalBody + "//input"), "Input with email");
    private IButton sendToMailButton = AqualityServices.getElementFactory().getButton(By.xpath(xpathModalBody + "//button"), "Send to mail button");
    private IButton okButtonAfterSend = AqualityServices.getElementFactory().getButton(By.xpath(xpathModalBody + "//button[@data-at-selector='successfullySentOkBtn']"), "Send to mail button");

    public ModalSendToEmailForm() {
        super(modalBody.getLocator(), "Modal form send to email");
    }

    public boolean checkCorrectProductInModal(String product) {
        String finalXpathLabelWithProductName = String.format(xpathLabelWithProductName, product);
        List<ILabel> labelWithProductName = AqualityServices.getElementFactory().findElements(By.xpath(xpathModalBody + finalXpathLabelWithProductName), ElementType.LABEL);
        return labelWithProductName.size() > 0;
    }

    public void sendMail() {
        waitSendToMailButton();
        clickSendToMailButton();
        waitForDisplayOkButton();
        clickOkButton();
    }

    public void waitSendToMailButton() {
        sendToMailButton.state().waitForClickable();
    }

    public void clickSendToMailButton() {
        sendToMailButton.click();
    }

    public void waitToBeClickableInputEmail() {
        inputEmail.state().waitForClickable();
    }

    public String getTextFromInputEmail() {
        return inputEmail.getValue();
    }

    public void waitForDisplayOkButton() {
        okButtonAfterSend.state().waitForDisplayed();
    }

    public void clickOkButton() {
        okButtonAfterSend.click();
    }

}

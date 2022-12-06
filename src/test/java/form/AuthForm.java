package form;

import aquality.selenium.browser.AqualityServices;
import aquality.selenium.elements.interfaces.IButton;
import aquality.selenium.elements.interfaces.ITextBox;
import aquality.selenium.forms.Form;
import org.openqa.selenium.By;
import util.data.TestDataHelper;


public class AuthForm extends Form {

    private ITextBox emailInput = AqualityServices.getElementFactory().getTextBox(By.xpath("//input[@name='email']"), "Email input");

    private ITextBox passwordInput = AqualityServices.getElementFactory().getTextBox(By.xpath("//input[@name='password']"), "Password input");

    private IButton singInButton = AqualityServices.getElementFactory().getButton(By.xpath("//kl-button[@atselector='welcomeSignInBtn']"), "Sing in button");

    private IButton cookieButton = AqualityServices.getElementFactory().getButton(By.id("CybotCookiebotDialogBodyLevelButtonAccept"), "'Ok' cookie button");

    public AuthForm() {
        super(By.xpath("//div[@data-at-selector='welcomeSignInTab']"), "Auth form");
    }


    public void performAuthorization(String email, String password)
    {
        if (cookieButtonIsDisplay()) {
            clickCookieButton();
        }
        emailWaitForClick();
        sendEmail(email);
        passwordWaitForClick();
        sendPassword(password);
        clickSingInButton();
    }

    public boolean cookieButtonIsDisplay() {
        return cookieButton.state().isDisplayed();
    }

    public void emailWaitForClick() {
        emailInput.state().waitForClickable();
    }

    public void passwordWaitForClick() {
        passwordInput.state().waitForClickable();
    }

    public void clickCookieButton() {
        cookieButton.click();
    }

    public void sendEmail(String email) {
        emailInput.sendKeys(email);
    }

    public void sendPassword(String password) {
        passwordInput.sendKeys(password);
    }

    public void clickSingInButton() {

        singInButton.click();
    }

}

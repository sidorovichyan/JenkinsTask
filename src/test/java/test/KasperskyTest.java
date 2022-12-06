package test;


import aquality.selenium.browser.AqualityServices;
import aquality.selenium.core.localization.ILocalizedLogger;
import entity.ProductAndOS;
import form.AuthForm;
import form.DownloadForm;
import form.ModalSendToEmailForm;
import form.SubscriptionForm;
import org.testng.Assert;
import org.testng.annotations.Test;
import util.data.TestDataHelper;
import util.mail.MailHelper;

public class KasperskyTest extends BaseTest {

    private static final ILocalizedLogger logger = AqualityServices.getLocalizedLogger();
    private static final String INBOX_FOLDER = "INBOX";
    private static final String NEWSLETTERS_FOLDER = "INBOX/Newsletters";

    @Test(dataProvider = "ProductAndOS")
    public void productCheck(ProductAndOS productAndOS) {
        logger.info("Authorization on the site");
        AuthForm authForm = new AuthForm();
        authForm.state().waitForDisplayed();
        authForm.performAuthorization(TestDataHelper.getInstance().getValue("username"),TestDataHelper.getInstance().getValue("password"));
        SubscriptionForm subscriptionForm = new SubscriptionForm();
        subscriptionForm.state().waitForDisplayed();
        subscriptionForm.clickDownloadLink();

        logger.info("Go to download tab");
        DownloadForm downloadForm = new DownloadForm();
        downloadForm.state().waitForDisplayed();
        downloadForm.clickButtonOS(productAndOS.getOs());

        logger.info("In the dialog box, click 'Send by mail'");
        downloadForm.clickSendToEmailButtonInCard(productAndOS.getProduct());

        logger.info("Opening a modal window");
        ModalSendToEmailForm modalSendToEmailForm = new ModalSendToEmailForm();
        modalSendToEmailForm.state().waitForDisplayed();
        Assert.assertTrue(modalSendToEmailForm.state().isDisplayed(), "Modal send to form not open");
        modalSendToEmailForm.waitToBeClickableInputEmail();
        Assert.assertEquals(modalSendToEmailForm.getTextFromInputEmail(), TestDataHelper.getInstance().getValue("username"), "Opened modal from with incorrect email");
        MailHelper mailHelper = new MailHelper();
        int sizeMessagesInboxBeforeSend = mailHelper.sizeMessagesFromInbox();
        int sizeMessagesNewslattersBeforeSend = mailHelper.sizeMessagesFromNewslatters();

        logger.info("Sending a message by mail");
        modalSendToEmailForm.sendMail();
        int numbSleep = 0;
        while (mailHelper.sizeMessagesFromInbox() == sizeMessagesInboxBeforeSend
                && sizeMessagesNewslattersBeforeSend == mailHelper.sizeMessagesFromNewslatters()) {
            try {
                numbSleep++;
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new Error("Error while try wait sending message");
            }
            if (numbSleep == 3) {
                throw new Error("Waiting for the message is too long, " + numbSleep + " seconds have passed");
            }
        }
        if (mailHelper.sizeMessagesFromInbox() > sizeMessagesInboxBeforeSend) {
            Assert.assertTrue(mailHelper.checkLastMailWithLinkToDownload(productAndOS.getProduct(), INBOX_FOLDER), "Wrong email link received");
        } else if (mailHelper.sizeMessagesFromNewslatters() > sizeMessagesNewslattersBeforeSend) {
            Assert.assertTrue(mailHelper.checkLastMailWithLinkToDownload(productAndOS.getProduct(), NEWSLETTERS_FOLDER), "Wrong email link received");
        }
        mailHelper.close();
    }
}

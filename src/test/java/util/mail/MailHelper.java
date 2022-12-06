package util.mail;

import com.google.common.collect.Lists;
import org.apache.commons.mail.util.MimeMessageParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import util.data.TestDataHelper;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class MailHelper {

    private static final String PATH_TO_MAIL_PROPS = "props/mail.json";

    private static final String INBOX_FOLDER = "INBOX";

    private static final String NEWSLETTERS_FOLDER = "INBOX/Newsletters";

    private static final String username = TestDataHelper.getInstance().getValue("username");

    private static final String password = TestDataHelper.getInstance().getValue("password-mailru");

    private Folder folder;

    private Store store;

    public MailHelper() {

    }

    public boolean checkLastMailWithLinkToDownload(String product, String folder) {
        Elements el = new Elements();
        List<Message> listMess = null;
        try {
            listMess = this.getMessagesFromFolder(folder);
            MimeMessage cmsg = new MimeMessage((MimeMessage) listMess.get(0));
            MimeMessageParser parser = new MimeMessageParser(cmsg);
            try {
                parser.parse();
            } catch (Exception e) {
                throw new Error("Error while trying parse MimeMessage: " + e);
            }
            String result = parser.getHtmlContent();
            Document doc = Jsoup.parse(result);
            el = doc.selectXpath(String.format("//a[@title='%s']", product));
        } catch (MessagingException e) {
            throw new Error("Error while parsing mail: " + e);
        }
        return el.size() > 0;
    }

    private List<Message> getMessagesFromFolder(String folderName) {
        List<Message> listMess = null;
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(Objects.requireNonNull(getClass().getClassLoader().getResource(PATH_TO_MAIL_PROPS)).getPath())) {
            properties.load(input);
        } catch (IOException e) {
            throw new Error("Can not load props from file: " + e);
        }
        try {
            store = this.startConnection(properties);
        } catch (MessagingException e) {
            throw new Error("Can not connect to email: " + e);
        }
        folder = this.allMessagesFromFolder(store, folderName);
        Message[] message;
        try {
            message = folder.getMessages();
            listMess = Lists.reverse(Arrays.stream(message).toList());
        } catch (MessagingException e) {
            throw new Error("Error while get messages from folder " + e);
        }
        return listMess;
    }

    private Store startConnection(Properties properties) throws MessagingException {
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        session.setDebug(false);
        Session emailSession = Session.getDefaultInstance(properties);
        Store store = emailSession.getStore("imaps");
        store.connect("imap.mail.ru", username, password);
        return store;
    }

    private Folder allMessagesFromFolder(Store store, String namefolder) {
        Folder inbox = null;
        try {
            if (namefolder.equals(NEWSLETTERS_FOLDER)) {
                inbox = store.getDefaultFolder().getFolder(NEWSLETTERS_FOLDER);
            } else if (namefolder.equals(INBOX_FOLDER)) {
                inbox = store.getDefaultFolder().getFolder(INBOX_FOLDER);
            }
            assert inbox != null;
            inbox.open(Folder.READ_ONLY);
        } catch (MessagingException e) {
            throw new Error("Can not open folder with messages: " + e);
        }
        return inbox;
    }

    public int sizeMessagesFromNewslatters() {
        List<Message> listMess = this.getMessagesFromFolder(NEWSLETTERS_FOLDER);
        return listMess.size();
    }

    public int sizeMessagesFromInbox() {
        List<Message> listMess = this.getMessagesFromFolder(INBOX_FOLDER);
        return listMess.size();
    }

    public void close() {
        try {
            folder.close();
            store.close();
        } catch (MessagingException e) {
            throw new Error("Error while close folder or store " + e);
        }

    }

}

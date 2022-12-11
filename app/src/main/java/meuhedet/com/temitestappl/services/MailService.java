package meuhedet.com.temitestappl.services;

import android.util.Log;

import java.util.*;

import javax.mail.*;

import meuhedet.com.temitestappl.BuildConfig;

public class MailService {
    public static boolean check() {
        String host = "imap.gmail.com";
        String username = "nikan2110isr@gmail.com"; //nimrodperlman2@gmail.com
        String password = BuildConfig.GOOGLE_PASSWORD_EMAIL; // gryoowcrwxmwogto
        boolean flag = false;
        try {
            //create properties field
            Properties properties = new Properties();
            properties.setProperty("mail.imap.ssl.enable", "true");
            Session emailSession = Session.getDefaultInstance(properties);
            //create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore("imap");
            store.connect(host, 993, username, password);
            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_WRITE);
            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            Log.i("Mail service", "Messages length---" + messages.length);
            for (Message message : messages) {
                String msg = message.getSubject().toString();
                if (msg.contains("robot")) {
                    flag = true;
                }
                message.setFlag(Flags.Flag.DELETED, true);
            }
            //close the store and folder objects
            emailFolder.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
}
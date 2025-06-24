package com.bonrix.dggenraterset.Utility;

import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

public class GmailForSMSEmailAlert {

    private static String HOST = "smtp.gmail.com";
    private static String PORT = "465";
    private static String FROM = "crm.bonrix@gmail.com";
    private static String TO = "harshilpansara86@gmail.com";
    private static String STARTTLS = "true";
    private static String AUTH = "true";
    private static String DEBUG = "true";
    private static String SOCKET_FACTORY = "javax.net.ssl.SSLSocketFactory";
    private static String SUBJECT = "Device Information";
public static final Logger log = Logger.getLogger(GmailForSMSEmailAlert.class);
   
    public static void main(String[] args) {
        // GmailSender.send();
    send("crm.bonrix@gmail.com","crm.bonrix@gmail.com","test");
    }

    public static synchronized void send(String From, String To, String Html) {
        //Use Properties object to set environment properties
        TO = To;
        FROM = From;

   
        log.info("remaining.......  Sending :  " + From + ":::" + To + "::::" + Html);
        String USER = "";
        String PASSWORD = "";
     //   Hostname hname = (Hostname) new HibernateDAO.addhost().HostListbyadmin("gmail").get(0);

       USER = "alert.saharsh@gmail.com";
        PASSWORD ="alertsaharsh123";
       
        FROM=USER;
        Properties props = new Properties();

        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.user", USER);

        props.put("mail.smtp.auth", AUTH);
        props.put("mail.smtp.starttls.enable", STARTTLS);
        props.put("mail.smtp.debug", DEBUG);

        props.put("mail.smtp.socketFactory.port", PORT);
        props.put("mail.smtp.socketFactory.class", SOCKET_FACTORY);
        props.put("mail.smtp.socketFactory.fallback", "false");

        try {

            //Obtain the default mail session
            Session session = Session.getInstance(props, null);
            session.setDebug(true);

            //Construct the mail message
            MimeMessage message = new MimeMessage(session);
            // message.setText(TEXT,"utf-8", "html");
            //message.setContent(message, "text/html");
            message.setSubject(SUBJECT);
            message.setFrom(new InternetAddress(FROM));
            message.addRecipient(RecipientType.TO, new InternetAddress(TO));
 
           // Create a Part for the html content

            MimeMultipart multipart = new MimeMultipart("related");

            // first part  (the html)
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(Html, "text/html");

            // add it
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            //setHTMLContent(message,Html);


            message.saveChanges();
            //Use Transport to deliver the message
            Transport transport = session.getTransport("smtp");
            transport.connect(HOST, USER, PASSWORD);
 
            transport.sendMessage(message, message.getAllRecipients());

            transport.close();

        } catch (MessagingException e) {
            e.printStackTrace();
            log.info(">>GmailSender " + e);

        }
    }

    public static synchronized void send(String From, String To, String Html, String sub)
    {
        //Use Properties object to set environment properties
        TO = To;
        FROM = From;
        SUBJECT = sub;
        String USER = "";
        String PASSWORD = "";
       // Hostname hname = (Hostname) new HibernateDAO.addhost().HostListbyadmin("admin").get(0);
        FROM = "alert.saharsh@gmail.com";
        USER = "alert.saharsh@gmail.com";
        PASSWORD ="alertsaharsh123";
        Properties props = new Properties();

        log.info("SendingHarshil :" + FROM + ":::" + To + "::::" + Html);
       
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.user", USER);

        props.put("mail.smtp.auth", AUTH);
        props.put("mail.smtp.starttls.enable", STARTTLS);
        props.put("mail.smtp.debug", DEBUG);

        props.put("mail.smtp.socketFactory.port", PORT);
        props.put("mail.smtp.socketFactory.class", SOCKET_FACTORY);
        props.put("mail.smtp.socketFactory.fallback", "false");

        try {

            //Obtain the default mail session
            Session session = Session.getInstance(props, null);
            session.setDebug(true);

            //Construct the mail message
            MimeMessage message = new MimeMessage(session);
            // message.setText(TEXT,"utf-8", "html");
            //message.setContent(message, "text/html");
            message.setSubject(SUBJECT);
            //  message.setFrom(new InternetAddress(FROM));
            message.addRecipient(RecipientType.TO, new InternetAddress(TO));



// Create a Part for the html content




            MimeMultipart multipart = new MimeMultipart("related");

            // first part  (the html)
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(Html, "text/html");

            // add it
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            //setHTMLContent(message,Html);


            message.saveChanges();
            //Use Transport to deliver the message
            Transport transport = session.getTransport("smtp");
            transport.connect(HOST, USER, PASSWORD);

            transport.sendMessage(message, message.getAllRecipients());

            transport.close();

        } catch (MessagingException e) {
            log.info(">>GmailSender " + e);
              e.printStackTrace();
                     
        }
    }
    public static synchronized void krishtracksend(String From, String To, String Html, String sub,String USER,String PASSWORD)
    {
        //Use Properties object to set environment properties
        TO = To;
        FROM = From;
        SUBJECT = sub;


       
     
        Properties props = new Properties();

        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.user", USER);

        props.put("mail.smtp.auth", AUTH);
        props.put("mail.smtp.starttls.enable", STARTTLS);
        props.put("mail.smtp.debug", DEBUG);

        props.put("mail.smtp.socketFactory.port", PORT);
        props.put("mail.smtp.socketFactory.class", SOCKET_FACTORY);
        props.put("mail.smtp.socketFactory.fallback", "false");

        try {

            //Obtain the default mail session
            Session session = Session.getInstance(props, null);
            session.setDebug(true);

            //Construct the mail message
            MimeMessage message = new MimeMessage(session);
            // message.setText(TEXT,"utf-8", "html");
            //message.setContent(message, "text/html");
            message.setSubject(SUBJECT);
            //  message.setFrom(new InternetAddress(FROM));
            message.addRecipient(RecipientType.TO, new InternetAddress(TO));



// Create a Part for the html content




            MimeMultipart multipart = new MimeMultipart("related");

            // first part  (the html)
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(Html, "text/html");

            // add it
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            //setHTMLContent(message,Html);


            message.saveChanges();
            //Use Transport to deliver the message
            Transport transport = session.getTransport("smtp");
            transport.connect(HOST, USER, PASSWORD);

            transport.sendMessage(message, message.getAllRecipients());

            transport.close();

        } catch (MessagingException e) {
            log.info(">>GmailSender " + e);
              e.printStackTrace();
                     
        }
    }
}
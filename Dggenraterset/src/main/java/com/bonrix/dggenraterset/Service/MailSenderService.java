package com.bonrix.dggenraterset.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailSenderService {
	
	// public static final Logger log = Logger.getLogger(VideoconDailySchedule.class);

	  
		String Html, Html1 = "", Html2, HtmlContent;
		String msg = "";
		//String email = "gpsbonrix@gmail.com,Vasudev.thakkar@larenon.com,yogendra.chauhan@larenon.com,Bhawesh.jha@larenon.com";
		private static String HOST = "smtp.gmail.com";
		private static String PORT = "465";
		private static String FROM = "";
		private static String TO = "";
		private static String STARTTLS = "true";
		private static String AUTH = "true";
		private static String DEBUG = "true";
		private static String SOCKET_FACTORY = "javax.net.ssl.SSLSocketFactory";
		private static String SUBJECT = "Videocon Daily Report";
		static String username = "crm.bonrix.in";
		static String password = "sajancrm2018";

		public void SendEMail(String msg, String dir, String filename) throws IOException {
			Properties prop = new Properties();
			InputStream input = getClass().getResourceAsStream("/resources/socket.properties");
			prop.load(input);
			String bcc = prop.getProperty("dbuser");
			String[] bcc1 = bcc.split(",");
			for (int i = 0; i < bcc1.length; i++) {
				send(bcc1[i], msg, dir, filename);
			}
		}

		public String send(String To, String Html, String dir, String filename) {
			TO = To;
			FROM = "alert.saharsh@gmail.com";
			String USER = username;
			String PASSWORD = password;

			FROM = USER;
			System.out.println("Sending::::" + "alert.saharsh@gmail.com" + ":::" + To + "::::" + Html);
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

				// Obtain the default mail session
				Session session = Session.getInstance(props, null);
				session.setDebug(true);

				// Construct the mail message
				MimeMessage message = new MimeMessage(session);
				// message.setText(TEXT,"utf-8", "html");
				// message.setContent(message, "text/html");
				message.setSubject(SUBJECT);
				message.setFrom(new InternetAddress(FROM));
				message.addRecipient(RecipientType.TO, new InternetAddress(TO));

				// Create a Part for the html content

				MimeMultipart multipart = new MimeMultipart();

				// first part (the html)
				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setContent(Html, "text/html");
				String file = dir + filename;
				String fileName = filename;
				DataSource source = new FileDataSource(file);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(fileName);
				// add it
				multipart.addBodyPart(messageBodyPart);
				message.setContent(multipart);
				// setHTMLContent(message,Html);

				message.saveChanges();
				// Use Transport to deliver the message
				Transport transport = session.getTransport("smtp");
				transport.connect(HOST, USER, PASSWORD);

				transport.sendMessage(message, message.getAllRecipients());

				transport.close();
				System.out.println("Sending::::  Sent");

			} catch (MessagingException e) {
				e.printStackTrace();
				System.out.println(">>GmailSender " + e);

			}
			return "Mail Sent";
		}
}

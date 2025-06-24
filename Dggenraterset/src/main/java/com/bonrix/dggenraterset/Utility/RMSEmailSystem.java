package com.bonrix.dggenraterset.Utility;

import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

public class RMSEmailSystem {
	private static Logger log = Logger.getLogger(RMSEmailSystem.class);
	 private static ResourceBundle rb = ResourceBundle.getBundle("applicationMessages");
	 
	 public static void main(String[] args)
	    {
	        System.out.println("I am a Geek");
	        EmailSystem("crm.bonrix@gmail.com","E://WP30CRS485.java","dfg");
	    }
	 
	public void SendEmail(List<String> attachment_PathList)
	{
		 String EmailIds = rb.getString("EMAIL_IDS");
		 String[] mailTo=EmailIds.split(",");
		 for(String emailAddress:mailTo)
		 {
			 EmailSystem(attachment_PathList,emailAddress);
		 }
		 
	}
	
	
	public static void EmailSystem(String to,String subject,String body)
	{   
		
	      // Sender's email ID needs to be mentioned
	      String from = "alert.saharsh@gmail.com";
	      final String username = "saharshalarmalert@gmail.com";//change accordingly
	      final String password = "zxeclgvnvekbumkm";//change accordingly

	      // Assuming you are sending email through relay.jangosmtp.net
	      String host = "smtp.gmail.com";

	      Properties props = new Properties();
	      props.put("mail.smtp.auth", "true");
	      props.put("mail.smtp.starttls.enable", "true");
	      props.put("mail.smtp.host", host);
	      props.put("mail.smtp.port", "25");
	      props.put("mail.smtp.ssl.protocols", "TLSv1.2");
	      // Get the Session object.
	      Session session = Session.getInstance(props,
	         new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	               return new PasswordAuthentication(username, password);
	            }
		});

	      try {
	            // Create a default MimeMessage object.
	            Message message = new MimeMessage(session);

	   	   // Set From: header field of the header.
		   message.setFrom(new InternetAddress(from));

		   // Set To: header field of the header.
		   message.setRecipients(Message.RecipientType.TO,
	              InternetAddress.parse(to));

		   // Set Subject: header field
		   message.setSubject(subject);

		   // Send the actual HTML message, as big as you like
		   message.setContent(
	              "<h1>"+body+"</h1>",
	             "text/html");

		   // Send message
		   Transport.send(message);

		   log.info("Sent message successfully....");

	      } catch (MessagingException e) {
		   e.printStackTrace();
		   throw new RuntimeException(e);
	      }
	   }
		
		
		
	/*	log.info(to);
		// Recipient's email ID needs to be mentioned.
	      //String to = "crm.bonrix@gmail.com";

	      // Sender's email ID needs to be mentioned
	      String from = "alert.saharsh@gmail.com";

	      final String username = "alert.saharsh@gmail.com";//change accordingly
	      final String password = "lgpsgdqdkfebfati";//change accordingly

	      // Assuming you are sending email through relay.jangosmtp.net
	      String host = "smtp.gmail.com";

	      Properties props = new Properties();
	      props.put("mail.smtp.auth", "true");
	      props.put("mail.smtp.starttls.enable", "true");
	      props.put("mail.smtp.host", host);
	      props.put("mail.smtp.port", "25");
	      props.put("mail.smtp.ssl.protocols", "TLSv1.2");
	      // Get the Session object.
	      Session session = Session.getInstance(props,
	         new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	               return new PasswordAuthentication(username, password);
	            }
	         });

	      try {
	         // Create a default MimeMessage object.
	         Message message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));

	         // Set To: header field of the header.
	         message.setRecipients(Message.RecipientType.TO,
	            InternetAddress.parse(to));

	         
	         // Set Subject: header field
	         message.setSubject("Monthly Deta MTD Report");

	         // Create the message part
	         BodyPart messageBodyPart = new MimeBodyPart();

	         // Now set the actual message
	         messageBodyPart.setText("Please find Enclosed");

	         Multipart multipart = new MimeMultipart("mixed");
	         if(attachment_PathList!=null) {
	         for (String str : attachment_PathList) {
	             MimeBodyPart messageBodyPart1 = new MimeBodyPart();
	             DataSource source = new FileDataSource(str);
	             messageBodyPart1.setDataHandler(new DataHandler(source));
	             messageBodyPart1.setFileName(source.getName());
	             multipart.addBodyPart(messageBodyPart1);
	         }
	         // Send the complete message parts
	         message.setContent(multipart);
	         }
	         
	     

	         // Send message
	         Transport.send(message);

	         log.info("Sent message successfully....");
	  
	      } catch (MessagingException e) {
	         throw new RuntimeException(e);
	      }*/
	
	
	public void EmailSystem(List<String> attachment_PathList,String to)
	{
		log.info(to);
		// Recipient's email ID needs to be mentioned.
	      //String to = "crm.bonrix@gmail.com";

	      // Sender's email ID needs to be mentioned
	      String from = "alert.saharsh@gmail.com";

	      final String username = "alert.saharsh@gmail.com";//change accordingly
	      final String password = "lgpsgdqdkfebfati";//change accordingly

	      // Assuming you are sending email through relay.jangosmtp.net
	      String host = "smtp.gmail.com";

	      Properties props = new Properties();
	      props.put("mail.smtp.auth", "true");
	      props.put("mail.smtp.starttls.enable", "true");
	      props.put("mail.smtp.host", host);
	      props.put("mail.smtp.port", "25");

	      // Get the Session object.
	      Session session = Session.getInstance(props,
	         new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	               return new PasswordAuthentication(username, password);
	            }
	         });

	      try {
	         // Create a default MimeMessage object.
	         Message message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));

	         // Set To: header field of the header.
	         message.setRecipients(Message.RecipientType.TO,
	            InternetAddress.parse(to));

	         
	         // Set Subject: header field
	         message.setSubject("Monthly Deta MTD Report");

	         // Create the message part
	         BodyPart messageBodyPart = new MimeBodyPart();

	         // Now set the actual message
	         messageBodyPart.setText("Please find Enclosed");

	       /*  List<String> attachment_PathList=new  ArrayList<String>(); 
	         attachment_PathList.add("/opt/VNT.txt");  
	         attachment_PathList.add("/opt/VNT1.txt");*/
	         Multipart multipart = new MimeMultipart("mixed");
	         for (String str : attachment_PathList) {
	             MimeBodyPart messageBodyPart1 = new MimeBodyPart();
	             DataSource source = new FileDataSource(str);
	             messageBodyPart1.setDataHandler(new DataHandler(source));
	             messageBodyPart1.setFileName(source.getName());
	             multipart.addBodyPart(messageBodyPart1);
	         }
	         
	         // Send the complete message parts
	         message.setContent(multipart);

	         // Send message
	         Transport.send(message);

	         log.info("Sent message successfully....");
	  
	      } catch (MessagingException e) {
	         throw new RuntimeException(e);
	      }
	}

}

package com.bonrix.dggenraterset.Utility;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

public class PlainTextEmail {
	
	private static final Logger log = Logger.getLogger(PlainTextEmail.class);
	public static void main(String[] args) throws AddressException, MessagingException {
		//sendPlainTextEmail();
		
}
	
	public static void sendPlainTextEmail(String mailTo,String subject,String message)  {
		log.info("SendEmail sendPlainTextEmail");
		try {
		String host = "smtp.gmail.com"; 
        String port = "587";
        String mailFrom = "alert.saharsh@gmail.com";
        String password = "lgpsgdqdkfebfati";

	        Properties properties = new Properties();  
	        properties.put("mail.smtp.host", host);  
	        properties.put("mail.smtp.port", port);
	        properties.put("mail.smtp.auth", "true");  
	        properties.put("mail.smtp.starttls.enable", "true");
	        properties.put("mail.smtp.user", mailFrom);
	
	        Session session = Session.getDefaultInstance(properties);
	        Message msg = new MimeMessage(session);
	
	        msg.setFrom(new InternetAddress(mailFrom));
	        InternetAddress[] toAddresses = { new InternetAddress(mailTo) };
			msg.setRecipients(Message.RecipientType.TO, toAddresses);
	 	        msg.setSubject(subject);
	 	        msg.setSentDate(new Date());
	 	        msg.setText(message);
	 	
	 	        Transport t = session.getTransport("smtp");   
	 	        t.connect(mailFrom, password);
	 	        t.sendMessage(msg, msg.getAllRecipients());
	 	        t.close();
	        	
	        } catch (AddressException e){
	        	log.info(e.getMessage());
			e.printStackTrace();
			}
		catch (MessagingException e) {
			log.info(e.getMessage());
			e.printStackTrace();
		}
	       
	
	    }
}


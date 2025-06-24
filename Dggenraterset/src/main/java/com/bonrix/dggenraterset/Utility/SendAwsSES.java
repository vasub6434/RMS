package com.bonrix.dggenraterset.Utility;


import com.bonrix.dggenraterset.Utility.SendAwsSES;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.log4j.Logger;

public class SendAwsSES {
	private static final Logger log = Logger.getLogger(SendAwsSES.class);

	static final String FROM = "alert.saharsh@gmail.com";

	static final String FROMNAME = "SaHarsh Solutions";

	static final String SMTP_USERNAME = "AKIASDWYBM7RJKCNOP5H";

	static final String SMTP_PASSWORD = "BD9pp7s5EvvVCPd18ZN9CZX9XEl7PRnIpAo7IbvsqyZW";

	static final String HOST = "email-smtp.ap-south-1.amazonaws.com";

	static final int PORT = 587;


	public void sendMail(String recipientAddressTo, String Subject, String bodyText, String filePath, String fileName)
			throws IOException, MessagingException {
		log.info("Email Metod");
		Properties props = System.getProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.port", Integer.valueOf(587));
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");
		props.put("mail.smtp.auth", "true");
		Session session = Session.getDefaultInstance(props);
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom((Address) new InternetAddress("alert.saharsh@gmail.com", "SaHarsh Solutions"));
		msg.setRecipient(Message.RecipientType.TO, (Address) new InternetAddress(recipientAddressTo));
		msg.setSubject(Subject);
		msg.setContent(bodyText, "text/html");
		MimeMultipart mimeMultipart = new MimeMultipart();
		MimeBodyPart textBodyPart = new MimeBodyPart();
		textBodyPart.setText(bodyText);
		MimeBodyPart attachmentBodyPart = new MimeBodyPart();
		DataSource source = new FileDataSource(String.valueOf(filePath) + "/" + fileName);
		attachmentBodyPart.setDataHandler(new DataHandler(source));
		attachmentBodyPart.setFileName(fileName);
		mimeMultipart.addBodyPart((BodyPart) textBodyPart);
		mimeMultipart.addBodyPart((BodyPart) attachmentBodyPart);
		msg.setContent((Multipart) mimeMultipart);
		Transport transport = session.getTransport();
		try {
			log.info("Sending...");
			transport.connect("email-smtp.ap-south-1.amazonaws.com", "AKIASDWYBM7RJKCNOP5H",
					"BD9pp7s5EvvVCPd18ZN9CZX9XEl7PRnIpAo7IbvsqyZW");
			transport.sendMessage((Message) msg, msg.getAllRecipients());
			log.info("Email sent!");
		} catch (Exception ex) {
			log.info("The email was not sent.");
			log.info("Error message: " + ex.getMessage());
		} finally {
			transport.close();
		}
	}

	public static void main(String[] args) throws IOException, MessagingException {
    	SendAwsSES mail=new SendAwsSES();
    	//mail.sendMail(recipientAddressTo,"DG Running Input History Report","Kindly Find Attached","C://","Desert.jpg");
   mail.sendMail("crm.bonrix@gmail.com", URLEncoder.encode("Sajan DG Running Input History Report","UTF-8"),  URLEncoder.encode("Kindly Find Attached","UTF-8"));
	}
	  
	public String sendMail(String recipientAddressTo, String Subject, String bodyText)
			throws IOException, MessagingException {
	//	log.info("Text Email Metod :: "+bodyText);
		Properties props = System.getProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.port", PORT);
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");
		props.put("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(props);

		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(FROM, FROMNAME));
		msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientAddressTo));
		msg.setSubject(URLDecoder.decode(Subject,"UTF-8"));
		//byte[] decodedBytes = URLDecoder.decode((bodyText,"UTF-8");
		msg.setContent(URLDecoder.decode( bodyText,"UTF-8"), "text/html");
		//log.info("Text Email Metod :: "+URLDecoder.decode(bodyText,"UTF-8"));
		Transport transport = session.getTransport();
		try {
			log.info("Sending...");
			transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);

			transport.sendMessage(msg, msg.getAllRecipients());
			log.info("Email sent!");
		} catch (Exception ex) {
			log.info("The email was not sent.");
			log.info("Error message: " + ex.getMessage());
		} finally {
			transport.close();
		}

		return "Email sent Successfully";
	}

}
/*
import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

public class SendAwsSES {
	
	private static final Logger log = Logger.getLogger(SendAwsSES.class);
	
	 static final String FROM = "alert.saharsh@gmail.com";
	    static final String FROMNAME = "SaHarsh Solutions";
	    
	    static final String SMTP_USERNAME = "AKIASDWYBM7RJKCNOP5H";
	    
	    static final String SMTP_PASSWORD = "BD9pp7s5EvvVCPd18ZN9CZX9XEl7PRnIpAo7IbvsqyZW";
	    
	    static final String HOST = "email-smtp.ap-south-1.amazonaws.com";
	    
	    static final int PORT = 587;
	    
	    
	public static void main(String[] args) throws IOException, MessagingException {
    	SendAwsSES mail=new SendAwsSES();
    	//mail.sendMail(recipientAddressTo,"DG Running Input History Report","Kindly Find Attached","C://","Desert.jpg");
   mail.sendMail("crm.bonrix@gmail.com","Sajan DG Running Input History Report",  "Kindly Find Attached");
	}
	public   void sendMail(String recipientAddressTo,String Subject,String bodyText,String filePath,String fileName) throws IOException, MessagingException {
    log.info("Email Metod");   
    Properties props = System.getProperties();
	props.put("mail.transport.protocol", "smtp");
	props.put("mail.smtp.port", PORT); 
	props.put("mail.smtp.starttls.enable", "true");
	props.put("mail.smtp.ssl.protocols", "TLSv1.2");
	props.put("mail.smtp.auth", "true");

	Session session = Session.getDefaultInstance(props);

    MimeMessage msg = new MimeMessage(session);
    msg.setFrom(new InternetAddress(FROM,FROMNAME));
    msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientAddressTo));
    msg.setSubject(Subject);
    msg.setContent(bodyText,"text/html");
   
    Multipart multipart = new MimeMultipart();
    MimeBodyPart textBodyPart = new MimeBodyPart();
    textBodyPart.setText(bodyText);
    MimeBodyPart attachmentBodyPart= new MimeBodyPart();
    DataSource source = new FileDataSource(filePath+"/"+fileName); // ex : "C:\\test.pdf"
    attachmentBodyPart.setDataHandler(new DataHandler(source));
    attachmentBodyPart.setFileName(fileName); // ex : "test.pdf"
    multipart.addBodyPart(textBodyPart);  // add the text part
    multipart.addBodyPart(attachmentBodyPart); // add the attachement part
    msg.setContent(multipart);  
    Transport transport = session.getTransport();
                
    try   
    {
    	log.info("Sending...");
        
        transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
    	
        transport.sendMessage(msg, msg.getAllRecipients());
        log.info("Email sent!");
    }
    catch (Exception ex) {
    	log.info("The email was not sent.");
    	log.info("Error message: " + ex.getMessage());
    }
    finally
    {
        transport.close();
    }
   
  }
	
	public   String sendMail(String recipientAddressTo,String Subject,String bodyText) throws IOException, MessagingException {
	    log.info("Text Email Metod");   
	    Properties props = System.getProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.port", PORT); 
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");
		props.put("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(props);

	    MimeMessage msg = new MimeMessage(session);
	    msg.setFrom(new InternetAddress(FROM,FROMNAME));
	    msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientAddressTo));
	    msg.setSubject(Subject);
	    msg.setContent(bodyText,"text/html");
	   
	    Transport transport = session.getTransport();
	                
	    try   
	    {
	    	log.info("Sending...");
	        
	        transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
	    	
	        transport.sendMessage(msg, msg.getAllRecipients());
	        log.info("Email sent!");
	    }
	    catch (Exception ex) {
	    	log.info("The email was not sent.");
	    	log.info("Error message: " + ex.getMessage());
	    }
	    finally
	    {
	        transport.close();
	    }
	    
	    return "Email sent Successfully";
	  }

}*/

package com.bonrix.dggenraterset.Utility;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

public class GmailEmailSender {
	
	public void EmailSystem(String to,String subject,String body,String filePath)
	{

   	 final String username = "saharshalarmalert@gmail.com";//change accordingly
	      final String password = "zxeclgvnvekbumkm";//change accordingly


       Properties props = new Properties();
       props.put("mail.smtp.auth", "true");  
       props.put("mail.smtp.starttls.enable", "true");
       props.put("mail.smtp.host", "smtp.gmail.com");
       
       props.put("mail.smtp.port", "587");
       props.put("mail.smtp.ssl.protocols", "TLSv1.2");


       Session session = Session.getInstance(props, new javax.mail.Authenticator() {
           protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(username, password);
           }
       });

       try {
           Message message = new MimeMessage(session);
           message.setFrom(new InternetAddress(username));
           message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to)); // Recipient's email address
           message.setSubject(subject);
           
           Multipart multipart = new MimeMultipart();
           
           BodyPart messageBodyPart = new MimeBodyPart();
           messageBodyPart.setText(body);
           multipart.addBodyPart(messageBodyPart);
           
           if(filePath.length()!=0) {
           messageBodyPart = new MimeBodyPart();
           DataSource source = new FileDataSource(filePath);
           messageBodyPart.setDataHandler(new DataHandler(source));
           messageBodyPart.setFileName(filePath);
           multipart.addBodyPart(messageBodyPart);
           }
           message.setContent(multipart);
           Transport.send(message);
           System.out.println("Email sent successfully!");

       } catch (MessagingException e) {
           throw new RuntimeException(e);
       }
	}
	
	
	// Send email with dynamic table content in HTML format
    public void sendEmailWithTable(String to, String subject, List<String[]> tableData) {

        final String username = System.getenv("EMAIL_USERNAME"); // Environment variable for username
        final String password = System.getenv("EMAIL_PASSWORD"); // Environment variable for password

        if (username == null || password == null) {
            throw new RuntimeException("Email credentials are not configured correctly.");
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to)); // Recipient's email address
            message.setSubject(subject);

            // Build the HTML content (dynamic table)
            StringBuilder htmlContent = new StringBuilder();
            htmlContent.append("<html><body>");
            htmlContent.append("<h2>Dynamic Data Table</h2>");
            htmlContent.append("<table border='1' cellpadding='5' cellspacing='0'>");
            htmlContent.append("<tr><th>#</th><th>Column 1</th><th>Column 2</th><th>Column 3</th></tr>");

            // Loop through the tableData to create table rows
            for (int i = 0; i < tableData.size(); i++) {
                String[] row = tableData.get(i);
                htmlContent.append("<tr>");
                htmlContent.append("<td>" + (i + 1) + "</td>");
                for (String cell : row) {
                    htmlContent.append("<td>" + cell + "</td>");
                }
                htmlContent.append("</tr>");
            }

            htmlContent.append("</table>");
            htmlContent.append("</body></html>");

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(htmlContent.toString(), "text/html");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // Set the message content
            message.setContent(multipart);
            Transport.send(message);
            System.out.println("Email sent successfully with dynamic table!");

        } catch (MessagingException e) {
            System.err.println("Error sending email: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
    	 final String username = "sales@saharsh.in";//change accordingly
	      final String password = "Golden@1234";//change accordingly

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("crm.bonrix@gmail.com")); // Recipient's email address
            message.setSubject("Test Email from JavaMail");
            
            // Create a multipart message
            Multipart multipart = new MimeMultipart();
            
            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("This is the email body.");
            multipart.addBodyPart(messageBodyPart);
            
            // Attach a file
            messageBodyPart = new MimeBodyPart();
            String filename = "C:\\Users\\crmbo\\Downloads\\Updated-DG Input History Report.csv";
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);

            // Set the message content
            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}

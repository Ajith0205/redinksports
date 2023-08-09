/**
 * 
 */
package com.red.ink.e_mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.stereotype.Component;

/**
 * @author ajith
 *
 */
@Component
public class SendMail {
	
	private static String SMTP_AUTH_USER = "redinksports@gmail.com"; // email_id of sender

	static String protocol = "smtp";
	static String host = "smtp.gmail.com";
	static String port = "587";
	static String user = "redinksports@gmail.com";
	static String password = "dignrxobcpbfvjwv";
	static boolean tls = true;
	static boolean auth = true;

	public static void sendMail(String[] to, String text, String subject, String cc) {
		try {
			Properties props = System.getProperties();
			props.setProperty("mail.transport.protocol", protocol);
			props.put("mail.smtp.starttls.enable", tls);
			props.setProperty("mail.smtp.host", host);
			props.put("mail.smtp.auth", auth);
			props.put("mail.smtp.port", port);
			props.setProperty("mail.user", user);
			props.setProperty("mail.password", password);

			Session mailSession = Session.getDefaultInstance(props, null); //
			mailSession.setDebug(true);
			Transport transport = mailSession.getTransport("smtp");
			MimeMessage message = new MimeMessage(mailSession);
			message.setSentDate(new java.util.Date());
			message.setSubject(subject);
			message.setFrom(new InternetAddress(SMTP_AUTH_USER));
			
			for(String toAddress : to) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
			}

			if (cc != null) {
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
			}

			MimeMultipart multipart = new MimeMultipart();

			// first part (the html)
			MimeBodyPart messageBodyPart = new MimeBodyPart();

			messageBodyPart.setContent(text, "text/html");

			// add it
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);

			transport.connect("smtp.gmail.com", user, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			System.out.println("Mail Sent.....");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendMailResetPassword(String to, String text) {
		try {
			Properties props = System.getProperties();
			props.setProperty("mail.transport.protocol", protocol);
			props.put("mail.smtp.starttls.enable", tls);
			props.setProperty("mail.smtp.host", host);
			props.put("mail.smtp.auth", auth);
			props.put("mail.smtp.port", port);
			props.setProperty("mail.user", user);
			props.setProperty("mail.password", password);

			Session mailSession = Session.getDefaultInstance(props, null); //
			mailSession.setDebug(true);
			Transport transport = mailSession.getTransport("smtp");
			MimeMessage message = new MimeMessage(mailSession);
			message.setSentDate(new java.util.Date());
			message.setSubject("Reset Password");
			message.setFrom(new InternetAddress(SMTP_AUTH_USER));

			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			MimeMultipart multipart = new MimeMultipart();

			// first part (the html)
			MimeBodyPart messageBodyPart = new MimeBodyPart();

			messageBodyPart.setContent(text, "text/html");

			// add it
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);

			transport.connect("smtp.gmail.com", user, password);
			transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
			transport.close();
			System.out.println("Mail Sent.....");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

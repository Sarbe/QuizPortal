package util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {

	public static void sendmail(String str) throws Exception {
		Properties props = new Properties();
		props.put("mail.smtp.host", ConfigFile.getKey("mail.smtp.host"));
		props.put("mail.smtp.socketFactory.port", ConfigFile.getKey("mail.smtp.port"));
		props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", ConfigFile.getKey("mail.smtp.port"));

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication("sarbe85@gmail.com", "harrypotter1$3");
					}
				});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("Sarbe_ss"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("sarbe97@gmail.com"));
			message.setSubject("Quiz Subject");


			message.setContent(str, "text/html");
			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}

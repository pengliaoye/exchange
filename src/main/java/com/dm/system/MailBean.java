package com.dm.system;

import javax.annotation.Resource;
import javax.mail.MailSessionDefinition;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@MailSessionDefinition(name = "java:comp/myMailSession", host = "smtp.gmail.com", transportProtocol = "smtps", properties = { "mail.debug=true" })
public class MailBean {

	@Resource(name = "java:comp/myMailSession")
	Session session;

	public void sendMail() {
		String from = "";
		String pwd = "";
		String to = "";

		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));

			message.setSubject("subject");
			message.setText("text");

			Transport t = session.getTransport();
			t.connect(from, pwd);
			t.sendMessage(message, message.getAllRecipients());
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

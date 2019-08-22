package com.cjosan.getwhatyoucode.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailSenderService {

	@Autowired
	private JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String username;

	public void sendTextContent(String emailTo, String subject, String message) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();

		mailMessage.setFrom(username);
		mailMessage.setTo(emailTo);
		mailMessage.setSubject(subject);
		mailMessage.setText(message);

		mailSender.send(mailMessage);
	}

	public void sendHtmlContent(String emailTo, String subject, String message) {
		MimeMessage mailMessage = mailSender.createMimeMessage();

		MimeMessageHelper helper = null;
		try {
			helper = new MimeMessageHelper(mailMessage, true);
			helper.setFrom(username);
			helper.setTo(emailTo);
			helper.setSubject(subject);
			helper.setText(message, true);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

		mailSender.send(mailMessage);
	}

}

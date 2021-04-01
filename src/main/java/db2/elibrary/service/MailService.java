package db2.elibrary.service;

import org.springframework.mail.javamail.JavaMailSender;


public interface MailService {
    void sendTextMail(String to, String subject, String content, JavaMailSender mailSender);
    void sendHtmlMail(String to, String subject, String content, JavaMailSender mailSender);
}

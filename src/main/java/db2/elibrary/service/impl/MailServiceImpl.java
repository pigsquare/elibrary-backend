package db2.elibrary.service.impl;

import db2.elibrary.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service("MailService")
@Slf4j
public class MailServiceImpl implements MailService {
    @Override
    public void sendTextMail(String to, String subject, String content, JavaMailSender mailSender) {
        SimpleMailMessage message = new SimpleMailMessage();
        String from = "no-reply@squarepig.cn";
        message.setFrom(from);
        message.setTo(to);
        message.setText(content);
        message.setSubject(subject);
        mailSender.send(message);
    }

    @Override
    public void sendHtmlMail(String to, String subject, String content, JavaMailSender mailSender) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            String from = "no-reply@service.shu.fyi";
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("发送HtmlMessage时发生异常！", e);
        }
    }
}

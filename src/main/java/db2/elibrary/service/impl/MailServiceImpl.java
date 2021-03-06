package db2.elibrary.service.impl;

import db2.elibrary.entity.BorrowRecord;
import db2.elibrary.entity.Reservation;
import db2.elibrary.service.MailService;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service("MailService")
@Slf4j
public class MailServiceImpl implements MailService {
    private final FreeMarkerConfigurer freeMarkerConfigurer;
    private final JavaMailSender mailSender;

    @Autowired
    public MailServiceImpl(FreeMarkerConfigurer freeMarkerConfigurer, JavaMailSender mailSender) {
        this.freeMarkerConfigurer = freeMarkerConfigurer;
        this.mailSender = mailSender;
    }

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
            String from = "no-reply@squarepig.cn";
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("??????HtmlMessage??????????????????", e);
        }
    }

    @Override
    public void sendBorrowSuccessMail(BorrowRecord record) {
        try{
            String name = record.getUser().getName();
            String mail = record.getUser().getEmail();
            if(mail==null || mail.isEmpty()) return;
            Map<String, Object> mailModel = new HashMap<>();
            Calendar date = Calendar.getInstance();
            String year = String.valueOf(date.get(Calendar.YEAR));
            mailModel.put("year", year);
            mailModel.put("name", name);
            mailModel.put("book_name", record.getBook().getBook().getName());
            mailModel.put("last_date", record.getLastReturnDate().toString());
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate("borrow_succeed_email.xhtml");
            String mailText = FreeMarkerTemplateUtils.processTemplateIntoString(template, mailModel);
            sendHtmlMail(mail,"??????????????????", mailText, mailSender);
        }catch (Exception e){
            log.error("??????????????????");
        }

    }

    @Override
    public void sendReserveSuccessMail(Reservation reservation) {
        try {
            String mail = reservation.getUser().getEmail();
            if(mail==null || mail.isEmpty()) return;
            String name = reservation.getUser().getName();
            Map<String, Object> mailModel = new HashMap<>();
            mailModel.put("name", name);
            mailModel.put("book_name", reservation.getBookInfo().getName());
            mailModel.put("reserved_day", reservation.getUser().getGrade().getMaxReserveTime());
            mailModel.put("last_date", reservation.getLastDate().toString());
            Calendar date = Calendar.getInstance();
            String year = String.valueOf(date.get(Calendar.YEAR));
            mailModel.put("year", year);
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate("reserve_email.xhtml");
            String mailText = FreeMarkerTemplateUtils.processTemplateIntoString(template, mailModel);
            sendHtmlMail(mail,"????????????????????????", mailText, mailSender);
        }catch (Exception e){
            log.error("??????????????????");
        }

    }

    @Override
    public void sendAboutDueMail(BorrowRecord record) {
        try{
            Map<String, Object> mailModel = new HashMap<>();
            String name = record.getUser().getName();
            String mail = record.getUser().getEmail();
            if(mail==null || mail.isEmpty()) return;
            Calendar date = Calendar.getInstance();
            String year = String.valueOf(date.get(Calendar.YEAR));
            mailModel.put("book_name", record.getBook().getBook().getName());
            mailModel.put("last_date", record.getLastReturnDate().toString());
            mailModel.put("year", year);
            mailModel.put("name", name);
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate("about_due_email.xhtml");
            String mailText = FreeMarkerTemplateUtils.processTemplateIntoString(template, mailModel);
            sendHtmlMail(mail,"??????????????????", mailText, mailSender);
        }catch (Exception e){
            log.error("??????????????????");
        }
    }
}

package db2.elibrary.service;

import db2.elibrary.entity.BorrowRecord;
import db2.elibrary.entity.Reservation;
import org.springframework.mail.javamail.JavaMailSender;


public interface MailService {
    void sendTextMail(String to, String subject, String content, JavaMailSender mailSender);
    void sendHtmlMail(String to, String subject, String content, JavaMailSender mailSender);
    void sendBorrowSuccessMail(BorrowRecord record);
    void sendReserveSuccessMail(Reservation reservation);
}

package db2.elibrary.service;

public interface SmsService {
    String sendVerifySms(String phone, String code);
    String sendReservationSuccessSms(String phone, String bookName, String reserveTime, Integer remainDays, String dueTime);
    String sendAboutDueSms(String phone, String bookName, String borrowTime, String dueTime);
}

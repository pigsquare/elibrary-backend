package db2.elibrary.service;

public interface SmsService {
    void SendVerifySms(String phone, String code);
}

package db2.elibrary.service.impl;

import db2.elibrary.service.SmsService;
import db2.elibrary.util.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {
    private SmsUtil smsUtil;

    @Autowired
    public SmsServiceImpl(SmsUtil smsUtil) {
        this.smsUtil = smsUtil;
    }

    @Override
    public String sendVerifySms(String phone, String code) {
        smsUtil.setPhoneNumberSet1(new String[]{phone});
        smsUtil.setTemplateId("911888");
        smsUtil.setTemplateParamSet1(new String[]{code,"30"});
        return smsUtil.sendSms();
    }

    @Override
    public String sendReservationSuccessSms(String phone, String bookName, String reserveTime, Integer remainDays, String dueTime) {
        smsUtil.setPhoneNumberSet1(new String[]{phone});
        smsUtil.setTemplateId("912983");
        smsUtil.setTemplateParamSet1(new String[]{reserveTime, bookName, remainDays.toString(), dueTime});
        return smsUtil.sendSms();
    }

    @Override
    public String sendOverdueSms(String phone, String bookName, String borrowTime, String dueTime) {
        smsUtil.setPhoneNumberSet1(new String[]{phone});
        smsUtil.setTemplateId("912713");
        smsUtil.setTemplateParamSet1(new String[]{borrowTime, bookName, dueTime});
        return smsUtil.sendSms();
    }
}

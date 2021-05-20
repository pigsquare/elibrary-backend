package db2.elibrary.scheduled.task;

import db2.elibrary.entity.BorrowRecord;
import db2.elibrary.repository.BorrowRecordRepository;
import db2.elibrary.service.BorrowRecordService;
import db2.elibrary.service.MailService;
import db2.elibrary.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

// TODO
// 日常通知流程，每天10：00执行，负责：
// 1. 发送即将逾期提醒（3天内）邮件和短信（每个用户只发最近到期的一本书）；
// 2. 发送图书超期邮件（每本书都发）；
// 3. （可选）欠费账户发送欠费邮件，自动更改用户等级

@Component
@Slf4j
public class DailyRemind {

    private final MailService mailService;
    private final BorrowRecordService borrowRecordService;
    private final SmsService smsService;

    public DailyRemind(MailService mailService, BorrowRecordRepository borrowRecordRepository, BorrowRecordService borrowRecordService, SmsService smsService) {
        this.mailService = mailService;
        this.borrowRecordService = borrowRecordService;
        this.smsService = smsService;
    }

    @Scheduled(cron = "0 41 * * * ?") //上线用 "0 0 10 * * ?"
    public void execute(){
        //发送即将逾期提醒（3天内）邮件和短信
        List<BorrowRecord> borrowRecordList = borrowRecordService.getAboutDueBorrowingList();
        for(BorrowRecord borrowRecord:borrowRecordList){
            mailService.sendAboutDueMail(borrowRecord);
            smsService.sendAboutDueSms(borrowRecord.getUser().getTel(),"《"+borrowRecord.getBook().getBook().getName().substring(0,10)+"》",borrowRecord.getBorrowTime().toString().substring(0,10),borrowRecord.getLastReturnDate().toString().substring(0,10));
        }
        //发送图书超期邮件

        log.info("scheduled task works!");
    }
}

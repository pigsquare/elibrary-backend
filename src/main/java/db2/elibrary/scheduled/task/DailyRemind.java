package db2.elibrary.scheduled.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// TODO
// 日常通知流程，每天10：00执行，负责：
// 1. 发送即将逾期提醒（3天内）短信（每个用户只发最近到期的一本书）；
// 2. 发送图书超期邮件（每本书都发）；
// 3. （可选）欠费账户发送欠费邮件，自动更改用户等级

@Component
@Slf4j
public class DailyRemind {
    @Scheduled(cron = "*/15 0 * * * ?") //上线用 "0 0 10 * * ?"
    public void execute(){
        log.info("scheduled task works!");
    }
}

package db2.elibrary.scheduled.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// TODO
// 日常结算流程，每天0：05执行，负责：
// 1. 结算已经逾期的图书费用与信用分；标准：0.2元/天/本、1分/天/本，（可选）不超过书本价格2倍
// 2. 终止超过7天未预约到图书或者已预约到但超期未借的预约记录；
// 3. （可选）生成日报

@Component
@Slf4j
public class DailySettlement {
    @Scheduled(cron = "*/15 0 * * * ?") //上线用 "0 5 0 * * ?"
    public void execute(){
        log.info("scheduled task works!");
    }
}

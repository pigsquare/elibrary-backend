package db2.elibrary.scheduled.task;

import db2.elibrary.entity.Reservation;
import db2.elibrary.entity.enums.ReserveStatusEnum;
import db2.elibrary.repository.BorrowRecordRepository;
import db2.elibrary.repository.ReservationRepository;
import db2.elibrary.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

// TODO
// 日常结算流程，每天0：05执行，负责：
// 1. 结算已经逾期的图书费用与信用分；标准：0.2元/天/本、1分/天/本，（可选）不超过书本价格2倍
// 2. 终止超过7天未预约到图书或者已预约到但超期未借的预约记录  FINISHED
// 3. （可选）生成日报

@Component
@Slf4j
public class DailySettlement {
    private final UserRepository userRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final ReservationRepository reservationRepository;

    public DailySettlement(UserRepository userRepository, BorrowRecordRepository borrowRecordRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.borrowRecordRepository = borrowRecordRepository;
        this.reservationRepository = reservationRepository;
    }

    @Scheduled(cron = "*/15 0 * * * ?") //上线用 "0 5 0 * * ?"
    public void execute(){
        log.info("scheduled task works!");
        // 终止超过7天未预约到图书或者已预约到但超期未借的预约记录
        List<Reservation> reservationList = reservationRepository.findBySubmitTimeBeforeAndCompleteIsFalseAndStatusEquals(new Timestamp(new Date().getTime()-7*24*60*60*1000),ReserveStatusEnum.WAITING);
        for(Reservation reservation:reservationList){
            reservation.setComplete(true);
            reservation.setStatus(ReserveStatusEnum.CLOSED);
            reservationRepository.save(reservation);
        }

        reservationList = reservationRepository.findByCompleteIsFalseAndStatusEqualsAndLastDateBefore(ReserveStatusEnum.RESERVED, new Date());
        for(Reservation reservation:reservationList){
            reservation.setComplete(true);
            reservation.setStatus(ReserveStatusEnum.CLOSED);
            reservationRepository.save(reservation);
        }

    }
}

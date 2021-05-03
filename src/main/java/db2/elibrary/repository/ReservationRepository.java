package db2.elibrary.repository;

import db2.elibrary.entity.Reservation;
import db2.elibrary.entity.enums.ReserveStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    @Query(value = "select * from reservation where reservation.book_id = ?1 and reservation.user_id = ?2 order by last_date desc",nativeQuery = true)
    List<Reservation> findReservationsByBookIdAndUserId(Integer bookId, String userId);
    List<Reservation> findByBookIdAndUserIdOrderBySubmitTimeDesc(Integer bookId, String userId);

    // 获取个人未终止的预约记录
    List<Reservation> findByUserIdAndCompleteIsFalseOrderBySubmitTimeDesc(String userId);
    // 获取个人预约记录
    List<Reservation> findByUserIdOrderBySubmitTimeDesc(String userId);
    // 获取某书未成功预约holding的预约列表
    List<Reservation> findByBookInfo_IsbnAndBookIsNullOrderBySubmitTime(String isbn);
    // 获取这个人有没有正在预约某本书
    List<Reservation> findByUserIdAndBookInfo_IsbnAndBookIsNull(String userId, String isbn);
    // 获取超过7天未预约到图书的预约列表
    List<Reservation> findBySubmitTimeBeforeAndCompleteIsFalseAndStatusEquals(Timestamp timestamp, ReserveStatusEnum reserveStatusEnum);
    // 获取已预约到但超期未借的预约记录
    List<Reservation> findByCompleteIsFalseAndStatusEqualsAndLastDateBefore(ReserveStatusEnum reserveStatusEnum, Date date);
}

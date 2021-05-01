package db2.elibrary.repository;

import db2.elibrary.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    @Query(value = "select * from reservation where reservation.book_id = ?1 and reservation.user_id = ?2 order by last_date desc",nativeQuery = true)
    public List<Reservation> findReservationsByBookIdAndUserId(Integer bookId, String userId);
    List<Reservation> findByBookIdAndUserIdOrderBySubmitTimeDesc(Integer bookId, String userId);

    // 获取个人未终止的预约记录
    List<Reservation> findByUserIdAndCompleteIsFalseOrderBySubmitTimeDesc(String userId);
    // 获取个人预约记录
    List<Reservation> findByUserIdOrderBySubmitTimeDesc(String userId);
    // 获取某本书的预约列表
    List<Reservation> findByBookInfo_IsbnAndCompleteIsFalseOrderBySubmitTime(String isbn);
    // 获取这个人有没有正在预约某本书
    List<Reservation> findByUserIdAndBookInfo_IsbnAndBookIsNull(String userId, String isbn);
}

package db2.elibrary.repository;

import db2.elibrary.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    @Query(value = "select * from reservation where reservation.book_id = ?1 and reservation.user_id = ?2 order by last_date desc",nativeQuery = true)
    public List<Reservation> findReservationsByBookIdAndUserId(Integer bookId, String userId);
}

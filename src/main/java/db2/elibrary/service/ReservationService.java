package db2.elibrary.service;

import db2.elibrary.entity.Reservation;

public interface ReservationService {
    Reservation findByBookIdAndUserId(Integer bookId, String userId);
    void saveReservation(Reservation reservation);
    String makeReservation(String isbn);
    Boolean cancelReservation(Integer id);
}

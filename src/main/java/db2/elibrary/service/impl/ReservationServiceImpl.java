package db2.elibrary.service.impl;

import db2.elibrary.entity.Reservation;
import db2.elibrary.repository.ReservationRepository;
import db2.elibrary.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReservationServiceImpl implements ReservationService {
    private ReservationRepository reservationRepository;
    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository){
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Reservation findByBookIdAndUserId(Integer bookId,String userId) {
        List<Reservation> reservations = reservationRepository.findReservationsByBookIdAndUserId(bookId,userId);
        if(reservations.isEmpty()){
            return null;
        }
        return reservations.get(0);
    }

    @Override
    public void saveReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }
}

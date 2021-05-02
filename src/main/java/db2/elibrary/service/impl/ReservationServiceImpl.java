package db2.elibrary.service.impl;

import db2.elibrary.entity.Book;
import db2.elibrary.entity.Reservation;
import db2.elibrary.entity.User;
import db2.elibrary.entity.enums.ReserveStatusEnum;
import db2.elibrary.repository.BookRepository;
import db2.elibrary.repository.ReservationRepository;
import db2.elibrary.repository.UserRepository;
import db2.elibrary.service.ReservationService;
import db2.elibrary.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@Slf4j
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository, UserRepository userRepository, BookRepository bookRepository){
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
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

    @Override
    public String  makeReservation(String isbn) {
        String res = "预约错误，请检查输入是否有误！";
        String userId = UserUtil.getCurrentUserAccount();
        // TODO: 1.用户是否合法；2.判断用户已经借出的书目+未完成预约的总数是否大于user.grade.max_holdings; 3.书是否存在； 4.读者是否有其他的正在借而且超期的图书
        User user = userRepository.findById(userId).get();  // FIXME: 有空指针问题
        // 这是未完成预约数量
        Integer reservationNum = reservationRepository.findByUserIdAndCompleteIsFalseOrderBySubmitTimeDesc(userId).size();
        Book book = bookRepository.findById(isbn).get();    // FIXME: 同上

        Reservation reservation = new Reservation();
        reservation.setComplete(false);
        reservation.setSubmitTime(new Timestamp(System.currentTimeMillis()));
        reservation.setStatus(ReserveStatusEnum.WAITING);
        reservation.setBookInfo(book);
        reservation.setUser(user);
        reservationRepository.save(reservation);
        res = "预约成功，等待图书归还！";

        // TODO: 检查当前在馆图书是否存在预约图书
        // res = "预约成功，已成功锁定图书！";
        return res;
    }
}

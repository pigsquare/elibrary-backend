package db2.elibrary.service.impl;

import db2.elibrary.entity.*;
import db2.elibrary.entity.enums.BookStatusEnum;
import db2.elibrary.entity.enums.ReserveStatusEnum;
import db2.elibrary.exception.AuthException;
import db2.elibrary.exception.NotFoundException;
import db2.elibrary.repository.*;
import db2.elibrary.service.BorrowRecordService;
import db2.elibrary.service.MailService;
import db2.elibrary.service.ReservationService;
import db2.elibrary.service.SmsService;
import db2.elibrary.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final HoldingRepository holdingRepository;
    private final MailService mailService;
    private final SmsService smsService;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository, UserRepository userRepository, BookRepository bookRepository, BorrowRecordRepository borrowRecordRepository, HoldingRepository holdingRepository, MailService mailService, SmsService smsService) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.borrowRecordRepository = borrowRecordRepository;
        this.holdingRepository = holdingRepository;
        this.mailService = mailService;
        this.smsService = smsService;
    }

    @Override
    public Reservation findByBookIdAndUserId(Integer bookId, String userId) {
        List<Reservation> reservations = reservationRepository.findReservationsByBookIdAndUserId(bookId, userId);
        if (reservations.isEmpty()) {
            return null;
        }
        return reservations.get(0);
    }

    @Override
    public void saveReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    @Override
    public String makeReservation(String isbn) {
        String res;
        String userId = UserUtil.getCurrentUserAccount();
        if (userId == null) {
            throw new AuthException("");
        }
        //??????????????????
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("???????????????");
        }
        User user = optionalUser.get();
        //??????????????????
        if (!reservationRepository.findByUserIdAndCompleteIsFalseAndBookInfo_IsbnOrderBySubmitTimeDesc(userId, isbn).isEmpty()){
            throw new AuthException("??????????????????");
        }
        //?????????????????????????????????+????????????????????????????????????user.grade.max_holdings
        Integer borrowNum = borrowRecordRepository.countBorrowRecordsByUserAndReturnTimeIsNull(user);
        Integer reservationNum = reservationRepository.findByUserIdAndCompleteIsFalseOrderBySubmitTimeDesc(userId).size();
        if (borrowNum + reservationNum > user.getGrade().getMaxHoldings()) {
            throw new AuthException("?????????????????????????????????");
        }
        // ???????????????
        Optional<Book> optionalBook = bookRepository.findById(isbn);
        if (optionalBook.isEmpty()) {
            throw new NotFoundException("???????????????");
        }
        //??????????????????????????????????????????????????????
        if (borrowRecordRepository.countByUserAndReturnTimeIsNullAndLateFeeGreaterThan(user, 0.0) > 0) {
            throw new AuthException("??????????????????????????????????????????");
        }
        Book book = optionalBook.get();
        Reservation reservation = new Reservation();
        reservation.setComplete(false);
        reservation.setSubmitTime(new Timestamp(System.currentTimeMillis()));
        reservation.setStatus(ReserveStatusEnum.WAITING);
        reservation.setBookInfo(book);
        reservation.setUser(user);
        res = "????????????????????????????????????";
        reservationRepository.save(reservation);
        List<Holding> holdingList = holdingRepository.findByBookAndStatus(book, BookStatusEnum.AVAILABLE);
        if (!holdingList.isEmpty()) {
            Holding holding = holdingList.get(0);
            holding.setStatus(BookStatusEnum.RESERVED);
            reservation.setBook(holding);
            reservation.setStatus(ReserveStatusEnum.RESERVED);
            reservation.setLastDate(new java.sql.Date((long) (user.getGrade().getMaxReserveTime()) * 24 * 3600 * 1000
                    + new Date().getTime()));
            holdingRepository.save(holding);
            reservationRepository.save(reservation);
            // ?????????????????????/?????????????????????
            mailService.sendReserveSuccessMail(reservation);

            String bookName = reservation.getBookInfo().getName();
            smsService.sendReservationSuccessSms(reservation.getUser().getTel(),
                    "???" + bookName.substring(0, Math.min(bookName.length(), 10)) + "???",
                    new java.sql.Date(reservation.getSubmitTime().getTime()).toString(),
                    reservation.getUser().getGrade().getMaxReserveTime(),
                    reservation.getLastDate().toString());
            res = "???????????????????????????????????????";
        }


        return res;
    }

    @Override
    public Boolean cancelReservation(Integer id) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        if(optionalReservation.isEmpty()){
            throw new NotFoundException("?????????????????????");
        }
        Reservation reservation = optionalReservation.get();
        if(!reservation.getUser().getId().equals(UserUtil.getCurrentUserAccount())){
            throw new AuthException("???????????????");
        }
        reservation.setComplete(true);
        reservation.setStatus(ReserveStatusEnum.CANCELLED);
        reservationRepository.save(reservation);
        if(reservation.getBook()!=null){
            Holding holding = reservation.getBook();
            holding.setStatus(BookStatusEnum.AVAILABLE);
            holdingRepository.save(holding);
            judgeBookStatus(holding);
        }
        return true;
    }

    @Override
    public List<Reservation> getReservedBookInLibrary() {
        return reservationRepository.findByStatusAndCompleteIsFalse(ReserveStatusEnum.RESERVED);
    }

    @Override
    public List<Reservation> getUserUncompletedReservation() {
        return reservationRepository.findByUserIdAndCompleteIsFalseOrderBySubmitTimeDesc(UserUtil.getCurrentUserAccount());
    }

    @Override
    public List<Reservation> getUserReservation() {
        return reservationRepository.findByUserIdOrderBySubmitTimeDesc(UserUtil.getCurrentUserAccount());
    }

    @Override
    public void judgeBookStatus(Holding holding) {
        var reservationList=reservationRepository.findByBookInfo_IsbnAndBookIsNullAndCompleteIsFalseOrderBySubmitTime(holding.getBook().getIsbn());
        if (!reservationList.isEmpty()){
            var reservation = reservationList.get(0);
            holding.setStatus(BookStatusEnum.RESERVED);
            reservation.setStatus(ReserveStatusEnum.RESERVED);
            reservation.setBook(holding);
            reservation.setLastDate(new java.sql.Date((long) (reservation.getUser().getGrade().getMaxReserveTime())
                    * 24 * 3600 * 1000 + new Date().getTime()));
            holdingRepository.save(holding);
            reservationRepository.save(reservation);
            // ?????????????????????/?????????????????????
            String bookName = reservation.getBookInfo().getName();
            smsService.sendReservationSuccessSms(reservation.getUser().getTel(),
                    "???" + bookName.substring(0, Math.min(bookName.length(), 10)) + "???",
                    new java.sql.Date(reservation.getSubmitTime().getTime()).toString(),
                    reservation.getUser().getGrade().getMaxReserveTime(),
                    reservation.getLastDate().toString());
            mailService.sendReserveSuccessMail(reservation);
        }
    }
}

package db2.elibrary.service.impl;

import db2.elibrary.entity.*;
import db2.elibrary.entity.enums.BookStatusEnum;
import db2.elibrary.entity.enums.ReserveStatusEnum;
import db2.elibrary.exception.AuthException;
import db2.elibrary.exception.NotFoundException;
import db2.elibrary.repository.*;
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
        String res = "预约错误，请检查输入是否有误！";
        String userId = UserUtil.getCurrentUserAccount();
        if (userId == null) {
            throw new AuthException("");
        }
        //用户是否存在
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("用户不存在");
        }
        User user = optionalUser.get();
        //判断用户已经借出的书目+未完成预约的总数是否大于user.grade.max_holdings
        Integer borrowNum = borrowRecordRepository.countBorrowRecordsByUserAndReturnTimeIsNull(user);
        Integer reservationNum = reservationRepository.findByUserIdAndCompleteIsFalseOrderBySubmitTimeDesc(userId).size();
        if (borrowNum + reservationNum > user.getGrade().getMaxHoldings()) {
            throw new AuthException("超过最大借书和预约数量");
        }
        // 书是否存在
        Optional<Book> optionalBook = bookRepository.findById(isbn);
        if (optionalBook.isEmpty()) {
            throw new NotFoundException("图书不存在");
        }
        //读者是否有其他的正在借而且超期的图书
        if (borrowRecordRepository.countByUserAndReturnTimeIsNullAndLateFeeGreaterThan(user, 0.0) > 0) {
            throw new AuthException("有其他的正在借而且超期的图书");
        }
        Book book = optionalBook.get();
        Reservation reservation = new Reservation();
        reservation.setComplete(false);
        reservation.setSubmitTime(new Timestamp(System.currentTimeMillis()));
        reservation.setStatus(ReserveStatusEnum.WAITING);
        reservation.setBookInfo(book);
        reservation.setUser(user);
        res = "预约成功，等待图书归还！";
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
            // 被预定，发邮件/短信通知预定者
            mailService.sendReserveSuccessMail(reservation);

            String bookName = reservation.getBookInfo().getName();
            smsService.sendReservationSuccessSms(reservation.getUser().getTel(),
                    "《" + bookName.substring(0, Math.min(bookName.length(), 10)) + "》",
                    new java.sql.Date(reservation.getSubmitTime().getTime()).toString(),
                    reservation.getUser().getGrade().getMaxReserveTime(),
                    reservation.getLastDate().toString());
            res = "预约成功，已成功锁定图书！";
        }


        return res;
    }

    @Override
    public Boolean cancelReservation(Integer id) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        if(optionalReservation.isEmpty()){
            throw new NotFoundException("预定记录不存在");
        }
        Reservation reservation = optionalReservation.get();
        if(!reservation.getUser().getId().equals(UserUtil.getCurrentUserAccount())){
            throw new AuthException("用户不合法");
        }
        reservation.setComplete(true);
        reservation.setStatus(ReserveStatusEnum.CANCELLED);
        reservationRepository.save(reservation);
        return true;
    }

    @Override
    public List<Holding> getReservedBookInLibrary() {
        return holdingRepository.findByStatus(BookStatusEnum.RESERVED);
    }
}

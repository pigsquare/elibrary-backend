package db2.elibrary.service.impl;

import db2.elibrary.entity.BorrowRecord;
import db2.elibrary.entity.Holding;
import db2.elibrary.entity.Reservation;
import db2.elibrary.entity.User;
import db2.elibrary.entity.enums.BookStatusEnum;
import db2.elibrary.entity.enums.ReserveStatusEnum;
import db2.elibrary.exception.AuthException;
import db2.elibrary.exception.NotFoundException;
import db2.elibrary.mapper.BorrowRecordMapper;
import db2.elibrary.repository.AdminRepository;
import db2.elibrary.repository.BorrowRecordRepository;
import db2.elibrary.repository.HoldingRepository;
import db2.elibrary.repository.ReservationRepository;
import db2.elibrary.service.*;
import db2.elibrary.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BorrowRecordServiceImpl implements BorrowRecordService {
    private final BorrowRecordRepository borrowRecordRepository;
    private final HoldingService holdingService;
    private final ReservationService reservationService;
    private final UserService userService;
    private final AdminRepository adminRepository;
    private final HoldingRepository holdingRepository;
    private final ReservationRepository reservationRepository;
    private final SmsService smsService;
    private final MailService mailService;
    private final BorrowRecordMapper borrowRecordMapper;

    @Autowired
    public BorrowRecordServiceImpl(BorrowRecordRepository borrowRecordRepository, HoldingService holdingService, ReservationService reservationService, UserService userService, AdminRepository adminRepository, HoldingRepository holdingRepository, ReservationRepository reservationRepository, SmsService smsService, MailService mailService, BorrowRecordMapper borrowRecordMapper) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.holdingService = holdingService;
        this.reservationService = reservationService;
        this.userService = userService;
        this.adminRepository = adminRepository;
        this.holdingRepository = holdingRepository;
        this.reservationRepository = reservationRepository;
        this.smsService = smsService;
        this.mailService = mailService;
        this.borrowRecordMapper = borrowRecordMapper;
    }

    @Override
    public Boolean borrowHolding(String cardNo, String barcode) {
        //??????????????????
        User user = userService.getUserByCardNo(cardNo);
        if (user.getBalance() < 0)
            throw new AuthException("???????????????????????????????????????");
        //???????????????????????????
        if(borrowRecordRepository.countByUserAndReturnTimeIsNullAndLateFeeGreaterThan(user, 0.0) > 0)
            throw new AuthException("???????????????");
        //??????????????????????????????
        log.info("????????????????????????" + borrowRecordRepository.countByUserAndReturnTimeIsNull(user));
        if (borrowRecordRepository.countByUserAndReturnTimeIsNull(user) >= user.getGrade().getMaxHoldings()) {
            throw new AuthException("??????????????????!");
        }

        //??????????????????????????????
        Holding holding = holdingService.getHoldingByBarcode(barcode);
        if (holding.getStatus() == BookStatusEnum.RESERVED) {
            Reservation reservation = reservationService.findByBookIdAndUserId(holding.getId(), user.getId());
            if (reservation != null && !reservation.getComplete() && reservation.getStatus() == ReserveStatusEnum.RESERVED && reservation.getLastDate().compareTo(new Date()) >= 0) {
                reservation.setComplete(true);
                reservation.setStatus(ReserveStatusEnum.FINISHED);
                reservationService.saveReservation(reservation);
                _borrowHolding(user, holding);
                return true;
            }
        } else if (holding.getStatus() == BookStatusEnum.AVAILABLE) {
            _borrowHolding(user, holding);
            return true;
        }
        return false;
    }

    private void _borrowHolding(User user, Holding holding) {
        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setBook(holding);
        var optionalAdmin = adminRepository.findByUserId(UserUtil.getCurrentUserAccount());
        optionalAdmin.ifPresent(borrowRecord::setAgent);
        borrowRecord.setExtend(false);
        borrowRecord.setBorrowTime(new Timestamp(new Date().getTime()));
        borrowRecord.setLastReturnDate(new java.sql.Date((long) (user.getGrade().getMaxBorrowTime()) * 24 * 3600 * 1000
                + new Date().getTime()));
        borrowRecord.setLateFee(0.0);
        borrowRecord.setUser(user);
        borrowRecordRepository.save(borrowRecord);
        holding.setStatus(BookStatusEnum.BORROWED);
        holdingRepository.save(holding);
        mailService.sendBorrowSuccessMail(borrowRecord);
    }

    @Override
    public Boolean returnHolding(String barcode){
        Holding holding = holdingService.getHoldingByBarcode(barcode);
        if (holding == null) {
            throw new NotFoundException("");
        }

        List<BorrowRecord> borrowRecordList = borrowRecordRepository.findByBook_BarcodeAndReturnTimeIsNullOrderByBorrowTimeDesc(barcode);
        if (borrowRecordList.isEmpty()) {
            // ??????????????????holding???????????????holding?????????
            processHoldingOfReturn(holding);
            throw new NotFoundException("??????????????????");
        }
        BorrowRecord borrowRecord = borrowRecordList.get(0);
        User user = borrowRecord.getUser();
        if (borrowRecord.getLastReturnDate().compareTo(new Date()) > 0) {
            // user.setBalance(user.getBalance() - borrowRecord.getLateFee());
            // ????????????????????????????????????
            user.setCredit(user.getCredit() - 5);
        }
        user.setCredit(user.getCredit() + 5);
        userService.updateUser(user);
        borrowRecord.setReturnTime(new Timestamp(new Date().getTime()));
        borrowRecordRepository.save(borrowRecord);
        processHoldingOfReturn(holding);

        return true;
    }

    private void processHoldingOfReturn(Holding holding){
        if (holding.getStatus() == BookStatusEnum.BORROWED) {
            holding.setStatus(BookStatusEnum.AVAILABLE);
            holdingRepository.save(holding);
//            var reservationList=reservationRepository.findByBookInfo_IsbnAndBookIsNullAndCompleteIsFalseOrderBySubmitTime(holding.getBook().getIsbn());
//            if (!reservationList.isEmpty()){
//                var reservation = reservationList.get(0);
//                holding.setStatus(BookStatusEnum.RESERVED);
//                reservation.setBook(holding);
//                reservation.setStatus(ReserveStatusEnum.RESERVED);
//                reservation.setLastDate(new java.sql.Date((long) (reservation.getUser().getGrade().getMaxReserveTime())
//                        * 24 * 3600 * 1000 + new Date().getTime()));
//                holdingRepository.save(holding);
//                reservationRepository.save(reservation);
//                // ?????????????????????/?????????????????????
//                String bookName = reservation.getBookInfo().getName();
//                smsService.sendReservationSuccessSms(reservation.getUser().getTel(),
//                        "???" + bookName.substring(0, Math.min(bookName.length(), 10)) + "???",
//                        new java.sql.Date(reservation.getSubmitTime().getTime()).toString(),
//                        reservation.getUser().getGrade().getMaxReserveTime(),
//                        reservation.getLastDate().toString());
//                mailService.sendReserveSuccessMail(reservation);
//            }
            reservationService.judgeBookStatus(holding);
        }
    }

    @Override
    public Boolean renewHolding(Integer recordId) {
        Optional<BorrowRecord> borrowRecordOptional = borrowRecordRepository.findById(recordId);
        if (borrowRecordOptional.isEmpty()) {
            throw new NotFoundException("?????????????????????");
        }
        BorrowRecord borrowRecord = borrowRecordOptional.get();
        if (borrowRecord.getExtend() || borrowRecord.getReturnTime() != null || borrowRecord.getLateFee() > 0) {
            throw new AuthException("??????????????????");
        }
        if (!borrowRecord.getUser().getId().equals(UserUtil.getCurrentUserAccount())){
            throw new AuthException("???????????????????????????");
        }

        if (borrowRecord.getUser().getBalance() < 0) {
            throw new AuthException("?????????????????????????????????????????????????????????");
        }
        borrowRecord.setExtend(true);
        borrowRecord.setLastReturnDate(new java.sql.Date((long) (borrowRecord.getUser().getGrade().getMaxBorrowTime()) * 24 * 3600 * 1000
                + new Date().getTime()));
        borrowRecordRepository.save(borrowRecord);
        return true;
    }

    @Override
    public List<BorrowRecord> getList() {
        List<BorrowRecord> borrowRecordList;
        borrowRecordList = borrowRecordRepository.findByUser_IdOrderByBorrowTimeDesc(UserUtil.getCurrentUserAccount());
        return borrowRecordList;
    }

    @Override
    public List<BorrowRecord> getBorrowingList() {
        List<BorrowRecord> borrowRecordList;
        borrowRecordList = borrowRecordRepository.findByUser_IdAndReturnTimeIsNullOrderByBorrowTimeDesc(UserUtil.getCurrentUserAccount());
        return borrowRecordList;
    }

    @Override
    public List<BorrowRecord> getBorrowingListByCardNo(String cardNo) {
        User user = userService.getUserByCardNo(cardNo);
        return borrowRecordRepository.findByUser_IdAndReturnTimeIsNullOrderByBorrowTimeDesc(user.getId());
    }

    @Override
    public Integer delayLastReturnDateForVacation(Date startTime, Date endTime) {
        return borrowRecordMapper.vocationRenewal(new java.sql.Date(startTime.getTime() - 3*24*3600*1000),
                new java.sql.Date(endTime.getTime() + 3*24*3600*1000));
    }

    @Override
    public List<BorrowRecord> getAboutDueBorrowingList() {
        List<Integer> recordIdList =  borrowRecordMapper.getAboutDueBorrowingList();
        List<BorrowRecord> borrowRecordList = new ArrayList<>();
        for (Integer i:
             recordIdList) {
            log.info("id???"+ i);
            var tmp = borrowRecordRepository.findById(i);
            tmp.ifPresent(borrowRecordList::add);
        }
        return borrowRecordList;
    }
}

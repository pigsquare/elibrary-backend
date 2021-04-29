package db2.elibrary.service.impl;

import db2.elibrary.entity.*;
import db2.elibrary.entity.enums.BookStatusEnum;
import db2.elibrary.entity.enums.ReserveStatusEnum;
import db2.elibrary.exception.AuthException;
import db2.elibrary.exception.NotFoundException;
import db2.elibrary.repository.AdminRepository;
import db2.elibrary.repository.BorrowRecordRepository;
import db2.elibrary.repository.HoldingRepository;
import db2.elibrary.service.BorrowRecordService;
import db2.elibrary.service.HoldingService;
import db2.elibrary.service.ReservationService;
import db2.elibrary.service.UserService;
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
public class BorrowRecordServiceImpl implements BorrowRecordService {
    private final BorrowRecordRepository borrowRecordRepository;
    private final HoldingService holdingService;
    private final ReservationService reservationService;
    private final UserService userService;
    private final AdminRepository adminRepository;
    private final HoldingRepository holdingRepository;

    @Autowired
    public BorrowRecordServiceImpl(BorrowRecordRepository borrowRecordRepository, HoldingService holdingService, ReservationService reservationService, UserService userService, AdminRepository adminRepository, HoldingRepository holdingRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.holdingService = holdingService;
        this.reservationService = reservationService;
        this.userService = userService;
        this.adminRepository = adminRepository;
        this.holdingRepository = holdingRepository;
    }

    @Override
    public Boolean borrowHolding(String cardNo, String barcode) {
        //用户是否欠费
        User user = userService.getUserByCardNo(cardNo);
        if (user.getBalance() < 0)
            throw new AuthException("账户余额不足，请及时缴费！");
        //用户是否超过借书上限
        log.info("当前已借书数量：" + borrowRecordRepository.countByUserAndReturnTimeIsNull(user));
        if (borrowRecordRepository.countByUserAndReturnTimeIsNull(user) >= user.getGrade().getMaxHoldings()) {
            throw new AuthException("超过借书上限!");
        }

        //根据藏书状态进行处理
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
    }

    @Override
    public Boolean returnHolding(String barcode) {
        Holding holding = holdingService.getHoldingByBarcode(barcode);
        if (holding == null) {
            throw new NotFoundException("");
        }

        List<BorrowRecord> borrowRecordList = borrowRecordRepository.findByBook_BarcodeAndReturnTimeIsNullOrderByBorrowTimeDesc(barcode);
        if (borrowRecordList.isEmpty()) {
            processHoldingOfReturn(holding);
            throw new NotFoundException("没有借出记录");
        }
        BorrowRecord borrowRecord = borrowRecordList.get(0);
        User user = borrowRecord.getUser();
        if (borrowRecord.getLastReturnDate().compareTo(new Date()) > 0) {
            user.setBalance(user.getBalance() - borrowRecord.getLateFee());
        }
        user.setCredit(user.getCredit() + 1);
        userService.updateUser(user);
        borrowRecord.setReturnTime(new Timestamp(new Date().getTime()));
        borrowRecordRepository.save(borrowRecord);
        processHoldingOfReturn(holding);

//        if(holding.getStatus()==BookStatusEnum.BORROWED){
//            holding.setStatus(BookStatusEnum.AVAILABLE);
//            holdingRepository.save(holding);
//        } else if(holding.getStatus()==BookStatusEnum.BORROWED_AND_BOOKED){
//            //TODO: 被预定，发邮件/短信通知预定者
//        }

        return true;
    }

    private void processHoldingOfReturn(Holding holding) {
        if (holding.getStatus() == BookStatusEnum.BORROWED) {
            holding.setStatus(BookStatusEnum.AVAILABLE);
            holdingRepository.save(holding);
        } else if (holding.getStatus() == BookStatusEnum.BORROWED_AND_BOOKED) {
            //TODO: 被预定，发邮件/短信通知预定者
            holding.setStatus(BookStatusEnum.RESERVED);
            holdingRepository.save(holding);
        }
    }

    @Override
    public Boolean renewHolding(Integer recordId) {
        Optional<BorrowRecord> borrowRecordOptional = borrowRecordRepository.findById(recordId);
        if (borrowRecordOptional.isEmpty()) {
            throw new NotFoundException("借阅记录不存在");
        }
        BorrowRecord borrowRecord = borrowRecordOptional.get();
        if (borrowRecord.getExtend()) {
            throw new AuthException("不可再次续借");
        }

        if (borrowRecord.getUser().getBalance() < 0) {
            throw new AuthException("账户余额不足，请缴费后再进行续借操作！");
        }
        borrowRecord.setExtend(true);
        borrowRecord.setLastReturnDate(new java.sql.Date((long) (borrowRecord.getUser().getGrade().getMaxBorrowTime()) * 24 * 3600 * 1000
                + new Date().getTime()));
        borrowRecordRepository.save(borrowRecord);
        return true;
    }
}

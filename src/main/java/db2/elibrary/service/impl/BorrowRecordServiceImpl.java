package db2.elibrary.service.impl;

import db2.elibrary.entity.*;
import db2.elibrary.entity.enums.BookStatusEnum;
import db2.elibrary.entity.enums.ReserveStatusEnum;
import db2.elibrary.exception.AuthException;
import db2.elibrary.repository.AdminRepository;
import db2.elibrary.repository.BorrowRecordRepository;
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

@Service
@Slf4j
public class BorrowRecordServiceImpl implements BorrowRecordService {
    private BorrowRecordRepository borrowRecordRepository;
    private HoldingService holdingService;
    private ReservationService reservationService;
    private UserService userService;
    private AdminRepository adminRepository;

    @Autowired
    public BorrowRecordServiceImpl(BorrowRecordRepository borrowRecordRepository, HoldingService holdingService, ReservationService reservationService, UserService userService, AdminRepository adminRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.holdingService = holdingService;
        this.reservationService = reservationService;
        this.userService = userService;
        this.adminRepository = adminRepository;
    }

    @Override
    public Boolean borrowHolding(String cardNo, String barcode) {
        //用户是否欠费
        User user = userService.getUserByCardNo(cardNo);
        if (user.getBalance() <= 0)
            throw new AuthException("账户余额不足，请及时缴费！");
        //用户是否超过借书上限
        if(borrowRecordRepository.countBorrowRecordsByUserAndReturnTimeIsNull(user) >= user.getGrade().getMaxHoldings()){
            throw new AuthException("超过借书上限!");
        }

        //根据藏书状态进行处理
        Holding holding = holdingService.getHoldingByBarcode(barcode);
        if (holding.getStatus() == BookStatusEnum.RESERVED) {
            Reservation reservation = reservationService.findByBookIdAndUserId(holding.getId(), user.getId());
            if(reservation != null && !reservation.getComplete() && reservation.getStatus()==ReserveStatusEnum.RESERVED && reservation.getLastDate().compareTo(new Date())>=0){
                reservation.setComplete(true);
                reservation.setStatus(ReserveStatusEnum.FINISHED);
                reservationService.saveReservation(reservation);
                _borrowHolding(user,holding);
            }
        } else if(holding.getStatus() == BookStatusEnum.AVAILABLE){
            _borrowHolding(user,holding);
        }
        return true;
    }

    private void _borrowHolding(User user,Holding holding){
        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setBook(holding);
        borrowRecord.setAgent(adminRepository.findByUserId(UserUtil.getCurrentUserAccount()).get());
        borrowRecord.setExtend(true);
        borrowRecord.setMemo("");
        borrowRecord.setBorrowTime(new Timestamp(new Date().getTime()));
        borrowRecord.setLastReturnDate(new java.sql.Date(user.getGrade().getMaxBorrowTime()+new Date().getTime()));
        borrowRecord.setLateFee(holding.getBook().getPrice());
        borrowRecordRepository.save(borrowRecord);
        holding.setStatus(BookStatusEnum.BORROWED);
    }
}

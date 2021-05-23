package db2.elibrary.service.impl;

import com.alipay.api.AlipayApiException;
import db2.elibrary.dto.CommonResponseDto;
import db2.elibrary.dto.pay.CompensationRequestDto;
import db2.elibrary.dto.pay.PayRequestDto;
import db2.elibrary.entity.Bill;
import db2.elibrary.entity.BorrowRecord;
import db2.elibrary.entity.Holding;
import db2.elibrary.entity.User;
import db2.elibrary.entity.enums.BookStatusEnum;
import db2.elibrary.repository.BillRepository;
import db2.elibrary.repository.BorrowRecordRepository;
import db2.elibrary.repository.HoldingRepository;
import db2.elibrary.service.BillService;
import db2.elibrary.service.BorrowRecordService;
import db2.elibrary.service.UserService;
import db2.elibrary.util.AliPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service("BillService")
@Slf4j
public class BillServiceImpl implements BillService {
    private final AliPayUtil aliPayUtil;
    private final UserService userService;
    private final BillRepository billRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final BorrowRecordService borrowRecordService;
    private final HoldingRepository holdingRepository;

    @Autowired
    public BillServiceImpl(AliPayUtil aliPayUtil, UserService userService, BillRepository billRepository, BorrowRecordRepository borrowRecordRepository, BorrowRecordService borrowRecordService, HoldingRepository holdingRepository) {
        this.aliPayUtil = aliPayUtil;
        this.userService = userService;
        this.billRepository = billRepository;
        this.borrowRecordRepository = borrowRecordRepository;
        this.borrowRecordService = borrowRecordService;
        this.holdingRepository = holdingRepository;
    }

    @Override
    public CommonResponseDto payOwe(PayRequestDto payRequestDto) {
        Double balance = userService.getBalance(payRequestDto.getCardNo()).getBalance();
        User user = userService.getUserByCardNo(payRequestDto.getCardNo());
        CommonResponseDto responseDto = new CommonResponseDto();
        responseDto.setMessage("操作错误");
        if(balance<0){
            try {
                Bill msg =aliPayUtil.payFee(0-balance, payRequestDto.getBarcode(), "缴纳欠款"+(0-balance)+"元");
                if(msg!= null && msg.getCode().equals("10000")){
                    responseDto.setMessage("支付成功 ￥"+(0-balance));
                    msg.setUser(user);
                    billRepository.save(msg);
                    user.setBalance(user.getBalance() + msg.getAmount());
                    userService.updateUser(user);
                }else {
                    responseDto.setMessage("支付失败");
                }
            } catch (AlipayApiException e) {
                log.error("支付时发生错误");
                return responseDto;
            }
        }

        return responseDto;
    }

    @Override
    public CommonResponseDto recharge(PayRequestDto payRequestDto) {
        User user = userService.getUserByCardNo(payRequestDto.getCardNo());
        CommonResponseDto responseDto = new CommonResponseDto();
        responseDto.setMessage("操作错误");
        if(payRequestDto.getTotal()==null)return responseDto;
        try {
            Bill aliPayRes= aliPayUtil.payFee(payRequestDto.getTotal(), payRequestDto.getBarcode(),
                    "充值"+payRequestDto.getTotal()+"元");
            if(aliPayRes.getCode().equals("10000")){
                responseDto.setMessage("充值成功 ￥"+aliPayRes.getAmount());
                aliPayRes.setUser(user);
                billRepository.save(aliPayRes);
                user.setBalance(user.getBalance() + aliPayRes.getAmount());
                userService.updateUser(user);
            }
        } catch (AlipayApiException e) {
            log.error("支付时发生错误");
            return responseDto;
        }
        return responseDto;
    }

    @Override
    public CommonResponseDto compensateBook(CompensationRequestDto compensationRequestDto) {
        User user = userService.getUserByCardNo(compensationRequestDto.getCardInfo());
        CommonResponseDto responseDto = new CommonResponseDto();
        responseDto.setMessage("操作错误");
        Optional<BorrowRecord> optionalBorrowRecord =
                borrowRecordRepository.findById(compensationRequestDto.getBorrowRecordId());
        if(optionalBorrowRecord.isPresent()){
            BorrowRecord borrowRecord = optionalBorrowRecord.get();
            Double fee = 10.0;
            if(compensationRequestDto.getIsCompensateBook()){
                try {
                    Bill bill = aliPayUtil.payFee(fee, compensationRequestDto.getBarcode(), "赔书《"+borrowRecord.getBook().getBook().getName()+"》加工费10元");
                    if(bill.getCode().equals("10000")){
                        responseDto.setMessage("赔书成功");
                        bill.setUser(user);
                        billRepository.save(bill);
                        borrowRecordService.returnHolding(borrowRecord.getBook().getBarcode());
                    }
                } catch (AlipayApiException e) {
                    log.error("支付时发生错误");
                    return responseDto;
                }
            }else{
                if(borrowRecord.getBook().getBook().getPrice()!=null){
                    fee+=borrowRecord.getBook().getBook().getPrice();
                } else {
                    fee += 50.0;
                }
                if(compensationRequestDto.getAdditionalFee()!=null){
                    fee += compensationRequestDto.getAdditionalFee();
                }
                try {
                    Bill bill = aliPayUtil.payFee(fee, compensationRequestDto.getBarcode(), "丢失赔偿《"+borrowRecord.getBook().getBook().getName()+"》");
                    if(bill.getCode().equals("10000")){
                        responseDto.setMessage("赔款成功");
                        bill.setUser(user);
                        billRepository.save(bill);
                        borrowRecord.setReturnTime(new Timestamp(System.currentTimeMillis()));
                        borrowRecord.setMemo("丢失赔款￥"+bill.getAmount());
                        borrowRecordRepository.save(borrowRecord);
                        Holding holding = borrowRecord.getBook();
                        holding.setStatus(BookStatusEnum.LOST);
                        holdingRepository.save(holding);
                    }
                } catch (AlipayApiException e) {
                    log.error("支付时发生错误");
                    return responseDto;
                }
            }
        }

        return responseDto;
    }
}

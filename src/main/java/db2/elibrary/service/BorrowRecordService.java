package db2.elibrary.service;

import db2.elibrary.entity.BorrowRecord;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface BorrowRecordService {
    //借书
    Boolean borrowHolding(String cardNo,String barcode);
    //还书
    Boolean returnHolding(String barcode);
    //续借
    Boolean renewHolding(Integer recordId);
    //获取当前用户借书列表
    List<BorrowRecord> getList();
    //获取当前用户借书未还列表
    List<BorrowRecord> getBorrowingList();
    //根据卡号获取当前用户借书未还列表
    List<BorrowRecord> getBorrowingListByCardNo(String cardNo);
    //根据假期起止日期延长还书时间
    Boolean delayLastReturnDateForVacation(Date startTime, Date endTime);
}

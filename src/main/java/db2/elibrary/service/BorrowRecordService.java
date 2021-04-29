package db2.elibrary.service;

import db2.elibrary.entity.BorrowRecord;

import java.util.List;

public interface BorrowRecordService {
    //借书
    Boolean borrowHolding(String cardNo,String barcode);
    //还书
    Boolean returnHolding(String barcode);
    //续借
    Boolean renewHolding(Integer recordId);
    //获取当前借书列表
    List<BorrowRecord> getList();
}

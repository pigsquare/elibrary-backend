package db2.elibrary.service;

public interface BorrowRecordService {
    //借书
    Boolean borrowHolding(String cardNo,String barcode);
    //还书
    Boolean returnHolding(String barcode);
}

package db2.elibrary.dto;

import db2.elibrary.entity.BorrowRecord;
import lombok.Data;

@Data
public class BorrowRecordResponseDto {
    private Integer recordId;
    private String bookName;
    private String author;
    private String publisher;

    private String borrowTime;
    private String lastReturnDate;
    private Boolean extend;
    private Boolean returned;
    private String returnTime;
    private Double lateFee;
    private String memo;

    public BorrowRecordResponseDto(BorrowRecord borrowRecord){
        this.recordId = borrowRecord.getId();
        this.bookName = borrowRecord.getBook().getBook().getName();
        this.author = borrowRecord.getBook().getBook().getAuthor();
        this.publisher = borrowRecord.getBook().getBook().getPublisher();
        this.borrowTime = borrowRecord.getBorrowTime().toString();
        this.lastReturnDate = borrowRecord.getLastReturnDate().toString();
        this.extend = borrowRecord.getExtend();
        this.returned = (borrowRecord.getReturnTime() != null);
        if (this.returned)
            this.returnTime = borrowRecord.getReturnTime().toString();
        this.lateFee = borrowRecord.getLateFee();
        this.memo = borrowRecord.getMemo();
    }
}

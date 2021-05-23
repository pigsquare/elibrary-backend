package db2.elibrary.dto.reservation;

import db2.elibrary.entity.Reservation;
import lombok.Data;

@Data
public class PersonalReservationResponseDto {
    // 返回详细信息，包括用户、图书、条码
    private Integer id;
    private String bookName;
    private String submitTime;
    private String lastDate;
    private String status;
    private String memo;
    private String isbn;
    private String barcode;
    private String author;
    private String publisher;
    public PersonalReservationResponseDto(Reservation reservation){
        this.id = reservation.getId();
        this.bookName = reservation.getBookInfo().getName();
        this.author = reservation.getBookInfo().getAuthor();
        this.publisher = reservation.getBookInfo().getPublisher();
        this.submitTime = reservation.getSubmitTime().toString().substring(0, 19);
        this.isbn = reservation.getBookInfo().getIsbn();
        this.status = reservation.getStatus().getChineseName();
        this.memo = reservation.getMemo();
        if(reservation.getBook() != null){
            this.barcode = reservation.getBook().getBarcode();
            this.lastDate = reservation.getLastDate().toString();
        } else{
            this.barcode = "---";
            this.lastDate = "---";
        }

    }
}
